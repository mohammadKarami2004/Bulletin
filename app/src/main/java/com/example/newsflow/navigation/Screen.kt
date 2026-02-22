package com.example.newsflow.navigation

sealed class Screen(val route : String) {
    object HomeScreen : Screen("home_screen")
    object LoginScreen : Screen("login_screen")
}