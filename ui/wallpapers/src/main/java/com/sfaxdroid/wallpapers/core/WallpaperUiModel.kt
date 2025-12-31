package com.sfaxdroid.wallpapers.core

internal data class WallpaperUiModel(val previewUrl: String, val detailUrl: String)

internal sealed class GroupUiModel {
    data class TAG(val list: List<String>) : GroupUiModel()
    data class CARROUSEL(val list: List<WallpaperUiModel>) : GroupUiModel()
    data class GRID(val list: List<WallpaperUiModel>) : GroupUiModel()
    data class RANDOM_GRID(val list: List<WallpaperUiModel>) : GroupUiModel()
    data class OF_THE_DAY(val list: List<WallpaperUiModel>) : GroupUiModel()
    data object HOME_HEADER : GroupUiModel()
}