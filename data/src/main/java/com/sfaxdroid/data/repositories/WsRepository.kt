package com.sfaxdroid.data.repositories

import com.sfaxdroid.data.entity.Response
import com.sfaxdroid.data.entity.TagResponse
import com.sfaxdroid.data.entity.WallpaperResponse

interface WsRepository {

    suspend fun getLiveWallpapers(
        file: String
    ): Response<WallpaperResponse>

    suspend fun getAllWallpaper(
        file: String
    ): Response<WallpaperResponse>

    suspend fun getLab(
        file: String
    ): Response<WallpaperResponse>

    suspend fun getCategory(
        file: String
    ): Response<WallpaperResponse>

    suspend fun getCategoryWallpaper(
        file: String
    ): Response<WallpaperResponse>

    suspend fun getTags(
        file: String
    ): Response<TagResponse>
}
