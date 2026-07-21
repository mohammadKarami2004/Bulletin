package com.bulletin.news.presentation.home

data class NewsCategory(val id: String?, val label: String)

val newsCategories = listOf(
    NewsCategory(id = null, label = "All"),
    NewsCategory(id = "general", label = "General"),
    NewsCategory(id = "business", label = "Business"),
    NewsCategory(id = "technology", label = "Technology"),
    NewsCategory(id = "science", label = "Science"),
    NewsCategory(id = "health", label = "Health"),
    NewsCategory(id = "sports", label = "Sports"),
    NewsCategory(id = "entertainment", label = "Entertainment")
)