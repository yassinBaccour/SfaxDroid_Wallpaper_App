package com.sfaxdroid.data.mapper

import com.sfaxdroid.domain.entity.WallpaperTheme
import javax.inject.Inject

internal class WallpaperThemeMapper @Inject constructor() {
    fun map(from: String) = when (from) {
        "vector" -> WallpaperTheme.NEW
        else -> WallpaperTheme.OTHER
    }
}

