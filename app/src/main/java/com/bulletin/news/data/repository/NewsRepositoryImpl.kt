package com.bulletin.news.data.repository

import com.bulletin.news.core.utils.Resource
import com.bulletin.news.core.utils.map
import com.bulletin.news.core.utils.safeApiCall
import com.bulletin.news.data.local.db.dao.ArticleDao
import com.bulletin.news.data.mapper.toDomain
import com.bulletin.news.data.mapper.toEntity
import com.bulletin.news.data.remote.api.NewsApi
import com.bulletin.news.domain.model.Article
import com.bulletin.news.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi,
    private val articleDao: ArticleDao
) : NewsRepository {

    override suspend fun getHeadlines(): Resource<List<Article>> {
        return safeApiCall { newsApi.getTopHeadlines("us") }
            .map { response -> response.articles.map { it.toDomain() } }
    }

    override suspend fun searchNews(query: String): Resource<List<Article>> {
        return safeApiCall { newsApi.searchNews(query) }
            .map { response -> response.articles.map { it.toDomain() } }
    }

    override suspend fun bookmarkArticle(article: Article) {
        articleDao.insertArticles(listOf(article.toEntity()))
    }

    override suspend fun deleteBookmark(article: Article) {
        articleDao.deleteArticle(article.toEntity())
    }

    override fun getBookmarks(): Flow<List<Article>> {
        return articleDao.getAllArticles().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun isBookmarked(url: String): Boolean {
        return articleDao.isBookmarked(url)
    }

    }