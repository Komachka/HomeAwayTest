package com.kstor.homeawaytest.domain

sealed class RepoResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : RepoResult<T>()
    data class Error<out T : Any>(val throwable: Throwable) : RepoResult<Nothing>()
}
