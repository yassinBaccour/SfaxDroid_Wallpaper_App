package com.sfaxdroid.data.service

import com.sfaxdroid.data.dto.SfaxDroidWallpaperResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface SfaxDroidServerApiService {

  @GET("/wallpapers/json/{fileName}")
  suspend fun getWallpapers(@Path("fileName") fileName: String): SfaxDroidWallpaperResponseDto
}
