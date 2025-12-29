package com.sfaxdroid.data.call

import javax.inject.Inject

class ApiRequestImpl @Inject constructor() : ApiRequester {
  override suspend fun <R, T> request(call: suspend () -> R, transform: (R) -> T): T =
    executePlainCall(call, converter = transform)
}
