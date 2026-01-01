package com.sfaxdroid.data.mapper

import com.sfaxdroid.data.dto.SfaxDroidWallpaperDto
import com.sfaxdroid.domain.config.AppConfig
import com.sfaxdroid.domain.entity.Wallpaper
import javax.inject.Inject

internal class WallpaperDtoMapper @Inject constructor(private val appConfig: AppConfig) {

    fun map(dto: SfaxDroidWallpaperDto): List<Wallpaper> {
        return (1..dto.nbImage).map { index ->

            val (fullUrl, previewUrl) = buildWallpaperUrls(dto, index)

            Wallpaper(
                label = "${dto.title}_$index",
                detailUrl = fullUrl,
                previewUrl = previewUrl,
                tag = listOf(),
                source = SOURCE
            )
        }
    }

    private fun buildWallpaperUrls(dto: SfaxDroidWallpaperDto, index: Int): Pair<String, String> {
        val baseUrl =
            appConfig.sfaxDroidBaseUrl + WALLPAPER_DIR + dto.path + "/" + dto.fileName + index
        val fullUrl = baseUrl + IMAGE_EXTENSION
        val previewUrl = baseUrl + PREVIEW_SUFFIX
        return Pair(fullUrl, previewUrl)
    }

    companion object {
        const val WALLPAPER_DIR = "/wallpapers/images/"
        const val PREVIEW_SUFFIX = "_preview.jpg"
        const val IMAGE_EXTENSION = ".jpg"
        const val SOURCE = "SfaxDroid"
    }
}