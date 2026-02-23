package com.example.newsflow.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationBarItem(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    object HomeScreen : NavigationBarItem("home", icon = Icons.Outlined.Home, "home_screen")
    object BookmarkScreen :
        NavigationBarItem("saved", icon = Icons.Outlined.BookmarkBorder, "bookmark_screen")

    companion object{
        val btmNavItems = listOf(HomeScreen, BookmarkScreen)
    }
}