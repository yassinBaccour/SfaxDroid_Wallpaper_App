package com.sfaxdroid.detail.ui

import android.Manifest
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sfaxdroid.detail.ui.WallpaperUtils.isColorDark
import com.sfaxdroid.details.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
@RequiresPermission(Manifest.permission.SET_WALLPAPER)
fun WallpaperDetail(url: String, tag: List<String>, source: String, goBack: () -> Unit) {

    val context = LocalContext.current
    var isSetWallpaperLoading by remember { mutableStateOf(false) }
    var isImageLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val text = stringResource(R.string.wallpaper_set_successfully)

    var isImageDark by remember { mutableStateOf(true) }

    Box {
        if (isImageLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                strokeWidth = 2.5.dp
            )
        }
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(url)
                .allowHardware(false)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.fillMaxSize(),
            onSuccess = { result ->
                isImageLoading = false
                bitmap = (result.result.drawable as BitmapDrawable).bitmap
                isImageDark = isColorDark(bitmap!!)
            }
        )
        GoBackButton(goBack = goBack, isImageDark = isImageDark)
        FloatingActionButton(
            onClick = {
                if (isSetWallpaperLoading) return@FloatingActionButton
                bitmap?.let {
                    coroutineScope.launch {
                        isSetWallpaperLoading = true
                        try {
                            withContext(Dispatchers.IO) {
                                WallpaperUtils.setWallpaperWithChooser(context, it)
                            }
                        } finally {
                            Toast.makeText(
                                context,
                                text,
                                Toast.LENGTH_LONG
                            ).show()
                            isSetWallpaperLoading = false
                        }
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            if (isSetWallpaperLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    strokeWidth = 2.5.dp
                )
            } else {
                Icon(Icons.Default.Wallpaper, contentDescription = null)
            }
        }
        SourceIndicator(source)
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

@Preview
@Composable
@RequiresPermission(Manifest.permission.SET_WALLPAPER)
internal fun WallpaperDetailPreview() = WallpaperDetail("", listOf(), "") {}

const val PARTNER_NAME = "Pixabay"
const val PUBLISHER_NAME = "SfaxDroid"
