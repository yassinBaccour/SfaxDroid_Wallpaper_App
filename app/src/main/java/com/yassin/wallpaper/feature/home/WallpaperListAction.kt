package com.yassin.wallpaper.feature.home

import com.sfaxdroid.data.mappers.BaseWallpaperView
import com.sfaxdroid.data.mappers.TagView

sealed class WallpaperListAction {
    data class LoadTags(var tagView: TagView) : WallpaperListAction()
    data class OpenItems(var wallpaperObject: BaseWallpaperView) : WallpaperListAction()
}
