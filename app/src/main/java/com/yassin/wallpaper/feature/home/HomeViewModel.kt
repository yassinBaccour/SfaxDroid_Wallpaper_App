package com.yassin.wallpaper.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.base.BaseViewModel
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
import com.yassin.wallpaper.feature.ScreenType
import com.yassin.wallpaper.feature.home.adapter.WallpapersListAdapter
import com.yassin.wallpaper.utils.AppName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    var getAllWallpapersUseCase: GetAllWallpapersUseCase,
    var getLiveWallpapersUseCase: GetLiveWallpapersUseCase,
    var getCategoryUseCase: GetCategoryUseCase,
    var getCatWallpapersUseCase: GetCatWallpapersUseCase,
    var getTagUseCase: GetTagUseCase,
    var fileManager: FileManager,
    var deviceManager: DeviceManager,
    @Named("app-name") var appName: AppName
) :
    BaseViewModel<WallpaperListViewEvents>(WallpaperListViewEvents()) {

    var screenName = savedStateHandle.get<String>(Constants.EXTRA_SCREEN_NAME).orEmpty()
    var screenType = savedStateHandle.get<String>(Constants.EXTRA_SCREEN_TYPE).orEmpty()
    var selectedLwpName = savedStateHandle.get<String>(Constants.KEY_LWP_NAME).orEmpty()
    var fileName = savedStateHandle.get<String>(Constants.EXTRA_JSON_FILE_NAME).orEmpty()
    var pendingActions: MutableSharedFlow<WallpaperListAction> = MutableSharedFlow()
    private var _uiEffects = Channel<WallpaperListEffects>()

    val uiEffects: Flow<WallpaperListEffects>
        get() = _uiEffects.receiveAsFlow()

    init {
        viewModelScope.launch {
            pendingActions.collect {
                when (it) {
                    is WallpaperListAction.LoadTags -> {
                        loadByTag(it.tagView)
                    }
                    is WallpaperListAction.OpenItems -> {
                        when (it.wallpaperObject) {
                            is SimpleWallpaperView -> {
                                _uiEffects.send(
                                    OpenWallpaperByType(
                                        it.wallpaperObject as SimpleWallpaperView,
                                        selectedLwpName, screenName
                                    )
                                )
                            }
                            is LwpItem -> {
                                _uiEffects.send(OpenLiveWallpaper(it.wallpaperObject as LwpItem))
                            }
                            is CategoryItem -> {
                                _uiEffects.send(OpenCategory(it.wallpaperObject as CategoryItem))
                            }
                        }
                    }
                }
            }
        }
        initInitialState()
        loadWallpaperByScreenType()
    }

    private fun initInitialState() {
        val cat = selectedLwpName.isNotEmpty() || screenType == "CAT_WALL"
        val title = when (selectedLwpName) {
            Constants.KEY_WORD_IMG_LWP -> screenName
            Constants.KEY_ANIM_2D_LWP -> screenName
            else -> when (screenType) {
                "CAT_WALL" -> screenName
                else -> ""
            }
        }
        viewModelScope.launch {
            setState {
                copy(
                    toolBarTitle = title,
                    isToolBarVisible = if (cat) true else appName != AppName.SfaxDroid,
                    isPrivacyButtonVisible = !cat && appName == AppName.LiliaGame,
                    setDisplayHomeAsUpEnabled = cat
                )
            }
        }
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

    private fun getTag(screenType: ScreenType) {
        viewModelScope.launch {
            when (val data =
                getTagUseCase(GetTagUseCase.Param(getTagFileNameByType(screenType))).first()) {
                is Response.SUCESS -> {
                    val rep = data.response as TagResponse
                    val tagList = rep.tagList.map { wall ->
                        TagToTagViewMap().map(wall, deviceManager.isSmallScreen())
                    }
                    setState { copy(tagList = tagList, isTagVisible = true, isRefresh = false) }
                }
                is Response.FAILURE -> {
                    setState { copy(isTagVisible = false, isRefresh = false) }
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
        when (val screenType = getType(screenType)) {
            is ScreenType.Lwp -> {
                viewModelScope.launch {
                    setState { copy(isTagVisible = false, isRefresh = true) }
                }
                getLiveWallpapers(screenType)
            }
            is ScreenType.Wall -> {
                viewModelScope.launch {
                    setState { copy(isTagVisible = true, isRefresh = true) }
                }
                getTag(screenType)
                getWallpapers(screenType, fileName)
            }
            is ScreenType.Cat -> {
                viewModelScope.launch {
                    setState { copy(isTagVisible = false, isRefresh = true) }
                }
                getCategory(screenType)
            }
            ScreenType.TEXTURE -> {
                viewModelScope.launch {
                    setState { copy(isTagVisible = true, isRefresh = true) }
                }
                getTag(screenType)
                getWallpapers(screenType, fileName)
            }
            ScreenType.TIMER -> {
                viewModelScope.launch {
                    setState { copy(isTagVisible = false, isRefresh = true) }
                }
                getSavedWallpaperList(screenType)
            }
            ScreenType.CatWallpaper -> {
                viewModelScope.launch {
                    setState { copy(isTagVisible = false, isRefresh = true) }
                }
                getCatWallpapers(screenType, fileName)
            }
            ScreenType.MIXED -> {
                viewModelScope.launch {
                    setState { copy(isTagVisible = false, isRefresh = true) }
                }
                getTag(screenType)
                getWallpapers(screenType, fileName)
            }
        }
    }

    private fun getLiveWallpapers(screenType: ScreenType) {
        viewModelScope.launch {
            when (
                val data =
                    getLiveWallpapersUseCase(GetLiveWallpapersUseCase.Param(byLanguage(fileName))).first()
            ) {
                is Response.SUCESS -> {
                    wrapWallpapers(
                        wallpaperList = (data.response as WallpaperResponse).wallpaperList.wallpapers.map { wall ->
                            WallpaperToLwpMapper().map(wall, deviceManager.isSmallScreen())
                        },
                        screenType = screenType
                    )
                }
                is Response.FAILURE -> {
                    setState { copy(isRefresh = false) }
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
            when (val data = getCategoryUseCase(GetCategoryUseCase.Param(fileName)).first()) {
                is Response.SUCESS -> {
                    wrapWallpapers(
                        wallpaperList = (data.response as WallpaperResponse).wallpaperList.wallpapers.map { wall ->
                            WallpaperToCategoryMapper().map(wall, deviceManager.isSmallScreen())
                        },
                        screenType = screenType
                    )
                }
                is Response.FAILURE -> {
                    setState { copy(isRefresh = false) }
                }
            }
        }
    }

    private fun getCatWallpapers(screenType: ScreenType, fileName: String) {
        viewModelScope.launch {
            when (val data =
                getCatWallpapersUseCase(GetCatWallpapersUseCase.Param(fileName)).first()) {
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
                    setState { copy(isRefresh = false) }
                }
            }
        }
    }

    private fun loadByTag(tagView: TagView) {
        when (tagView.type) {
            TagType.Category -> getCatWallpapers(ScreenType.Wall, tagView.fileName)
            TagType.Texture -> getWallpapers(ScreenType.Wall, tagView.fileName)
            else -> getWallpapers(ScreenType.Wall, tagView.fileName)
        }
    }

    private fun getWallpapers(screenType: ScreenType, fileName: String) {
        viewModelScope.launch {
            when (val data =
                getAllWallpapersUseCase(GetAllWallpapersUseCase.Param(fileName)).first()) {
                is Response.SUCESS -> {
                    wrapWallpapers(
                        (data.response as WallpaperResponse).wallpaperList.wallpapers.map { wall ->
                            WallpaperToViewMapper().map(
                                wall,
                                deviceManager.isSmallScreen()
                            )
                        },
                        screenType
                    )
                }
                is Response.FAILURE -> {
                    setState {
                        copy(isRefresh = false)
                    }
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
            is ScreenType.MIXED -> WallpapersListAdapter.TYPE_WALLPAPER
            else -> WallpapersListAdapter.TYPE_LWP
        }
    }

    private fun wrapWallpapers(
        wallpaperList: List<BaseWallpaperView>,
        screenType: ScreenType
    ) {
        viewModelScope.launch {
            setState {
                copy(
                    itemsList = getWrappedListWithType(wallpaperList, screenType),
                    isRefresh = false
                )
            }
        }
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

    fun submitAction(uiAction: WallpaperListAction) {
        viewModelScope.launch {
            pendingActions.emit(uiAction)
        }
    }
}
