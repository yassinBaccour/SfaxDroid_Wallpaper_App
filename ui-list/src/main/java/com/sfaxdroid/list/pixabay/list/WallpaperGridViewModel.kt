package com.sfaxdroid.list.pixabay.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.base.Constants.MIXED
import com.sfaxdroid.base.utils.TagUtils
import com.sfaxdroid.base.utils.TopWallsUtils
import com.sfaxdroid.data.entity.PixaSearch
import com.sfaxdroid.data.entity.PixaTagWithSearchData
import com.sfaxdroid.data.entity.TopWall
import com.sfaxdroid.data.mappers.PixaResponse
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
@Inject constructor(private val getPixaWallpapersUseCase: GetPixaWallpapersUseCase) : ViewModel() {
    private lateinit var tagsWithSearchData: List<PixaTagWithSearchData>
    private val tagUtils = TagUtils()
    private var wallapaperResponseHits = MutableStateFlow(PixaResponse.empty.hits)
    private var topWallListFlow = MutableStateFlow(listOf<TopWall>())
    private val selectedItem = MutableStateFlow(0)
    val state = combine(
        topWallListFlow, wallapaperResponseHits, selectedItem
    ) { topWalls, wallpaper, selected ->
        WallpapersUiState(
            topWalls = topWalls,
            wallpapersList = wallpaper,
            selectedItem = selected,
            tagsWithSearchData = tagsWithSearchData
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WallpapersUiState(),
    )

    init {
        viewModelScope.launch {
            tagsWithSearchData =
                tagUtils.provideMixedWallpaper { getPixaWallpapersUseCase.getUrl(it) }
        }
        provideTopWalls()
    }

    private fun provideTopWalls() = viewModelScope.launch {
        val topWallPicListFlow =
            getPixaWallpapersUseCase.getResult(PixaSearch("wallpaper", "", "10"))
        topWallPicListFlow.onEach {
            topWallListFlow.value += (TopWall(
                it, TopWallsUtils().getCatchyString(it.downloads, it.likes, it.views)
            ))


        }
    }

    internal fun updateScreen(tagWithSearchData: PixaTagWithSearchData, pos: Int) {
        provideWallpaper(tagWithSearchData)
        selectItem(pos)
    }

    private fun getPictureList(searchData: PixaSearch) = viewModelScope.launch {
        wallapaperResponseHits.value = getPixaWallpapersUseCase.getResult(searchData)
    }

    private fun selectItem(index: Int) = viewModelScope.launch { selectedItem.value = index }

    private fun provideWallpaper(pixaTagWithSearchData: PixaTagWithSearchData) =
        viewModelScope.launch {
            if (pixaTagWithSearchData.tag.name == MIXED) {
                tagUtils.provideMixedWallpaper { getPixaWallpapersUseCase.getUrl(it) }
            } else {
                getPictureList(pixaTagWithSearchData.search)
            }
        }


}
