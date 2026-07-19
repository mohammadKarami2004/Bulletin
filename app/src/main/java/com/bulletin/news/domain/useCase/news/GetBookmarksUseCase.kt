package com.bulletin.news.domain.useCase.news

import com.bulletin.news.domain.repository.NewsRepository
import javax.inject.Inject

class GetBookmarksUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    operator fun invoke() = newsRepository.getBookmarks()

}