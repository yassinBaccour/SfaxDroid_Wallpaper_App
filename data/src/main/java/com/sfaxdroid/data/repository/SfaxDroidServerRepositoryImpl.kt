package com.sfaxdroid.data.repository

import com.sfaxdroid.data.call.ApiRequester
import com.sfaxdroid.data.mapper.WallpaperGroupDtoMapper
import com.sfaxdroid.data.service.SfaxDroidServerApiService
import com.sfaxdroid.domain.repository.SfaxDroidServerRepository
import javax.inject.Inject

internal class SfaxDroidServerRepositoryImpl @Inject constructor(
    private val apiRequester: ApiRequester,
    private val apiService: SfaxDroidServerApiService,
    private val productDtoMapper: WallpaperGroupDtoMapper,
) : SfaxDroidServerRepository {

    override suspend fun getWallpapers() = apiRequester.request(
        call = { apiService.getWallpapers() },
        transform = { productDtoMapper.map(it) }
    )

}
