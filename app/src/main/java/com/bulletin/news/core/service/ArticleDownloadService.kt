package com.bulletin.news.core.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.bulletin.news.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticleDownloadService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, "news_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("در حال دانلود...")
            .setContentText("مقاله در حال دانلود است")
            .build()

        startForeground(2, notification)

        CoroutineScope(Dispatchers.IO).launch {
            val url = intent?.getStringExtra("url")


            stopSelf()
        }

        return START_NOT_STICKY
    }
    override fun onBind(intent: Intent?): IBinder? = null
}