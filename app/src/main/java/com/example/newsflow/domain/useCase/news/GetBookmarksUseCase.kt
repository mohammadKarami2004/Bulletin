package com.example.newsflow.domain.useCase.news

import com.example.newsflow.domain.repository.NewsRepository
import javax.inject.Inject

class GetBookmarksUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    fun invoke() = newsRepository.getBookmarks()

}