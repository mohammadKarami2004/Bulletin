package com.bulletin.news

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bulletin.news.presentation.navigation.AppNavGraph
import com.bulletin.news.presentation.settings.SettingsViewModel
import com.bulletin.news.ui.theme.BulletinTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // به‌جای inject مستقیم SettingDataStore، از همون SettingsViewModel که
    // برای SettingsScreen هم استفاده می‌شه استفاده می‌کنیم. اینجوری منطق
    // "دارک‌مود از کجا میاد" فقط یه‌جا (خودِ ViewModel) تعریف شده،
    // نه اینکه Activity هم مستقل بره سراغ DataStore.
    private val settingsViewModel: SettingsViewModel by viewModels()

    // Android 13 (API 33) به بعد نمایش نوتیف نیاز به این permission داره.
    // خودِ NewsNotificationManager قبل از notify() چک می‌کنه که permission داده شده یا نه،
    // ولی درخواست گرفتنش باید یه‌جایی از UI اتفاق بیفته - اینجا موقع باز شدن اپ می‌پرسیم.
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* نتیجه رو نیازی نیست هندل کنیم؛ اگه رد بشه فقط نوتیف نشون داده نمی‌شه */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestNotificationPermissionIfNeeded()

        enableEdgeToEdge()
        setContent {
            val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()

            BulletinTheme(darkTheme = uiState.isDarkMode) {
                AppNavGraph()
            }
        }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

        val granted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        if (!granted) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}