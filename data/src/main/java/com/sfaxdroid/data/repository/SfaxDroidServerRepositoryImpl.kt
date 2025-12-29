package com.sfaxdroid.data.repository

import com.sfaxdroid.data.call.ApiRequester
import com.sfaxdroid.data.mapper.WallpaperGroupDtoMapper
import com.sfaxdroid.data.service.SfaxDroidServerApiService
import com.sfaxdroid.domain.config.AppConfig
import com.sfaxdroid.domain.repository.SfaxDroidServerRepository
import javax.inject.Inject

internal class SfaxDroidServerRepositoryImpl @Inject constructor(
    private val apiRequester: ApiRequester,
    private val apiService: SfaxDroidServerApiService,
    private val productDtoMapper: WallpaperGroupDtoMapper,
    private val appConfig: AppConfig
) : SfaxDroidServerRepository {

    override suspend fun getWallpapers() = apiRequester.request(
        call = { apiService.getWallpapers(getFileName()) },
        transform = { productDtoMapper.map(it) }
    )

    private fun getFileName() = WALLPAPER_SOURCE + appConfig.jsonVersion + WALLPAPER_EXT

    companion object {
        const val WALLPAPER_SOURCE = "wallpapers_v"
        const val WALLPAPER_EXT = ".json"
    }

}
