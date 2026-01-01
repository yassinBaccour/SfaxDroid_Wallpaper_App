package com.sfaxdroid.data.mapper

import com.sfaxdroid.data.dto.SfaxDroidWallpaperResponseDto
import com.sfaxdroid.domain.entity.WallpaperGroup
import javax.inject.Inject

internal class WallpaperGroupDtoMapper @Inject constructor(
    private val wallpaperDtoMapper: WallpaperDtoMapper,
    private val wallpaperThemeMapper: WallpaperThemeMapper
) {
    fun map(from: SfaxDroidWallpaperResponseDto) = from.categories.map {
        WallpaperGroup(
            title = it.title,
            wallpapers = wallpaperDtoMapper.map(it),
            theme = wallpaperThemeMapper.map(it.path)
        )
    }
}

