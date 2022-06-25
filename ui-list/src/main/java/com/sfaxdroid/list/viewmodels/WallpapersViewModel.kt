package com.sfaxdroid.list.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.DeviceManager
import com.sfaxdroid.data.entity.AppName
import com.sfaxdroid.data.entity.Response
import com.sfaxdroid.data.mappers.TagToTagViewMap
import com.sfaxdroid.data.mappers.TagView
import com.sfaxdroid.data.mappers.WallpaperToViewMapper
import com.sfaxdroid.domain.GetCatWallpapersUseCase
import com.sfaxdroid.domain.GetTagUseCase
import com.sfaxdroid.list.ListUtils.getWrappedListWithType
import com.sfaxdroid.list.ScreenType
import com.sfaxdroid.list.WallpaperListState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class WallpapersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getTagUseCase: GetTagUseCase,
    var getCatWallpapersUseCase: GetCatWallpapersUseCase,
    var deviceManager: DeviceManager,
    @Named("appLanguage") var appLanguage: String,
    @Named("app-name") var appName: AppName
) : ViewModel() {

    private var fileName = savedStateHandle.get<String>(Constants.EXTRA_JSON_FILE_NAME).orEmpty()
    private var screenType = savedStateHandle.get<String>(Constants.EXTRA_SCREEN_TYPE).orEmpty()
    private var selectedLwpName = savedStateHandle.get<String>(Constants.KEY_LWP_NAME).orEmpty()
    private var screenName = savedStateHandle.get<String>(Constants.EXTRA_SCREEN_NAME).orEmpty()
    private val selectedItem = MutableStateFlow(0)
    private val selectedItemFlow: Flow<Int>
        get() = selectedItem

    val state = combine(
        getCatWallpapersUseCase.flow,
        getTagUseCase.flow,
        selectedItemFlow
    ) { wallpaper, tags, selected ->
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

        WallpaperListState(
            itemsList = list,
            tagList = tagsList,
            isRefresh = false,
            isTagVisible = appName == AppName.AccountOne,
            tagSelectedPosition = selected
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WallpaperListState(
            isRefresh = false, toolBarTitle = getScreenTitle(),
            isToolBarVisible = getToolBarVisibility(),
            isPrivacyButtonVisible = getPrivacyButtonVisibility(),
            setDisplayHomeAsUpEnabled = isWithToolBar()
        )
    )

    init {
        getCatWallpapersUseCase(GetCatWallpapersUseCase.Param(fileName))
        getTagUseCase(GetTagUseCase.Param(getTagFileNameByType(getType(screenType))))
    }

    fun getWallpaperByTag(tagView: TagView) {
        getCatWallpapersUseCase(GetCatWallpapersUseCase.Param(tagView.fileName))
    }

    fun updateSelectedPosition(pos: Int) {
        viewModelScope.launch {
            selectedItem.value = pos
        }
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

    private fun getToolBarVisibility() =
        if (isWithToolBar()) true else appName != AppName.AccountOne

    private fun isWithToolBar() = selectedLwpName.isNotEmpty() || screenType == "CAT_WALL"

    private fun getScreenTitle(): String {
        return when (selectedLwpName) {
            Constants.KEY_WORD_IMG_LWP -> screenName
            Constants.KEY_ANIM_2D_LWP -> screenName
            else -> when (screenType) {
                "CAT_WALL" -> screenName
                else -> ""
            }
        }
    }

    private fun getPrivacyButtonVisibility() = !isWithToolBar() && appName == AppName.AccountTwo

}