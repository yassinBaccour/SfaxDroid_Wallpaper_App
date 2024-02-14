package com.sfaxdroid.list.pixa

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.sfaxdroid.bases.NavScreen
import com.sfaxdroid.bases.encodeUrl
import com.sfaxdroid.data.mappers.PixaItem

@Composable
fun PixaBayGridItem(picture: PixaItem,onWallpaperClick: (String) -> Unit) {
    SubcomposeAsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.125.dp)
            .height(220.45.dp)
            .clickable {
                onWallpaperClick(picture.largeImageURL.encodeUrl())
            },
        loading = {
            CircularProgressIndicator()
        },
        contentScale = ContentScale.Crop,
        model = picture.webformatURL.replace("_640.", "_180."),
        contentDescription = "this image has " + picture.likes + " likes"
    )
}