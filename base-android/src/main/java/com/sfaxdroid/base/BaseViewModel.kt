package com.sfaxdroid.base

import androidx.lifecycle.ViewModel
import com.sfaxdroid.bases.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class BaseViewModel<State : UiState> constructor(initialState: State) : ViewModel() {

    private val stateMutex = Mutex()

    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    protected suspend fun setState(reduce: State.() -> State) {
        stateMutex.withLock {
            _uiState.value = reduce(_uiState.value)
        }
    }
}
