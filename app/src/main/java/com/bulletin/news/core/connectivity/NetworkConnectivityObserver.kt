package com.bulletin.news.core.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.bulletin.news.core.utils.NetworkState
import com.bulletin.news.core.worker.NewsSyncWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkConnectivityObserver @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private var isRegistered = false

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            NetworkState.isConnected.value = true
            val syncRequest = OneTimeWorkRequestBuilder<NewsSyncWorker>().build()
            WorkManager.getInstance(context).enqueue(syncRequest)
        }

        override fun onLost(network: Network) {
            NetworkState.isConnected.value = hasActiveInternetConnection()
        }

        override fun onCapabilitiesChanged(
            network: Network,
            capabilities: NetworkCapabilities
        ) {
            NetworkState.isConnected.value = capabilities.isUsable()
        }
    }

    fun start() {
        if (isRegistered) return
        NetworkState.isConnected.value = hasActiveInternetConnection()

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, networkCallback)
        isRegistered = true
    }

    fun stop() {
        if (!isRegistered) return
        connectivityManager.unregisterNetworkCallback(networkCallback)
        isRegistered = false
    }

    private fun hasActiveInternetConnection(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.isUsable()
    }

    private fun NetworkCapabilities.isUsable(): Boolean {
        return hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}
