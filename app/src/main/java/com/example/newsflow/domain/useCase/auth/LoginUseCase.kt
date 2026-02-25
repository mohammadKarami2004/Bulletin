package com.example.newsflow.domain.useCase.auth

import com.example.newsflow.data.local.datastore.TokenDataStore
import com.example.newsflow.data.remote.dto.auth.LoginRequest
import com.example.newsflow.data.remote.dto.auth.LoginResponse
import com.example.newsflow.domain.repository.AuthRepository
import com.example.newsflow.utils.Resource
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenDataStore: TokenDataStore
) {
    suspend fun invoke(request: LoginRequest): Resource<LoginResponse> {
        val result = authRepository.login(request)
        if (result is Resource.Success) {
            tokenDataStore.saveTokens(result.data.accessToken, result.data.refreshToken)
        }
        return result
    }
}