package com.bulletin.news.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.bulletin.news.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NewsNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun createChannel() {
        val channel = NotificationChannel(
            "news_channel",
            "News Updates",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    fun showNotification(title: String, message: String) {
        val notification = NotificationCompat.Builder(context, "news_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .build()

        notificationManager.notify(1, notification)
    }
}