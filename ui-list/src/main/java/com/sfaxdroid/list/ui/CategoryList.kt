package com.sfaxdroid.list.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.sfaxdroid.list.WallpaperListState
import com.sfaxdroid.list.rememberFlowWithLifecycle
import com.sfaxdroid.list.viewmodels.CategoryViewModel

@Composable
fun CategoryList(openWallpaper: (String) -> Unit) {
    CategoryList(viewModel = hiltViewModel(), openWallpaper = openWallpaper)
}

@Composable
internal fun CategoryList(
    viewModel: CategoryViewModel,
    openWallpaper: (String) -> Unit
) {

    val state by rememberFlowWithLifecycle(flow = viewModel.state).collectAsState(
        initial = WallpaperListState()
    )
    Text(text = "Wallpaper" + state.itemsList.size)

}