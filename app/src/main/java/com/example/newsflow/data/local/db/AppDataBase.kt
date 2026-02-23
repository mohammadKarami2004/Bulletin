package com.example.newsflow.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsflow.data.local.db.dao.ArticleDao
import com.example.newsflow.data.local.db.entity.ArticleEntity


@Database(
    entities = [ArticleEntity::class],
    version = 1
)
abstract class AppDataBase: RoomDatabase() {
    abstract fun articleDao(): ArticleDao

}