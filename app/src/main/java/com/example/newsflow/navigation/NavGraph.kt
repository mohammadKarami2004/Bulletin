package com.example.newsflow.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.newsflow.presentation.auth.LoginScreen
import com.example.newsflow.presentation.auth.LoginViewModel
import com.example.newsflow.presentation.bookmark.BookmarkScreen
import com.example.newsflow.presentation.bookmark.BookmarkViewModel
import com.example.newsflow.presentation.home.HomeScreen
import com.example.newsflow.presentation.home.HomeViewModel
import com.example.newsflow.presentation.main.MainViewModel
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

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.LoginScreen.route) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onItemClick = { navController.navigate(it) }
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
                HomeScreen(uiState)
            }
            composable(Screen.BookmarkScreen.route) {
                val viewModel: BookmarkViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                BookmarkScreen(uiState, onDelete = viewModel::deleteBookmark)

            }

        }


    }
}