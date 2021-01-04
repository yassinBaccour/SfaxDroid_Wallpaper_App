package com.sfaxdroid.data.repositories

import com.sfaxdroid.data.entity.WallpaperResponse
import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WsService
@Inject constructor(retrofit: Retrofit) : ApiClient {

    private val articleApi by lazy { retrofit.create(ApiClient::class.java) }

    override fun getLiveWallpapers(guid: String): Call<WallpaperResponse> {
        return articleApi.getLiveWallpapers(guid)
    }

    override fun getAllWallpapers(guid: String): Call<WallpaperResponse> {
        return articleApi.getAllWallpapers(guid)
    }

    override fun getLab(guid: String): Call<WallpaperResponse> {
        return articleApi.getLab(guid)
    }

    override fun getCategory(guid: String): Call<WallpaperResponse> {
        return articleApi.getCategory(guid)
    }

    override fun getCategoryWallpaper(guid: String): Call<WallpaperResponse> {
        return articleApi.getCategoryWallpaper(guid)
    }
}