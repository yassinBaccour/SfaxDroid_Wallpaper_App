package com.sfaxdroid.wallpapers.core.mapper

import com.sfaxdroid.domain.entity.Wallpaper
import com.sfaxdroid.wallpapers.core.WallpaperUiModel

internal fun Wallpaper.toUiModel() = WallpaperUiModel(this.previewUrl, this.detailUrl)