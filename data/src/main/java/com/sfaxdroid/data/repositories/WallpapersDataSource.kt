package com.sfaxdroid.data.repositories

import com.sfaxdroid.data.entity.Response
import com.sfaxdroid.data.entity.TagResponse
import com.sfaxdroid.data.entity.WallpaperResponse
import javax.inject.Inject

class WallpapersDataSource @Inject constructor(private val service: WsService) : WsRepository {

    override suspend fun getLiveWallpapers(file: String): Response<WallpaperResponse> {
        return service.getLiveWallpapers(file).executeWs().toResult()
    }

    override suspend fun getAllWallpaper(file: String): Response<WallpaperResponse> {
        return service.getAllWallpapers(file).executeWs().toResult()
    }

    override suspend fun getLab(file: String): Response<WallpaperResponse> {
        return service.getLab(file).executeWs().toResult()
    }

    override suspend fun getCategory(file: String): Response<WallpaperResponse> {
        return service.getCategory(file).executeWs().toResult()
    }

    override suspend fun getCategoryWallpaper(file: String): Response<WallpaperResponse> {
        return service.getCategoryWallpaper(file).executeWs().toResult()
    }

    override suspend fun getTags(file: String): Response<TagResponse> {
        return service.getTags(file).executeWs().toResult()
    }
}
