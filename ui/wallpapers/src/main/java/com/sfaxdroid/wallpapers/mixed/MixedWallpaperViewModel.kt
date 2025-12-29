package com.sfaxdroid.wallpapers.mixed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.domain.usecase.GetWallpaperUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MixedWallpaperViewModel @Inject constructor(
    private val getWallpaperUseCase: GetWallpaperUseCase,
    private val mixedWallpaperUiModelMapper: MixedWallpaperUiModelMapper
) : ViewModel() {

    private val _state = MutableStateFlow<MixedWallpaperUiState>(MixedWallpaperUiState.Loading)

    val uiState = _state.onStart {
        getCustomUrlProduct()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = MixedWallpaperUiState.Loading
    )

    internal fun getCustomUrlProduct() = viewModelScope.launch {
        val result = getWallpaperUseCase.execute().fold(
            onSuccess = {
                MixedWallpaperUiState.Success(mixedWallpaperUiModelMapper.map(it))
            },
            onFailure = { MixedWallpaperUiState.Failure })
        _state.update { result }
    }

}