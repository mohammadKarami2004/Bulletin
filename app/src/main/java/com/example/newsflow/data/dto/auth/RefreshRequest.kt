package com.example.newsflow.data.dto.auth

data class RefreshRequest(
    val refresh_token: String,
    val expiresInMins : Int = 30
)
