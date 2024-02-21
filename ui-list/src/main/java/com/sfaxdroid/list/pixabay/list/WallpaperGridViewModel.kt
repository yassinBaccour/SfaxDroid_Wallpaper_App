package com.sfaxdroid.list.pixabay.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sfaxdroid.data.entity.PixaSearch
import com.sfaxdroid.data.entity.PixaTagWithSearchData
import com.sfaxdroid.data.mappers.PixaItem
import com.sfaxdroid.data.mappers.PixaResponse
import com.sfaxdroid.data.mappers.TagView
import com.sfaxdroid.domain.GetPixaWallpapersUseCase
import com.sfaxdroid.list.pixabay.PixaPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
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
    private val selectedItem = MutableStateFlow(0)
    private lateinit var pixaItemListFlow : Flow<PagingData<PixaItem>>



    val state =
        combine(pixaItemListFlow, selectedItem) { wallpapers, selected ->
            WallpapersUiState(
                wallpapersList = wallpapers,
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
        initTagsWithSearchData()
        viewModelScope.launch {
            getPictureList(PixaSearch("landscape,car", "", "20"))
        }

    }

    private fun getPictureList(searchData: PixaSearch) =
        viewModelScope.launch {
            pixaItemListFlow = Pager(PagingConfig(pageSize = 20)) {
                PixaPagingSource(getPixaWallpapersUseCase, searchData)
            }.flow.cachedIn(viewModelScope)
            }

    fun selectItem(index: Int) = viewModelScope.launch { selectedItem.value = index }

    fun provideWallpaper(searchData: PixaSearch) {
            getPictureList(searchData)
    }

    private fun initTagsWithSearchData() {
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
    }


}
