package com.example.newsflow.data.remote.dto.news

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleDto>

)
