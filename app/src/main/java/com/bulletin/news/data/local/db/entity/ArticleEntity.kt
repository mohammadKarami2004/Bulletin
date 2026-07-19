package com.bulletin.news.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article")
data class ArticleEntity(
    val source: String,
    val author: String?,
    val title: String,
    val description: String?,
    @PrimaryKey val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
)
