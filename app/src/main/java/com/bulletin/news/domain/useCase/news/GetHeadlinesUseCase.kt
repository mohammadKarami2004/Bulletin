package com.bulletin.news.domain.useCase.news

import com.bulletin.news.domain.model.Article
import com.bulletin.news.domain.repository.NewsRepository
import com.bulletin.news.core.utils.Resource
import javax.inject.Inject

class GetHeadlinesUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(): Resource<List<Article>> {
        return repository.getHeadlines()
    }
}

