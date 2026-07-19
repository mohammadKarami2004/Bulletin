package com.bulletin.news.presentation.navigation

import com.bulletin.news.domain.model.Article
import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object HomeScreen : Screen



    @Serializable
    data object BookmarkScreen : Screen

    @Serializable
    data object SettingsScreen : Screen

    @Serializable
    data class DetailScreen(val article: Article) : Screen
}
