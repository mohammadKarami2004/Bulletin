package com.bulletin.news.core.utils

import retrofit2.Response


sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()

    data class Error<T>(
        val message: String,
        val code: Int? = null,           // اضافه شد
        val exception: Throwable? = null // اضافه شد
    ) : Resource<T>()

    data class Loading<T>(val progress: Int? = null) : Resource<T>() // پیشرفت اختیاری
}



suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Resource<T> {
    return try {
        val response = apiCall.invoke()

        if (response.isSuccessful) {
            response.body()?.let {
                Resource.Success(it)
            } ?: Resource.Error("Response body is null")
        } else {
            Resource.Error(
                message = response.message() ?: "Unknown API error",
                code = response.code()
            )
        }
    } catch (e: Exception) {
        Resource.Error(
            message = e.message ?: "Network error occurred",
            exception = e
        )
    }
}

inline fun <T, R> Resource<T>.map(transform: (T) -> R): Resource<R> {
    return when (this) {
        is Resource.Success -> Resource.Success(transform(data))
        is Resource.Error -> Resource.Error(message, code, exception)
        is Resource.Loading -> Resource.Loading()
    }
}