package com.example.newsflow.data.remote.dto.auth

data class RefreshRequest(
    val refreshToken: String,
    val expiresInMins : Int = 30
)
