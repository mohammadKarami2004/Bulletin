package com.bulletin.news.domain.useCase.news

import androidx.paging.PagingData
import com.bulletin.news.domain.model.Article
import com.bulletin.news.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke(query: String): Flow<PagingData<Article>> {
        return repository.searchNewsPager(query)
    }
}