package com.hugoguerrero.tecno.data.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.hugoguerrero.tecno.data.model.NotificationDto
import com.hugoguerrero.tecno.data.remote.NotificationRemoteDataSource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotificationRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : NotificationRemoteDataSource {

    private val notificationsCollection = firestore.collection("notifications")
    private val tokensCollection = firestore.collection("user_tokens")

    override suspend fun saveUserNotificationToken(userId: String, token: String) {
        try {
            val data = mapOf("token" to token, "updatedAt" to System.currentTimeMillis())
            tokensCollection.document(userId).set(data).await()
        } catch (e: Exception) {
            // Manejo de error si quieres loguear
        }
    }

    override suspend fun sendNotification(notification: NotificationDto): Boolean {
        return try {
            val id = notificationsCollection.document().id
            val newNotification = notification.copy(
                id = id,
                createdAt = System.currentTimeMillis(),
                isRead = false
            )
            notificationsCollection.document(id).set(newNotification).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getNotifications(userId: String): List<NotificationDto> {
        return try {
            notificationsCollection
                .whereEqualTo("userId", userId)
                .orderBy("createdAt")
                .get()
                .await()
                .toObjects(NotificationDto::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun markAsRead(notificationId: String): Boolean {
        return try {
            notificationsCollection
                .document(notificationId)
                .update("isRead", true)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
