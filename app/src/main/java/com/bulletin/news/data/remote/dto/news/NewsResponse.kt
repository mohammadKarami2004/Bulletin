package com.bulletin.news.data.remote.dto.news

import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleDto>
)
