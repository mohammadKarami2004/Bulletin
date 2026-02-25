package com.example.newsflow.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationBarItem(
    val title: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val route: String
) {
    object HomeScreen : NavigationBarItem(
        title = "home",
        icon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home,
        route = "home_screen"
    )
    object BookmarkScreen : NavigationBarItem(
        title = "saved",
        icon = Icons.Outlined.BookmarkBorder,
        selectedIcon = Icons.Filled.Bookmark,
        route = "bookmark_screen"
    )
    object SettingsScreen : NavigationBarItem(
        title = "settings",
        icon = Icons.Outlined.Settings,
        selectedIcon = Icons.Filled.Settings,
        route = "settings_screen"
    )

    companion object {
        val btmNavItems = listOf(HomeScreen, BookmarkScreen,SettingsScreen)
    }
    }