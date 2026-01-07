package com.sfaxdroid.wallpapers.mixed

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sfaxdroid.wallpapers.core.GroupUiModel
import com.sfaxdroid.wallpapers.core.view.FailureScreenMinimal
import com.sfaxdroid.wallpapers.core.view.LoadingContent
import com.sfaxdroid.wallpapers.core.view.list.WallpaperContentList

@Composable
fun MixedWallpaperScreen(
    openDetail: (String, List<String>, String) -> Unit,
    openTag: (String, Pair<String, String>, Boolean) -> Unit,
    openSkyBox: () -> Unit
) =
    MixedWallpaperScreen(viewModel = hiltViewModel(), openDetail = openDetail, openTag = openTag, openSkyBox = openSkyBox)

@Composable
private fun MixedWallpaperScreen(
    viewModel: MixedWallpaperViewModel,
    openDetail: (String, List<String>, String) -> Unit,
    openTag: (String, Pair<String, String>, Boolean) -> Unit,
    openSkyBox: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    when (state) {
        is MixedWallpaperUiState.Success -> MixedWallpaperContent(
            state = (state as MixedWallpaperUiState.Success).sections,
            openDetail = openDetail,
            openTag = openTag,
            openSkyBox = openSkyBox
        )

        MixedWallpaperUiState.Loading -> LoadingContent()
        MixedWallpaperUiState.Failure -> FailureScreenMinimal(viewModel::getCustomUrlProduct)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MixedWallpaperContent(
    state: List<GroupUiModel>,
    openDetail: (String, List<String>, String) -> Unit,
    openTag: (String, Pair<String, String>, Boolean) -> Unit,
    openSkyBox: () -> Unit
) {
    Scaffold { innerPadding ->
        WallpaperContentList(
            modifier = Modifier.padding(innerPadding),
            group = state,
            openDetail = openDetail,
            openTag = openTag,
            loadTag = { x, y -> },
            openSkyBox = openSkyBox
        )
    }
}



