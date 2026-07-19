package com.bulletin.news.core.di

import com.bulletin.news.data.remote.api.NewsApi
import com.bulletin.news.data.remote.interceptor.NewsAuthInterceptor
import com.bulletin.news.core.utils.newsRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    private val jsonConverterFactory = json.asConverterFactory("application/json".toMediaType())

    @Provides
    fun baseUrlNews() = "https://newsapi.org/v2/"

    @Provides
    @Singleton
    fun okHttpClient() = OkHttpClient.Builder()
        .addInterceptor(NewsAuthInterceptor())
        .build()

    @Provides
    @Singleton
    @newsRetrofit
    fun provideNewsRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl(baseUrlNews())
        .client(okHttpClient)
        .addConverterFactory(jsonConverterFactory)
        .build()

    @Provides
    @Singleton
    fun provideNewsService(@newsRetrofit retrofit: Retrofit): NewsApi =
        retrofit.create(NewsApi::class.java)
}