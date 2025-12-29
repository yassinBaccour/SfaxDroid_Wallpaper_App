package com.sfaxdroid.data.mapper

import com.sfaxdroid.data.dto.PartnerWallpaperResponseDto
import com.sfaxdroid.data.dto.SfaxDroidWallpaperResponseDto
import com.sfaxdroid.domain.entity.Wallpaper
import com.sfaxdroid.domain.entity.WallpaperGroup
import javax.inject.Inject

class PartnerWallpaperDtoMapper @Inject constructor() {

    fun map(from: PartnerWallpaperResponseDto, title: String) = WallpaperGroup(title, from.hits.map {
        Wallpaper(
            label = title,
            previewUrl = it.previewURL,
            detailUrl = it.largeImageURL,
            tag = it.tags.split(",")
                .map { tag -> tag.trim() })
    })

}