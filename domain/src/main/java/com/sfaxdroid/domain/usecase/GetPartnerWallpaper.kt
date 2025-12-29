package com.sfaxdroid.domain.usecase

import com.sfaxdroid.domain.repository.PartnerServerRepository
import javax.inject.Inject

class GetPartnerWallpaper @Inject constructor(
    private val partnerServerRepository: PartnerServerRepository
) {
    suspend fun execute(searchTerm: String) = runCatching {
        partnerServerRepository.getWallpapers(searchTerm)
    }
}