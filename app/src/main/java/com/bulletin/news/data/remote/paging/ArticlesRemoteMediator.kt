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

/**
 * پیاده‌سازی رسمی الگوی "Room به‌عنوان source of truth" برای Paging 3:
 * https://developer.android.com/topic/libraries/architecture/paging/v3-network-db
 *
 * تفاوت این با HeadlinesPagingSource قدیمی: اونجا هر بار مستقیم از شبکه
 * می‌خوندیم (و اگه آفلاین بودی، هیچی نداشتی نشون بدی). اینجا UI همیشه از
 * Room می‌خونه (که آفلاین هم در دسترسه)؛ این RemoteMediator فقط مسئول
 * پرکردن Room از شبکه‌ست، وقتی که لازم باشه.
 */
@OptIn(ExperimentalPagingApi::class)
class ArticlesRemoteMediator(
    category: String?,
    private val newsApi: NewsApi,
    private val db: AppDataBase
) : RemoteMediator<Int, CachedArticleEntity>() {

    private val categoryKey = category ?: "all"
    private val apiCategory = category

    override suspend fun initialize(): InitializeAction {
        // برای این پروژه cache قدیمی رو معتبر در نظر می‌گیریم تا وقتی
        // کاربر خودش refresh (pull-to-refresh) بزنه؛ یعنی موقع باز کردن
        // مجدد اپ، بلافاصله یه ریکوئست شبکه‌ی جدید نمی‌زنیم.
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CachedArticleEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1

                LoadType.PREPEND -> {
                    // چون همیشه از صفحه‌ی ۱ شروع می‌کنیم، هیچ‌وقت به عقب لود نمی‌کنیم
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

            // تصمیم "صفحه‌ی بعدی هست یا نه" رو از پاسخ خام API می‌گیریم،
            // نه از چیزی که بعداً توی Room ذخیره می‌شه - همون درسی که از
            // باگ HeadlinesPagingSource قبلی گرفتیم.
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