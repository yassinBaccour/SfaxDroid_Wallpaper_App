package com.sfaxdroid.data.call

interface ApiRequester {
  suspend fun <R, T> request(
    call: suspend () -> R,
    transform: (response: R) -> T
  ): T
}
