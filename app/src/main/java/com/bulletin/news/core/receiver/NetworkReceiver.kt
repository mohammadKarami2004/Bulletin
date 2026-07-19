package com.bulletin.news.core.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.bulletin.news.core.utils.NetworkState
import com.bulletin.news.core.worker.NewsSyncWorker


class NetworkReceiver : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("NetworkReceiver", "onReceive called!")
        context ?: return

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val isConnected = connectivityManager.activeNetworkInfo?.isConnected == true

        Log.d("NetworkReceiver", "isConnected: $isConnected")

        NetworkState.isConnected.value = isConnected

        if (isConnected) {
            val syncRequest = OneTimeWorkRequestBuilder<NewsSyncWorker>()
                .build()
            WorkManager.getInstance(context).enqueue(syncRequest)
        }
    }
}