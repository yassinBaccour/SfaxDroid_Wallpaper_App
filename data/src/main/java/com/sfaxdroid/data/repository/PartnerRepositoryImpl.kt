package com.sfaxdroid.data.repository

import com.sfaxdroid.data.call.ApiRequester
import com.sfaxdroid.data.mapper.PartnerWallpaperDtoMapper
import com.sfaxdroid.data.service.PartnerApiService
import com.sfaxdroid.domain.config.AppConfig
import com.sfaxdroid.domain.repository.PartnerServerRepository
import javax.inject.Inject

internal class PartnerRepositoryImpl @Inject constructor(
    private val apiRequester: ApiRequester,
    private val partnerApiService: PartnerApiService,
    private val mapper: PartnerWallpaperDtoMapper,
    private val appConfig: AppConfig
) : PartnerServerRepository {

    override suspend fun getWallpapers(searchTerm: String) =
        apiRequester.request(
            call = {
                partnerApiService.getWallpapers(
                    searchTerm = searchTerm,
                    apiKey = appConfig.partnerApiKey,
                    imageType = IMAGE_TYPE,
                    perPage = PHOTO_PER_PAGE,
                    safeSearch = SAFE_SEARCH,
                    page = PAGE,
                    category = CATEGORY,
                    orientation = ORIENTATION
                )
            },
            transform = { mapper.map(it, searchTerm) }
        )

    companion object {
        const val PAGE = "1"
        const val IMAGE_TYPE = "photo"
        const val SAFE_SEARCH = "true"
        const val PHOTO_PER_PAGE = "100"
        const val ORIENTATION = "vertical"
        const val CATEGORY = "backgrounds"
    }
}
