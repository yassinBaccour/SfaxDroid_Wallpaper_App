package com.sfaxdroid.list.pixabay.list

import com.sfaxdroid.data.mappers.PixaItem
import com.sfaxdroid.data.mappers.TagView

data class WallpapersUiState(
    val wallpapersList: List<PixaItem> = arrayListOf(),
    val tags: List<TagView> = arrayListOf(),
    val selectedItem: Int = 0
)