package com.sfaxdroid.data.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Call

open class BaseNetwork {

    suspend fun <T> request(
        call: Call<T>,
        transform: (T) -> Response,
        default: T?
    ): Response {
        return withContext(Dispatchers.IO) {
            try {
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
}

sealed class Response {
    class SUCESS(var response: Any) : Response()
    class FAILURE(var failure: Failure) : Response()
}
