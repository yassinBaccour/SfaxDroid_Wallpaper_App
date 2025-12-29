package com.sfaxdroid.domain.usecase

import com.sfaxdroid.domain.entity.WallpaperGroup
import com.sfaxdroid.domain.repository.PartnerServerRepository
import com.sfaxdroid.domain.repository.SfaxDroidServerRepository
import javax.inject.Inject

class GetTagWallpaperUseCase @Inject constructor(
    private val sfaxDroidServerRepository: SfaxDroidServerRepository,
    private val partnerServerRepository: PartnerServerRepository
) {
    suspend fun execute(tag: String, partnerSource: Boolean) = runCatching {
        val group = mutableListOf<WallpaperGroup>()
        sfaxDroidServerRepository.getWallpapers().firstOrNull { it.title == tag }?.also {
            group.add(it)
        }
        if (partnerSource) {
            group.add(partnerServerRepository.getWallpapers(tag))
        }
        group
    }
}