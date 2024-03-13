package com.yassin.wallpaper.home

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.sfaxdroid.bases.NavScreen
import com.sfaxdroid.data.entity.AppName
import com.yassin.wallpaper.AppNavigation
import com.yassin.wallpaper.R

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
internal fun MainHome(appName: AppName, onTabChanged: (String) -> Unit) {

    val navController = rememberNavController()

    Scaffold(bottomBar = {
        if (appName == AppName.AccountOne) {
            val currentSelectedItem by navController.currentScreenAsState()
            HomeBottomNavigation(
                selectedNavigation = currentSelectedItem,
                onNavigationSelected = { selected ->
                    onTabChanged(selected.route)
                    navController.navigate(selected.route)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }) {
        Row(Modifier.fillMaxSize()) {
            AppNavigation(
                navController = navController,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            )
        }
    }
}

@Composable
internal fun HomeBottomNavigation(
    selectedNavigation: NavScreen,
    onNavigationSelected: (NavScreen) -> Unit,
    modifier: Modifier = Modifier,
) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = contentColorFor(MaterialTheme.colors.surface),
        modifier = modifier
    ) {
        HomeNavigationItems.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Box(Modifier.size(24.dp)) {
                        AsyncImage(
                            if (selectedNavigation == item.screen) item.iconImageVectorSelected else item.iconImageVector,
                            contentDescription = stringResource(item.contentDescriptionResId),
                        )
                    }
                },
                label = {
                    Text(
                        text = stringResource(item.labelResId),
                        style = MaterialTheme.typography.caption,
                        color = if (selectedNavigation == item.screen) MaterialTheme.colors.onSecondary else Color.Black
                    )
                },
                selected = selectedNavigation == item.screen,
                onClick = { onNavigationSelected(item.screen) },
            )
        }
    }
}

@Stable
@Composable
private fun NavController.currentScreenAsState(): State<NavScreen> {
    val selectedItem = remember { mutableStateOf<NavScreen>(NavScreen.Wallpaper) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == NavScreen.Wallpaper.route } -> {
                    selectedItem.value = NavScreen.Wallpaper
                }
                destination.hierarchy.any { it.route == NavScreen.LiveWallpaper.route } -> {
                    selectedItem.value = NavScreen.LiveWallpaper
                }
                destination.hierarchy.any { it.route == NavScreen.Category.route } -> {
                    selectedItem.value = NavScreen.Category
                }
                destination.hierarchy.any { it.route == NavScreen.Pixabay.route } ->
                    selectedItem.value = NavScreen.Pixabay
            }
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}

private val HomeNavigationItems = listOf(
    HomeNavigationItem(
        screen = NavScreen.Wallpaper,
        labelResId = R.string.screen_wallpaper,
        contentDescriptionResId = R.string.screen_wallpaper,
        iconImageVector = R.drawable.ic_wallpaper,
        iconImageVectorSelected = R.drawable.ic_wallpaper_selected
    ),
    HomeNavigationItem(
        screen = NavScreen.LiveWallpaper,
        labelResId = R.string.screen_lwp,
        contentDescriptionResId = R.string.screen_lwp,
        iconImageVector = R.drawable.ic_lwp,
        iconImageVectorSelected = R.drawable.ic_lwp_selected
    ),
    HomeNavigationItem(
        screen = NavScreen.Category,
        labelResId = R.string.screen_category,
        contentDescriptionResId = R.string.screen_category,
        iconImageVector = R.drawable.ic_category,
        iconImageVectorSelected = R.drawable.ic_category_selected
    ),
    HomeNavigationItem(
        screen = NavScreen.Pixabay,
        labelResId = R.string.screen_pixa_wall,
        contentDescriptionResId = R.string.screen_pixa_wall,
        iconImageVector = R.drawable.ic_nature,
        iconImageVectorSelected = R.drawable.ic_nature_selected
    )
)