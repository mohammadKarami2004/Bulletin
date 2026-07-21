package com.bulletin.news.core.utils

import retrofit2.HttpException
import java.io.IOException

sealed class AppError(val userMessage: String, val isRetryable: Boolean) {

    data object NoInternet : AppError(
        userMessage = "No internet connection. Please check your network and try again.",
        isRetryable = true
    )

    data object RateLimited : AppError(
        userMessage = "Too many requests right now. Please wait a moment and try again.",
        isRetryable = true
    )

    data object Unauthorized : AppError(
        userMessage = "There's a configuration problem with the news service.",
        isRetryable = false
    )

    data class ServerError(val code: Int) : AppError(
        userMessage = "The news service is having issues right now. Please try again later.",
        isRetryable = true
    )

    data class Unknown(val raw: String?) : AppError(
        userMessage = raw?.takeIf { it.isNotBlank() } ?: "Something went wrong.",
        isRetryable = true
    )
}

fun Throwable.toAppError(): AppError = when (this) {
    is IOException -> AppError.NoInternet

    is HttpException -> when (code()) {
        401, 403 -> AppError.Unauthorized
        429 -> AppError.RateLimited
        in 500..599 -> AppError.ServerError(code())
        else -> AppError.Unknown(message())
    }

    else -> AppError.Unknown(localizedMessage)
}