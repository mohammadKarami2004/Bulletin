package com.bulletin.news.core.di

import android.content.Context
import androidx.room.Room
import com.bulletin.news.data.local.datastore.SettingDataStore
import com.bulletin.news.data.local.datastore.dataStore
import com.bulletin.news.data.local.db.AppDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideSettingsDataStore(@ApplicationContext context: Context): SettingDataStore {
        return SettingDataStore(context.dataStore)
    }

    @Provides
    @Singleton
    fun provideAppDataBase(@ApplicationContext context: Context): AppDataBase {
        return Room.databaseBuilder(context, AppDataBase::class.java, "bulletin.db")
            .fallbackToDestructiveMigration(true)
            .build()
    }
}