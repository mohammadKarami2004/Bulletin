package com.example.newsflow.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.newsflow.worker.NewsSyncWorker


class NetworkReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return

        val syncRequest = OneTimeWorkRequestBuilder<NewsSyncWorker>()
            .build()

        WorkManager.getInstance(context).enqueue(syncRequest)
    }
}