package com.sfaxdroid.list.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.sfaxdroid.base.Constants
import com.sfaxdroid.data.mappers.BaseWallpaperView
import com.sfaxdroid.data.mappers.ItemWrapperList
import com.sfaxdroid.bases.ScreenType

open class BaseViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    protected var fileName = savedStateHandle.get<String>(Constants.EXTRA_JSON_FILE_NAME).orEmpty()
    protected var screenType = savedStateHandle.get<String>(Constants.EXTRA_SCREEN_TYPE).orEmpty()
    protected var screenName = savedStateHandle.get<String>(Constants.EXTRA_SCREEN_NAME).orEmpty()

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
            is ScreenType.Cat -> Constants.TYPE_CAT
            is ScreenType.Lwp -> Constants.TYPE_LWP
            else -> Constants.TYPE_WALLPAPER
        }
    }

}