package com.bulletin.news.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val screen: Screen
) {
    data object Home : BottomNavItem(
        title = "home",
        icon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home,
        screen = Screen.HomeScreen
    )
    data object Bookmarks : BottomNavItem(
        title = "saved",
        icon = Icons.Outlined.BookmarkBorder,
        selectedIcon = Icons.Filled.Bookmark,
        screen = Screen.BookmarkScreen
    )
    data object Settings : BottomNavItem(
        title = "settings",
        icon = Icons.Outlined.Settings,
        selectedIcon = Icons.Filled.Settings,
        screen = Screen.SettingsScreen
    )

    companion object {
        val items = listOf(Home, Bookmarks, Settings)
    }
}
