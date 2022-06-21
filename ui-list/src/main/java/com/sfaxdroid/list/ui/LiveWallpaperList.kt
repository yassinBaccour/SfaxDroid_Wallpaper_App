package com.sfaxdroid.list.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.sfaxdroid.list.WallpaperListState
import com.sfaxdroid.list.rememberFlowWithLifecycle
import com.sfaxdroid.list.viewmodels.LwpViewModel

@Composable
fun LiveWallpaperList(openLiveWallpaper: () -> Unit) {
    LiveWallpaperList(viewModel = hiltViewModel(), openLiveWallpaper)
}

@Composable
internal fun LiveWallpaperList(viewModel: LwpViewModel, openLiveWallpaper: () -> Unit) {
    val state by rememberFlowWithLifecycle(flow = viewModel.state).collectAsState(
        initial = WallpaperListState()
    )
    Text(text = state.itemsList.size.toString())
}