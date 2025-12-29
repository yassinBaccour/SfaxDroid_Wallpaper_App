package com.sfaxdroid.wallpapers.tag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.domain.usecase.GetTagWallpaperUseCase
import com.sfaxdroid.wallpapers.mixed.MixedWallpaperUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TagScreenViewModel @Inject constructor(
    private val getTagWallpaperUseCase: GetTagWallpaperUseCase,
    private val tagUiModelMapper: TagUiModelMapper
) : ViewModel() {

    private val _state = MutableStateFlow<TagUiState>(TagUiState.Loading)

    val uiState = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = MixedWallpaperUiState.Loading
    )

    internal fun getCustomUrlProduct(tag: String, partnerSource: Boolean) = viewModelScope.launch {
        val result = getTagWallpaperUseCase.execute(tag, partnerSource).fold(
            onSuccess = { wallpaper ->
                TagUiState.Success(tagUiModelMapper.map(wallpaper))
            },
            onFailure = { TagUiState.Failure })
        _state.update { result }
    }
}