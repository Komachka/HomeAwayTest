package com.kstor.homeawaytest.data

sealed class ApiResult<out T : Any> {
    data class Succsses<out T : Any>(val data: T) : ApiResult<T>()
    data class Error<out T : Any>(val throwable: Throwable) : ApiResult<Nothing>()
}
