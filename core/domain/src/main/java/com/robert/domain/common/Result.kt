package com.robert.domain.common


sealed class Result<out T> {

    data class Data<T>(val value: T) : Result<T>()

    data class Error(
        val message: String? = null,
        val throwable: Throwable? = null,
    ) : Result<Nothing>()

    data object Loading : Result<Nothing>()
}
