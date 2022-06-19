package com.sfaxdroid.list

import com.sfaxdroid.data.mappers.CategoryItem
import com.sfaxdroid.data.mappers.LwpItem
import com.sfaxdroid.data.mappers.SimpleWallpaperView

sealed class WallpaperListEffects
data class OpenWallpaperByType(
    val simpleWallpaperView: SimpleWallpaperView, var parentName: String, var screenName: String
) : WallpaperListEffects()

data class OpenLiveWallpaper(val wallpaperObject: LwpItem) : WallpaperListEffects()
data class OpenCategory(val wallpaperObject: CategoryItem) : WallpaperListEffects()

