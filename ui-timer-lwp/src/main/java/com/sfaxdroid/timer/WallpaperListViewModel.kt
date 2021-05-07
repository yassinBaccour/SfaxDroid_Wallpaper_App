package com.sfaxdroid.timer

import androidx.hilt.Assisted
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.DeviceManager
import com.sfaxdroid.base.FileManager
import com.sfaxdroid.data.entity.Wallpaper
import com.sfaxdroid.data.entity.WallpaperResponse
import com.sfaxdroid.data.mappers.SimpleWallpaperView
import com.sfaxdroid.data.mappers.WallpaperToViewMapper
import com.sfaxdroid.data.repositories.Response
import com.sfaxdroid.domain.GetAllWallpapersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WallpaperListViewModel @Inject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    var getAllWallpapersUseCase: GetAllWallpapersUseCase,
    var deviceManager: DeviceManager,
    var fileManager: FileManager
) : ViewModel() {

    var wallpaperListLiveData: MutableLiveData<List<SimpleWallpaperView>> =
        MutableLiveData()

    var screen = savedStateHandle.get<String>(Constants.EXTRA_SCREEN_TYPE).orEmpty()

    init {
        if (screen == Constants.KEY_ADDED_LIST_TIMER_LWP) {
            loadFromStorage()
        } else {
            viewModelScope.launch {
                when (
                    val data =
                        getAllWallpapersUseCase(GetAllWallpapersUseCase.Param("new.json"))
                ) {
                    is Response.SUCESS -> {
                        wallpaperListLiveData.value =
                            (data.response as WallpaperResponse).wallpaperList.wallpapers.map { wall ->
                                WallpaperToViewMapper().map(
                                    wall,
                                    deviceManager.isSmallScreen()
                                )
                            }
                    }
                    is Response.FAILURE -> {
                    }
                }
            }
        }
    }

    fun loadFromStorage() {
        val list = fileManager.getPermanentDirListFiles()
        wallpaperListLiveData.value = list.map {
            WallpaperToViewMapper().map(
                Wallpaper(name = "", desc = "", url = it.path, "", "", ""),
                deviceManager.isSmallScreen()
            )
        }
    }
}
