package com.sfaxdroid.list.pixabay.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.data.entity.PixaSearch
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

    private var wallapaperResponseHits = MutableStateFlow(PixaResponse.empty.hits)
    private val selectedItem = MutableStateFlow(0)
    val state = combine(wallapaperResponseHits, selectedItem) { wallpaper, selected ->
        WallpapersUiState(
            wallpapersList = wallpaper, tags = arrayListOf(
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
        wallapaperResponseHits.value = getPixaWallpapersUseCase.getResult()
    }

    fun selectItem(index: Int) = viewModelScope.launch {
        selectedItem.value = index
    }

    fun provideWallpaper(tagView: TagView) {
        viewModelScope.launch {
            if (tagView.name == "Nature"){
                wallapaperResponseHits.value = getPixaWallpapersUseCase.getResult(PixaSearch("landscape", "nature"))
            }else if (tagView.name == "Cars"){
                wallapaperResponseHits.value = getPixaWallpapersUseCase.getResult(PixaSearch("cars", "transportation"))
            }else getPictureList()
        }
    }

}