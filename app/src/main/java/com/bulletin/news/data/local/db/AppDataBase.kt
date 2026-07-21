package com.bulletin.news.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bulletin.news.data.local.db.dao.ArticleDao
import com.bulletin.news.data.local.db.dao.CachedArticleDao
import com.bulletin.news.data.local.db.dao.RemoteKeyDao
import com.bulletin.news.data.local.db.entity.ArticleEntity
import com.bulletin.news.data.local.db.entity.CachedArticleEntity
import com.bulletin.news.data.local.db.entity.RemoteKeyEntity

@Database(
    entities = [ArticleEntity::class, CachedArticleEntity::class, RemoteKeyEntity::class],
    version = 4
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun cachedArticleDao(): CachedArticleDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}