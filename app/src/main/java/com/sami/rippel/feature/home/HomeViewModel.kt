package com.sami.rippel.feature.home

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.sami.rippel.allah.R
import com.sami.rippel.feature.home.adapter.WallpapersListAdapter
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.utils.FileUtils
import com.sfaxdroid.data.entity.WallpaperResponse
import com.sfaxdroid.data.mappers.*
import com.sfaxdroid.data.repositories.Response
import com.sfaxdroid.domain.GetAllWallpapersUseCase
import com.sfaxdroid.domain.GetCatWallpapersUseCase
import com.sfaxdroid.domain.GetCategoryUseCase
import com.sfaxdroid.domain.GetLiveWallpapersUseCase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    var getAllWallpapersUseCase: GetAllWallpapersUseCase,
    var getLiveWallpapersUseCase: GetLiveWallpapersUseCase,
    var getCategoryUseCase: GetCategoryUseCase,
    var getCatWallpapersUseCase: GetCatWallpapersUseCase
) :
    ViewModel() {

    var screen = savedStateHandle.get<String>(Constants.EXTRA_SCREEN_TYPE).orEmpty()
    var fileName = savedStateHandle.get<String>(Constants.EXTRA_JSON_FILE_NAME).orEmpty()

    var wallpaperListLiveData: MutableLiveData<List<ItemWrapperList<Any>>> =
        MutableLiveData()

    init {
        loadWallpaperByScreenType()
    }

    private fun getType(type: String): AllInOneFragment.ScreenType {
        return when (type) {
            "LWP" -> AllInOneFragment.ScreenType.Lwp
            "CAT" -> AllInOneFragment.ScreenType.Cat
            "LAB" -> AllInOneFragment.ScreenType.Lab
            "TEXTURE" -> AllInOneFragment.ScreenType.TEXTURE
            "TIMER" -> AllInOneFragment.ScreenType.TIMER
            "CAT_WALL" -> AllInOneFragment.ScreenType.CatWallpaper
            else -> AllInOneFragment.ScreenType.Wall
        }
    }

    private fun loadWallpaperByScreenType() {
        when (val screenType = getType(screen)) {
            is AllInOneFragment.ScreenType.Lwp -> {
                getLiveWallpapers(screenType)
            }
            is AllInOneFragment.ScreenType.Wall -> {
                getWallpapers(screenType)
            }
            is AllInOneFragment.ScreenType.Cat -> {
                getCategory(screenType)
            }
            AllInOneFragment.ScreenType.TEXTURE -> {
                GlobalScope.launch {
                    getWallpapers(screenType)
                }
            }
            AllInOneFragment.ScreenType.TIMER -> {
                getSavedWallpaperList(screenType, null!!)
            }
            AllInOneFragment.ScreenType.CatWallpaper -> {
                getCatWallpapers(screenType)
            }
        }
    }


    private fun getLiveWallpapers(screenType: AllInOneFragment.ScreenType) {
        GlobalScope.launch {
            getLiveWallpapersUseCase(GetLiveWallpapersUseCase.Param(fileName))
            {
                when (it) {
                    is Response.SUCESS -> {
                        val rep =
                            (it.response as Response.SUCESS).response as WallpaperResponse
                        val list = rep.wallpaperList.wallpapers.map { wall ->
                            WallpaperToLwpMapper().map(wall)
                        }
                        wrapWallpapers(list, screenType)
                    }
                    is Response.FAILURE -> {

                    }
                }
            }
        }
    }

    private fun getCategory(screenType: AllInOneFragment.ScreenType) {
        GlobalScope.launch {
            getCategoryUseCase(GetCategoryUseCase.Param(fileName))
            {
                when (it) {
                    is Response.SUCESS -> {
                        val rep =
                            (it.response as Response.SUCESS).response as WallpaperResponse
                        val list = rep.wallpaperList.wallpapers.map { wall ->
                            WallpaperToCategoryMapper().map(wall)
                        }
                        wrapWallpapers(list, screenType)
                    }
                    is Response.FAILURE -> {

                    }
                }
            }
        }
    }

    private fun getCatWallpapers(screenType: AllInOneFragment.ScreenType) {
        GlobalScope.launch {
            getCatWallpapersUseCase(GetCatWallpapersUseCase.Param(fileName))
            {
                when (it) {
                    is Response.SUCESS -> {
                        val rep =
                            (it.response as Response.SUCESS).response as WallpaperResponse
                        val list = rep.wallpaperList.wallpapers.map { wall ->
                            WallpaperToViewMapper().map(wall)
                        }
                        wrapWallpapers(list, screenType)
                    }
                    is Response.FAILURE -> {

                    }
                }
            }
        }
    }


    private fun getWallpapers(screenType: AllInOneFragment.ScreenType) {
        GlobalScope.launch {
            getAllWallpapersUseCase(GetAllWallpapersUseCase.Param(fileName))
            {
                when (it) {
                    is Response.SUCESS -> {
                        val rep =
                            (it.response as Response.SUCESS).response as WallpaperResponse
                        val list = rep.wallpaperList.wallpapers.map { wall ->
                            WallpaperToViewMapper().map(wall)
                        }
                        wrapWallpapers(list, screenType)
                    }
                    is Response.FAILURE -> {

                    }
                }
            }
        }
    }

    private fun getSavedWallpaperList(screenType: AllInOneFragment.ScreenType, context: Context) {
        val list = FileUtils.getPermanentDirListFiles(
            context,
            context.getString(R.string.app_namenospace)
        )?.map {
            SimpleWallpaperView(it?.path.orEmpty())
        }.orEmpty()
        wrapWallpapers(list, screenType)
    }

    private fun getItemType(screenType: AllInOneFragment.ScreenType): Int {
        return when (screenType) {
            is AllInOneFragment.ScreenType.Wall -> WallpapersListAdapter.TYPE_SQUARE_WALLPAPER
            is AllInOneFragment.ScreenType.CatWallpaper -> WallpapersListAdapter.TYPE_SQUARE_WALLPAPER
            is AllInOneFragment.ScreenType.TEXTURE -> WallpapersListAdapter.TYPE_SQUARE_WALLPAPER
            is AllInOneFragment.ScreenType.Cat -> WallpapersListAdapter.TYPE_CAT
            is AllInOneFragment.ScreenType.Lab -> WallpapersListAdapter.TYPE_LAB
            else -> WallpapersListAdapter.TYPE_LWP
        }
    }

    private fun wrapWallpapers(
        mList: List<BaseWallpaperView>,
        screenType: AllInOneFragment.ScreenType
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
