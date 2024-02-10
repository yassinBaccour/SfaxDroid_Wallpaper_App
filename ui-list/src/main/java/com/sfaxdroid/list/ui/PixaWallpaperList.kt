package com.sfaxdroid.list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sfaxdroid.bases.NavScreen
import com.sfaxdroid.data.mappers.PixaItem
import com.sfaxdroid.list.viewmodels.PixaViewModel


@Composable
fun PixaWallpaperList(navController: NavController) {
    PixaWallpaperList(pixaViewModel = hiltViewModel(), navController)

}

@Composable
fun PixaWallpaperList(pixaViewModel: PixaViewModel, navController: NavController) {
    pixaViewModel.getPictureList()
    Surface(
        modifier = Modifier
            .background(Color.Blue)
            .fillMaxWidth()
            .height(260.dp)
            .padding(.5.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(2)
    ) {
        Pictures(pixaViewModel.pixaApiResponse.hits, navController)
    }

}


fun openDetailsFragment(navController: NavController) {
    navController.navigate(NavScreen.Detail.route)
}

@Composable
fun PictureItem(picture: PixaItem, navController: NavController) {


    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.125.dp)
            .height(220.45.dp)
            .clickable {
                openDetailsFragment(navController)
            },
        contentScale = ContentScale.Crop,
        model = picture.previewURL,
        contentDescription = "this image has " + picture.likes + " likes"
    )

}


@Composable
fun Pictures(pictureList: List<PixaItem>, navController: NavController) {

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Adaptive(124.dp)
    ) {
        items(pictureList.size) { index ->
            PictureItem(pictureList[index], navController)
        }
    }
}


