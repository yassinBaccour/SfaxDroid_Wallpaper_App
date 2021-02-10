package com.sfaxdroid.domain

import com.sfaxdroid.data.repositories.Response
import kotlinx.coroutines.*

abstract class UseCase<out Type, in Params> where Type : Any {

    abstract suspend fun run(params: Params): Response

    suspend operator fun invoke(params: Params): Response =
        withContext(Dispatchers.IO) {
            run(params)
        }
}
