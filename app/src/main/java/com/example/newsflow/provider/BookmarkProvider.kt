package com.example.newsflow.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import androidx.room.Room
import com.example.newsflow.data.local.db.AppDataBase
import androidx.core.net.toUri

class BookmarkProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "com.example.newsflow.provider"
        val CONTENT_URI: Uri = "content://$AUTHORITY/bookmarks".toUri()
    }

    private lateinit var db: AppDataBase

    override fun onCreate(): Boolean {
        db = Room.databaseBuilder(
            context!!,
            AppDataBase::class.java,
            "newsFlow.db"
        ).allowMainThreadQueries()
            .build()
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String?>?,
        selection: String?,
        selectionArgs: Array<out String?>?,
        sortOrder: String?
    ): Cursor? {
        return db.articleDao().getAllArticlesAsCursor()
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String?>?): Int = 0
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String?>?): Int = 0
    override fun getType(uri: Uri): String? = null
}