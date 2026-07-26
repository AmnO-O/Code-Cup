package com.example.codecup.data

import com.example.codecup.data.database.NotificationDao
import com.example.codecup.data.database.NotificationEntity
import kotlinx.coroutines.flow.Flow

class NotificationsRepository(private val notificationDao: NotificationDao) {

    val notifications: Flow<List<NotificationEntity>> = notificationDao.getAll()

    val unreadCount: Flow<Int> = notificationDao.unreadCount()

    suspend fun add(title: String, body: String) {
        notificationDao.insert(
            NotificationEntity(title = title, body = body, dateMillis = System.currentTimeMillis())
        )
    }

    suspend fun markAllRead() = notificationDao.markAllRead()
}
