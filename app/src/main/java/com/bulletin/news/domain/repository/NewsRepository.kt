package com.bulletin.news.domain.repository

import com.bulletin.news.domain.model.Article
import com.bulletin.news.core.utils.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getHeadlines(): Resource<List<Article>>
    suspend fun searchNews(query: String): Resource<List<Article>>

    suspend fun bookmarkArticle(article: Article)
    suspend fun deleteBookmark(article: Article)
    fun getBookmarks(): Flow<List<Article>>
    suspend fun isBookmarked(url: String): Boolean
}