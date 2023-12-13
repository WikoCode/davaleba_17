package com.example.myapplication.common

sealed class Resource<D>(
    val data: D? = null,
    val errorMessage: String? = null,
    val loading: Boolean = false
) {
    class Success<D>(data: D?) : Resource<D>(data = data)
    class Error<D>(errorMessage: String?) : Resource<D>(errorMessage = errorMessage)
    class Loading<T>(loading: Boolean): Resource<T>(loading = loading)
}