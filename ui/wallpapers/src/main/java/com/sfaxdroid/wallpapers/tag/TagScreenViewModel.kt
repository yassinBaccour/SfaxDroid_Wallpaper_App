package com.sfaxdroid.wallpapers.tag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.domain.usecase.GetTagWallpaperUseCase
import com.sfaxdroid.wallpapers.core.GroupUiModel
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
    private val tagUiModelMapper: TagUiModelMapper,
    private val wallpaperToTagMapper: WallpaperToTagMapper
) : ViewModel() {

    private val _state = MutableStateFlow<TagUiState>(TagUiState.Loading)

    private val tags = MutableStateFlow<List<Pair<String, String>>>(listOf())

    val uiState = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = MixedWallpaperUiState.Loading
    )

    internal fun getCustomUrlProduct(tag: Pair<String, String>, partnerSource: Boolean) =
        viewModelScope.launch {
            val result = getTagWallpaperUseCase.execute(tag, partnerSource).fold(
                onSuccess = { wallpaper ->
                    if (tags.value.isEmpty()) {
                        tags.value = wallpaperToTagMapper.map(wallpaper)
                    }
                    TagUiState.Success(tagUiModelMapper.map(wallpaper, tags.value))
                },
                onFailure = { TagUiState.Failure })
            _state.update { result }
        }

    internal fun loadNewTag(tag: Pair<String, String>, partnerSource: Boolean) =
        viewModelScope.launch {
            _state.value = TagUiState.Success(
                listOf(
                    GroupUiModel.PARTNER_TAG_CARROUSEL(tags.value),
                    GroupUiModel.LOADING_GRID
                )
            )
            val result = getTagWallpaperUseCase.execute(tag, partnerSource).fold(
                onSuccess = { wallpaper ->
                    TagUiState.Success(tagUiModelMapper.map(wallpaper, tags.value))
                },
                onFailure = { TagUiState.Failure })
            _state.update { result }
        }
}