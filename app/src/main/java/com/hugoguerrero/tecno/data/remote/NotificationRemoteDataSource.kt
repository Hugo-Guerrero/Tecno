package com.hugoguerrero.tecno.data.remote

import com.hugoguerrero.tecno.data.model.NotificationDto

interface NotificationRemoteDataSource {
    suspend fun saveUserNotificationToken(userId: String, token: String)
    suspend fun sendNotification(notification: NotificationDto): Boolean
    suspend fun getNotifications(userId: String): List<NotificationDto>
    suspend fun markAsRead(notificationId: String): Boolean
}
