package com.sfaxdroid.data.repositories

import retrofit2.Call

open class BaseNetwork {

    fun <T> request(
        call: Call<T>,
        transform: (T) -> Response,
        default: T?
    ): Response {
        return try {
            val response = call.execute()
            when (response.isSuccessful) {
                true -> transform((response.body() ?: default!!))
                false -> Response.FAILURE(Failure.FeatureFailure)
            }
        } catch (exception: Throwable) {
            Response.FAILURE(Failure.ServerError)
        }
    }
}

sealed class Response {
    class SUCESS(var response: Any) : Response()
    class FAILURE(var failure: Failure) : Response()
}
