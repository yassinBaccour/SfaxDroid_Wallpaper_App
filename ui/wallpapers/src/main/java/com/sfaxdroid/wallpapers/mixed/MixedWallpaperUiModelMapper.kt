package com.sfaxdroid.wallpapers.mixed

import com.sfaxdroid.domain.entity.WallpaperGroup
import com.sfaxdroid.wallpapers.core.GroupUiModel
import com.sfaxdroid.wallpapers.core.WallpaperUiModel
import com.sfaxdroid.wallpapers.core.mapper.toUiModel
import javax.inject.Inject
import kotlin.collections.shuffled

internal class MixedWallpaperUiModelMapper @Inject constructor() {

    fun map(wallpaperGroup: List<WallpaperGroup>) =
        mutableListOf(
            GroupUiModel.HOME_HEADER,
            GroupUiModel.CARROUSEL(wallpaperGroup.first { it.title == "New" }.wallpapers.shuffled().take(10).map { it.toUiModel() }),
            GroupUiModel.TAG(wallpaperGroup.map { it.title }.distinct()),
            GroupUiModel.OF_THE_DAY(getMixedWallpaper(wallpaperGroup).take(9)),
            GroupUiModel.RANDOM_GRID(getMixedWallpaper(wallpaperGroup))
        )

    private fun getMixedWallpaper(wallpaperGroup: List<WallpaperGroup>): List<WallpaperUiModel> {
        return wallpaperGroup.asSequence()
            .flatMap { group ->
                group.wallpapers
                    .shuffled()
                    .take(MIN_WALLPAPER_PER_GROUP)
                    .asSequence()
                    .map { it.toUiModel() }
            }
            .toList()
            .shuffled()
    }

    companion object {
        private const val MIN_WALLPAPER_PER_GROUP = 10
    }
}