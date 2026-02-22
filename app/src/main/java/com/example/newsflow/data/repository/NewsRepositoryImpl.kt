package com.example.newsflow.data.repository

import com.example.newsflow.data.mapper.toDomain
import com.example.newsflow.data.remote.api.NewsApi
import com.example.newsflow.domain.model.Article
import com.example.newsflow.domain.repository.NewsRepository
import com.example.newsflow.utils.Resource
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(private val newsApi: NewsApi) : NewsRepository {
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
}