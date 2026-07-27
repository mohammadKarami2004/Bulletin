package com.bulletin.news.data.remote.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.bulletin.news.data.local.db.AppDataBase
import com.bulletin.news.data.local.db.entity.CachedArticleEntity
import com.bulletin.news.data.local.db.entity.RemoteKeyEntity
import com.bulletin.news.data.mapper.toCachedEntity
import com.bulletin.news.data.remote.api.NewsApi
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ArticlesRemoteMediator(
    category: String?,
    private val newsApi: NewsApi,
    private val db: AppDataBase
) : RemoteMediator<Int, CachedArticleEntity>() {

    private val categoryKey = category ?: "all"
    private val apiCategory = category

    override suspend fun initialize(): InitializeAction {
        val hasCachedData = db.cachedArticleDao().countByCategory(categoryKey) > 0
        return if (hasCachedData) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CachedArticleEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val remoteKey = db.remoteKeyDao().remoteKeyByCategory(categoryKey)
                    val nextKey = remoteKey?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    nextKey
                }
            }

            val response = newsApi.getTopHeadlines(
                category = apiCategory,
                page = page,
                pageSize = state.config.pageSize
            )

            if (!response.isSuccessful) {
                return MediatorResult.Error(HttpException(response))
            }

            val rawArticles = response.body()?.articles.orEmpty()
            val endOfPaginationReached = rawArticles.isEmpty()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.cachedArticleDao().clearCategory(categoryKey)
                    db.remoteKeyDao().insertOrReplace(RemoteKeyEntity(categoryKey, nextKey = null))
                }

                val nextKey = if (endOfPaginationReached) null else page + 1
                db.remoteKeyDao().insertOrReplace(RemoteKeyEntity(categoryKey, nextKey = nextKey))

                val baseOrder = (page - 1) * state.config.pageSize
                db.cachedArticleDao().insertAll(
                    rawArticles.mapIndexed { index, dto ->
                        dto.toCachedEntity(category = categoryKey, sortOrder = baseOrder + index)
                    }
                )
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}