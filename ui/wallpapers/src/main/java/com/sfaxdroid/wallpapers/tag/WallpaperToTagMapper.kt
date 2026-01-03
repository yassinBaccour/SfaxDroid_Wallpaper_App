package com.sfaxdroid.wallpapers.tag

import com.sfaxdroid.domain.entity.WallpaperGroup
import javax.inject.Inject

internal class WallpaperToTagMapper @Inject constructor() {

    fun map(wallpaperGroup: List<WallpaperGroup>) = wallpaperGroup
        .flatMap { it.wallpapers }
        .flatMap { it.tag }
        .map { it to "" }
        .distinctBy { it.first }
}