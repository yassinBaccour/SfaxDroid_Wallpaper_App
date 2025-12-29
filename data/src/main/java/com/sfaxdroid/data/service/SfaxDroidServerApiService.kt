package com.sfaxdroid.data.service

import com.sfaxdroid.data.dto.SfaxDroidWallpaperResponseDto
import retrofit2.http.GET

interface SfaxDroidServerApiService {

  @GET("/wallpapers/json/wallpapers.json")
  suspend fun getWallpapers(): SfaxDroidWallpaperResponseDto
}
