package com.bulletin.news.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bulletin.news.data.local.datastore.SettingDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsDataStore: SettingDataStore,
)
    :ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()



        init {
            viewModelScope.launch {
                settingsDataStore.isDarkMode().collect { isDark ->
                    _uiState.update { it.copy(isDarkMode = isDark) }
                }
            }
        }

    fun toggleDarkMode(state: Boolean) {
        viewModelScope.launch {
            settingsDataStore.changeThemeMode(state)
        }
    }

}