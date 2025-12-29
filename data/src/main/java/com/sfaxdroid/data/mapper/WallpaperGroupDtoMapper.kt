package com.sfaxdroid.data.mapper

import com.sfaxdroid.data.dto.SfaxDroidWallpaperResponseDto
import com.sfaxdroid.domain.entity.WallpaperGroup
import javax.inject.Inject

internal class WallpaperGroupDtoMapper @Inject constructor(
    private val wallpaperDtoMapper: WallpaperDtoMapper
) {
    fun map(from: SfaxDroidWallpaperResponseDto) = from.categories.map {
        WallpaperGroup(it.title, wallpaperDtoMapper.map(it))
    }
}

