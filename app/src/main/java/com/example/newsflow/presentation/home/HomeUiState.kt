package com.example.newsflow.presentation.home

import com.example.newsflow.domain.model.Article

data class HomeUiState(
    val news: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)