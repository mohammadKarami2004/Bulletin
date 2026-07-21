package com.bulletin.news.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bulletin.news.data.remote.dto.news.ArticleDto
import retrofit2.HttpException
import retrofit2.Response
import com.bulletin.news.data.remote.dto.news.NewsResponse
import java.io.IOException

abstract class BaseNewsPagingSource : PagingSource<Int, ArticleDto>() {

    private val seenUrls = mutableSetOf<String>()

    abstract suspend fun fetchPage(page: Int, pageSize: Int): Response<NewsResponse>

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleDto> {
        val page = params.key ?: STARTING_PAGE

        return try {
            val response = fetchPage(page, params.loadSize)

            if (!response.isSuccessful) {
                return LoadResult.Error(HttpException(response))
            }

            val rawArticles = response.body()?.articles.orEmpty()
            val articles = rawArticles.filter { seenUrls.add(it.url) }

            LoadResult.Page(
                data = articles,
                prevKey = if (page == STARTING_PAGE) null else page - 1,
                nextKey = if (rawArticles.isEmpty()) null else page + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ArticleDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val STARTING_PAGE = 1
    }
}