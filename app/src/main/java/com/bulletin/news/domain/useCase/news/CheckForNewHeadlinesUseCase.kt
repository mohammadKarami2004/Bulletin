package com.bulletin.news.domain.useCase.news

import com.bulletin.news.core.utils.Resource
import com.bulletin.news.domain.repository.NewsRepository
import javax.inject.Inject

class CheckForNewHeadlinesUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(): Resource<Unit> {
        return repository.checkForNewHeadlines()
    }
}