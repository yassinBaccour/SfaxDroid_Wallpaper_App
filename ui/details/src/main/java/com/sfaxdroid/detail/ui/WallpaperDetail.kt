package com.sfaxdroid.detail.ui

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sfaxdroid.commion.ui.compose.Destination
import com.sfaxdroid.details.R


@Composable
fun WallpaperDetail(
    detail: Destination.Detail,
    goBack: () -> Unit,
    openTag: (String) -> Unit
) {
    val viewModel = hiltViewModel<WallpaperDetailViewModel, WallpaperDetailViewModel.Factory>(
        creationCallback = { factory ->
            factory.create(detail)
        }
    )
    WallpaperDetail(
        viewModel = viewModel,
        goBack = goBack,
        openTag = openTag
    )
}

@Composable
@RequiresPermission(Manifest.permission.SET_WALLPAPER)
internal fun WallpaperDetail(
    viewModel: WallpaperDetailViewModel,
    goBack: () -> Unit,
    openTag: (String) -> Unit
) {

    val context = LocalContext.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        viewModel.events.flowWithLifecycle(lifecycleOwner.lifecycle).collect {
            when (it) {
                is WallpaperDetailEvent.OpenTag -> openTag(it.tag)
                is WallpaperDetailEvent.ShowMessage -> showToast(
                    context = context,
                    textId = it.message
                )
            }
        }
    }

    WallpaperDetailContent(
        wallpaperDetailUiModel = state,
        goBack = goBack,
        openTag = openTag,
        setWallpaper = { viewModel.setAsWallpaper(context) },
        addBitmap = viewModel::addBitmap
    )

}

@Composable
@RequiresPermission(Manifest.permission.SET_WALLPAPER)
internal fun WallpaperDetailContent(
    wallpaperDetailUiModel: WallpaperDetailUiState,
    goBack: () -> Unit,
    openTag: (String) -> Unit,
    setWallpaper: () -> Unit,
    addBitmap: (Bitmap) -> Unit
) {

    val context = LocalContext.current

    Box {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(wallpaperDetailUiModel.url)
                .allowHardware(false)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.fillMaxSize(),
            onSuccess = { result ->
                addBitmap.invoke((result.result.drawable as BitmapDrawable).bitmap)
            }
        )
        GoBackButton(goBack = goBack, isImageDark = wallpaperDetailUiModel.isImageDark)
        MoreInformationButton(
            tags = wallpaperDetailUiModel.tag,
            isImageDark = wallpaperDetailUiModel.isImageDark,
            source = wallpaperDetailUiModel.source,
            openTag = openTag
        )
        FloatingActionButton(
            onClick = {
                if (wallpaperDetailUiModel.isWallpaperLoading) return@FloatingActionButton
                setWallpaper.invoke()
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            if (wallpaperDetailUiModel.isWallpaperLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    strokeWidth = 2.5.dp
                )
            } else {
                Icon(Icons.Default.Wallpaper, contentDescription = null)
            }
        }
        SourceIndicator(wallpaperDetailUiModel.source)
    }
}


@Composable
private fun TagsContent(tags: List<String>, openTag: (String) -> Unit) {
    LazyRow {
        items(tags) {
            FilledTonalButton(
                modifier = Modifier
                    .height(30.dp)
                    .padding(start = 8.dp)
                    .alpha(0.8f),
                onClick = { openTag.invoke(it) },
                contentPadding = PaddingValues(horizontal = 1.dp, vertical = 1.dp)
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    text = it,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun BoxScope.SourceIndicator(source: String) {
    if (source != PUBLISHER_NAME) {
        Card(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .navigationBarsPadding()
                .padding(16.dp)
                .alpha(0.8f)
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                text = stringResource(R.string.source) + " : " + PARTNER_NAME,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun BoxScope.GoBackButton(
    goBack: () -> Unit,
    isImageDark: Boolean
) {
    IconButton(
        onClick = { goBack.invoke() },
        modifier = Modifier
            .statusBarsPadding()
            .align(Alignment.TopStart)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            tint = if (isImageDark) Color.White else Color.Black
        )
    }
}

@Composable
private fun BoxScope.MoreInformationButton(
    tags: List<String>,
    isImageDark: Boolean,
    source: String,
    openTag: (String) -> Unit
) {
    if (source != PUBLISHER_NAME) {
        var showMore by rememberSaveable { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .align(Alignment.TopEnd),
            horizontalAlignment = Alignment.End
        ) {
            IconButton(
                onClick = { showMore = !showMore }) {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    tint = if (isImageDark) Color.White else Color.Black
                )
            }
            if (showMore) {
                TagsContent(tags, openTag)
            }
        }
    }
}

private fun showToast(context: Context, textId: Int) = Toast.makeText(
    context,
    context.getString(textId),
    Toast.LENGTH_LONG
).show()

@Preview
@Composable
@RequiresPermission(Manifest.permission.SET_WALLPAPER)
internal fun WallpaperDetailPreview() = WallpaperDetailContent(
    wallpaperDetailUiModel = WallpaperDetailUiState(),
    goBack = {}, openTag = {}, setWallpaper = {}, addBitmap = { })

const val PARTNER_NAME = "Pixabay"
const val PUBLISHER_NAME = "SfaxDroid"
