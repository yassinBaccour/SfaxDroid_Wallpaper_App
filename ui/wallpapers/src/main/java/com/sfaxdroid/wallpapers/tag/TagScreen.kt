package com.sfaxdroid.wallpapers.tag

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sfaxdroid.wallpapers.core.view.WallpaperContentList
import com.sfaxdroid.wallpapers.mixed.MixedWallpaperUiState

@Composable
fun TagScreen(openDetail: (String) -> Unit) =
    WallpaperScreen(viewModel = hiltViewModel(), openDetail)

@Composable
private fun WallpaperScreen(viewModel: TagScreenViewModel, openDetail: (String) -> Unit) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    when (state) {
        is TagUiState.Success -> WallpaperContentList(
            state = (state as MixedWallpaperUiState.Success).sections,
            openDetail = openDetail
        )

        TagUiState.Loading -> {}
        TagUiState.Failure -> {}
    }
}

