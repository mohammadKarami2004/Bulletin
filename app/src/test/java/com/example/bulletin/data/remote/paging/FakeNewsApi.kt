package com.example.bulletin.data.remote.paging

import com.bulletin.news.data.remote.api.NewsApi
import com.bulletin.news.data.remote.dto.news.NewsResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

/**
 * پیاده‌سازی fake از NewsApi مخصوص تست - بدون نیاز به شبکه‌ی واقعی یا
 * کتابخونه‌ی mocking. هر شماره‌صفحه یه پاسخ از پیش‌تعریف‌شده برمی‌گردونه.
 *
 * @param pages نگاشت شماره‌صفحه -> پاسخی که باید برگرده
 * @param errorOnPage اگه ست بشه، دقیقاً همون صفحه یه پاسخ خطا (HTTP 500) برمی‌گردونه
 */
class FakeNewsApi(
    private val pages: Map<Int, Response<NewsResponse>>,
    private val errorOnPage: Int? = null
) : NewsApi {

    override suspend fun getTopHeadlines(
        country: String,
        category: String?,
        page: Int,
        pageSize: Int
    ): Response<NewsResponse> {
        if (page == errorOnPage) {
            return Response.error(
                500,
                "server error".toResponseBody("text/plain".toMediaTypeOrNull())
            )
        }
        return pages[page]
            ?: Response.success(NewsResponse(status = "ok", totalResults = 0, articles = emptyList()))
    }

    override suspend fun searchNews(query: String, page: Int, pageSize: Int): Response<NewsResponse> {
        return getTopHeadlines(country = "us", category = null, page = page, pageSize = pageSize)
    }
}