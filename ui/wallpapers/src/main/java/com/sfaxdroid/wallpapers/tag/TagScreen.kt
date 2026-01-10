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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sfaxdroid.commion.ui.compose.Destination
import com.sfaxdroid.wallpapers.core.view.FailureScreenMinimal
import com.sfaxdroid.wallpapers.core.view.LoadingContent
import com.sfaxdroid.wallpapers.core.view.list.WallpaperContentList

@Composable
fun TagScreen(
    key: Destination.Tag,
    openDetail: (String, List<String>, String) -> Unit,
    openTag: (String, Pair<String, String>, Boolean) -> Unit,
    goBack: () -> Unit
) {
    val viewModel = hiltViewModel<TagScreenViewModel, TagScreenViewModel.Factory>(
        creationCallback = { factory ->
            factory.create(key)
        }
    )
    TagScreen(
        viewModel = viewModel,
        openDetail = openDetail,
        openTag = openTag,
        goBack = goBack
    )
}

@Composable
private fun TagScreen(
    viewModel: TagScreenViewModel,
    openDetail: (String, List<String>, String) -> Unit,
    openTag: (String, Pair<String, String>, Boolean) -> Unit,
    goBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    when (state) {
        is TagState.Success -> TagContent(
            state = state as TagState.Success,
            openDetail = openDetail,
            openTag = openTag,
            goBack = goBack,
            loadTag = viewModel::getNewWallpaperByTag
        )

        TagState.Loading -> LoadingContent()
        TagState.Failure -> FailureScreenMinimal(viewModel::getWallpaperByTag)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TagContent(
    state: TagState.Success,
    openDetail: (String, List<String>, String) -> Unit,
    openTag: (String, Pair<String, String>, Boolean) -> Unit,
    goBack: () -> Unit,
    loadTag: (Pair<String, String>) -> Unit
) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = state.title) },
            navigationIcon = {
                IconButton(onClick = { goBack.invoke() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        )
    }) { innerPadding ->
        WallpaperContentList(
            modifier = Modifier.padding(innerPadding),
            group = state.sections,
            openDetail = openDetail,
            openTag = openTag,
            loadTag = loadTag,
            openSkyBox = {}
        )
    }
}

