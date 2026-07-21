package com.bulletin.news.presentation.navigation

import android.content.Intent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.bulletin.news.presentation.bookmark.BookmarkScreen
import com.bulletin.news.presentation.bookmark.BookmarkViewModel
import com.bulletin.news.presentation.detail.DetailScreen
import com.bulletin.news.presentation.detail.DetailViewModel
import com.bulletin.news.presentation.home.HomeScreen
import com.bulletin.news.presentation.home.HomeViewModel
import com.bulletin.news.presentation.settings.SettingsScreen
import com.bulletin.news.presentation.settings.SettingsViewModel
import com.bulletin.news.core.utils.NetworkState
import com.bulletin.news.domain.model.Article
import kotlin.reflect.typeOf

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavGraph() {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val isDetailScreen = currentDestination?.hasRoute(Screen.DetailScreen::class) == true


    Scaffold(
        topBar = {
            val isConnected by NetworkState.isConnected.collectAsStateWithLifecycle()
            if (!isConnected) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.error)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Internet is not available!", color = MaterialTheme.colorScheme.onError)
                }
            }
        },
        bottomBar = {
            if (!isDetailScreen) {
                BottomNavBar(
                    currentDestination = currentDestination,
                    onItemClick = { screen ->
                        navController.navigate(screen) {
                            popUpTo(Screen.HomeScreen) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        SharedTransitionLayout {
            NavHost(
                navController = navController,
                startDestination = Screen.HomeScreen,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable<Screen.HomeScreen> {
                    val viewModel: HomeViewModel = hiltViewModel()
                    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
                    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
                    val articles = viewModel.articles.collectAsLazyPagingItems()
                    HomeScreen(
                        articles = articles,
                        searchQuery = searchQuery,
                        selectedCategory = selectedCategory,
                        onSearchQueryChanged = viewModel::onSearchQueryChanged,
                        onCategorySelected = viewModel::onCategorySelected,
                        onArticleClick = { article -> navController.navigate(Screen.DetailScreen(article)) },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@composable
                    )
                }
                composable<Screen.BookmarkScreen> {
                    val viewModel: BookmarkViewModel = hiltViewModel()
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                    BookmarkScreen(
                        uiState = uiState,
                        onDelete = viewModel::deleteBookmark,
                        onArticleClick = { article -> navController.navigate(Screen.DetailScreen(article)) }
                    )
                }
                composable<Screen.DetailScreen>(
                    typeMap = mapOf(typeOf<Article>() to serializableNavType<Article>())
                ) {
                    val viewModel: DetailViewModel = hiltViewModel()
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                    val context = LocalContext.current

                    DetailScreen(
                        uiState = uiState,
                        onBackClick = { navController.popBackStack() },
                        onBookmarkClick = viewModel::toggleBookmark,
                        onShareClick = { url ->
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, url)
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share article"))
                        },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@composable
                    )
                }
                composable<Screen.SettingsScreen> {
                    val viewModel: SettingsViewModel = hiltViewModel()
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                    SettingsScreen(
                        uiState = uiState,
                        onDarkModeToggle = viewModel::toggleDarkMode
                    )
                }
            }
        }
    }
}