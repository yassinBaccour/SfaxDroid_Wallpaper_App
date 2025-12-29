package com.sfaxdroid.wallpapers.tag

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sfaxdroid.wallpapers.core.view.WallpaperContentList
import com.sfaxdroid.wallpapers.mixed.MixedWallpaperUiState

@Composable
fun TagScreen(tag: String, openDetail: (String) -> Unit) =
    WallpaperScreen(viewModel = hiltViewModel(), tag = tag, openDetail = openDetail)

@Composable
private fun WallpaperScreen(
    viewModel: TagScreenViewModel,
    tag: String,
    openDetail: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.getCustomUrlProduct(tag)
    }
    when (state) {
        is TagUiState.Success -> WallpaperContentList(
            state = (state as TagUiState.Success).sections,
            openDetail = openDetail
        )

        TagUiState.Loading -> {}
        TagUiState.Failure -> {}
    }
}

