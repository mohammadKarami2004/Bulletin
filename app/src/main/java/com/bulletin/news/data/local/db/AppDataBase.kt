package com.bulletin.news.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bulletin.news.data.local.db.dao.ArticleDao
import com.bulletin.news.data.local.db.entity.ArticleEntity


@Database
    (
    entities = [ArticleEntity::class],
    version = 3
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}