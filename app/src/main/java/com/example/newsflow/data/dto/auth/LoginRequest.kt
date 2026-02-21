package com.example.newsflow.data.dto.auth

data class LoginRequest(
    val username: String,
    val password: String,
    val expiresInMins : Int = 30
)