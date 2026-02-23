package com.example.newsflow.data.remote.api

import com.example.newsflow.data.remote.dto.news.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String
    ): Response<NewsResponse>

    @GET("everything")
    suspend fun searchNews(
        @Query("q") query: String
    ): Response<NewsResponse>
}