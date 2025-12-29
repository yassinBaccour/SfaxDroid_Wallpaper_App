package com.sfaxdroid.wallpapers.mixed

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sfaxdroid.wallpapers.core.GroupUiModel
import com.sfaxdroid.wallpapers.core.view.WallpaperContentList

@Composable
fun MixedWallpaperScreen(openDetail: (String) -> Unit) =
    MixedWallpaperScreen(viewModel = hiltViewModel(), openDetail)

@Composable
private fun MixedWallpaperScreen(viewModel: MixedWallpaperViewModel, openDetail: (String) -> Unit) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    when (state) {
        is MixedWallpaperUiState.Success -> MixedWallpaperContent(
            state = (state as MixedWallpaperUiState.Success).sections,
            openDetail = openDetail
        )

        MixedWallpaperUiState.Loading -> {}
        MixedWallpaperUiState.Failure -> {}
    }
}

@Composable
private fun MixedWallpaperContent(state: List<GroupUiModel>, openDetail: (String) -> Unit) {
    Scaffold { innerPadding ->
        WallpaperContentList(
            modifier = Modifier.padding(innerPadding),
            state = state,
            openDetail = openDetail
        )
    }
}



