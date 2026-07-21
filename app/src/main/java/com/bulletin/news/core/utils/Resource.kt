package com.bulletin.news.core.utils

import retrofit2.HttpException
import retrofit2.Response

sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val error: AppError) : Resource<T>()
    data class Loading<T>(val progress: Int? = null) : Resource<T>() // پیشرفت اختیاری
}

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Resource<T> {
    return try {
        val response = apiCall.invoke()

        if (response.isSuccessful) {
            response.body()?.let {
                Resource.Success(it)
            } ?: Resource.Error(AppError.Unknown("Response body is null"))
        } else {
            Resource.Error(HttpException(response).toAppError())
        }
    } catch (e: Exception) {
        Resource.Error(e.toAppError())
    }
}

inline fun <T, R> Resource<T>.map(transform: (T) -> R): Resource<R> {
    return when (this) {
        is Resource.Success -> Resource.Success(transform(data))
        is Resource.Error -> Resource.Error(error)
        is Resource.Loading -> Resource.Loading(progress)
    }
}