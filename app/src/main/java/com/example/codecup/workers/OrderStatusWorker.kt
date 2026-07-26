package com.example.codecup.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.codecup.data.database.AppDatabase
import com.example.codecup.data.database.NotificationEntity
import com.example.codecup.models.OrderStatus
import com.example.codecup.ui.utils.NotificationHelper
import kotlinx.coroutines.delay

/**
 * Simulates the cafe preparing an order: Received -> Preparing -> Ready, writing each
 * transition to Room so it survives the app being killed, then fires a local notification.
 */
class OrderStatusWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val orderId = inputData.getString(KEY_ORDER_ID) ?: return Result.failure()
        val database = AppDatabase.getDatabase(applicationContext)

        delay(PREPARING_DELAY_MS)
        database.orderDao().updateStatus(orderId, OrderStatus.Preparing.name)

        delay(READY_DELAY_MS)
        database.orderDao().updateStatus(orderId, OrderStatus.Ready.name)

        database.notificationDao().insert(
            NotificationEntity(
                title = "Your coffee is ready! ☕",
                body = "Order #$orderId is ready for pickup. See you soon!",
                dateMillis = System.currentTimeMillis()
            )
        )
        NotificationHelper(applicationContext).showOrderReadyNotification(orderId)

        return Result.success()
    }

    companion object {
        const val KEY_ORDER_ID = "order_id"
        const val PREPARING_DELAY_MS = 5_000L
        const val READY_DELAY_MS = 10_000L
    }
}
