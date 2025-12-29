package com.sfaxdroid.data.service

import com.sfaxdroid.data.dto.PartnerWallpaperResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PartnerApiService {

    @GET("/api/")
    suspend fun getWallpapers(
        @Query("key") apiKey: String,
        @Query("q") searchTerm: String,
        @Query("image_type") imageType: String,
        @Query("per_page") perPage: String,
        @Query("category") category: String,
        @Query("safesearch") safeSearch: String,
        @Query("page") page: String,
        @Query("orientation") orientation: String,
    ): PartnerWallpaperResponseDto
}
