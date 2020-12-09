package com.sami.rippel.feature.singleview

import com.sfaxdroid.base.WallpaperObject


class CarouselView(
    var title: String,
    var articleList: List<WallpaperObject> = arrayListOf(),
    var carouselTypeEnum: CarouselTypeEnum
)

sealed class CarouselTypeEnum {
    object LIVEWALLPAPER : CarouselTypeEnum()
    object CATEGORY : CarouselTypeEnum()
}