package com.yassin.wallpaper.feature.home

import com.sfaxdroid.bases.UiState
import com.sfaxdroid.data.mappers.TagView
import javax.annotation.concurrent.Immutable

@Immutable
data class WallpaperListViewEvents(
    var itemsList: List<ItemWrapperList<Any>> = arrayListOf(),
    var tagList: List<TagView> = arrayListOf(),
    var isTagVisible: Boolean = false,
    var isRefresh: Boolean = false
) : UiState