package com.example.bulletin.data.remote.paging

import androidx.paging.PagingSource
import com.bulletin.news.data.remote.dto.news.ArticleDto
import com.bulletin.news.data.remote.dto.news.NewsResponse
import com.bulletin.news.data.remote.dto.news.Source
import com.bulletin.news.data.remote.paging.HeadlinesPagingSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

/**
 * تست‌های BaseNewsPagingSource (از طریق HeadlinesPagingSource، چون خودِ
 * BaseNewsPagingSource abstract هست و تفاوتی برای این تست‌ها نداره).
 *
 * این تست‌ها دقیقاً همون سناریویی رو پوشش می‌دن که موقع تست دستی روی گوشی
 * واقعی پیدا شد: NewsAPI گاهی یه مقاله رو توی چند صفحه تکرار می‌کنه، و
 * دفعه‌ی اول که این رو فیکس کردیم (فقط با فیلترکردن تکراری‌ها)، یه باگ
 * جدید ساختیم: اگه یه صفحه‌ی کامل تکراری باشه، pagination اشتباهی متوقف
 * می‌شد. این تست‌ها هر دو رفتار رو مستند می‌کنن تا دوباره خراب نشن.
 */
class BaseNewsPagingSourceTest {

    private fun article(url: String) = ArticleDto(
        source = Source(id = null, name = "Test Source"),
        author = "Author",
        title = "Title for $url",
        description = "description",
        url = url,
        urlToImage = null,
        publishedAt = "2026-07-01T00:00:00Z",
        content = "content"
    )

    private fun successResponse(articles: List<ArticleDto>): Response<NewsResponse> =
        Response.success(NewsResponse(status = "ok", totalResults = articles.size, articles = articles))

    private fun refresh() =
        PagingSource.LoadParams.Refresh<Int>(key = null, loadSize = 20, placeholdersEnabled = false)

    private fun append(key: Int) =
        PagingSource.LoadParams.Append(key = key, loadSize = 20, placeholdersEnabled = false)

    @Test
    fun `first page loads correctly and sets nextKey`() = runTest {
        val fakeApi = FakeNewsApi(
            pages = mapOf(1 to successResponse(listOf(article("a"), article("b"))))
        )
        val pagingSource = HeadlinesPagingSource(fakeApi, category = null)

        val result = pagingSource.load(refresh()) as PagingSource.LoadResult.Page

        assertEquals(listOf("a", "b"), result.data.map { it.url })
        assertEquals(2, result.nextKey)
        assertNull(result.prevKey)
    }

    @Test
    fun `duplicate articles across pages are filtered out`() = runTest {
        val fakeApi = FakeNewsApi(
            pages = mapOf(
                1 to successResponse(listOf(article("a"), article("b"), article("c"))),
                // صفحه‌ی ۲: c تکراریِ صفحه‌ی ۱ه، d و e واقعاً جدیدن
                2 to successResponse(listOf(article("c"), article("d"), article("e")))
            )
        )
        val pagingSource = HeadlinesPagingSource(fakeApi, category = null)

        pagingSource.load(refresh())
        val page2 = pagingSource.load(append(2)) as PagingSource.LoadResult.Page

        assertEquals(listOf("d", "e"), page2.data.map { it.url })
    }

    @Test
    fun `a page that is entirely duplicates does not wrongly stop pagination`() = runTest {
        // رگرشن تست برای همون باگی که پیدا شد: صفحه‌ی ۲ کاملاً تکراریِ
        // صفحه‌ی ۱ه، ولی صفحه‌ی ۳ واقعاً خبر جدید داره. nextKey نباید
        // به‌خاطر خالی‌شدنِ لیست بعد از فیلتر، اشتباهی null بشه.
        val fakeApi = FakeNewsApi(
            pages = mapOf(
                1 to successResponse(listOf(article("a"), article("b"))),
                2 to successResponse(listOf(article("a"), article("b"))), // کاملاً تکراری
                3 to successResponse(listOf(article("f")))
            )
        )
        val pagingSource = HeadlinesPagingSource(fakeApi, category = null)

        pagingSource.load(refresh())
        val page2 = pagingSource.load(append(2)) as PagingSource.LoadResult.Page

        // نکته‌ی اصلی تست: بعد از فیلتر هیچی نمونده، ولی چون API خودش
        // خالی برنگردونده بود، باید بتونیم صفحه‌ی بعد رو هم بگیریم.
        assertTrue(page2.data.isEmpty())
        assertEquals(3, page2.nextKey)

        val page3 = pagingSource.load(append(3)) as PagingSource.LoadResult.Page
        assertEquals(listOf("f"), page3.data.map { it.url })
    }

    @Test
    fun `a truly empty response from the API stops pagination`() = runTest {
        val fakeApi = FakeNewsApi(
            pages = mapOf(
                1 to successResponse(listOf(article("a"))),
                2 to successResponse(emptyList()) // API واقعاً دیگه چیزی نداره
            )
        )
        val pagingSource = HeadlinesPagingSource(fakeApi, category = null)

        pagingSource.load(refresh())
        val page2 = pagingSource.load(append(2)) as PagingSource.LoadResult.Page

        assertNull(page2.nextKey)
    }

    @Test
    fun `HTTP error returns LoadResult Error instead of crashing`() = runTest {
        val fakeApi = FakeNewsApi(pages = emptyMap(), errorOnPage = 1)
        val pagingSource = HeadlinesPagingSource(fakeApi, category = null)

        val result = pagingSource.load(refresh())

        assertTrue(result is PagingSource.LoadResult.Error)
    }
}