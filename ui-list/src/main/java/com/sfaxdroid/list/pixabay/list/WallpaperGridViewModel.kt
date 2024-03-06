package com.sfaxdroid.list.pixabay.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.base.Constants.MIXED
import com.sfaxdroid.base.utils.TagUtils
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
@Inject constructor(private val getPixaWallpapersUseCase: GetPixaWallpapersUseCase) : ViewModel() {
    private lateinit var tagsWithSearchData: List<PixaTagWithSearchData>
    private var tagNameToId = mutableMapOf<String, String>()
    private var wallapaperResponseHits = MutableStateFlow(PixaResponse.empty.hits)
    private val selectedItem = MutableStateFlow(0)
    val state = combine(wallapaperResponseHits, selectedItem) { wallpaper, selected ->
        WallpapersUiState(
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
        tagNameToId = TagUtils().mapTagNameToId()
        viewModelScope.launch { provideMixedWallpaper() }
    }

    fun updateScreen(tagWithSearchData: PixaTagWithSearchData, pos: Int) {
        provideWallpaper(tagWithSearchData)
        selectItem(pos)
    }

    private fun getPictureList(searchData: PixaSearch) = viewModelScope.launch {
        wallapaperResponseHits.value = getPixaWallpapersUseCase.getResult(searchData)
    }

    private fun selectItem(index: Int) = viewModelScope.launch { selectedItem.value = index }

    @Suppress("IMPLICIT_CAST_TO_ANY")
    private fun provideWallpaper(pixaTagWithSearchData: PixaTagWithSearchData) =
        if (pixaTagWithSearchData.tag.name == MIXED) {
            provideMixedWallpaper()
        } else {
            getPictureList(pixaTagWithSearchData.search)
        }


    private fun getTagImgUrl(tag: String): MutableStateFlow<String> {
        val url = MutableStateFlow("")
        viewModelScope.launch {
            url.value = tagNameToId[tag]?.let { getPixaWallpapersUseCase.getUrl(it) }.toString()
        }
        return url
    }

    private fun provideMixedWallpaper() {
        tagsWithSearchData = listOf(
            PixaTagWithSearchData(
                tagImgUrl = getTagImgUrl("Mixed"),
                tag = TagView("Mixed", "Mixed"),
                search = PixaSearch("", "")
            ),
            PixaTagWithSearchData(
                tagImgUrl = getTagImgUrl("Music"),
                tag = TagView("Music", "Music"),
                search = PixaSearch("music+background", "music")
            ),
            PixaTagWithSearchData(
                tagImgUrl = getTagImgUrl("Nature"),
                tag = TagView("Nature", "Nature"),
                search = PixaSearch("landscape", "nature")
            ),
            PixaTagWithSearchData(
                tagImgUrl = getTagImgUrl("Cats"),
                tag = TagView("Cats", "Cats"),
                search = PixaSearch("domestic+cat", "animals")
            ),
            PixaTagWithSearchData(
                tagImgUrl = getTagImgUrl("Cars"),
                tag = TagView("Cars", "Cars"),
                search = PixaSearch("sports+car", "transportation")
            ),
            PixaTagWithSearchData(
                tagImgUrl = getTagImgUrl("Dogs"),
                tag = TagView("Dogs", "Dogs"),
                search = PixaSearch("dogs", "animals")
            ),
            PixaTagWithSearchData(
                tagImgUrl = getTagImgUrl("Buildings"),
                tag = TagView("Buildings", "Buildings"),
                search = PixaSearch("city", "buildings")
            ),
            PixaTagWithSearchData(
                tagImgUrl = getTagImgUrl("Travel"),
                tag = TagView("Travel", "Travel"),
                search = PixaSearch("vacation", "travel")
            ),
        )
    }
}
