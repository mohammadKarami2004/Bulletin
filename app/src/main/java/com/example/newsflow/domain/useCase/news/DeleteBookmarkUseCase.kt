package com.example.newsflow.domain.useCase.news

import com.example.newsflow.domain.model.Article
import com.example.newsflow.domain.repository.NewsRepository
import javax.inject.Inject

class DeleteBookmarkUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend fun invoke(article: Article) = newsRepository.deleteBookmark(article)
}
