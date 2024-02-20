package com.sfaxdroid.list.pixabay.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.data.entity.PixaSearch
import com.sfaxdroid.data.entity.PixaTagWithSearchData
import com.sfaxdroid.data.mappers.PixaResponse
import com.sfaxdroid.data.mappers.TagView
import com.sfaxdroid.domain.GetPixaWallpapersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class WallpaperGridViewModel
@Inject
constructor(private val getPixaWallpapersUseCase: GetPixaWallpapersUseCase) : ViewModel() {
    lateinit var tagsWithSearchData: List<PixaTagWithSearchData>
    private var wallapaperResponseHits = MutableStateFlow(PixaResponse.empty.hits)
    private val selectedItem = MutableStateFlow(0)
    val state =
        combine(wallapaperResponseHits, selectedItem) { wallpaper, selected ->
            WallpapersUiState(
                wallpapersList = wallpaper,
                selectedItem = selected,
                tagsWithSearchData = tagsWithSearchData
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = WallpapersUiState(),
            )

    init {
        provideMixedWallpaper()
    }

    private fun getPictureList(tagWithSearchData: PixaTagWithSearchData) =
        viewModelScope.launch {
            wallapaperResponseHits.value = getPixaWallpapersUseCase.getResult(tagWithSearchData)
        }

    fun selectItem(index: Int) = viewModelScope.launch { selectedItem.value = index }

    fun provideWallpaper(pixaTagWithSearchData: PixaTagWithSearchData) {
        if (pixaTagWithSearchData.tag.name == "Mixed") {
            provideMixedWallpaper()
        } else {
            getPictureList(pixaTagWithSearchData)
        }
    }

    private fun provideMixedWallpaper() {
        tagsWithSearchData =
            listOf(
                PixaTagWithSearchData(
                    TagView("Mixed", "Mixed"),
                    PixaSearch("landscape,cars", "")
                ),
                PixaTagWithSearchData(
                    TagView("Nature", "Nature"),
                    PixaSearch("landscape", "nature")
                ),
                PixaTagWithSearchData(
                    TagView("Cars", "Cars"),
                    PixaSearch("sports+car", "transportation")
                ),
            )
        getPictureList(tagsWithSearchData[0])
    }
}
