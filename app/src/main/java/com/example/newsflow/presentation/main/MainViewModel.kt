package com.example.newsflow.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsflow.data.local.datastore.TokenDataStore
import com.example.newsflow.utils.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val tokenDataStore: TokenDataStore) : ViewModel() {

    val authState = tokenDataStore.isLoggedInFlow().map{
        if (it) AuthState.LoggedIn else AuthState.LoggedOut
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), AuthState.Loading)

}