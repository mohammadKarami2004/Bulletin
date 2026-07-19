package com.bulletin.news.data.remote.dto.news

import kotlinx.serialization.Serializable

@Serializable
data class ArticleDto(
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
)

@Serializable
data class Source(
    val id: String?,
    val name: String
)
