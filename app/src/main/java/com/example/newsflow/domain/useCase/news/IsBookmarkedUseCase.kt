package com.example.newsflow.domain.useCase.news

import com.example.newsflow.domain.repository.NewsRepository
import javax.inject.Inject

class IsBookmarkedUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    suspend fun invoke(url: String): Boolean {
        return newsRepository.isBookmarked(url)

    }

}