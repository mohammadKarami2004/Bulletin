package com.bulletin.news

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bulletin.news.data.local.datastore.SettingDataStore
import com.bulletin.news.presentation.navigation.AppNavGraph
import com.bulletin.news.core.receiver.NetworkReceiver
import com.bulletin.news.ui.theme.BulletinTheme
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
        enableEdgeToEdge()
        setContent {
            val isDarkMode by settingDataStore.isDarkMode()
                .collectAsStateWithLifecycle(initialValue = false)

            BulletinTheme(darkTheme = isDarkMode) {
                AppNavGraph()
            }
        }
    }
}





