package com.example.students.features.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.students.R
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber
import java.util.concurrent.atomic.AtomicInteger

object PushUtils {

    const val PUSH_HUB_ACTION_CODE = "PUSH_HUB_ACTION_CODE"
    const val CODE_VALUE = "CODE_VALUE"
    private lateinit var channel: NotificationChannel
    private const val CHANNEL_ID: String = "smartbank-channel-id"
    private const val CHANNEL_NAME: String = "smartbank-channel-name"
    private const val CHANNEL_DESCRIPTION: String = "smartbank-channel-description"
    private const val CODE_KEY: String = "code"
    private const val TYPE_KEY: String = "type"
    private const val AUTH_VALUE: String = "AUTH"

    fun onPushNotificationReceived(context: Context?, message: RemoteMessage?) {
        if (message != null && context != null) {

            val notification = message.notification
            val title: String = notification?.title ?: "Students"
            val body: String = message.data["body"] ?: ""

            Timber.d("Message Notification Title: $title")
            Timber.d("Message Notification Body: $body")


            if (!PushUtils::channel.isInitialized) {
                createNotificationChannel(context)
            }

            createNotification(context, title, body)
        }
    }

    private fun sendCodeToActivity(context: Context, code: String) {
        val intent = Intent(PUSH_HUB_ACTION_CODE)
        intent.putExtra(CODE_VALUE, code)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

    private fun createNotification(context: Context, title: String, message: String) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setColor(ContextCompat.getColor(context, R.color.colorAccent))
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_MAX)
        NotificationManagerCompat.from(context).notify(NotificationID.iD, builder.build())
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME, importance
            ).apply {
                description = CHANNEL_DESCRIPTION
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    //Copied from: https://stackoverflow.com/questions/25713157/generate-int-unique-id-as-android-notification-id
    object NotificationID {
        private val c: AtomicInteger = AtomicInteger(0)
        val iD: Int
            get() = c.incrementAndGet()
    }

}