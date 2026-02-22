package com.example.newsflow.domain.useCase.news

import com.example.newsflow.domain.model.Article
import com.example.newsflow.domain.repository.NewsRepository
import com.example.newsflow.utils.Resource
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    suspend fun invoke() : Resource<List<Article>>{
        return newsRepository.getHeadlines()
    }
}


