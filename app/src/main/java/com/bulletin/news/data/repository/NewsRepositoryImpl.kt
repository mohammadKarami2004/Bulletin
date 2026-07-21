package com.bulletin.news.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.bulletin.news.core.utils.Resource
import com.bulletin.news.core.utils.map
import com.bulletin.news.core.utils.safeApiCall
import com.bulletin.news.data.local.db.AppDataBase
import com.bulletin.news.data.mapper.toDomain
import com.bulletin.news.data.mapper.toEntity
import com.bulletin.news.data.remote.api.NewsApi
import com.bulletin.news.data.remote.paging.ArticlesRemoteMediator
import com.bulletin.news.data.remote.paging.SearchPagingSource
import com.bulletin.news.domain.model.Article
import com.bulletin.news.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi,
    private val db: AppDataBase
) : NewsRepository {

    /**
     * Offline-first: به‌جای خوندن مستقیم از شبکه، Room اینجا source of truth ئه.
     * ArticlesRemoteMediator مسئوله Room رو از شبکه پر نگه داره؛ خودِ Pager
     * همیشه از cached_articles می‌خونه - یعنی حتی آفلاین هم آخرین دیتای
     * موفق رو نشون می‌ده، نه یه صفحه‌ی خالی.
     * https://developer.android.com/topic/libraries/architecture/paging/v3-network-db
     */
    @OptIn(ExperimentalPagingApi::class)
    override fun getHeadlinesPager(category: String?): Flow<PagingData<Article>> {
        val categoryKey = category ?: "all"
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = ArticlesRemoteMediator(category, newsApi, db)
        ) {
            db.cachedArticleDao().pagingSource(categoryKey)
        }.flow.map { pagingData -> pagingData.map { it.toDomain() } }
    }

    // سرچ عمداً cache نمی‌شه: تعداد query های ممکنه نامحدوده، پس نگه‌داشتنش
    // توی Room ارزشی نداره (بر خلاف چندتا category ثابتِ headlines).
    override fun searchNewsPager(query: String): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false)
        ) {
            SearchPagingSource(newsApi, query)
        }.flow.map { pagingData -> pagingData.map { it.toDomain() } }
    }

    override suspend fun checkForNewHeadlines(): Resource<Unit> {
        return safeApiCall { newsApi.getTopHeadlines() }.map { }
    }

    override suspend fun bookmarkArticle(article: Article) {
        db.articleDao().insertArticles(listOf(article.toEntity()))
    }

    override suspend fun deleteBookmark(article: Article) {
        db.articleDao().deleteArticle(article.toEntity())
    }

    override fun getBookmarks(): Flow<List<Article>> {
        return db.articleDao().getAllArticles().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun isBookmarked(url: String): Boolean {
        return db.articleDao().isBookmarked(url)
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}