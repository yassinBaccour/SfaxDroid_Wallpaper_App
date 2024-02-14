package com.sfaxdroid.list.pixabay.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.data.mappers.PixaResponse
import com.sfaxdroid.data.mappers.TagView
import com.sfaxdroid.domain.GetPixaWallpapersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn


@HiltViewModel
internal class WallpaperGridViewModel
@Inject constructor(
    private val getPixaWallpapersUseCase: GetPixaWallpapersUseCase
) : ViewModel() {

    private var wallapaperResponse = MutableStateFlow(PixaResponse.empty)
    private val selectedItem = MutableStateFlow(0)
    val state = combine(wallapaperResponse, selectedItem) { wallpaper, selected ->
        WallpapersUiState(
            wallpapersList = wallpaper.hits, tags = arrayListOf(
                TagView("Mixed", "Mixed"),
                TagView("Nature", "Nature"),
                TagView("Cars", "Cars")
            ), selectedItem = selected
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WallpapersUiState(),
    )

    init {
        getPictureList()
    }

    private fun getPictureList() = viewModelScope.launch {
        wallapaperResponse.value = getPixaWallpapersUseCase.getResult()
    }

    fun selectItem(index: Int) = viewModelScope.launch {
        selectedItem.value = index
    }

    fun provideWallpaper(tagView: TagView) {
        //CALL ws
    }

}