package com.example.bulletin.data.repository

import com.bulletin.news.core.utils.Resource
import com.bulletin.news.data.local.db.AppDataBase
import com.bulletin.news.data.remote.dto.news.NewsResponse
import com.bulletin.news.data.repository.NewsRepositoryImpl
import com.example.bulletin.data.remote.paging.FakeNewsApi
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

/**
 * تست‌های checkForNewHeadlines - متد سبکی که فقط NewsSyncWorker استفاده‌ش
 * می‌کنه (برخلاف getHeadlinesPager که کل UI ازش استفاده می‌کنه).
 * AppDataBase اینجا نیازی نیست واقعی باشه چون این متد اصلاً بهش سر نمی‌زنه؛
 * یه mock ساده (relaxed) کافیه.
 */
class NewsRepositoryImplTest {

    private val fakeDb = mockk<AppDataBase>(relaxed = true)

    @Test
    fun `checkForNewHeadlines returns Success when the API call succeeds`() = runTest {
        val fakeApi = FakeNewsApi(
            pages = mapOf(
                1 to Response.success(
                    NewsResponse(
                        status = "ok",
                        totalResults = 0,
                        articles = emptyList()
                    )
                )
            )
        )
        val repository = NewsRepositoryImpl(fakeApi, fakeDb)

        val result = repository.checkForNewHeadlines()

        assertTrue(result is Resource.Success<*>)
    }

    @Test
    fun `checkForNewHeadlines returns Error when the API call fails`() = runTest {
        val fakeApi = FakeNewsApi(pages = emptyMap(), errorOnPage = 1)
        val repository = NewsRepositoryImpl(fakeApi, fakeDb)

        val result = repository.checkForNewHeadlines()

        assertTrue(result is Resource.Error<*>)
    }
}