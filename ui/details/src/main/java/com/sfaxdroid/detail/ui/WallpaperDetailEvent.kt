package com.sfaxdroid.detail.ui

import androidx.annotation.StringRes

internal sealed class WallpaperDetailEvent {
    data class OpenTag(val tag: String)  : WallpaperDetailEvent()
    data class ShowMessage(@StringRes val message: Int) : WallpaperDetailEvent()
}