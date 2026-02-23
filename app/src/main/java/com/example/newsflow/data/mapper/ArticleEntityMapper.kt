package com.example.newsflow.data.mapper

import com.example.newsflow.data.local.db.entity.ArticleEntity
import com.example.newsflow.domain.model.Article

fun ArticleEntity.toDomain() = Article(
    source = source,
    author = author,
    title = title,
    description = description,
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    content = content
)

fun Article.toEntity() = ArticleEntity(
    source = source,
    author = author,
    title = title,
    description = description,
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    content = content
)