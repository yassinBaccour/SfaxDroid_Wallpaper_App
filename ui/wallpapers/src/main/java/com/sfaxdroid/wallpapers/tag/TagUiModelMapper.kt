package com.sfaxdroid.wallpapers.tag

import com.sfaxdroid.domain.entity.WallpaperGroup
import com.sfaxdroid.wallpapers.core.GroupUiModel
import com.sfaxdroid.wallpapers.core.WallpaperUiModel
import com.sfaxdroid.wallpapers.core.mapper.toUiModel
import javax.inject.Inject

internal class TagUiModelMapper @Inject constructor() {

    fun map(wallpaperGroup: List<WallpaperGroup>) =
        listOf(
            GroupUiModel.PARTNER_TAG_CARROUSEL(wallpaperGroup
                .flatMap { it.wallpapers }
                .flatMap { it.tag }
                .map { it to "" }
                .distinctBy { it.first }
            ),
            GroupUiModel.GRID(getWallpapers(wallpaperGroup)))

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