package com.sfaxdroid.data.repositories

import com.sfaxdroid.data.entity.WallpaperResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

internal interface ApiClient {

    @GET("{file}")
    fun getLiveWallpapers(
        @Path("file") guid: String
    ): Call<WallpaperResponse>

    @GET("{file}")
    fun getAllWallpapers(
        @Path("file") guid: String
    ): Call<WallpaperResponse>

    @GET("{file}")
    fun getLab(
        @Path("file") guid: String
    ): Call<WallpaperResponse>

    @GET("{file}")
    fun getCategory(
        @Path("file") guid: String
    ): Call<WallpaperResponse>
}