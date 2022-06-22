package com.yassin.wallpaper.home

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.yassin.wallpaper.AppNavigation
import com.yassin.wallpaper.HomeNavigationItem
import com.yassin.wallpaper.R
import com.yassin.wallpaper.Screen

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
internal fun MainHome(onTabChanged: (String) -> Unit) {

    val navController = rememberAnimatedNavController()

    Scaffold(bottomBar = {
        val currentSelectedItem by navController.currentScreenAsState()
        HomeBottomNavigation(
            selectedNavigation = currentSelectedItem,
            onNavigationSelected = { selected ->
                onTabChanged(selected.route)
                navController.navigate(selected.route)
            },
            modifier = Modifier.fillMaxWidth()
        )
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
    selectedNavigation: Screen,
    onNavigationSelected: (Screen) -> Unit,
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
                    HomeNavigationItemIcon(
                        item = item,
                        selected = selectedNavigation == item.screen
                    )
                },
                label = { Text(text = stringResource(item.labelResId)) },
                selected = selectedNavigation == item.screen,
                onClick = { onNavigationSelected(item.screen) },
            )
        }
    }
}

@Composable
private fun HomeNavigationItemIcon(item: HomeNavigationItem, selected: Boolean) {
    val painter = when (item) {
        is HomeNavigationItem.ResourceIcon -> painterResource(item.iconResId)
        is HomeNavigationItem.ImageVectorIcon -> rememberVectorPainter(item.iconImageVector)
    }
    val selectedPainter = when (item) {
        is HomeNavigationItem.ResourceIcon -> item.selectedIconResId?.let { painterResource(it) }
        is HomeNavigationItem.ImageVectorIcon -> item.selectedImageVector?.let {
            rememberVectorPainter(it)
        }
    }

    if (selectedPainter != null) {
        Crossfade(targetState = selected) {
            Icon(
                painter = if (it) selectedPainter else painter,
                contentDescription = stringResource(item.contentDescriptionResId),
            )
        }
    } else {
        Icon(
            painter = painter,
            contentDescription = stringResource(item.contentDescriptionResId),
        )
    }
}

@Stable
@Composable
private fun NavController.currentScreenAsState(): State<Screen> {
    val selectedItem = remember { mutableStateOf<Screen>(Screen.Wallpaper) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == Screen.Wallpaper.route } -> {
                    selectedItem.value = Screen.Wallpaper
                }
                destination.hierarchy.any { it.route == Screen.LiveWallpaper.route } -> {
                    selectedItem.value = Screen.LiveWallpaper
                }
                destination.hierarchy.any { it.route == Screen.Category.route } -> {
                    selectedItem.value = Screen.Category
                }
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
    HomeNavigationItem.ImageVectorIcon(
        screen = Screen.Wallpaper,
        labelResId = R.string.screen_wallpaper,
        contentDescriptionResId = R.string.screen_wallpaper,
        iconImageVector = Icons.Filled.Search,
        selectedImageVector = Icons.Filled.Search,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = Screen.LiveWallpaper,
        labelResId = R.string.screen_lwp,
        contentDescriptionResId = R.string.screen_lwp,
        iconImageVector = Icons.Filled.Search,
        selectedImageVector = Icons.Filled.Search,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = Screen.Category,
        labelResId = R.string.screen_category,
        contentDescriptionResId = R.string.screen_category,
        iconImageVector = Icons.Filled.Search,
        selectedImageVector = Icons.Filled.Search,
    )
)