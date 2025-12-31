package com.sfaxdroid.data.dto

import androidx.annotation.Keep

@Keep
class PartnerWallpaperResponseDto(
    val total: Int,
    val totalHits: Int,
    val hits: List<PartnerWallpaperDto>
)