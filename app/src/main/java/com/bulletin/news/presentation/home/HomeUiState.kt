package com.bulletin.news.presentation.home

import com.bulletin.news.domain.model.Article

data class HomeUiState(
    val news: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)