package com.sfaxdroid.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.bases.UiEffect
import com.sfaxdroid.bases.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

import kotlinx.coroutines.launch
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