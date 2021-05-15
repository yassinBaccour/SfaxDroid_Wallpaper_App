package com.sfaxdroid.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.bases.UiEffect
import com.sfaxdroid.bases.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

import kotlinx.coroutines.launch

abstract class BaseViewModel<State : UiState> : ViewModel() {

    private val initialState: State by lazy { createInitialState() }
    abstract fun createInitialState(): State

    val currentState: State
        get() = uiState.value

    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    protected fun setState(reduce: State.() -> State) {
        val newState = currentState.reduce()
        _uiState.value = newState
    }

}