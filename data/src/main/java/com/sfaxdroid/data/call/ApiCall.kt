package com.sfaxdroid.data.call

import com.sfaxdroid.data.call.Result.Success
import com.sfaxdroid.data.call.Result.Failure
import retrofit2.HttpException
import retrofit2.Response

suspend fun <R, T> executePlainCall(
  suspendFunc: suspend () -> R,
  converter: (response: R) -> T,
) : T = executeCall(suspendFunc, converter).getOrElses { throw it }

suspend fun <R, T> executeCall(
  suspendFunc: suspend () -> R,
  converter: (response: R) -> T
): Result<T> = try {
    val response = suspendFunc()
    if (response is Response<*> && response.code() !in HttpStatusCode.CODE_200.code..HttpStatusCode.CODE_399.code) {
      throw HttpException(response)
    }
    Success(converter(response))
  } catch (exception: Exception) {
    Failure(Error())
  }


