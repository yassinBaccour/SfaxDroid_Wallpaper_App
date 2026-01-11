package com.sfaxdroid.detail.ui

import androidx.annotation.StringRes
import com.sfaxdroid.commion.ui.compose.Destination

internal sealed class WallpaperDetailEvent {
    data class NavigateToDestination(val destination: Destination)  : WallpaperDetailEvent()
    data class ShowMessage(@StringRes val message: Int) : WallpaperDetailEvent()
}