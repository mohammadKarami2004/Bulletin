package com.bulletin.news.data.remote.paging

import com.bulletin.news.data.remote.api.NewsApi
import com.bulletin.news.data.remote.dto.news.NewsResponse
import retrofit2.Response

class HeadlinesPagingSource(
    private val api: NewsApi,
    private val category: String?
) : BaseNewsPagingSource() {

    override suspend fun fetchPage(page: Int, pageSize: Int): Response<NewsResponse> {
        return api.getTopHeadlines(category = category, page = page, pageSize = pageSize)
    }
}