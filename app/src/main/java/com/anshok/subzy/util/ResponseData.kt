package com.anshok.subzy.util

sealed interface ResponseData<T> {

    data class Success<T>(val data: T) : ResponseData<T>

    data class Error<T>(val type: ResponseError, val message: String? = null) : ResponseData<T>

    enum class ResponseError {
        NO_INTERNET,
        CLIENT_ERROR,
        SERVER_ERROR,
        NOT_FOUND,
        UNKNOWN_ERROR
    }
}
