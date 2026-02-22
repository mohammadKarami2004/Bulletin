package com.example.newsflow.di

import com.example.newsflow.data.remote.api.AuthApi
import com.example.newsflow.data.remote.api.NewsApi
import com.example.newsflow.data.remote.interceptor.NewsAuthInterceptor
import com.example.newsflow.utils.authRetrofit
import com.example.newsflow.utils.newsRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
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
    @authRetrofit
    fun authRetrofit() = Retrofit.Builder()
        .baseUrl(baseUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideAuthService(@authRetrofit retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)






    //news//
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
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    @Provides
    @Singleton
    fun provideNewsService(@newsRetrofit retrofit: Retrofit): NewsApi =
        retrofit.create(NewsApi::class.java)
}