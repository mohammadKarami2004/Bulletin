package com.example.newsflow.data.remote.dto.auth

data class RefreshResponse(
    val accessToken : String,
    val refreshToken : String,
)
