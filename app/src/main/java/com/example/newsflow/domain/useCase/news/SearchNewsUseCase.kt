package com.example.newsflow.domain.useCase.news

import com.example.newsflow.domain.model.Article
import com.example.newsflow.domain.repository.NewsRepository
import com.example.newsflow.utils.Resource
import javax.inject.Inject

class SearchNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend fun invoke(query: String): Resource<List<Article>> {
        return newsRepository.searchNews(query)
    }
}