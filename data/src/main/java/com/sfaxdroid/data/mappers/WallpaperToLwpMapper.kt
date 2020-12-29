package com.sfaxdroid.data.mappers

import com.sfaxdroid.data.entity.Wallpaper

class WallpaperToLwpMapper : SfaxDroidMapper<Wallpaper, LwpItem> {

    override fun map(from: Wallpaper?): LwpItem {
        return LwpItem(
            from?.name ?: "",
            from?.desc ?: "",
            from?.type ?: "",
            from?.url ?: ""
        )
    }

}