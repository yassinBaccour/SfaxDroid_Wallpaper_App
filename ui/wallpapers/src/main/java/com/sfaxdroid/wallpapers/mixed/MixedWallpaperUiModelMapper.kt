package com.sfaxdroid.wallpapers.mixed

import com.sfaxdroid.domain.entity.WallpaperGroup
import com.sfaxdroid.domain.entity.WallpaperTheme
import com.sfaxdroid.wallpapers.core.GroupUiModel
import com.sfaxdroid.wallpapers.core.WallpaperUiModel
import com.sfaxdroid.wallpapers.core.mapper.toUiModel
import javax.inject.Inject
import kotlin.collections.shuffled

internal class MixedWallpaperUiModelMapper @Inject constructor() {

    fun map(wallpaperGroup: List<WallpaperGroup>) =
        mutableListOf(
            GroupUiModel.HOME_HEADER,
            GroupUiModel.CARROUSEL(
                wallpaperGroup.first { it.theme == WallpaperTheme.NEW }.wallpapers.shuffled().take(10)
                    .map { it.toUiModel() }, "New"
            ),
            GroupUiModel.TAG(wallpaperGroup.map { Pair(it.title, "Arabic") }.distinct()),
            GroupUiModel.OF_THE_DAY(getMixedWallpaper(wallpaperGroup).take(9), ""),
            GroupUiModel.PARTNER_TAG(getWallpaperTagCategoryPairs()),
            GroupUiModel.RANDOM_GRID(getMixedWallpaper(wallpaperGroup), "")
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

    private fun getWallpaperTagCategoryPairs(): List<Pair<String, String>> = listOf(
        "Nature" to "nature",
        "Mountains" to "nature",
        "Forest" to "nature",
        "Ocean" to "nature",
        "Desert" to "nature",
        "Sunset" to "nature",

        "Space" to "science",
        "Galaxy" to "science",
        "Abstract" to "backgrounds",
        "Minimal" to "backgrounds",
        "Dark" to "backgrounds",
        "Gradient" to "backgrounds",

        "City" to "places",
        "Skyscrapers" to "buildings",
        "Architecture" to "buildings",
        "Urban" to "places",

        "Cars" to "transportation",
        "Motorcycles" to "transportation",
        "Supercars" to "transportation",

        "Animals" to "animals",
        "Wildlife" to "animals",
        "Birds" to "animals",

        "Technology" to "computer",
        "Cyberpunk" to "computer",
        "Futuristic" to "computer",

        "Gaming" to "computer",
        "Anime" to "people",
        "Fantasy" to "backgrounds",
        "Art" to "backgrounds"
    )

    companion object {
        private const val MIN_WALLPAPER_PER_GROUP = 10
    }
}