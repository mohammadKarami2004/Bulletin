package com.bulletin.news.presentation.bookmark

import com.bulletin.news.domain.model.Article

data class BookmarkUiState(
    val news: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
