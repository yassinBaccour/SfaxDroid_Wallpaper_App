package com.yassin.wallpaper.feature.home

import androidx.hilt.Assisted
import androidx.lifecycle.*
import com.yassin.wallpaper.feature.ScreenType
import com.yassin.wallpaper.feature.home.adapter.WallpapersListAdapter
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.DeviceManager
import com.sfaxdroid.base.FileManager
import com.sfaxdroid.data.entity.TagResponse
import com.sfaxdroid.data.entity.WallpaperResponse
import com.sfaxdroid.data.mappers.*
import com.sfaxdroid.data.repositories.Response
import com.sfaxdroid.domain.GetAllWallpapersUseCase
import com.sfaxdroid.domain.GetCatWallpapersUseCase
import com.sfaxdroid.domain.GetCategoryUseCase
import com.sfaxdroid.domain.GetLiveWallpapersUseCase
import com.sfaxdroid.domain.GetTagUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    var getAllWallpapersUseCase: GetAllWallpapersUseCase,
    var getLiveWallpapersUseCase: GetLiveWallpapersUseCase,
    var getCategoryUseCase: GetCategoryUseCase,
    var getCatWallpapersUseCase: GetCatWallpapersUseCase,
    var getTagUseCase: GetTagUseCase,
    var fileManager: FileManager,
    var deviceManager: DeviceManager
) :
    ViewModel() {

    var screen = savedStateHandle.get<String>(Constants.EXTRA_SCREEN_TYPE).orEmpty()
    var fileName = savedStateHandle.get<String>(Constants.EXTRA_JSON_FILE_NAME).orEmpty()

    var wallpaperListLiveData: MutableLiveData<List<ItemWrapperList<Any>>> =
        MutableLiveData()

    var tagListLiveData: MutableLiveData<List<TagView>> =
        MutableLiveData()

    var tagVisibility: MutableLiveData<Boolean> =
        MutableLiveData()

    init {
        loadWallpaperByScreenType()
    }

    private fun getType(type: String): ScreenType {
        return when (type) {
            "LWP" -> ScreenType.Lwp
            "CAT" -> ScreenType.Cat
            "LAB" -> ScreenType.Lab
            "TEXTURE" -> ScreenType.TEXTURE
            "TIMER" -> ScreenType.TIMER
            "CAT_WALL" -> ScreenType.CatWallpaper
            else -> ScreenType.Wall
        }
    }

    private fun getTag(screenType: ScreenType) {
        viewModelScope.launch {
            when (val data = getTagUseCase(GetTagUseCase.Param(getTagFileNameByType(screenType)))) {
                is Response.SUCESS -> {
                    val rep = data.response as TagResponse
                    tagListLiveData.value = rep.tagList.map { wall ->
                        TagToTagViewMap().map(wall, deviceManager.isSmallScreen())
                    }
                }
                is Response.FAILURE -> {
                    tagVisibility.value = false
                }
            }

        }
    }

    private fun getTagFileNameByType(screenType: ScreenType): String {
        return when (screenType) {
            ScreenType.TEXTURE -> "texture_tags.json"
            else -> "wallpaper_tags.json"
        }
    }

    private fun loadWallpaperByScreenType() {
        when (val screenType = getType(screen)) {
            is ScreenType.Lwp -> {
                tagVisibility.value = false
                getLiveWallpapers(screenType)
            }
            is ScreenType.Wall -> {
                tagVisibility.value = true
                getTag(screenType)
                getWallpapers(screenType, fileName)
            }
            is ScreenType.Cat -> {
                tagVisibility.value = false
                getCategory(screenType)
            }
            ScreenType.TEXTURE -> {
                tagVisibility.value = true
                getTag(screenType)
                getWallpapers(screenType, fileName)
            }
            ScreenType.TIMER -> {
                tagVisibility.value = false
                getSavedWallpaperList(screenType)
            }
            ScreenType.CatWallpaper -> {
                tagVisibility.value = false
                getCatWallpapers(screenType, fileName)
            }
        }
    }

    private fun getLiveWallpapers(screenType: ScreenType) {
        viewModelScope.launch {
            when (val data =
                getLiveWallpapersUseCase(GetLiveWallpapersUseCase.Param(byLanguage(fileName)))) {
                is Response.SUCESS -> {
                    wrapWallpapers(
                        wallpaperList = (data.response as WallpaperResponse).wallpaperList.wallpapers.map { wall ->
                            WallpaperToLwpMapper().map(wall, deviceManager.isSmallScreen())
                        },
                        screenType = screenType
                    )
                }
                is Response.FAILURE -> {
                }
            }

        }
    }

    private fun byLanguage(fileName: String): String {
        return when (Locale.getDefault().language) {
            "ar" -> fileName.replace("lwp", "lwp_ar")
            "fr" -> fileName.replace("lwp", "lwp_fr")
            else -> fileName
        }
    }

    private fun getCategory(screenType: ScreenType) {
        viewModelScope.launch {
            when (val data = getCategoryUseCase(GetCategoryUseCase.Param(fileName))) {
                is Response.SUCESS -> {
                    wrapWallpapers(
                        wallpaperList = (data.response as WallpaperResponse).wallpaperList.wallpapers.map { wall ->
                            WallpaperToCategoryMapper().map(wall, deviceManager.isSmallScreen())
                        },
                        screenType = screenType
                    )
                }
                is Response.FAILURE -> {
                }
            }
        }
    }

    private fun getCatWallpapers(screenType: ScreenType, fileName: String) {
        viewModelScope.launch {
            when (val data = getCatWallpapersUseCase(GetCatWallpapersUseCase.Param(fileName))) {
                is Response.SUCESS -> {
                    val wallpaperList =
                        (data.response as WallpaperResponse).wallpaperList.wallpapers.map { wall ->
                            WallpaperToViewMapper().map(
                                wall,
                                deviceManager.isSmallScreen()
                            )
                        }
                    wrapWallpapers(wallpaperList, screenType)
                }
                is Response.FAILURE -> {
                }
            }
        }
    }

    fun loadByTag(tagView: TagView) {
        when (tagView.type) {
            TagType.Category -> getCatWallpapers(ScreenType.Wall, tagView.fileName)
            TagType.Texture -> getWallpapers(ScreenType.Wall, tagView.fileName)
            else -> getWallpapers(ScreenType.Wall, tagView.fileName)
        }
    }

    private fun getWallpapers(screenType: ScreenType, fileName: String) {
        viewModelScope.launch {
            when (val data = getAllWallpapersUseCase(GetAllWallpapersUseCase.Param(fileName))) {
                is Response.SUCESS -> {
                    wrapWallpapers((data.response as WallpaperResponse).wallpaperList.wallpapers.map { wall ->
                        WallpaperToViewMapper().map(
                            wall,
                            deviceManager.isSmallScreen()
                        )
                    }, screenType)
                }
                is Response.FAILURE -> {
                }
            }
        }
    }

    private fun getSavedWallpaperList(screenType: ScreenType) {
        val list = fileManager.getPermanentDirListFiles().map {
            SimpleWallpaperView(it.path.orEmpty())
        }
        wrapWallpapers(list, screenType)
    }

    private fun getItemType(screenType: ScreenType): Int {
        return when (screenType) {
            is ScreenType.Wall -> WallpapersListAdapter.TYPE_SQUARE_WALLPAPER
            is ScreenType.CatWallpaper -> WallpapersListAdapter.TYPE_SQUARE_WALLPAPER
            is ScreenType.TEXTURE -> WallpapersListAdapter.TYPE_SQUARE_WALLPAPER
            is ScreenType.Cat -> WallpapersListAdapter.TYPE_CAT
            is ScreenType.Lab -> WallpapersListAdapter.TYPE_LAB
            else -> WallpapersListAdapter.TYPE_LWP
        }
    }

    private fun wrapWallpapers(
        wallpaperList: List<BaseWallpaperView>,
        screenType: ScreenType
    ) {
        wallpaperListLiveData.value = getWrappedListWithType(wallpaperList, screenType)
    }

    private fun getWrappedListWithType(
        wallpaperList: List<BaseWallpaperView>,
        screenType: ScreenType
    ): MutableList<ItemWrapperList<Any>> {
        val mixedListItem: MutableList<ItemWrapperList<Any>> = arrayListOf()
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


    fun getMixed() {
        /*
        if (isMixed) {
            val lwpList: List<LwpItem> = arrayListOf()
            val lwp = CarouselView("Live Wallpaper 4K", lwpList, CarouselTypeEnum.LWP)
            listItem.add(6, ItemWrapperList(lwp, WallpapersListAdapter.TYPE_CAROUSEL))
            //todo fix

            val wallpaperObjects =
                ViewModel.Current.getWallpaperCategoryFromName("ImageCategoryNew")
                    .getGetWallpapersList()

            val cat = CarouselView("All Category", wallpaperObjects, CarouselTypeEnum.CATEGORY)
            listItem.add(13, ItemWrapperList(cat, ArticleListAdapter.TYPE_CAROUSEL))
        }
        */


    }

}
