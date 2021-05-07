package com.sfaxdroid.data.repositories

interface WsRepository {

    suspend fun getLiveWallpapers(
        file: String
    ): Response

    suspend fun getAllWallpaper(
        file: String
    ): Response

    suspend fun getLab(
        file: String
    ): Response

    suspend fun getCategory(
        file: String
    ): Response

    suspend fun getCategoryWallpaper(
        file: String
    ): Response

    suspend fun getTags(
        file: String
    ): Response
}
