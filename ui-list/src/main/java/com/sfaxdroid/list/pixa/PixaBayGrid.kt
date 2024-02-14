package com.sfaxdroid.list.pixa

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sfaxdroid.bases.NavScreen
import com.sfaxdroid.data.mappers.PixaItem

@Composable
internal fun PixaBayGrid(pictureList: List<PixaItem>, navController: NavController) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Adaptive(124.dp)
    ) {
        items(pictureList.size) { index ->
            PixaBayGridItem(pictureList[index]){
                 url -> navController.navigate(NavScreen.Pixabay.route + NavScreen.Detail.route + "/" + url)
            }
        }
    }
}