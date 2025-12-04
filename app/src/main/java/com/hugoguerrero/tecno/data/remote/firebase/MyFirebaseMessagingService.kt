package com.hugoguerrero.tecno.data.remote.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hugoguerrero.tecno.R
import com.hugoguerrero.tecno.data.repository.AuthRepository
import com.hugoguerrero.tecno.domain.use_case.UpdateFcmTokenUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var updateFcmTokenUseCase: UpdateFcmTokenUseCase

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.notification?.let { notification ->
            sendNotification(notification.title, notification.body)
        }
    }

    private fun sendNotification(title: String?, messageBody: String?) {
        val channelId = "default_channel_id"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Asegúrate de tener este drawable
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Desde Android Oreo (API 26) se necesita un NotificationChannel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notificaciones Generales",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID de la notificación */, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        
        val userId = authRepository.getCurrentUser()?.uid
        if (userId != null) {
            CoroutineScope(Dispatchers.IO).launch {
                updateFcmTokenUseCase(userId, token)
            }
        }
    }
}
