package com.sfaxdroid.list.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.sfaxdroid.data.mappers.ItemWrapperList
import com.sfaxdroid.list.WallpaperViewState
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
        initial = WallpaperViewState()
    )
    MixedScreen(viewState = state, openDetail = openDetail)
}

@Composable
internal fun MixedScreen(
    viewState: WallpaperViewState,
    openDetail: (ItemWrapperList) -> Unit
) {

}