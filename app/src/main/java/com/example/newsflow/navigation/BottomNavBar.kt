package com.example.newsflow.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onItemClick: (String) -> Unit
) {
    NavigationBar {
        NavigationBarItem.btmNavItems.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = { onItemClick(screen.route) },
                icon = { Icon(screen.icon, screen.title) },
                label = { Text(screen.title) }
            )
        }
    }
}