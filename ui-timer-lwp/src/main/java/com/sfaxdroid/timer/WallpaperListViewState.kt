package com.sfaxdroid.timer

import com.sfaxdroid.bases.UiState
import com.sfaxdroid.data.mappers.SimpleWallpaperView
import javax.annotation.concurrent.Immutable

@Immutable
internal data class WallpaperListViewState(
    val wallpaperList: List<SimpleWallpaperView> = arrayListOf(),
    val isRefresh: Boolean = false,
    val nbImage: Int = 0
) : UiState
