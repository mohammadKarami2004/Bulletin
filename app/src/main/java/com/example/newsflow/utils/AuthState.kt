package com.example.newsflow.utils


sealed class AuthState {
    object Loading : AuthState()
    object LoggedIn : AuthState()
    object LoggedOut: AuthState()
}
