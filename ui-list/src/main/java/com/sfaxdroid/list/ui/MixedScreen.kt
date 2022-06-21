package com.sfaxdroid.list.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.sfaxdroid.data.mappers.ItemWrapperList
import com.sfaxdroid.list.WallpaperListState
import com.sfaxdroid.list.rememberFlowWithLifecycle
import com.sfaxdroid.list.viewmodels.MixedScreenViewModel

@Composable
fun MixedScreen(openDetail: (ItemWrapperList) -> Unit) {
    MixedScreen(viewModel = hiltViewModel(), openDetail = openDetail)
}

@Composable
internal fun MixedScreen(
    viewModel: MixedScreenViewModel,
    openDetail: (ItemWrapperList) -> Unit
) {
    val state by rememberFlowWithLifecycle(flow = viewModel.state).collectAsState(
        initial = WallpaperListState()
    )
    Text(text = state.itemsList.size.toString())
}