package com.sfaxdroid.domain

import com.sfaxdroid.data.repositories.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

abstract class UseCase<out Type, in Params> where Type : Any {

    abstract suspend fun run(params: Params): Response

    suspend operator fun invoke(params: Params): Response =
        withContext(Dispatchers.IO) {
            run(params)
        }
}

abstract class ResultUseCase<in P, R> {
    operator fun invoke(params: P): Flow<R> = flow {
        emit(doWork(params))
    }

    suspend fun executeSync(params: P): R = doWork(params)

    protected abstract suspend fun doWork(params: P): R
}
