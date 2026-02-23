package com.example.newsflow.presentation.bookmark

import com.example.newsflow.domain.model.Article

data class BookmarkUiState(
    val news: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
