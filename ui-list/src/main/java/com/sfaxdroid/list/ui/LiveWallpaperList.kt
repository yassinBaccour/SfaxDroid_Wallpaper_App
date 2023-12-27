package com.sfaxdroid.list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sfaxdroid.data.mappers.LwpItem
import com.sfaxdroid.list.WallpaperViewState
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
        initial = WallpaperViewState()
    )
    LiveWallpaperList(viewState = state, openLiveWallpaper = openLiveWallpaper)
}

@Composable
internal fun LiveWallpaperList(
    viewState: WallpaperViewState,
    openLiveWallpaper: (wallpaperObject: LwpItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary),
        contentPadding = PaddingValues(5.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(viewState.itemsList.size) { message ->
            val obj = viewState.itemsList[message]
            GenerateItem(obj) {
                openLiveWallpaper(it as LwpItem)
            }
        }
    }
}