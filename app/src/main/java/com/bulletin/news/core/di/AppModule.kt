package com.bulletin.news.core.di

import com.bulletin.news.data.repository.NewsRepositoryImpl
import com.bulletin.news.domain.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun  bindNewsRepository(
        impl: NewsRepositoryImpl
    ): NewsRepository

}