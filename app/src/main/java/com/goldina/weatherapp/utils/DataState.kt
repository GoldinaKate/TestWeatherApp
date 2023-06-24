package com.goldina.weatherapp.utils

sealed class DataState<out R> {
    data class Success<out T>(val data: T): DataState<T>()
    data class Error(val exception: Throwable): DataState<Nothing>()
    data class OtherError(val error: String): DataState<Nothing>()
    object Loading: DataState<Nothing>()
}
