package com.bulletin.news.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * RemoteMediator باید بین اجراها (و حتی بعد از بسته‌شدن اپ) بدونه
 * "صفحه‌ی بعدی کدومه". چون Paging خودش این حالت رو نگه نمی‌داره
 * (برخلاف PagingSource ساده که هربار از صفر می‌سازیمش)، این اطلاعات
 * باید جایی persist بشه - همین جدول.
 *
 * یه ردیف به‌ازای هر دسته‌بندی (category == "all" برای فید بدون فیلتر).
 */
@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey val category: String,
    val nextKey: Int?
)