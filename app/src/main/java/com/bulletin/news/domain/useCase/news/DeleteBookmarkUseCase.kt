package com.bulletin.news.domain.useCase.news

import com.bulletin.news.domain.model.Article
import com.bulletin.news.domain.repository.NewsRepository
import javax.inject.Inject

class DeleteBookmarkUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(article: Article) = newsRepository.deleteBookmark(article)
}
