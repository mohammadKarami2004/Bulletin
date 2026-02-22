package com.example.newsflow.domain.useCase.auth

import com.example.newsflow.data.remote.dto.auth.LoginRequest
import com.example.newsflow.data.remote.dto.auth.LoginResponse
import com.example.newsflow.utils.Resource
import com.example.newsflow.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend fun invoke(loginRequest: LoginRequest): Resource<LoginResponse> {
        return authRepository.login(loginRequest)
    }
}