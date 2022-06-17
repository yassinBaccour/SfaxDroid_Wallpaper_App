package com.yassin.wallpaper.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.base.BaseViewModel
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.DeviceManager
import com.sfaxdroid.base.FileManager
import com.sfaxdroid.data.mappers.*
import com.sfaxdroid.data.entity.Response
import com.sfaxdroid.data.entity.WallpaperResponse
import com.sfaxdroid.domain.GetAllWallpapersUseCase
import com.sfaxdroid.domain.GetCatWallpapersUseCase
import com.sfaxdroid.domain.GetCategoryUseCase
import com.sfaxdroid.domain.GetLiveWallpapersUseCase
import com.sfaxdroid.domain.GetTagUseCase
import com.yassin.wallpaper.feature.ScreenType
import com.yassin.wallpaper.feature.home.adapter.WallpapersListAdapter
import com.sfaxdroid.data.entity.AppName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    var getAllWallpapersUseCase: GetAllWallpapersUseCase,
    var getLiveWallpapersUseCase: GetLiveWallpapersUseCase,
    var getCategoryUseCase: GetCategoryUseCase,
    var getCatWallpapersUseCase: GetCatWallpapersUseCase,
    var wallpaperToLwpMapper: WallpaperToLwpMapper,
    var getTagUseCase: GetTagUseCase,
    var fileManager: FileManager,
    var deviceManager: DeviceManager,
    @Named("app-name") var appName: AppName,
    @Named("appLanguage") var appLanguage: String
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
                    is WallpaperListAction.UpdateTagSelectedPosition -> {
                        setState {
                            copy(tagSelectedPosition = it.position)
                        }
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
        viewModelScope.launch {
            setState {
                copy(
                    toolBarTitle = getScreenTitle(),
                    isToolBarVisible = getToolBarVisibility(),
                    isPrivacyButtonVisible = getPrivacyButtonVisibility(),
                    setDisplayHomeAsUpEnabled = isWithToolBar()
                )
            }
        }
    }

    private fun getPrivacyButtonVisibility() = !isWithToolBar() && appName == AppName.AccountTwo

    private fun getToolBarVisibility() = if (isWithToolBar()) true else appName != AppName.AccountOne

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
            when (val result =
                getTagUseCase(GetTagUseCase.Param(getTagFileNameByType(screenType))).first()) {
                is Response.SUCCESS -> {
                    val tagList = result.response.tagList.map { wall ->
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

    private fun tagByLanguage(): String {
        return if (appLanguage == "ar") {
            "wallpaper_tags_ar.json"
        } else "wallpaper_tags.json"
    }

    private fun getTagFileNameByType(screenType: ScreenType): String {
        return when (screenType) {
            ScreenType.TEXTURE -> tagByLanguage()
            else -> tagByLanguage()
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
                    setState { copy(isTagVisible = false, isRefresh = true) }
                }
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
                getCatWallpapers(screenType, fileName)
            }
        }
    }

    private fun getLiveWallpapers(screenType: ScreenType) {
        viewModelScope.launch {
            when (
                val data =
                    getLiveWallpapersUseCase(GetLiveWallpapersUseCase.Param(byLanguage(fileName))).first()
            ) {
                is Response.SUCCESS -> {
                    wrapWallpapers(
                        wallpaperList = data.response.wallpaperList.wallpapers.map { wall ->
                            wallpaperToLwpMapper.map(wall, deviceManager.isSmallScreen())
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
        return when (appLanguage) {
            "ar" -> fileName.replace("lwp", "lwp_ar")
            "fr" -> fileName.replace("lwp", "lwp_fr")
            else -> fileName
        }
    }

    private fun getCategory(screenType: ScreenType) {
        viewModelScope.launch {
            when (val data = getCategoryUseCase(GetCategoryUseCase.Param(fileName)).first()) {
                is Response.SUCCESS -> {
                    wrapWallpapers(
                        wallpaperList = data.response.wallpaperList.wallpapers.map { wall ->
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

    private fun getCatWallpapers(
        screenType: ScreenType,
        fileName: String,
        isSquare: Boolean = false
    ) {
        viewModelScope.launch {
            when (val result =
                getCatWallpapersUseCase(GetCatWallpapersUseCase.Param(fileName)).first()) {
                is Response.SUCCESS -> {
                    val wallpaperList = result.response.wallpaperList.wallpapers.map { wall ->
                        WallpaperToViewMapper().map(
                            wall,
                            deviceManager.isSmallScreen()
                        )
                    }
                    wrapWallpapers(wallpaperList, screenType, isSquare)
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
            TagType.CategorySquare -> getCatWallpapers(ScreenType.Wall, tagView.fileName, true)
            TagType.Texture -> getWallpapers(ScreenType.Wall, tagView.fileName)
            else -> {
                if (tagView.fileName.contains("new") && appName == AppName.AccountTwo)
                    getMixed()
                else getWallpapers(ScreenType.Wall, tagView.fileName)
            }
        }
    }

    private fun getWallpapers(screenType: ScreenType, fileName: String) {
        viewModelScope.launch {
            when (val result =
                getAllWallpapersUseCase(GetAllWallpapersUseCase.Param(fileName)).first()) {
                is Response.SUCCESS -> {
                    val list = result.response.wallpaperList.wallpapers.map { wall ->
                        WallpaperToViewMapper().map(
                            wall,
                            deviceManager.isSmallScreen()
                        )
                    }.shuffled()
                    wrapWallpapers(
                        list,
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

    private fun getItemType(screenType: ScreenType, isSquare: Boolean = false): Int {

        val wallpaperCellViewTypeByApp =
            if (isSquare) WallpapersListAdapter.TYPE_SQUARE_WALLPAPER else WallpapersListAdapter.TYPE_WALLPAPER

        return when (screenType) {
            is ScreenType.Cat -> WallpapersListAdapter.TYPE_CAT
            is ScreenType.Lab -> WallpapersListAdapter.TYPE_LAB
            is ScreenType.Lwp -> WallpapersListAdapter.TYPE_LWP
            else -> wallpaperCellViewTypeByApp
        }
    }

    private fun wrapWallpapers(
        wallpaperList: List<BaseWallpaperView>,
        screenType: ScreenType,
        isSquare: Boolean = false
    ) {
        viewModelScope.launch {
            setState {
                copy(
                    itemsList = getWrappedListWithType(
                        wallpaperList,
                        screenType,
                        isSquare
                    ),
                    isRefresh = false
                )
            }
        }
    }

    private fun getWrappedListWithType(
        wallpaperList: List<BaseWallpaperView>,
        screenType: ScreenType,
        isSquare: Boolean = false
    ): MutableList<ItemWrapperList> {
        val mixedListItem: MutableList<ItemWrapperList> = arrayListOf()
        wallpaperList.forEach {
            mixedListItem.add(
                ItemWrapperList(
                    it,
                    getItemType(screenType, isSquare)
                )
            )
        }
        return mixedListItem
    }

    private fun getWallpaper(response: Response<WallpaperResponse>): List<SimpleWallpaperView> {
        return when (response) {
            is Response.SUCCESS -> {
                response.response.wallpaperList.wallpapers.map { wall ->
                    WallpaperToViewMapper().map(wall, deviceManager.isSmallScreen())
                }
            }
            is Response.FAILURE -> arrayListOf()
        }
    }

    private fun getMixed() {
        viewModelScope.launch {
            getAllWallpapersUseCase(GetAllWallpapersUseCase.Param("new.json")).zip(
                getLiveWallpapersUseCase(GetLiveWallpapersUseCase.Param(byLanguage("lwp.json")))
            ) { wallpaperResponse, liveWallpaperResponse ->

                val mixedListItem: MutableList<ItemWrapperList> = arrayListOf()

                val lwpList = when (liveWallpaperResponse) {
                    is Response.FAILURE -> {
                        arrayListOf()
                    }
                    is Response.SUCCESS -> {
                        liveWallpaperResponse.response.wallpaperList.wallpapers.map { wall ->
                            wallpaperToLwpMapper.map(wall, deviceManager.isSmallScreen())
                        }
                    }
                }

                mixedListItem.add(
                    ItemWrapperList(
                        CarouselView("Live Wallpaper 4K", lwpList, CarouselTypeEnum.LWP),
                        WallpapersListAdapter.TYPE_CAROUSEL
                    )
                )

                getWallpaper(wallpaperResponse).map {
                    mixedListItem.add(
                        ItemWrapperList(
                            it,
                            WallpapersListAdapter.TYPE_WALLPAPER
                        )
                    )
                }
                return@zip mixedListItem
            }.flowOn(Dispatchers.Default)
                .catch { e ->
                }.collect {
                    setState {
                        copy(
                            itemsList = it,
                            isRefresh = false
                        )
                    }
                }
        }
    }

    fun submitAction(uiAction: WallpaperListAction) {
        viewModelScope.launch {
            pendingActions.emit(uiAction)
        }
    }
}
