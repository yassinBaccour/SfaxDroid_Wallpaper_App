package com.sfaxdroid.list

import com.sfaxdroid.data.mappers.BaseWallpaperView
import com.sfaxdroid.data.mappers.ItemWrapperList
import com.sfaxdroid.list.adapter.WallpapersListAdapter

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
            is ScreenType.Cat -> WallpapersListAdapter.TYPE_CAT
            is ScreenType.Lab -> WallpapersListAdapter.TYPE_LAB
            is ScreenType.Lwp -> WallpapersListAdapter.TYPE_LWP
            else -> WallpapersListAdapter.TYPE_WALLPAPER
        }
    }

}