package com.bulletin.news.domain.useCase.news

import androidx.paging.PagingData
import com.bulletin.news.domain.model.Article
import com.bulletin.news.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHeadlinesUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke(category: String? = null): Flow<PagingData<Article>> {
        return repository.getHeadlinesPager(category)
    }
}