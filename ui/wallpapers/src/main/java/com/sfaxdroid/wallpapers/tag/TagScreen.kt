package com.sfaxdroid.wallpapers.tag

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sfaxdroid.wallpapers.core.GroupUiModel
import com.sfaxdroid.wallpapers.core.view.WallpaperContentList
import com.sfaxdroid.wallpapers.mixed.MixedWallpaperUiState

@Composable
fun TagScreen(tag: String, openDetail: (String) -> Unit, openTag: (String) -> Unit) =
    WallpaperScreen(
        viewModel = hiltViewModel(),
        tag = tag,
        openDetail = openDetail,
        openTag = openTag
    )

@Composable
private fun WallpaperScreen(
    viewModel: TagScreenViewModel,
    tag: String,
    openDetail: (String) -> Unit,
    openTag: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.getCustomUrlProduct(tag, true)
    }
    when (state) {
        is TagUiState.Success -> TagScreenContent(
            state = (state as TagUiState.Success).sections,
            openDetail = openDetail,
            openTag = openTag
        )

        TagUiState.Loading -> {}
        TagUiState.Failure -> {}
    }
}

@Composable
private fun TagScreenContent(
    state: List<GroupUiModel>,
    openDetail: (String) -> Unit,
    openTag: (String) -> Unit
) {
    Scaffold { innerPadding ->
        WallpaperContentList(
            modifier = Modifier.padding(innerPadding),
            state = state,
            openDetail = openDetail,
            openTag = openTag
        )
    }
}

