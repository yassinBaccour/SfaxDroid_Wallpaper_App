package com.sami.rippel.feature.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.sami.rippel.feature.ScreenType
import com.sami.rippel.feature.home.adapter.WallpapersListAdapter
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.DeviceManager
import com.sfaxdroid.base.FileManager
import com.sfaxdroid.data.entity.WallpaperResponse
import com.sfaxdroid.data.mappers.*
import com.sfaxdroid.data.repositories.Response
import com.sfaxdroid.domain.GetAllWallpapersUseCase
import com.sfaxdroid.domain.GetCatWallpapersUseCase
import com.sfaxdroid.domain.GetCategoryUseCase
import com.sfaxdroid.domain.GetLiveWallpapersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    var getAllWallpapersUseCase: GetAllWallpapersUseCase,
    var getLiveWallpapersUseCase: GetLiveWallpapersUseCase,
    var getCategoryUseCase: GetCategoryUseCase,
    var getCatWallpapersUseCase: GetCatWallpapersUseCase,
    var fileManager: FileManager,
    var deviceManager: DeviceManager
) :
    ViewModel() {

    var screen = savedStateHandle.get<String>(Constants.EXTRA_SCREEN_TYPE).orEmpty()
    var fileName = savedStateHandle.get<String>(Constants.EXTRA_JSON_FILE_NAME).orEmpty()

    var wallpaperListLiveData: MutableLiveData<List<ItemWrapperList<Any>>> =
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

    private fun loadWallpaperByScreenType() {
        when (val screenType = getType(screen)) {
            is ScreenType.Lwp -> {
                getLiveWallpapers(screenType)
            }
            is ScreenType.Wall -> {
                getWallpapers(screenType)
            }
            is ScreenType.Cat -> {
                getCategory(screenType)
            }
            ScreenType.TEXTURE -> {
                getWallpapers(screenType)
            }
            ScreenType.TIMER -> {
                getSavedWallpaperList(screenType)
            }
            ScreenType.CatWallpaper -> {
                getCatWallpapers(screenType)
            }
        }
    }

    private fun getLiveWallpapers(screenType: ScreenType) {
        viewModelScope.launch {
            when (val data = getLiveWallpapersUseCase(GetLiveWallpapersUseCase.Param(fileName))) {
                is Response.SUCESS -> {
                    val rep = data.response as WallpaperResponse
                    val list = rep.wallpaperList.wallpapers.map { wall ->
                        WallpaperToLwpMapper().map(wall, deviceManager.isSmallScreen())
                    }
                    wrapWallpapers(list, screenType)
                }
                is Response.FAILURE -> {

                }
            }

        }
    }

    private fun getCategory(screenType: ScreenType) {
        viewModelScope.launch {
            when (val data = getCategoryUseCase(GetCategoryUseCase.Param(fileName))) {
                is Response.SUCESS -> {
                    val rep = data.response as WallpaperResponse
                    val list = rep.wallpaperList.wallpapers.map { wall ->
                        WallpaperToCategoryMapper().map(wall, deviceManager.isSmallScreen())
                    }
                    wrapWallpapers(list, screenType)
                }
                is Response.FAILURE -> {
                }
            }
        }
    }

    private fun getCatWallpapers(screenType: ScreenType) {
        viewModelScope.launch {
            when (val data = getCatWallpapersUseCase(GetCatWallpapersUseCase.Param(fileName))) {
                is Response.SUCESS -> {
                    val rep = data.response as WallpaperResponse
                    val list = rep.wallpaperList.wallpapers.map { wall ->
                        WallpaperToViewMapper().map(
                            wall,
                            deviceManager.isSmallScreen()
                        )
                    }
                    wrapWallpapers(list, screenType)
                }
                is Response.FAILURE -> {
                }
            }
        }
    }


    private fun getWallpapers(screenType: ScreenType) {
        viewModelScope.launch {
            when (val data = getAllWallpapersUseCase(GetAllWallpapersUseCase.Param(fileName))) {
                is Response.SUCESS -> {
                    val rep = data.response as WallpaperResponse
                    val list = rep.wallpaperList.wallpapers.map { wall ->
                        WallpaperToViewMapper().map(
                            wall,
                            deviceManager.isSmallScreen()
                        )
                    }
                    wrapWallpapers(list, screenType)
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
        mList: List<BaseWallpaperView>,
        screenType: ScreenType
    ) {
        val listItem: MutableList<ItemWrapperList<Any>> = arrayListOf()
        mList.forEach {
            listItem.add(
                ItemWrapperList(
                    it,
                    getItemType(screenType)
                )
            )
        }
        wallpaperListLiveData.value = listItem
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
