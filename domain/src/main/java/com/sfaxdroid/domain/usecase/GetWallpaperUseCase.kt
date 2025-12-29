package com.sfaxdroid.domain.usecase

import com.sfaxdroid.domain.repository.SfaxDroidServerRepository
import javax.inject.Inject

class GetWallpaperUseCase @Inject constructor(
    private val sfaxDroidServerRepository: SfaxDroidServerRepository
) {
    suspend fun execute() = runCatching {
        sfaxDroidServerRepository.getWallpapers()
    }
}