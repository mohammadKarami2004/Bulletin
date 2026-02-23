package com.example.newsflow.domain.useCase.auth

import com.example.newsflow.data.local.datastore.TokenDataStore
import javax.inject.Inject

class LogOutUseCase @Inject constructor(private val tokenDataStore: TokenDataStore) {
    suspend fun invoke() {
        tokenDataStore.clearTokens()
    }
}