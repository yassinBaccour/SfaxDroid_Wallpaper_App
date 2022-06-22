package com.sfaxdroid.timer

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.base.BaseViewModel
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.DeviceManager
import com.sfaxdroid.base.FileManager
import com.sfaxdroid.base.extension.getFileName
import com.sfaxdroid.data.entity.Wallpaper
import com.sfaxdroid.data.mappers.WallpaperToViewMapper
import com.sfaxdroid.data.entity.Response
import com.sfaxdroid.domain.GetAllWallpapersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class WallpaperListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    var getAllWallpapersUseCase: GetAllWallpapersUseCase,
    var deviceManager: DeviceManager,
    var fileManager: FileManager
) : BaseViewModel<WallpaperListViewState>(WallpaperListViewState()) {

    var screen = savedStateHandle.get<String>(Constants.EXTRA_SCREEN_TYPE).orEmpty()
    private var pendingActions = MutableSharedFlow<WallpaperListAction>()

    private var _uiEffects = MutableSharedFlow<WallpaperListEffects>(extraBufferCapacity = 100)
    val uiEffects: Flow<WallpaperListEffects>
        get() = _uiEffects.asSharedFlow()


    private suspend fun saveBitmap(bitmap: Bitmap, fileName: String) {
        viewModelScope.launch {
            setState { copy(isRefresh = true) }
            val result = fileManager.saveBitmapToStorage(
                bitmap,
                fileName,
                Constants.SAVE_PERMANENT,
            )
            bitmap.recycle()
            if (result) {
                val nbSavedImage = fileManager.getPermanentDirListFiles().size
                setState { copy(nbImage = nbSavedImage, isRefresh = false) }
                _uiEffects.emit(WallpaperListEffects.SaveSuccess)
            } else {
                _uiEffects.emit(WallpaperListEffects.SaveError)
            }
        }
    }


    init {

        viewModelScope.launch {
            pendingActions.collect {
                when (it) {
                    is WallpaperListAction.LoadFromStorage -> {
                        val info = fileManager.getPermanentDirWithFile(it.fileName)
                        if (info.exists())
                            info.delete()
                        loadFromStorage()
                    }
                    is WallpaperListAction.SaveBitmap -> {
                        saveBitmap(it.bitmap, it.fileName)
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
            loadFromWs()
        }
    }

    private fun loadFromWs() {
        viewModelScope.launch {
            setState { copy(isRefresh = true) }
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

    fun submitAction(action: WallpaperListAction) {
        viewModelScope.launch { pendingActions.emit(action) }
    }
}
