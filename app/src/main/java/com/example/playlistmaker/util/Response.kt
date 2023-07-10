package com.example.playlistmaker.util

sealed class Response<T>(val data: T? = null, val isFailed: Boolean? = null) {
    class Success<T>(data: T): Response<T>(data)
    class Error<T>(isFailed: Boolean, data: T? = null): Response<T>(data, isFailed)
}
