package com.sfaxdroid.wallpapers.tag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.domain.usecase.GetPartnerWallpaper
import com.sfaxdroid.wallpapers.core.GroupUiModel
import com.sfaxdroid.wallpapers.mixed.MixedWallpaperUiModelMapper
import com.sfaxdroid.wallpapers.mixed.MixedWallpaperUiState
import com.sfaxdroid.wallpapers.core.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TagScreenViewModel @Inject constructor(
    private val getPartnerWallpaper: GetPartnerWallpaper
) : ViewModel() {

    private val _state = MutableStateFlow<TagUiState>(TagUiState.Loading)

    val uiState = _state.onStart {
        getCustomUrlProduct("")
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = MixedWallpaperUiState.Loading
    )

    internal fun getCustomUrlProduct(tag: String) = viewModelScope.launch {
        val result = getPartnerWallpaper.execute(tag).fold(
            onSuccess = { wallpaper ->
                TagUiState.Success(listOf(GroupUiModel.GRID(wallpaper.map { it.toUiModel() })))
            },
            onFailure = { TagUiState.Failure })
        _state.update { result }
    }
}