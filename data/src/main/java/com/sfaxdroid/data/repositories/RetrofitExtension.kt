package com.sfaxdroid.data.repositories

import com.sfaxdroid.data.entity.Logger
import com.sfaxdroid.data.entity.Response
import retrofit2.Call
import retrofit2.HttpException

fun <T> Call<T>.executeWs(): retrofit2.Response<T> {
    val call = if (isExecuted) clone() else this
    return call.execute()
}

fun <T> retrofit2.Response<T>.toResult(logger: Logger): Response<T> = try {
    logger.d(raw().networkResponse()?.request()?.url().toString())
    logger.d(message())
    if (isSuccessful) {
        Response.SUCCESS(body()!!)
    } else
        Response.FAILURE(toException())
} catch (e: Exception) {
    Response.FAILURE(toException())
}

fun <T> retrofit2.Response<T>.toException() = HttpException(this)
