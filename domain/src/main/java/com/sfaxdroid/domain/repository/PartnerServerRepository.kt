package com.sfaxdroid.domain.repository

import com.sfaxdroid.domain.entity.WallpaperGroup

interface PartnerServerRepository {
    suspend fun getWallpapers(searchTerm: String): WallpaperGroup
}
