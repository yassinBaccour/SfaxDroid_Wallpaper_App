package com.sfaxdroid.list.pixabay.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.sfaxdroid.bases.encodeUrl
import com.sfaxdroid.data.mappers.PixaItem

@Composable
internal fun WallpaperItem(picture: PixaItem, onWallpaperClick: (String) -> Unit) {
    Card(
        elevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .height(
                LocalConfiguration
                    .current
                    .screenHeightDp
                    .dp / 5f
            ),
        shape = RoundedCornerShape(8.dp),

    ){
        SubcomposeAsyncImage(
            modifier = Modifier.fillMaxSize()
                .clickable {
                    onWallpaperClick(picture.largeImageURL.encodeUrl())
                },
            loading = {
                LoadingImg(picture.previewURL)
            },
            contentScale = ContentScale.Crop,
            model = picture.webformatURL,
            contentDescription = null
        )
    }
}