package com.example.newsflow.data.repository

import com.example.newsflow.data.local.db.dao.ArticleDao
import com.example.newsflow.data.mapper.toDomain
import com.example.newsflow.data.mapper.toEntity
import com.example.newsflow.data.remote.api.NewsApi
import com.example.newsflow.domain.model.Article
import com.example.newsflow.domain.repository.NewsRepository
import com.example.newsflow.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(private val newsApi: NewsApi,
    private val articleDao: ArticleDao) : NewsRepository {
    override suspend fun getHeadlines(): Resource<List<Article>> {
        return try {
            val response = newsApi.getTopHeadlines("us")
            if (response.isSuccessful && response.body()!=null){
                Resource.Success(response.body()!!.articles.map { it.toDomain() })
            }else{
                Resource.Error(response.message())
            }
    }catch (e: Exception){
            Resource.Error(e.message ?: "خطای ناشناخته")
        }
    }

    override suspend fun searchNews(query: String): Resource<List<Article>> {
        return try {
            val response = newsApi.searchNews(query)
            if (response.isSuccessful && response.body()!=null){
                Resource.Success(response.body()!!.articles.map { it.toDomain() })
            }else{
                Resource.Error(response.message())
            }
        }catch (e: Exception){
            Resource.Error(e.message ?: "خطای ناشناخته")
        }
    }

    override suspend fun bookmarkArticle(article: Article) {
        articleDao.insertArticles(listOf(article.toEntity()))
    }

    override suspend fun deleteBookmark(article: Article) {
        articleDao.deleteArticle(article.toEntity())
    }

    override fun getBookmarks(): Flow<List<Article>> {
        return articleDao.getAllArticles().map { articles -> articles.map { article -> article.toDomain() }}

    }
    override suspend fun isBookmarked(url: String): Boolean {
        return articleDao.isBookmarked(url)
    }

}