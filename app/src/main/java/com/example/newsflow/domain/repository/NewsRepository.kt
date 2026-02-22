package com.example.newsflow.domain.repository

import com.example.newsflow.domain.model.Article
import com.example.newsflow.utils.Resource

interface NewsRepository {
    suspend fun getHeadlines() : Resource<List<Article>>
}