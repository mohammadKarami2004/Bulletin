package com.bulletin.news.domain.useCase.news

import com.bulletin.news.domain.model.Article
import com.bulletin.news.domain.repository.NewsRepository
import com.bulletin.news.core.utils.Resource
import javax.inject.Inject

class SearchNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(query: String): Resource<List<Article>> {
        return newsRepository.searchNews(query)
    }
}