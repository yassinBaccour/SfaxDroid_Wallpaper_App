package com.sfaxdroid.list.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sfaxdroid.data.entity.AppName
import com.sfaxdroid.data.mappers.BaseWallpaperView
import com.sfaxdroid.data.mappers.LwpItem
import com.sfaxdroid.list.WallpaperListState
import com.sfaxdroid.list.rememberFlowWithLifecycle
import com.sfaxdroid.list.viewmodels.LwpViewModel

@Composable
fun LiveWallpaperList(openLiveWallpaper: (wallpaperObject: LwpItem) -> Unit) {
    LiveWallpaperList(viewModel = hiltViewModel(), openLiveWallpaper)
}

@Composable
internal fun LiveWallpaperList(
    viewModel: LwpViewModel,
    openLiveWallpaper: (wallpaperObject: LwpItem) -> Unit
) {
    val state by rememberFlowWithLifecycle(flow = viewModel.state).collectAsState(
        initial = WallpaperListState()
    )
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(5.dp)
    ) {
        items(state.itemsList.size) { message ->
            val obj = state.itemsList[message]
            GenerateItem(obj) {
                 openLiveWallpaper(it as LwpItem)
            }
        }
    }
}