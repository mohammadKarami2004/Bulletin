package com.example.newsflow.data.remote.api

import com.example.newsflow.data.dto.auth.LoginRequest
import com.example.newsflow.data.dto.auth.LoginResponse
import com.example.newsflow.data.dto.auth.RefreshRequest
import com.example.newsflow.data.dto.auth.RefreshResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface AuthApi {

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @GET("me")
    suspend fun getUser(
        @Header("Authorization") token: String
    ): Response<LoginResponse>

    @POST("refresh")
    suspend fun refresh(
        @Body request: RefreshRequest
    ): Response<RefreshResponse>

}