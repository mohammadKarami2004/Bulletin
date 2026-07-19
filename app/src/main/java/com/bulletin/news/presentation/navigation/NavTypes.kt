package com.bulletin.news.presentation.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json

inline fun <reified T> serializableNavType(
    isNullableAllowed: Boolean = false,
    json: Json = Json
): NavType<T> = object : NavType<T>(isNullableAllowed = isNullableAllowed) {

    override fun get(bundle: Bundle, key: String): T? {
        return bundle.getString(key)?.let { json.decodeFromString<T>(it) }
    }

    override fun parseValue(value: String): T {
        return json.decodeFromString<T>(Uri.decode(value))
    }

    override fun serializeAsValue(value: T): String {
        return Uri.encode(json.encodeToString(value))
    }

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, json.encodeToString(value))
    }
}