package com.sfaxdroid.timer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.base.BaseViewModel
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.DeviceManager
import com.sfaxdroid.base.FileManager
import com.sfaxdroid.base.extension.getFileName
import com.sfaxdroid.data.entity.Wallpaper
import com.sfaxdroid.data.entity.WallpaperResponse
import com.sfaxdroid.data.mappers.WallpaperToViewMapper
import com.sfaxdroid.data.repositories.Response
import com.sfaxdroid.domain.GetAllWallpapersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WallpaperListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    var getAllWallpapersUseCase: GetAllWallpapersUseCase,
    var deviceManager: DeviceManager,
    var fileManager: FileManager
) : BaseViewModel<WallpaperListViewState>() {

    var screen = savedStateHandle.get<String>(Constants.EXTRA_SCREEN_TYPE).orEmpty()
    private var pendingActions = MutableSharedFlow<WallpaperListAction>()

    private var _uiEffects = MutableSharedFlow<WallpaperListEffects>(extraBufferCapacity = 100)
    val uiEffects: Flow<WallpaperListEffects>
        get() = _uiEffects.asSharedFlow()

    init {

        viewModelScope.launch {
            pendingActions.collect {
                when (it) {
                    is WallpaperListAction.LoadFromStorage -> {
                        val info = fileManager.getTemporaryDirWithFile(it.fileName)
                        if (info.exists())
                            info.delete()
                        loadFromStorage()
                    }
                    is WallpaperListAction.SaveBitmap -> {
                        fileManager.saveBitmapToStorage(
                            it.bitmap,
                            it.fileName,
                            Constants.SAVE_PERMANENT,
                        )
                        it.bitmap.recycle()
                    }
                    is WallpaperListAction.ImageItemClick -> {
                        val exist =
                            fileManager.getPermanentDirWithFile(it.url.getFileName()).exists()
                        if (exist) _uiEffects.emit(WallpaperListEffects.DeleteImage(it.url)) else _uiEffects.emit(
                            WallpaperListEffects.SaveImage(it.url)
                        )
                    }
                }
            }
        }

        if (screen == Constants.KEY_ADDED_LIST_TIMER_LWP) {
            loadFromStorage()
        } else {
            viewModelScope.launch {
                setState { copy(isRefresh = true) }
                when (
                    val data =
                        getAllWallpapersUseCase(GetAllWallpapersUseCase.Param("new.json")).first()
                ) {
                    is Response.SUCESS -> {
                        val wallpaperListLiveData =
                            (data.response as WallpaperResponse).wallpaperList.wallpapers.map { wall ->
                                WallpaperToViewMapper().map(
                                    wall,
                                    deviceManager.isSmallScreen()
                                )
                            }
                        viewModelScope.launch {
                            setState {
                                copy(
                                    wallpaperList = wallpaperListLiveData,
                                    isRefresh = false
                                )
                            }
                        }
                    }
                    is Response.FAILURE -> {
                        setState { copy(isRefresh = false) }
                    }
                }
            }
        }
    }

    private fun loadFromStorage() {
        val list = fileManager.getPermanentDirListFiles()
        val wallpaperListLiveData = list.map {
            WallpaperToViewMapper().map(
                Wallpaper(name = "", desc = "", url = it.path, "", "", ""),
                deviceManager.isSmallScreen()
            )
        }
        viewModelScope.launch {
            setState { copy(wallpaperList = wallpaperListLiveData) }
        }
    }

    override fun createInitialState(): WallpaperListViewState {
        return WallpaperListViewState()
    }

    fun submitAction(action: WallpaperListAction) {
        viewModelScope.launch { pendingActions.emit(action) }
    }
}
