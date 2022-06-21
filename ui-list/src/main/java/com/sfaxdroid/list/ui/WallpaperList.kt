package com.sfaxdroid.list.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.sfaxdroid.list.WallpaperListState
import com.sfaxdroid.list.rememberFlowWithLifecycle
import com.sfaxdroid.list.viewmodels.WallpapersViewModel

@Composable
fun WallpaperList(openDetail: (String) -> Unit) {
    WallpaperList(viewModel = hiltViewModel(), openDetail = openDetail)
}

@Composable
internal fun WallpaperList(
    viewModel: WallpapersViewModel,
    openDetail: (String) -> Unit
) {
    val state by rememberFlowWithLifecycle(flow = viewModel.state).collectAsState(
        initial = WallpaperListState()
    )
    Text(text = state.itemsList.size.toString())
}