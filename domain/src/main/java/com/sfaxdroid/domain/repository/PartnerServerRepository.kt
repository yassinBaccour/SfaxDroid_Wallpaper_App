package com.sfaxdroid.domain.repository

import com.sfaxdroid.domain.entity.Wallpaper

interface PartnerServerRepository {
  suspend fun getWallpapers(searchTerm: String): List<Wallpaper>
}
