package com.bulletin.news.data.remote.paging

import com.bulletin.news.data.remote.api.NewsApi
import com.bulletin.news.data.remote.dto.news.NewsResponse
import retrofit2.Response

class SearchPagingSource(
    private val api: NewsApi,
    private val query: String
) : BaseNewsPagingSource() {

    override suspend fun fetchPage(page: Int, pageSize: Int): Response<NewsResponse> {
        return api.searchNews(query = query, page = page, pageSize = pageSize)
    }
}