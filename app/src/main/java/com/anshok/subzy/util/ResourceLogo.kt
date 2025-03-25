package com.anshok.subzy.util

sealed interface ResourceLogo<T> {
    data class Success<T>(val data: T) : ResourceLogo<T>
    data class Error<T>(val type: ResponseError, val message: String? = null) : ResourceLogo<T>

    enum class ResponseError {
        NO_INTERNET,
        CLIENT_ERROR,
        SERVER_ERROR,
        NOT_FOUND,
        UNKNOWN_ERROR // Новый случай
    }
}
