package com.example.newsflow.data.dto.auth

data class RefreshResponse(
    val accessToken : String,
    val refreshToken : String,
)
