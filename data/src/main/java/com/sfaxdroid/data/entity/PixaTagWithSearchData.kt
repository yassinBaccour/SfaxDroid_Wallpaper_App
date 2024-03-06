package com.sfaxdroid.data.entity

import com.sfaxdroid.data.mappers.TagView
import kotlinx.coroutines.flow.MutableStateFlow

data class PixaTagWithSearchData(
    val tagImgUrl: MutableStateFlow<String>, val tag: TagView, val search: PixaSearch
)
