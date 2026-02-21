package com.example.newsflow.di

import com.example.newsflow.data.remote.api.AuthApi
import com.example.newsflow.utils.authRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun baseUrl() = "https://dummyjson.com/auth/"

    @Provides
    @Singleton
    fun authRetrofit () = Retrofit.Builder()
        .baseUrl(baseUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideAuthService( retrofit : Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

//    @Provides
//    @Singleton
//    fun provideNewsService(@newsRetrofit  retrofit : Retrofit): ApiService = retrofit.create(ApiService::class.java)
}