package com.sfaxdroid.timer

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.base.DeviceManager
import com.sfaxdroid.base.FileManager
import com.sfaxdroid.data.entity.WallpaperResponse
import com.sfaxdroid.data.mappers.SimpleWallpaperView
import com.sfaxdroid.data.mappers.WallpaperToViewMapper
import com.sfaxdroid.data.repositories.Response
import com.sfaxdroid.domain.GetAllWallpapersUseCase
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class WallpaperListViewModel @ViewModelInject constructor(
    var getAllWallpapersUseCase: GetAllWallpapersUseCase,
    var deviceManager: DeviceManager,
    var fileManager: FileManager
) : ViewModel() {

    var wallpaperListLiveData: MutableLiveData<List<SimpleWallpaperView>> =
        MutableLiveData()

    init {
        var list = fileManager.getPermanentDirListFiles()

        viewModelScope.launch {
            when (val data = getAllWallpapersUseCase(GetAllWallpapersUseCase.Param("new.json"))) {
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