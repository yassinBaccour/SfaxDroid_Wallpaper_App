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
import com.sfaxdroid.data.mappers.CategoryItem
import com.sfaxdroid.list.WallpaperViewState
import com.sfaxdroid.list.rememberFlowWithLifecycle
import com.sfaxdroid.list.viewmodels.CategoryViewModel

@Composable
fun CategoryList(openWallpaper: (wallpaperObject: CategoryItem) -> Unit) {
    CategoryList(viewModel = hiltViewModel(), openWallpaper = openWallpaper)
}

@Composable
internal fun CategoryList(
    viewModel: CategoryViewModel,
    openWallpaper: (wallpaperObject: CategoryItem) -> Unit
) {
    val state by rememberFlowWithLifecycle(flow = viewModel.state).collectAsState(
        initial = WallpaperViewState()
    )
    CategoryList(viewState = state, openWallpaper = openWallpaper)
}

@Composable
internal fun CategoryList(
    viewState: WallpaperViewState,
    openWallpaper: (wallpaperObject: CategoryItem) -> Unit
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
                openWallpaper(it as CategoryItem)
            }
        }
    }
}

