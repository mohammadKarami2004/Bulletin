package com.bulletin.news.data.local.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bulletin.news.data.local.db.entity.CachedArticleEntity

@Dao
interface CachedArticleDao {

    @Query("SELECT * FROM cached_articles WHERE category = :category ORDER BY sortOrder ASC")
    fun pagingSource(category: String): PagingSource<Int, CachedArticleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<CachedArticleEntity>)

    @Query("DELETE FROM cached_articles WHERE category = :category")
    suspend fun clearCategory(category: String)
}