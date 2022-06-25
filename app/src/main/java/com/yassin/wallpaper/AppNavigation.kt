package com.yassin.wallpaper

import android.os.Bundle
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.extension.getFullUrl
import com.sfaxdroid.bases.NavScreen
import com.sfaxdroid.data.entity.AppName
import com.sfaxdroid.data.entity.LiveWallpaper
import com.sfaxdroid.data.mappers.SimpleWallpaperView
import com.sfaxdroid.list.ui.CategoryList
import com.sfaxdroid.list.ui.LiveWallpaperList
import com.sfaxdroid.list.ui.WallpaperList

@ExperimentalAnimationApi
@Composable
internal fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = NavScreen.Wallpaper.route,
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
        route = NavScreen.Wallpaper.route,
        startDestination = "home/" + NavScreen.Wallpaper.route,
    ) {
        addWallpaper(
            navController, "home/" + NavScreen.Wallpaper.route, listOf(
                navArgument("keyJsonFileName") {
                    defaultValue = "4k.json"
                })
        )
        addDetail(navController, "home/" + NavScreen.Detail.route, arrayListOf())
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addLwpAsStartDestination(
    navController: NavController
) {
    navigation(
        route = NavScreen.LiveWallpaper.route,
        startDestination = "home/" + NavScreen.LiveWallpaper.route,
    ) {
        addLiveWallpaper(
            navController, "home/" + NavScreen.LiveWallpaper.route, listOf(
                navArgument("keyJsonFileName") {
                    defaultValue = "lwp.json"
                })
        )
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addCatAsStartDestination(
    navController: NavController
) {
    navigation(
        route = NavScreen.Category.route,
        startDestination = "home/" + NavScreen.Category.route,
    ) {
        addCategory(
            navController, "home/" + NavScreen.Category.route, listOf(
                navArgument("keyJsonFileName") {
                    defaultValue = "category.json"
                })
        )
        addWallpaper(navController, "home/" + NavScreen.Wallpaper.route, arrayListOf())
        addDetail(navController, "home/" + NavScreen.Detail.route, arrayListOf())
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.addWallpaper(
    navController: NavController,
    root: String,
    list: List<NamedNavArgument>
) {
    composable(root, arguments = list) {
        WallpaperList { baseWallpaperView, selectedLwpName, screenName, appName ->
            val wallpaper = baseWallpaperView as SimpleWallpaperView
            openWallpaperByType(
                navController,
                wallpaper,
                selectedLwpName,
                screenName,
                appName
            )
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
            when (it.type) {
                is LiveWallpaper.WordImg -> {
                    //navToTexture(Constants.KEY_WORD_IMG_LWP, wallpaperObject.name)
                }
                is LiveWallpaper.SkyView -> {
                    //Utils.openLiveWallpaper<SkyLiveWallpaper>(requireContext())
                }
                is LiveWallpaper.TimerLwp -> {
                    //navToTimer(wallpaperObject.name)
                }
                is LiveWallpaper.Word2d -> {
                    //navToTexture(Constants.KEY_ANIM_2D_LWP, wallpaperObject.name)
                }
                else -> {}
            }
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
            Bundle().apply {
                putString(Constants.EXTRA_JSON_FILE_NAME, it.file)
                putString(Constants.EXTRA_SCREEN_NAME, it.name)
                putString(Constants.EXTRA_SCREEN_TYPE, "CAT_WALL")
            }
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

private fun openWallpaperByType(
    navController: NavController,
    wallpaperObject: SimpleWallpaperView,
    selectedLwpName: String,
    screenName: String,
    appName: AppName
) {
    when (selectedLwpName) {
        Constants.KEY_WORD_IMG_LWP -> {
            Bundle().apply {
                putString(
                    Constants.EXTRA_URL_TO_DOWNLOAD,
                    wallpaperObject.detailUrl.getFullUrl()
                )
                putString(
                    Constants.EXTRA_SCREEN_NAME,
                    screenName
                )
            }
        }
        Constants.KEY_ANIM_2D_LWP -> {
            Bundle().apply {
                putString(
                    Constants.EXTRA_URL_TO_DOWNLOAD,
                    wallpaperObject.detailUrl.getFullUrl()
                )
                putString(
                    Constants.EXTRA_SCREEN_NAME,
                    screenName
                )
            }
        }
        Constants.KEY_RIPPLE_LWP -> {
            Bundle().apply {
                putString(
                    Constants.EXTRA_IMG_URL,
                    wallpaperObject.detailUrl.getFullUrl()
                )
                putBoolean(
                    Constants.KEY_IS_FULL_SCREEN,
                    appName == AppName.AccountTwo
                )
                if (selectedLwpName.isNotEmpty()) {
                    putString(Constants.KEY_LWP_NAME, selectedLwpName)
                }
            }
        }
        Constants.KEY_ADD_TIMER_LWP -> {
            showDetailViewActivity(
                navController,
                wallpaperObject,
                Constants.KEY_ADD_TIMER_LWP,
                appName
            )
        }
        Constants.KEY_ADDED_LIST_TIMER_LWP -> {
            showDetailViewActivity(
                navController,
                wallpaperObject,
                Constants.KEY_ADDED_LIST_TIMER_LWP,
                appName
            )
        }
        else -> {
            showDetailViewActivity(navController, wallpaperObject, appName = appName)
        }
    }
}

private fun showDetailViewActivity(
    navController: NavController,
    wallpaperObject: SimpleWallpaperView,
    lwpName: String = "",
    appName: AppName
) {
    Bundle().apply {
        putString(
            Constants.EXTRA_IMG_URL,
            wallpaperObject.detailUrl.getFullUrl()
        )
        putBoolean(
            Constants.KEY_IS_FULL_SCREEN,
            appName == AppName.AccountTwo
        )
        if (lwpName.isNotEmpty()) {
            putString(Constants.KEY_LWP_NAME, lwpName)
        }
    }
}
