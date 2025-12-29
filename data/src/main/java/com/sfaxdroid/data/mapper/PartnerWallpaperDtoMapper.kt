package com.sfaxdroid.data.mapper

import com.sfaxdroid.data.dto.PartnerWallpaperResponseDto
import com.sfaxdroid.data.dto.SfaxDroidWallpaperResponseDto
import com.sfaxdroid.domain.entity.Wallpaper
import javax.inject.Inject

class PartnerWallpaperDtoMapper @Inject constructor() {

    fun map(from: PartnerWallpaperResponseDto) = from.hits.map {
        Wallpaper(label = "", previewUrl = it.previewURL, detailUrl = it.largeImageURL, tag = it.tags.split(",")
            .map { tag -> tag.trim() })
    }

}