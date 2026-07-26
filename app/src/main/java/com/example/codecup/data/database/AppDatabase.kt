package com.example.codecup.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.codecup.models.FavoriteProduct
import com.example.codecup.models.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Product::class,
        FavoriteProduct::class,
        CartItemEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        PointsHistoryEntity::class,
        NotificationEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun favoritesDao(): FavoritesDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun pointsHistoryDao(): PointsHistoryDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Instance ?: Room.databaseBuilder(context, AppDatabase::class.java, "code_cup_database")
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .addCallback(seedCallback)
                    .build()
                    .also { Instance = it }
            }
        }

        /**
         * Upserts the app-defined menu on every open: first launch seeds it, later
         * launches refresh names/prices/images in place. REPLACE on the stable primary
         * keys makes this idempotent — no duplicate rows are possible.
         */
        private val seedCallback = object : Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                val database = Instance ?: return
                CoroutineScope(Dispatchers.IO).launch {
                    database.productDao().insertAll(SeedData.products)
                }
            }
        }
    }
}
