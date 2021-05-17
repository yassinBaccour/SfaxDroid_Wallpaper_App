package com.sfaxdroid.data.entity

sealed class Response<E> {
    class SUCCESS<E>(var response: E) : Response<E>()
    class FAILURE<E>(val throwable: Throwable) : Response<E>()
}
