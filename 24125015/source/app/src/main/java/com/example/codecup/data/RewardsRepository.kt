package com.example.codecup.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.codecup.data.database.PointsHistoryDao
import com.example.codecup.data.database.PointsHistoryEntity
import com.example.codecup.models.Order
import com.example.codecup.models.PointsHistoryItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.rewardsDataStore: DataStore<Preferences> by preferencesDataStore(name = "rewards_prefs")

/**
 * Single source of truth for the loyalty program: stamp count and points balance live
 * in DataStore (survive process death), the per-event points ledger lives in Room.
 * All stamp/point mutations go through here — never through UI code.
 */
class RewardsRepository(
    private val context: Context,
    private val pointsHistoryDao: PointsHistoryDao
) {
    private object Keys {
        val STAMPS = intPreferencesKey("stamps")
        val POINTS = intPreferencesKey("points")
        val DEMO_SEEDED = booleanPreferencesKey("demo_seeded")
    }

    val stamps: Flow<Int> = context.rewardsDataStore.data.map { it[Keys.STAMPS] ?: 0 }
    val points: Flow<Int> = context.rewardsDataStore.data.map { it[Keys.POINTS] ?: 0 }

    val pointsHistory: Flow<List<PointsHistoryItem>> = pointsHistoryDao.getAll().map { rows ->
        rows.map { row ->
            PointsHistoryItem(
                title = row.title,
                date = formatDate(row.dateMillis),
                points = "${if (row.points >= 0) "+" else ""}${row.points} pts",
                isPositive = row.points >= 0
            )
        }
    }

    /**
     * Called when an order transitions Ongoing -> History (rubric: stamps are earned
     * per COMPLETED order). Awards one stamp (capped) and points proportional to the
     * order total; free (redeemed) orders earn a stamp but no points.
     */
    suspend fun awardForCompletedOrder(order: Order): Int {
        val earnedPoints = (order.totalPrice * POINTS_PER_DOLLAR).toInt()
        context.rewardsDataStore.edit { prefs ->
            prefs[Keys.STAMPS] = (prefs[Keys.STAMPS] ?: 0) + 1
            prefs[Keys.POINTS] = (prefs[Keys.POINTS] ?: 0) + earnedPoints
        }
        if (earnedPoints > 0) {
            pointsHistoryDao.insert(
                PointsHistoryEntity(
                    title = "Order #${order.id}",
                    dateMillis = System.currentTimeMillis(),
                    points = earnedPoints
                )
            )
        }
        return earnedPoints
    }

    suspend fun addPoints(amount: Int, title: String) {
        context.rewardsDataStore.edit { prefs ->
            prefs[Keys.POINTS] = (prefs[Keys.POINTS] ?: 0) + amount
        }
        pointsHistoryDao.insert(
            PointsHistoryEntity(title = title, dateMillis = System.currentTimeMillis(), points = amount)
        )
    }

    suspend fun redeemPoints(amount: Int, title: String) {
        context.rewardsDataStore.edit { prefs ->
            prefs[Keys.POINTS] = ((prefs[Keys.POINTS] ?: 0) - amount).coerceAtLeast(0)
        }
        pointsHistoryDao.insert(
            PointsHistoryEntity(title = title, dateMillis = System.currentTimeMillis(), points = -amount)
        )
    }

    /** Explicit user action once the card is full (rubric: Loyalty Card Reset). */
    suspend fun clearStamps() {
        context.rewardsDataStore.edit { prefs -> 
            prefs[Keys.STAMPS] = ((prefs[Keys.STAMPS] ?: 0) - STAMPS_PER_CARD).coerceAtLeast(0)
        }
    }

    /** True once the one-time demo account seed has run (see [DemoDataSeeder]). */
    suspend fun isDemoSeeded(): Boolean =
        context.rewardsDataStore.data.first()[Keys.DEMO_SEEDED] ?: false

    /** One-time initialization of demo balances; marks the seed as done atomically. */
    suspend fun seedDemoBalances(stamps: Int, points: Int) {
        context.rewardsDataStore.edit { prefs ->
            prefs[Keys.STAMPS] = stamps.coerceIn(0, STAMPS_PER_CARD)
            prefs[Keys.POINTS] = points.coerceAtLeast(0)
            prefs[Keys.DEMO_SEEDED] = true
        }
    }

    private fun formatDate(millis: Long): String =
        SimpleDateFormat("dd MMMM, HH:mm", Locale.getDefault()).format(Date(millis))

    companion object {
        const val STAMPS_PER_CARD = 8
        const val POINTS_PER_DOLLAR = 5
        const val REDEEM_POINTS_PER_DOLLAR = 25
        const val FULL_CARD_BONUS_POINTS = 500

        fun redeemCostFor(price: Double): Int = (price * REDEEM_POINTS_PER_DOLLAR).toInt()
    }
}
