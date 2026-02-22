package com.example.newsflow.data.remote.dto.auth

data class LoginResponse(
    val id : Int,
    val username : String,
    val firstName : String,
    val lastName : String,
    val email : String,
    val gender : String,
    val image : String,
    val accessToken : String,
    val refreshToken : String,
)