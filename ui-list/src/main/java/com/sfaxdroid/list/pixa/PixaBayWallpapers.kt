package com.sfaxdroid.list.pixa

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController


@Composable
fun PixaBayWallpapers(navController: NavController) {
    PixaBayWallpapers(pixaBayViewModel = hiltViewModel(), navController)
}

@Composable
private fun PixaBayWallpapers(pixaBayViewModel: PixabayViewModel, navController: NavController) {
    PixaBayGrid(pixaBayViewModel.pixaApiResponse.hits.shuffled(), navController)
}

