package com.sfaxdroid.list.pixabay.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sfaxdroid.bases.NavScreen
import com.sfaxdroid.data.mappers.PixaItem
import com.sfaxdroid.list.rememberFlowWithLifecycle
import com.sfaxdroid.list.ui.InitTagList


@Composable
fun WallpaperGrid(navController: NavController) {
    WallpaperGrid(viewModel = hiltViewModel()) {
        navController.navigate(NavScreen.Pixabay.route + NavScreen.Detail.route + "/" + it)
    }
}

@Composable
internal fun WallpaperGrid(
    viewModel: WallpaperGridViewModel, openWallpaper: (String) -> Unit
) {

    val state by rememberFlowWithLifecycle(flow = viewModel.state).collectAsState(
        initial = WallpapersUiState()
    )

    Column {
        InitTagList(
            modifier = Modifier
                .height(50.dp),
            tagList = state.tags,
            selectedItem = 0
        ) { tagView, pos ->
            viewModel.provideWallpaper(tagView)
            viewModel.selectItem(pos)
        }
        WallpaperGrid(
            pictureList = state.wallpapersList.shuffled(), openWallpaper = openWallpaper
        )
    }
}

@Composable
internal fun WallpaperGrid(pictureList: List<PixaItem>, openWallpaper: (String) -> Unit) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(), columns = GridCells.Adaptive(124.dp)
    ) {
        items(pictureList.size) { index ->
            WallpaperItems(pictureList[index]) { url ->
                openWallpaper(url)
            }
        }
    }
}

