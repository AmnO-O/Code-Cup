package com.example.codecup.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.codecup.data.OrderRepository
import com.example.codecup.models.OrderStatus
import com.example.codecup.ui.utils.NotificationHelper
import kotlinx.coroutines.delay

class OrderStatusWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val orderId = inputData.getString("order_id") ?: return Result.failure()
        val orderRepository = OrderRepository.getInstance()
        val notificationHelper = NotificationHelper(applicationContext)

        // Simulate Received -> Preparing (Wait 5 seconds)
        delay(5000)
        orderRepository.updateOrderStatus(orderId, OrderStatus.Preparing)

        // Simulate Preparing -> Ready (Wait 10 seconds)
        delay(10000)
        orderRepository.updateOrderStatus(orderId, OrderStatus.Ready)

        // Trigger Notification
        notificationHelper.showOrderReadyNotification(orderId)

        return Result.success()
    }
}
