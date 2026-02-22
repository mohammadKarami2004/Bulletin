package com.example.newsflow.data.repository

import android.util.Log
import com.example.newsflow.data.remote.dto.auth.LoginRequest
import com.example.newsflow.data.remote.dto.auth.LoginResponse
import com.example.newsflow.data.remote.dto.auth.RefreshRequest
import com.example.newsflow.data.remote.dto.auth.RefreshResponse
import com.example.newsflow.data.remote.api.AuthApi
import com.example.newsflow.utils.Resource
import com.example.newsflow.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authApi: AuthApi): AuthRepository {

    override suspend fun login(loginRequest: LoginRequest): Resource<LoginResponse> {
        return try {
            val response = authApi.login(loginRequest)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Log.d("AuthRepo", "Code: ${response.code()} Message: ${response.message()} Error: ${response.errorBody()?.string()}")
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "خطای ناشناخته")
        }
    }

    override suspend fun refreshToken(refreshRequest: RefreshRequest): Resource<RefreshResponse> {

        return try {
            val response = authApi.refresh(refreshRequest)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "خطای ناشناخته")
        }


    }
}