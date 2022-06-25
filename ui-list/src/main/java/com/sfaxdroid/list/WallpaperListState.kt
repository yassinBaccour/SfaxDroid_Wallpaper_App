package com.sfaxdroid.list

import com.sfaxdroid.bases.UiState
import com.sfaxdroid.data.mappers.ItemWrapperList
import com.sfaxdroid.data.mappers.TagView
import javax.annotation.concurrent.Immutable

@Immutable
data class WallpaperListState(
    var itemsList: List<ItemWrapperList> = arrayListOf(),
    var tagList: List<TagView> = arrayListOf(),
    var isTagVisible: Boolean = false,
    var isRefresh: Boolean = true,
    var isToolBarVisible: Boolean = false,
    var isPrivacyButtonVisible: Boolean = false,
    var toolBarTitle: String = "",
    var setDisplayHomeAsUpEnabled: Boolean = false,
    var tagSelectedPosition: Int = 0,
    var isError: Boolean = false
) : UiState {
    companion object {
        val Empty = WallpaperListState()
    }
}