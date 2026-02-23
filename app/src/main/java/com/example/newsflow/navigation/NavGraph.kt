package com.example.newsflow.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newsflow.presentation.auth.LoginScreen
import com.example.newsflow.presentation.auth.LoginViewModel
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



    NavHost(navController = navController, startDestination = startDestination) {
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

    }


}