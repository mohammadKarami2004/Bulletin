package com.example.newsflow.navigation

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.newsflow.presentation.auth.LoginScreen
import com.example.newsflow.presentation.auth.LoginViewModel
import com.example.newsflow.presentation.bookmark.BookmarkScreen
import com.example.newsflow.presentation.bookmark.BookmarkViewModel
import com.example.newsflow.presentation.detail.DetailScreen
import com.example.newsflow.presentation.detail.DetailViewModel
import com.example.newsflow.presentation.home.HomeScreen
import com.example.newsflow.presentation.home.HomeViewModel
import com.example.newsflow.presentation.main.MainViewModel
import com.example.newsflow.presentation.settings.SettingsScreen
import com.example.newsflow.presentation.settings.SettingsViewModel
import com.example.newsflow.utils.AuthState

@Composable
fun AppNavGraph() {
    val viewModel: MainViewModel = hiltViewModel()
    val authState by viewModel.authState.collectAsStateWithLifecycle()

    val startDestination = when (authState) {
        AuthState.Loading -> return
        AuthState.LoggedIn -> Screen.HomeScreen.route
        AuthState.LoggedOut -> Screen.LoginScreen.route
    }
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Log.d("Navigation", "currentRoute: $currentRoute")

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.LoginScreen.route &&
                currentRoute?.startsWith("detail_screen") == false){
                BottomNavBar(
                    currentRoute = currentRoute,
                    onItemClick = { route ->
                        navController.navigate(route) {
                            popUpTo(Screen.HomeScreen.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController, startDestination = startDestination, modifier = Modifier
                .padding(paddingValues)
        ) {
            composable(Screen.LoginScreen.route) {
                val viewModel: LoginViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                LaunchedEffect(uiState.isSuccess) {
                    if (uiState.isSuccess) {
                        navController.navigate(Screen.HomeScreen.route) {
                            popUpTo(Screen.LoginScreen.route)
                            { inclusive = true }
                        }

                    }
                }
                LoginScreen(
                    uiState = uiState,
                    onLoginClick = viewModel::login
                )
            }
            composable(Screen.HomeScreen.route) {
                val viewModel: HomeViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                HomeScreen(
                    uiState = uiState,
                    onArticleClick = { article ->
                        navController.navigate(Screen.DetailScreen.createRoute(article))
                    }
                )
            }
            composable(Screen.BookmarkScreen.route) {
                val viewModel: BookmarkViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                BookmarkScreen(
                    uiState = uiState,
                    onDelete = viewModel::deleteBookmark,
                    onArticleClick = { article ->
                        navController.navigate(Screen.DetailScreen.createRoute(article))
                    }
                )

            }
            composable(
                route = Screen.DetailScreen.route,
                arguments = listOf(navArgument("article") { type = NavType.StringType })
            ) {
                val viewModel: DetailViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                DetailScreen(
                    uiState = uiState,
                    onBookmarkClick = viewModel::toggleBookmark
                )
            }
            composable(Screen.SettingsScreen.route) {
                val viewModel: SettingsViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                SettingsScreen(
                    uiState = uiState,
                    onDarkModeToggle = viewModel::toggleDarkMode,
                    onLogoutClick = viewModel::logout
                )

            }

        }


    }
}