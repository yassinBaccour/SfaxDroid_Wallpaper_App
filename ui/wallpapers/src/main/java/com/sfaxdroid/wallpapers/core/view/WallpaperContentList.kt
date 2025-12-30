package com.sfaxdroid.wallpapers.core.view

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sfaxdroid.wallpapers.core.GroupUiModel

@Composable
internal fun WallpaperContentList(
    modifier: Modifier = Modifier,
    group: List<GroupUiModel>,
    openDetail: (String) -> Unit,
    openTag: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(group) {
            when (it) {
                is GroupUiModel.CARROUSEL -> {}
                is GroupUiModel.GRID -> WallpaperGrid(it.list, openDetail)
                is GroupUiModel.TAG -> WallpaperTags(it.list, openTag)
            }
        }
    }
}