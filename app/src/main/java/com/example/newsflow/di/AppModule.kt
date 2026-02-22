package com.example.newsflow.di

import com.example.newsflow.data.repository.AuthRepositoryImpl
import com.example.newsflow.data.repository.NewsRepositoryImpl
import com.example.newsflow.domain.repository.AuthRepository
import com.example.newsflow.domain.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun  bindNewsRepository(
        impl: NewsRepositoryImpl
    ): NewsRepository

}