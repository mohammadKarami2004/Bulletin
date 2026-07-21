package com.bulletin.news.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * جدول جدا برای cache کردن فید اصلی (headlines)، جدا از ArticleEntity
 * که برای بوکمارک‌هاست. این دوتا رو عمداً جدا نگه داشتیم چون معنی
 * متفاوتی دارن: این جدول فقط یه cache موقتیه (با هر refresh ممکنه
 * پاک/جایگزین بشه)، ولی ArticleEntity (بوکمارک) باید تا وقتی کاربر
 * خودش حذفش نکنه بمونه.
 */
@Entity(tableName = "cached_articles")
data class CachedArticleEntity(
    @PrimaryKey val url: String,
    val category: String,
    val source: String,
    val author: String?,
    val title: String,
    val description: String?,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?,
    // برای حفظ همون ترتیبی که NewsAPI برگردونده (چون ORDER BY url یا publishedAt
    // لزوماً همون ترتیب API نیست)
    val sortOrder: Int
)