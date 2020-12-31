package com.sami.rippel.feature.home

import com.sfaxdroid.data.mappers.BaseWallpaperView


class CarouselView(
    var title: String,
    var articleList: List<BaseWallpaperView> = arrayListOf(),
    var carouselTypeEnum: CarouselTypeEnum
)

sealed class CarouselTypeEnum {
    object LWP : CarouselTypeEnum()
    object CATEGORY : CarouselTypeEnum()
}