package com.bulletin.news.core.utils

import kotlinx.coroutines.flow.MutableStateFlow

object NetworkState {
    val isConnected = MutableStateFlow(true)
}