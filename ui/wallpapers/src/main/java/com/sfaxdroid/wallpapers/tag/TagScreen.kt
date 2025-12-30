package com.sfaxdroid.wallpapers.tag

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sfaxdroid.wallpapers.core.GroupUiModel
import com.sfaxdroid.wallpapers.core.view.FailureScreenMinimal
import com.sfaxdroid.wallpapers.core.view.LoadingContent
import com.sfaxdroid.wallpapers.core.view.WallpaperContentList

@Composable
fun TagScreen(
    tag: String,
    openDetail: (String) -> Unit,
    openTag: (String) -> Unit,
    goBack: () -> Unit
) =
    WallpaperScreen(
        viewModel = hiltViewModel(),
        tag = tag,
        openDetail = openDetail,
        openTag = openTag,
        goBack = goBack
    )

@Composable
private fun WallpaperScreen(
    viewModel: TagScreenViewModel,
    tag: String,
    openDetail: (String) -> Unit,
    openTag: (String) -> Unit,
    goBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.getCustomUrlProduct(tag, false)
    }
    when (state) {
        is TagUiState.Success -> TagScreenContent(
            group = (state as TagUiState.Success).sections,
            title = tag,
            openDetail = openDetail,
            openTag = openTag,
            goBack = goBack
        )

        TagUiState.Loading -> LoadingContent()
        TagUiState.Failure -> FailureScreenMinimal { viewModel.getCustomUrlProduct(tag, false) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TagScreenContent(
    group: List<GroupUiModel>,
    title: String,
    openDetail: (String) -> Unit,
    openTag: (String) -> Unit,
    goBack: () -> Unit
) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = title) },
            navigationIcon = {
                IconButton(onClick = { goBack.invoke() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )
    }) { innerPadding ->
        WallpaperContentList(
            modifier = Modifier.padding(innerPadding),
            group = group,
            openDetail = openDetail,
            openTag = openTag
        )
    }
}

