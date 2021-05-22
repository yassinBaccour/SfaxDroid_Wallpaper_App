package com.yassin.wallpaper.feature.home

import com.sfaxdroid.data.mappers.BaseWallpaperView
import com.sfaxdroid.data.mappers.TagView

sealed class WallpaperListAction {
    data class LoadTags(var tagView: TagView) : WallpaperListAction()
    data class UpdateTagSelectedPosition(var position: Int) : WallpaperListAction()
    data class OpenItems(var wallpaperObject: BaseWallpaperView) : WallpaperListAction()
}
