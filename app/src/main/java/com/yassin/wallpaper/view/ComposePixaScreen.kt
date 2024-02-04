package com.yassin.wallpaper.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.yassin.wallpaper.Model.PixaPicture
import com.yassin.wallpaper.viewModel.PictureViewModel
import timber.log.Timber

val pictureViewModel by lazy { PictureViewModel() }

@Preview
@Composable
fun PixaScreen() {
    pictureViewModel.getPictureList()
    Surface(
        modifier = Modifier
            .background(Color.Blue)
            .fillMaxWidth()
            .height(260.dp)
            .padding(.5.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(2)
    ) {
        Pictures(pictureViewModel.pixaApiResponse.hits)
    }

}


@Composable
fun PictureItem(picture: PixaPicture) {

    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.125.dp)
            .height(220.45.dp),
        contentScale = ContentScale.Crop,
        model = picture.previewURL,
        contentDescription = "this image has " + picture.likes + " likes",
    )

}


@Composable
fun Pictures(pictureList: List<PixaPicture>) {

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Adaptive(124.dp)
    ) {
        Timber.d("PictureListSize: ${pictureList.size}")
        items(pictureList.size) { index ->
            PictureItem(pictureList[index])
            Timber.d("PictureItemIndex: $index")
        }
    }
}


