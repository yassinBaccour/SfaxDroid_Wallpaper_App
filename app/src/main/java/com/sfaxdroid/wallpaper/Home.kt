package com.sfaxdroid.wallpaper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.sfaxdroid.detail.ui.WallpaperDetail
import com.sfaxdroid.wallpaper.ui.theme.WallpaperAppTheme
import com.sfaxdroid.wallpapers.mixed.MixedWallpaperScreen
import com.sfaxdroid.wallpapers.tag.TagScreen

@Composable
fun HomeScreen() {
    val backStack = remember { mutableStateListOf<Any>(Destination.Wallpaper) }
    WallpaperAppTheme {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<Destination.Wallpaper> {
                    MixedWallpaperScreen(
                        openDetail = { backStack += Destination.Detail(it, listOf(), "") },
                        openTag = {  title, tag , showPartner ->
                            backStack += Destination.Tag(title, tag, showPartner) }
                    )
                }
                entry<Destination.Tag> { key ->
                    TagScreen(
                        title = key.title,
                        tag = key.tag,
                        loadFromPartner = key.loadFromPartner,
                        openDetail = { backStack += Destination.Detail(it, listOf(), "") },
                        openTag = {  title, tag , showPartner ->
                            backStack += Destination.Tag(title, tag, showPartner)
                        },
                        goBack = { backStack.removeLastOrNull() })
                }
                entry<Destination.Detail> { key ->
                    WallpaperDetail(
                        url = key.url,
                        tag = key.tag,
                        source = key.source
                    ) { backStack.removeLastOrNull() }
                }
            }
        )
    }
}