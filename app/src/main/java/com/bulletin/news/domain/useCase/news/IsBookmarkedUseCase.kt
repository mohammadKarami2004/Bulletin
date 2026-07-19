package com.bulletin.news.domain.useCase.news

import com.bulletin.news.domain.repository.NewsRepository
import javax.inject.Inject

class IsBookmarkedUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    suspend operator fun invoke(url: String): Boolean {
        return newsRepository.isBookmarked(url)

    }

}