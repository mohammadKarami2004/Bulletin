package com.example.newsflow.di

import android.content.Context
import androidx.room.Room
import com.example.newsflow.data.local.datastore.SettingDataStore
import com.example.newsflow.data.local.datastore.TokenDataStore
import com.example.newsflow.data.local.datastore.dataStore
import com.example.newsflow.data.local.db.AppDataBase
import com.example.newsflow.data.local.db.dao.ArticleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Provides
    @Singleton
    fun provideTokenDataStore(@ApplicationContext context: Context): TokenDataStore {
        return TokenDataStore(context.dataStore)
    }

    @Provides
    @Singleton
    fun provideSettingsDataStore(@ApplicationContext context: Context): SettingDataStore {
        return SettingDataStore(context.dataStore)
    }
    @Provides
    @Singleton
    fun provideAppDataBase(@ApplicationContext context: Context): AppDataBase{
        return Room.databaseBuilder(context,AppDataBase::class.java,"newsFlow.db")
            .build()
    }
    @Provides
    @Singleton
    fun provideArticleDao(db: AppDataBase): ArticleDao {
        return db.articleDao()
    }
}