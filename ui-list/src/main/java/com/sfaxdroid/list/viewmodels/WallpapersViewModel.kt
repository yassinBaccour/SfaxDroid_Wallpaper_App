package com.sfaxdroid.list.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.DeviceManager
import com.sfaxdroid.data.entity.Response
import com.sfaxdroid.data.mappers.TagToTagViewMap
import com.sfaxdroid.data.mappers.WallpaperToViewMapper
import com.sfaxdroid.domain.GetAllWallpapersUseCase
import com.sfaxdroid.domain.GetCategoryUseCase
import com.sfaxdroid.domain.GetTagUseCase
import com.sfaxdroid.list.ListUtils.getWrappedListWithType
import com.sfaxdroid.list.ScreenType
import com.sfaxdroid.list.WallpaperListState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class WallpapersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getAllWallpapersUseCase: GetAllWallpapersUseCase,
    getTagUseCase: GetTagUseCase,
    var deviceManager: DeviceManager,
    @Named("appLanguage") var appLanguage: String
) : ViewModel() {

    private var fileName = savedStateHandle.get<String>(Constants.EXTRA_JSON_FILE_NAME).orEmpty()
    private var screenType = savedStateHandle.get<String>(Constants.EXTRA_SCREEN_TYPE).orEmpty()

    val state = getAllWallpapersUseCase.flow.combine(getTagUseCase.flow) { wallpaper, tags ->
        val list = when (wallpaper) {
            is Response.SUCCESS -> {
                getWrappedListWithType(
                    wallpaper.response.wallpaperList.wallpapers.map { wall ->
                        WallpaperToViewMapper().map(
                            wall,
                            deviceManager.isSmallScreen()
                        )
                    }.shuffled(),
                    ScreenType.Wall
                )
            }
            is Response.FAILURE -> arrayListOf()
        }

        val tagsList = when (tags) {
            is Response.SUCCESS -> tags.response.tagList.map { wall ->
                TagToTagViewMap().map(wall, deviceManager.isSmallScreen())
            }
            is Response.FAILURE -> arrayListOf()
        }

        WallpaperListState(itemsList = list, tagList = tagsList, isRefresh = false)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WallpaperListState(isRefresh = true),
    )

    init {
        getAllWallpapersUseCase(GetAllWallpapersUseCase.Param(fileName))
        getTagUseCase(GetTagUseCase.Param(getTagFileNameByType(getType(screenType))))
    }

    private fun getTagFileNameByType(screenType: ScreenType): String {
        return when (screenType) {
            ScreenType.TEXTURE -> tagByLanguage()
            else -> tagByLanguage()
        }
    }

    private fun tagByLanguage(): String {
        return if (appLanguage == "ar") {
            "wallpaper_tags_ar.json"
        } else "wallpaper_tags.json"
    }

    private fun getType(type: String): ScreenType {
        return when (type) {
            "LWP" -> ScreenType.Lwp
            "CAT" -> ScreenType.Cat
            "LAB" -> ScreenType.Lab
            "TEXTURE" -> ScreenType.TEXTURE
            "TIMER" -> ScreenType.TIMER
            "CAT_WALL" -> ScreenType.CatWallpaper
            "MIXED" -> ScreenType.MIXED
            else -> ScreenType.Wall
        }
    }
}