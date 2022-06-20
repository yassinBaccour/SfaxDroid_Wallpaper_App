package com.yassin.wallpaper

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.navigation

internal sealed class Screen(val route: String) {
    object Home : Screen("home")
    object LiveWallpaper : Screen("liveWallpaper")
    object Category : Screen("category")
}

@ExperimentalAnimationApi
@Composable
internal fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
    ) {
        addHomeTopLevel(navController)
        addLwpTopLevel(navController)
        addCatTopLevel(navController)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addHomeTopLevel(
    navController: NavController
) {
    navigation(
        route = Screen.Home.route,
        startDestination = Screen.Home.route,
    ) {
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addLwpTopLevel(
    navController: NavController
) {
    navigation(
        route = Screen.LiveWallpaper.route,
        startDestination = "",
    ) {
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addCatTopLevel(
    navController: NavController
) {
    navigation(
        route = Screen.Category.route,
        startDestination = "",
    ) {
    }
}
