package com.sfaxdroid.list.pixabay.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.data.entity.PixaSearch
import com.sfaxdroid.data.entity.PixaTagWithSearchData
import com.sfaxdroid.data.mappers.PixaResponse
import com.sfaxdroid.data.mappers.TagView
import com.sfaxdroid.domain.GetPixaWallpapersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
internal class WallpaperGridViewModel
@Inject
constructor(private val getPixaWallpapersUseCase: GetPixaWallpapersUseCase) : ViewModel() {
    private lateinit var tagsWithSearchData: List<PixaTagWithSearchData>
    private val tagNameToId = mutableMapOf<String, String>()
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
        mapTagNameToId()
        viewModelScope.launch { provideMixedWallpaper() }
    }

    private fun getPictureList(searchData: PixaSearch) =
        viewModelScope.launch {
            wallapaperResponseHits.value = getPixaWallpapersUseCase.getResult(searchData)
        }

    fun selectItem(index: Int) = viewModelScope.launch { selectedItem.value = index }

    fun provideWallpaper(pixaTagWithSearchData: PixaTagWithSearchData) {
        if (pixaTagWithSearchData.tag.name == "Mixed") {
            provideMixedWallpaper()
        } else {
            getPictureList(pixaTagWithSearchData.search)
        }
    }

    private fun mapTagNameToId() {
        tagNameToId["Nature"] = "3078326"
        tagNameToId["Cars"] = "4361321"
        tagNameToId["Music"] = "2912447"
        tagNameToId["Dogs"] = "7694627"
        tagNameToId["Cats"] = "7517522"
        tagNameToId["Buildings"] = "5057263"
        tagNameToId["Travel"] = "4888643"
    }

    private fun getTagImgUrl(tag: String): MutableStateFlow<String> {
        var url = MutableStateFlow("")
        viewModelScope.launch {
            url.value = tagNameToId[tag]?.let { getPixaWallpapersUseCase.getUrl(it) }.toString()
            url.collect { Log.d("WallpaperGridViewModel", "getTagImgUrl: $it") }
        }
        return url
    }

    private fun provideMixedWallpaper() {
        tagsWithSearchData =
            listOf(
                PixaTagWithSearchData(
                    getTagImgUrl("Mixed"),
                    TagView("Mixed", "Mixed"),
                    PixaSearch("", "")
                ),
                PixaTagWithSearchData(
                    getTagImgUrl("Music"),
                    TagView("Music", "Music"),
                    PixaSearch("music+background", "music")
                ),
                PixaTagWithSearchData(
                    getTagImgUrl("Nature"),
                    TagView("Nature", "Nature"),
                    PixaSearch("landscape", "nature")
                ),
                PixaTagWithSearchData(
                    getTagImgUrl("Cats"),
                    TagView("Cats", "Cats"),
                    PixaSearch("domestic+cat", "animals")
                ),
                PixaTagWithSearchData(
                    getTagImgUrl("Cars"),
                    TagView("Cars", "Cars"),
                    PixaSearch("sports+car", "transportation")
                ),
                PixaTagWithSearchData(
                    getTagImgUrl("Dogs"),
                    TagView("Dogs", "Dogs"),
                    PixaSearch("dogs", "animals")
                ),
                PixaTagWithSearchData(
                    getTagImgUrl("Buildings"),
                    TagView("Buildings", "Buildings"),
                    PixaSearch("city", "buildings")
                ),
                PixaTagWithSearchData(
                    getTagImgUrl("Travel"),
                    TagView("Travel", "Travel"),
                    PixaSearch("vacation", "travel")
                ),
            )
        getPictureList(tagsWithSearchData[0].search)
    }
}
