package com.example.newsflow.navigation

import android.net.Uri
import com.example.newsflow.domain.model.Article
import com.google.gson.Gson

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home_screen")
    object LoginScreen : Screen("login_screen")
    object BookmarkScreen : Screen("bookmark_screen")
    object SettingsScreen : Screen("settings_screen")
    object DetailScreen : Screen("detail_screen/{article}") {
        fun createRoute(article: Article) =
            "detail_screen/${Uri.encode(Gson().toJson(article))}"
    }
}