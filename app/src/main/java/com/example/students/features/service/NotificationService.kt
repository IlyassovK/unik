package com.example.students.features.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import com.example.students.utils.GlobalPreferences
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.koin.android.ext.android.inject

class NotificationService : FirebaseMessagingService() {
    private val prefs: GlobalPreferences by inject()

    private lateinit var channel: NotificationChannel

    private val mNotificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        createNotification(remoteMessage = message)
    }

    private fun createNotification(remoteMessage: RemoteMessage) {
        PushUtils.onPushNotificationReceived(this, remoteMessage)

//        val notificationBuilder: NotificationCompat.Builder =
//            NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.neil_i_armstrong)
//                .setContentTitle(remoteMessage.data["title"])
//                .setContentText(remoteMessage.data["body"])
////                .setAutoCancel(true)
////                .setNumber(0)
////                .setPriority(if (Build.VERSION.SDK_INT > 25) NotificationManager.IMPORTANCE_HIGH else Notification.PRIORITY_MAX)
////                .setStyle(
////                    NotificationCompat.BigTextStyle()
////                        .bigText(remoteMessage.data["body"])
////                )
//
//        NotificationManagerCompat.from(this).notify(Random.nextInt(), notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i("Raf1", "token:${token}")
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME, importance
            ).apply {
                description = "CHANNEL_DESCRIPTION"
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "CHANNEL_ID"
        const val CHANNEL_NAME = "CHANNEL_NAME"
    }
}