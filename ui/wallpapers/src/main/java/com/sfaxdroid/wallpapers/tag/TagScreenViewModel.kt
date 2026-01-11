package com.sfaxdroid.wallpapers.tag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.commion.ui.compose.Destination
import com.sfaxdroid.domain.usecase.GetTagWallpaperUseCase
import com.sfaxdroid.wallpapers.mixed.MixedWallpaperUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = TagScreenViewModel.Factory::class)
internal class TagScreenViewModel @AssistedInject constructor(
    @Assisted val navKey: Destination.Tag,
    private val getTagWallpaperUseCase: GetTagWallpaperUseCase,
    private val tagUiModelMapper: TagUiModelMapper,
    private val wallpaperToTagMapper: WallpaperToTagMapper
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(navKey: Destination.Tag): TagScreenViewModel
    }

    private val wallpaperTagState = MutableStateFlow<TagState>(TagState.Loading)

    private val tags = MutableStateFlow<List<Pair<String, String>>>(listOf())

    val state = wallpaperTagState.onStart {
        getWallpaperByTag()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = MixedWallpaperUiState.Loading
    )

    internal fun getWallpaperByTag() =
        viewModelScope.launch {
            val result = getTagWallpaperUseCase.execute(
                tag = navKey.tag,
                partnerSource = navKey.loadFromPartner
            ).fold(
                onSuccess = { wallpaper ->
                    val tagList = wallpaperToTagMapper.map(wallpaper)
                    tags.value = tagList
                    TagState.Success(
                        title = navKey.title,
                        sections = tagUiModelMapper.toUiModel(wallpaper, tagList),
                        loadFromPartner = navKey.loadFromPartner,
                        tags = navKey.tag
                    )
                },
                onFailure = { TagState.Failure })
            wallpaperTagState.update { result }
        }

    internal fun getNewWallpaperByTag(tag: Pair<String, String>) {
        wallpaperTagState.update {
            val currentState = wallpaperTagState.value as TagState.Success
            currentState.copy(sections = tagUiModelMapper.toLoadingUiModel(tags.value))
        }
        viewModelScope.launch {
            val result = getTagWallpaperUseCase.execute(tag, navKey.loadFromPartner).fold(
                onSuccess = { wallpaper ->
                    val currentState = wallpaperTagState.value as TagState.Success
                    currentState.copy(sections = tagUiModelMapper.toUiModel(wallpaper, tags.value))
                },
                onFailure = { TagState.Failure })
            wallpaperTagState.update { result }
        }
    }
}