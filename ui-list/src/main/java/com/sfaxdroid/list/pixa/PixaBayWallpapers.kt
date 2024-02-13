package com.sfaxdroid.list.pixa

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sfaxdroid.bases.NavScreen


@Composable
fun PixaBayWallpapers(navController: NavController) {
    PixaBayWallpapers(pixaBayViewModel = hiltViewModel(), navController)
}

@Composable
fun PixaBayWallpapers(pixaBayViewModel: PixaBayViewModel, navController: NavController) {
    PixaBayGrid(pixaBayViewModel.pixaApiResponse.hits.shuffled(), navController)
}

