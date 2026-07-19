package com.bulletin.news.data.mapper

import com.bulletin.news.data.local.db.entity.ArticleEntity
import com.bulletin.news.data.remote.dto.news.ArticleDto
import com.bulletin.news.domain.model.Article

fun ArticleDto.toDomain() = Article(
    source = source.name ?: "",
    author = author,
    title = title,
    description = description,
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    content = content
)
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