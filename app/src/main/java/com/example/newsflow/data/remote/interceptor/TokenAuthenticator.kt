package com.example.newsflow.data.remote.interceptor

import com.example.newsflow.data.local.datastore.TokenDataStore
import com.example.newsflow.data.remote.api.AuthApi
import com.example.newsflow.data.remote.dto.auth.RefreshRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val authApi: AuthApi,
    private val tokenDataStore: TokenDataStore
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = runBlocking { tokenDataStore.getRefreshToken() } ?: return null

        val refreshResponse = runBlocking {
            authApi.refresh(RefreshRequest(refreshToken))
        }

        if (refreshResponse.isSuccessful && refreshResponse.body() != null) {
            val newAccessToken = refreshResponse.body()!!.accessToken
            val newRefreshToken = refreshResponse.body()!!.refreshToken

            runBlocking {
                tokenDataStore.saveTokens(newAccessToken, newRefreshToken)
            }

            return response.request.newBuilder()
                .header("Authorization", "Bearer $newAccessToken")
                .build()
        }

        return null
    }
}