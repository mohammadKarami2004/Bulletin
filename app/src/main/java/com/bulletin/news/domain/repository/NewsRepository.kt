package com.bulletin.news.domain.repository

import androidx.paging.PagingData
import com.bulletin.news.domain.model.Article
import com.bulletin.news.core.utils.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getHeadlinesPager(category: String?): Flow<PagingData<Article>>
    fun searchNewsPager(query: String): Flow<PagingData<Article>>

    // متد سبک و one-shot، فقط برای NewsSyncWorker که نیازی به کل لیست/pagination نداره
    // و فقط می‌خواد بدونه fetch جواب داد یا نه (برای تصمیم notify کردن / retry کردن).
    suspend fun checkForNewHeadlines(): Resource<Unit>

    suspend fun bookmarkArticle(article: Article)
    suspend fun deleteBookmark(article: Article)
    fun getBookmarks(): Flow<List<Article>>
    suspend fun isBookmarked(url: String): Boolean
}