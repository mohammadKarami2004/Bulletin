package com.bulletin.news.presentation.detail

import com.bulletin.news.domain.model.Article

data class DetailUiState(
    val article: Article,
    val isBookmarked: Boolean = false
)
