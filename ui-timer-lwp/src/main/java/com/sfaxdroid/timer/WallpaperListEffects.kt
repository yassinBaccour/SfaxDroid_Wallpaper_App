package com.sfaxdroid.timer

import com.sfaxdroid.bases.UiEffect

sealed class WallpaperListEffects : UiEffect {
    data class DeleteImage(var url: String) : WallpaperListEffects()
    data class SaveImage(var url: String) : WallpaperListEffects()
}
