package com.sfaxdroid.data.repositories

import com.sfaxdroid.data.entity.Logger
import javax.inject.Inject

class WallpaperRepositoryImpl @Inject constructor(
    private val service: WsService,
    private val logger: Logger
) : WallpaperRepository {

    override suspend fun getLiveWallpapers(file: String) = service.getLiveWallpapers(file).executeWs().toResult(logger)

    override suspend fun getAllWallpaper(file: String) = service.getAllWallpapers(file).executeWs().toResult(logger)

    override suspend fun getLab(file: String) = service.getLab(file).executeWs().toResult(logger)

    override suspend fun getCategory(file: String) = service.getCategory(file).executeWs().toResult(logger)

    override suspend fun getCategoryWallpaper(file: String) = service.getCategoryWallpaper(file).executeWs().toResult(logger)

    override suspend fun getTags(file: String) = service.getTags(file).executeWs().toResult(logger)
}
