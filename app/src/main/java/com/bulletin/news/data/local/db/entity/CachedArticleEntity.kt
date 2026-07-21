package com.bulletin.news.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_articles")
data class CachedArticleEntity(
    @PrimaryKey val url: String,
    val category: String,
    val source: String,
    val author: String?,
    val title: String,
    val description: String?,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?,
    // برای حفظ همون ترتیبی که NewsAPI برگردونده (چون ORDER BY url یا publishedAt
    // لزوماً همون ترتیب API نیست)
    val sortOrder: Int
)