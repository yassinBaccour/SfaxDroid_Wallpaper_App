package com.sfaxdroid.wallpapers.tag

import com.sfaxdroid.domain.entity.WallpaperGroup
import com.sfaxdroid.wallpapers.core.GroupUiModel
import com.sfaxdroid.wallpapers.core.WallpaperUiModel
import com.sfaxdroid.wallpapers.core.mapper.toUiModel
import javax.inject.Inject

internal class TagUiModelMapper @Inject constructor() {

    fun toUiModel(wallpaperGroup: List<WallpaperGroup>, tag: List<Pair<String, String>>) =
        listOf(
            GroupUiModel.PARTNER_TAG_CARROUSEL(tag),
            GroupUiModel.GRID(getWallpapers(wallpaperGroup))
        )

    fun toLoadingUiModel(tag: List<Pair<String, String>>) = listOf(
        GroupUiModel.PARTNER_TAG_CARROUSEL(tag),
        GroupUiModel.LOADING_GRID
    )

    private fun getWallpapers(wallpaperGroup: List<WallpaperGroup>): List<WallpaperUiModel> {
        return wallpaperGroup.asSequence()
            .flatMap { group ->
                group.wallpapers
                    .asSequence()
                    .map { it.toUiModel() }
            }
            .toList()
    }
}