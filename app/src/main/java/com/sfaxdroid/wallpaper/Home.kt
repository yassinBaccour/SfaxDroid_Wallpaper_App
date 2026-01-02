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
fun HomeScreen(rateApp: () -> Unit) {
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
                        openDetail = { url, tags, source ->
                            backStack += Destination.Detail(url = url, tag = tags, source = source)
                        },
                        openTag = { title, tag, showPartner ->
                            backStack += Destination.Tag(
                                title = title,
                                tag = tag,
                                loadFromPartner = showPartner
                            )
                        }
                    )
                }
                entry<Destination.Tag> { key ->
                    rateApp.invoke()
                    TagScreen(
                        title = key.title,
                        tag = key.tag,
                        loadFromPartner = key.loadFromPartner,
                        openDetail = { url, tags, source ->
                            backStack += Destination.Detail(url = url, tag = tags, source = source)
                        },
                        openTag = { title, tag, showPartner ->
                            backStack += Destination.Tag(
                                title = title,
                                tag = tag,
                                loadFromPartner = showPartner
                            )
                        },
                        goBack = { backStack.removeLastOrNull() })
                }
                entry<Destination.Detail> { key ->
                    WallpaperDetail(
                        url = key.url,
                        tag = key.tag,
                        source = key.source,
                        goBack = { backStack.removeLastOrNull() },
                        openTag = { tag ->
                            backStack += Destination.Tag(
                                title = tag,
                                tag = Pair(tag, ""),
                                loadFromPartner = true
                            )
                        }
                    )
                }
            }
        )
    }
}