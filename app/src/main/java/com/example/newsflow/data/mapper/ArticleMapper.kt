package com.example.newsflow.data.mapper

import com.example.newsflow.data.remote.dto.news.ArticleDto
import com.example.newsflow.domain.model.Article

fun ArticleDto.toDomain() = Article(
    source = source.name,
    author = author,
    title = title,
    description = description,
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    content = content
)