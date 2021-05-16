package com.sfaxdroid.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class ResultUseCase<in P, R> {
    operator fun invoke(params: P): Flow<R> = flow {
        emit(doWork(params))
    }

    protected abstract suspend fun doWork(params: P): R
}
