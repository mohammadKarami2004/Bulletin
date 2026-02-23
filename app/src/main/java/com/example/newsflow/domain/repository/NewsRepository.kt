package com.example.newsflow.domain.repository

import com.example.newsflow.domain.model.Article
import com.example.newsflow.utils.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getHeadlines(): Resource<List<Article>>
    suspend fun searchNews(query: String): Resource<List<Article>>
    suspend fun bookmarkArticle(article: Article)
    suspend fun deleteBookmark(article: Article)
    fun getBookmarks(): Flow<List<Article>>
    suspend fun isBookmarked(url: String): Boolean




}