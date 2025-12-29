package com.sfaxdroid.domain.repository

import com.sfaxdroid.domain.entity.WallpaperGroup

interface SfaxDroidServerRepository {
    suspend fun getWallpapers(): List<WallpaperGroup>
}