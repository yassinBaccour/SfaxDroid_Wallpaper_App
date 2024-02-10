package com.sfaxdroid.list.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun WallpaperDetail(url: String?) {
    AsyncImage(
        modifier = Modifier
            .fillMaxSize(),
        contentScale = ContentScale.Crop,
        model = url,
        contentDescription = null
    )
}