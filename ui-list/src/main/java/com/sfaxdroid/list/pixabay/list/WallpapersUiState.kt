package com.sfaxdroid.list.pixabay.list

import com.sfaxdroid.data.entity.PixaTagWithSearchData
import com.sfaxdroid.data.entity.TopWall
import com.sfaxdroid.data.mappers.PixaItem

data class WallpapersUiState(
    val wallpapersList: List<PixaItem> = arrayListOf(),
    val tagsWithSearchData: List<PixaTagWithSearchData> = arrayListOf(),
    val selectedItem: Int = 0,
    val topWalls: List<TopWall> = arrayListOf()
)
