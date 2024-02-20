package com.sfaxdroid.list.pixabay.list

import androidx.paging.PagingData
import com.sfaxdroid.data.entity.PixaTagWithSearchData
import com.sfaxdroid.data.mappers.PixaItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class WallpapersUiState(
    val wallpapersList: Flow<PagingData<PixaItem>> = flowOf(),
    val tagsWithSearchData: List<PixaTagWithSearchData> = arrayListOf(),
    val selectedItem: Int = 0
)
