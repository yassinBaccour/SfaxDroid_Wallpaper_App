package com.sfaxdroid.list.pixabay.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.sfaxdroid.data.entity.PixaSearch
import com.sfaxdroid.data.entity.PixaTagWithSearchData
import com.sfaxdroid.data.mappers.PixaResponse
import com.sfaxdroid.data.mappers.TagView
import com.sfaxdroid.domain.GetPixaWallpapersUseCase
import com.sfaxdroid.list.pixabay.PixaPagingSource
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
constructor(
    private val getPixaWallpapersUseCase: GetPixaWallpapersUseCase,
) : ViewModel() {
    lateinit var tagsWithSearchData: List<PixaTagWithSearchData>
    private var wallapaperResponseHits = MutableStateFlow(PixaResponse.empty.hits)
    private val selectedItem = MutableStateFlow(0)



















    val pixaItemListFlow= Pager(PagingConfig(pageSize = 20)) {
        PixaPagingSource(getPixaWallpapersUseCase, PixaSearch("landscape", "nature", "20"))
    }.flow.cachedIn(viewModelScope)

















    val state =
        combine(wallapaperResponseHits, selectedItem) { wallpaper, selected ->
            WallpapersUiState(
                wallpapersList = pixaItemListFlow,
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
                    PixaSearch("landscape,sports+car", "", "20")
                ),
                PixaTagWithSearchData(
                    TagView("Nature", "Nature"),
                    PixaSearch("landscape", "nature", "20")
                ),
                PixaTagWithSearchData(
                    TagView("Cars", "Cars"),
                    PixaSearch("sports+car", "transportation", "20")
                ),
            )
        getPictureList(tagsWithSearchData[0])
    }
}
