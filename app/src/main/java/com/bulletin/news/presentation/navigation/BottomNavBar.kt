package com.bulletin.news.presentation.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute

@Composable
fun BottomNavBar(
    currentDestination: NavDestination?,
    onItemClick: (Screen) -> Unit
) {
    NavigationBar {
        BottomNavItem.items.forEach { item ->
            val selected = currentDestination?.hasRoute(item.screen::class) == true
            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item.screen) },
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) }
            )
        }
    }
}
