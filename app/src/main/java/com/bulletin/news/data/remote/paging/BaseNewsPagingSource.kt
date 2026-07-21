package com.bulletin.news.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bulletin.news.data.remote.dto.news.ArticleDto
import retrofit2.HttpException
import retrofit2.Response
import com.bulletin.news.data.remote.dto.news.NewsResponse
import java.io.IOException

/**
 * منطق مشترک صفحه‌بندی برای هر دو منبع (top-headlines و everything).
 * طبق راهنمای رسمی Paging 3:
 * https://developer.android.com/topic/libraries/architecture/paging/v3-paged-data#paging-source
 *
 * هر زیرکلاس فقط باید بگه چطور یه صفحه از دیتا رو fetch کنه؛ منطق
 * صفحه‌بندی (prevKey/nextKey/refreshKey) اینجا یه‌بار پیاده شده.
 */
abstract class BaseNewsPagingSource : PagingSource<Int, ArticleDto>() {

    // NewsAPI (به‌خصوص پلن رایگان) گاهی بین صفحه‌ها یه مقاله رو دوبار برمی‌گردونه
    // (چون لیستشون بین درخواست‌ها آپدیت می‌شه و ایندکس‌بندی داخلیشون جابه‌جا می‌شه).
    // چون `url` رو به‌عنوان key یکتا توی LazyColumn استفاده می‌کنیم، این تکراری‌ها
    // باید همین‌جا (توی لایه‌ی دیتا) فیلتر بشن، وگرنه Compose با
    // "Key was already used" کرش می‌کنه.
    // این Set طول عمر یه instance از PagingSource رو داره (یعنی یه "generation")
    // و با هر refresh/recreate از نو ساخته می‌شه، پس مشکلی برای pull-to-refresh نداره.
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
                // مهم: این باید بر اساس پاسخِ خام API باشه، نه لیست فیلترشده.
                // اگه یه صفحه کامل تکراریِ صفحه‌ی قبل باشه (که توی NewsAPI پیش میاد)،
                // بعد از فیلتر `articles` خالی می‌شه؛ ولی این به این معنی نیست که
                // صفحه‌ی بعدی هم خبر جدید نداره. تصمیم "ادامه بدیم یا نه" فقط باید
                // بر این باشه که خودِ API چیزی برگردونده یا نه.
                nextKey = if (rawArticles.isEmpty()) null else page + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ArticleDto>): Int? {
        // موقع refresh (مثلاً pull-to-refresh) نزدیک‌ترین صفحه به موقعیت فعلی کاربر رو پیدا می‌کنیم
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val STARTING_PAGE = 1
    }
}