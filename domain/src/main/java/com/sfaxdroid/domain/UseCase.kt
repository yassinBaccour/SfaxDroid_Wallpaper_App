package com.sfaxdroid.domain

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

abstract class ResultUseCase<in P, R> {
    private val paramState = MutableSharedFlow<P>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    //Remove distinctUntilChanged() until add Room DataBase to filter new element in DB
    val flow: Flow<R> = paramState
        .distinctUntilChanged()
        .flatMapLatest { doWork(it) }
        .distinctUntilChanged()

    operator fun invoke(params: P) {
        paramState.tryEmit(params)
    }

    protected abstract suspend fun doWork(params: P): Flow<R>
}
