package com.sfaxdroid.list.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sfaxdroid.data.entity.AppName
import com.sfaxdroid.data.mappers.BaseWallpaperView
import com.sfaxdroid.data.mappers.SimpleWallpaperView
import com.sfaxdroid.list.WallpaperListState
import com.sfaxdroid.list.rememberFlowWithLifecycle
import com.sfaxdroid.list.viewmodels.WallpapersViewModel

@Composable
fun WallpaperList(openDetail: (BaseWallpaperView, String, String, AppName) -> Unit) {
    WallpaperList(viewModel = hiltViewModel(), openDetail = openDetail)
}

@Composable
internal fun WallpaperList(
    viewModel: WallpapersViewModel,
    openDetail: (BaseWallpaperView, String, String, AppName) -> Unit
) {
    val state by rememberFlowWithLifecycle(flow = viewModel.state).collectAsState(
        initial = WallpaperListState()
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        items(state.itemsList.size) { message ->
            val obj = state.itemsList[message]
            GenerateItem(obj) {
                openDetail(it, "", "", AppName.AccountOne)
            }
        }
    }
}