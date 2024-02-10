package com.sfaxdroid.list.pixa

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sfaxdroid.bases.NavScreen
import com.sfaxdroid.bases.encodeUrl
import com.sfaxdroid.data.mappers.PixaItem
import okio.ByteString.Companion.encodeUtf8

@Composable
fun PixaBayGridItem(picture: PixaItem, navController: NavController) {
    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.125.dp)
            .height(220.45.dp)
            .clickable {
                navController.navigate(NavScreen.Pixabay.route + NavScreen.Detail.route + "/" + picture.largeImageURL.encodeUrl())
            },
        contentScale = ContentScale.Crop,
        model = picture.previewURL,
        contentDescription = "this image has " + picture.likes + " likes"
    )
}