package com.sfaxdroid.detail.ui

import android.graphics.Bitmap

internal data class WallpaperDetailUiState(
    val url: String = "",
    val wallpaper: Bitmap? = null,
    val tag: List<String> = arrayListOf(),
    val source: String = "",
    val isImageDark: Boolean = false,
    val isWallpaperLoading: Boolean = true
)
