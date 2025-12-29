package com.sfaxdroid.wallpapers.mixed

import com.sfaxdroid.wallpapers.core.GroupUiModel

internal sealed class MixedWallpaperUiState {
    data class Success(val sections: List<GroupUiModel>) : MixedWallpaperUiState()
    data object Failure : MixedWallpaperUiState()
    data object Loading : MixedWallpaperUiState()
}
