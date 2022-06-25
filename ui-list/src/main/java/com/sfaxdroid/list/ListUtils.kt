package com.sfaxdroid.list

import com.sfaxdroid.data.mappers.BaseWallpaperView
import com.sfaxdroid.data.mappers.ItemWrapperList

object ListUtils {

    fun getWrappedListWithType(
        wallpaperList: List<BaseWallpaperView>,
        screenType: ScreenType
    ): MutableList<ItemWrapperList> {
        val mixedListItem: MutableList<ItemWrapperList> = arrayListOf()
        wallpaperList.forEach {
            mixedListItem.add(
                ItemWrapperList(
                    it,
                    getItemType(screenType)
                )
            )
        }
        return mixedListItem
    }

    private fun getItemType(screenType: ScreenType): Int {
        return when (screenType) {
            is ScreenType.Cat -> TYPE_CAT
            is ScreenType.Lab -> TYPE_LAB
            is ScreenType.Lwp -> TYPE_LWP
            else -> TYPE_WALLPAPER
        }
    }


    const val TYPE_WALLPAPER = 1
    const val TYPE_CAROUSEL = 2
    const val TYPE_SQUARE_WALLPAPER = 3
    const val TYPE_ADS = 4
    const val TYPE_LWP = 5
    const val TYPE_CAT = 6
    const val TYPE_LAB = 7

}