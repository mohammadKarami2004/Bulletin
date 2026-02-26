package com.example.newsflow.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.newsflow.utils.NetworkState
import com.example.newsflow.utils.NetworkState.isConnected
import com.example.newsflow.worker.NewsSyncWorker


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