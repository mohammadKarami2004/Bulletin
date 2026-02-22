package com.example.newsflow.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class NewsAuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader("X-Api-Key", "58fa664dcb104d6f8b069428e65a4360")
            .build()
        return chain.proceed(newRequest)
    }
}


