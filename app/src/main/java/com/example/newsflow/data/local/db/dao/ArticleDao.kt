package com.example.newsflow.data.local.db.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsflow.data.local.db.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Query("SELECT * FROM article")
    fun getAllArticles(): Flow<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Delete
    suspend fun deleteArticle(article: ArticleEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM article WHERE url = :url)")
    suspend fun isBookmarked(url: String): Boolean


    @Query("SELECT * FROM article")
    fun getAllArticlesAsCursor(): Cursor

}


