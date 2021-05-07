package com.sfaxdroid.data.repositories

import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WsService
@Inject constructor(retrofit: Retrofit) : ApiClient {

    private val articleApi by lazy { retrofit.create(ApiClient::class.java) }

    override fun getLiveWallpapers(guid: String) = articleApi.getLiveWallpapers(guid)

    override fun getAllWallpapers(guid: String) = articleApi.getAllWallpapers(guid)

    override fun getLab(guid: String) = articleApi.getLab(guid)

    override fun getCategory(guid: String) = articleApi.getCategory(guid)

    override fun getCategoryWallpaper(guid: String) = articleApi.getCategoryWallpaper(guid)

    override fun getTags(guid: String) = articleApi.getTags(guid)
}
