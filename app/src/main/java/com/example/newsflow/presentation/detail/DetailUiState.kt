package com.example.newsflow.presentation.detail

import com.example.newsflow.domain.model.Article

data class DetailUiState(
    val article: Article? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isBookmarked: Boolean = false
)