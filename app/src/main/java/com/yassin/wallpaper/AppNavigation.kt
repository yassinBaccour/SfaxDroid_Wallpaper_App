package com.yassin.wallpaper

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.sfaxdroid.list.ui.CategoryList
import com.sfaxdroid.list.ui.LiveWallpaperList
import com.sfaxdroid.list.ui.WallpaperList

sealed class Screen(val route: String) {
    object Wallpaper : Screen("wallpaper")
    object LiveWallpaper : Screen("liveWallpaper")
    object Category : Screen("category")
    object Detail : Screen("detail")
}

@ExperimentalAnimationApi
@Composable
internal fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.Wallpaper.route,
        modifier = modifier,
    ) {
        addWallpaperAsStartDestination(navController)
        addLwpAsStartDestination(navController)
        addCatAsStartDestination(navController)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addWallpaperAsStartDestination(
    navController: NavController
) {
    navigation(
        route = Screen.Wallpaper.route,
        startDestination = "home/" + Screen.Wallpaper.route,
    ) {
        addWallpaper(navController, "home/" + Screen.Wallpaper.route, arrayListOf())
        addDetail(navController, "home/" + Screen.Detail.route, arrayListOf())
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addLwpAsStartDestination(
    navController: NavController
) {
    navigation(
        route = Screen.LiveWallpaper.route,
        startDestination = "home/" + Screen.LiveWallpaper.route,
    ) {
        addLiveWallpaper(navController, "home/" + Screen.LiveWallpaper.route, arrayListOf())
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addCatAsStartDestination(
    navController: NavController
) {
    navigation(
        route = Screen.Category.route,
        startDestination = "home/" + Screen.Category.route,
    ) {
        addCategory(navController, "home/" + Screen.Category.route, arrayListOf())
        addWallpaper(navController, "home/" + Screen.Wallpaper.route, arrayListOf())
        addDetail(navController, "home/" + Screen.Detail.route, arrayListOf())
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.addWallpaper(
    navController: NavController,
    root: String,
    list: List<NamedNavArgument>
) {
    composable(root, arguments = list) {
        WallpaperList {
            navController.navigate(it)
        }
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.addLiveWallpaper(
    navController: NavController,
    root: String,
    list: List<NamedNavArgument>
) {
    composable(root, arguments = list) {
        LiveWallpaperList {
            navController.navigate("")
        }
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.addCategory(
    navController: NavController,
    root: String,
    list: List<NamedNavArgument>
) {
    composable(root, arguments = list) {
        CategoryList {
            navController.navigate(it)
        }
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.addDetail(
    navController: NavController,
    root: String,
    list: List<NamedNavArgument>
) {
    composable(root, arguments = list) {

    }
}
