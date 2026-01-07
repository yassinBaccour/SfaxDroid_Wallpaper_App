package com.sfaxdroid.wallpapers.core

internal data class WallpaperUiModel(val previewUrl: String, val detailUrl: String, val tag: List<String>, val source: String)

internal sealed class GroupUiModel {
    data object HOME_HEADER : GroupUiModel()
    data class TAG(val list: List<Pair<String, String>>) : GroupUiModel()
    data class PARTNER_TAG(val list: List<Pair<String, String>>) : GroupUiModel()
    data class CARROUSEL(val list: List<WallpaperUiModel>, val showMoreTag: String = "") : GroupUiModel()
    data class GRID(val list: List<WallpaperUiModel>, val showMoreTag: String = "") : GroupUiModel()
    data class RANDOM_GRID(val list: List<WallpaperUiModel>, val showMoreTag: String = "") : GroupUiModel()
    data class OF_THE_DAY(val list: List<WallpaperUiModel>, val showMoreTag: String = "") : GroupUiModel()
    data class PARTNER_TAG_CARROUSEL(val list: List<Pair<String, String>>) : GroupUiModel()
    data object SkyBox : GroupUiModel()
    data object LOADING_GRID : GroupUiModel()
}