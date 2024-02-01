package com.yassin.wallpaper

import android.icu.number.Scale
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.yassin.wallpaper.Model.PixaPicture
import com.yassin.wallpaper.viewModel.PictureViewModel

val pictureViewModel by lazy { PictureViewModel() }
@Composable
fun Screen() {
    Surface(Modifier.fillMaxSize()){
        pictureViewModel.getPictureList()
        Pictures(pictureViewModel.pictureListResponse)
    }

}



@Composable
fun PictureItem(picture: PixaPicture) {
    Card(
        modifier = Modifier
            .padding(1.dp)
            .fillMaxWidth()
            .aspectRatio(16f / 9f),
        elevation = 3.dp
    ){
        AsyncImage(
            model = picture.previewURL,
            contentDescription = "this image has "+picture.likes+" likes",
            modifier = Modifier.fillMaxSize()
        )
    }
}


@Composable
fun Pictures(pictureList: List<PixaPicture>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(128.dp)
    ) {
        items(pictureList.size) { index ->
            PictureItem(pictureList[index])
        }
    }
}


