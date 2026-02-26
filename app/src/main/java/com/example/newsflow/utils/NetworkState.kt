package com.example.newsflow.utils

import kotlinx.coroutines.flow.MutableStateFlow

object NetworkState {
    val isConnected = MutableStateFlow(true)
}