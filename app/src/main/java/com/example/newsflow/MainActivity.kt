package com.example.newsflow

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.newsflow.data.local.datastore.SettingDataStore
import com.example.newsflow.navigation.AppNavGraph
import com.example.newsflow.receiver.NetworkReceiver
import com.example.newsflow.ui.theme.NewsFlowTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var settingDataStore: SettingDataStore
    private lateinit var networkReceiver: NetworkReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkReceiver = NetworkReceiver()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkReceiver, filter)
        setContent {
            val isDarkMode by settingDataStore.isDarkMode()
                .collectAsStateWithLifecycle(initialValue = false)

            NewsFlowTheme(darkTheme = isDarkMode) {
                AppNavGraph()
            }
        }
    }
}