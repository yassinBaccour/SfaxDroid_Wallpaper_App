package com.sfaxdroid.domain.usecase

import com.sfaxdroid.domain.entity.Wallpaper
import com.sfaxdroid.domain.entity.WallpaperGroup
import com.sfaxdroid.domain.entity.WallpaperTheme
import com.sfaxdroid.domain.repository.PartnerServerRepository
import com.sfaxdroid.domain.repository.SfaxDroidServerRepository
import javax.inject.Inject

class GetTagWallpaperUseCase @Inject constructor(
    private val sfaxDroidServerRepository: SfaxDroidServerRepository,
    private val partnerServerRepository: PartnerServerRepository
) {
    suspend fun execute(tag: Pair<String, String>, partnerSource: Boolean) = runCatching {
        val group = mutableListOf<WallpaperGroup>()
        if (tag.first.isEmpty()) {
            val mixed = getMixedWallpaper(sfaxDroidServerRepository.getWallpapers())
            group.add(WallpaperGroup(title = "", wallpapers = mixed, WallpaperTheme.MIXED))
        } else {
            sfaxDroidServerRepository.getWallpapers().firstOrNull { it.title == tag.first }?.also {
                group.add(it)
            }
        }
        if (partnerSource) {
            group.add(partnerServerRepository.getWallpapers(tag.first, tag.second))
        }
        group
    }

    private fun getMixedWallpaper(wallpaperGroup: List<WallpaperGroup>): List<Wallpaper> =
        wallpaperGroup.asSequence()
            .flatMap { group ->
                group.wallpapers
                    .shuffled()
                    .take(10)
                    .asSequence()
                    .map { it }
            }
            .toList()
            .shuffled()
            .take(50)
}