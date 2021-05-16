package com.sfaxdroid.data.repositories

import com.sfaxdroid.data.entity.Response
import retrofit2.Call
import retrofit2.HttpException

fun <T> Call<T>.executeWs(): retrofit2.Response<T> {
    val call = if (isExecuted) clone() else this
    return call.execute()
}

fun <T> retrofit2.Response<T>.toResult(): Response<T> = try {
    if (isSuccessful) {
        Response.SUCCESS(body()!!)
    } else
        Response.FAILURE(toException())
} catch (e: Exception) {
    Response.FAILURE(toException())
}

fun <T> retrofit2.Response<T>.toException() = HttpException(this)
