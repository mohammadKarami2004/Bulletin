package com.example.newsflow.domain.repository

import com.example.newsflow.data.remote.dto.auth.LoginRequest
import com.example.newsflow.data.remote.dto.auth.LoginResponse
import com.example.newsflow.data.remote.dto.auth.RefreshRequest
import com.example.newsflow.data.remote.dto.auth.RefreshResponse
import com.example.newsflow.utils.Resource

interface AuthRepository {
    suspend fun login(loginRequest: LoginRequest): Resource<LoginResponse>
    suspend fun refreshToken(refreshRequest: RefreshRequest): Resource<RefreshResponse>
}