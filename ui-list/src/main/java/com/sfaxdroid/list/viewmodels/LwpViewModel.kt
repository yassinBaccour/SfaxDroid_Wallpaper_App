package com.sfaxdroid.list.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.base.DeviceManager
import com.sfaxdroid.data.entity.Response
import com.sfaxdroid.data.mappers.WallpaperToLwpMapper
import com.sfaxdroid.domain.GetLiveWallpapersUseCase
import com.sfaxdroid.bases.ScreenType
import com.sfaxdroid.list.WallpaperViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
internal class LwpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getLiveWallpapersUseCase: GetLiveWallpapersUseCase,
    var wallpaperToLwpMapper: WallpaperToLwpMapper,
    var deviceManager: DeviceManager,
    @Named("appLanguage") var appLanguage: String
) : BaseViewModel(savedStateHandle) {

    var state = getLiveWallpapersUseCase.flow.map {
        val itemsList = getWrappedListWithType(when (it) {
            is Response.SUCCESS -> it.response.wallpaperList.wallpapers.map { wall ->
                wallpaperToLwpMapper.map(wall, deviceManager.isSmallScreen())
            }
            is Response.FAILURE -> arrayListOf()
        }, ScreenType.Lwp)
        WallpaperViewState(itemsList, isRefresh = false, isError = itemsList.isEmpty())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WallpaperViewState.Empty,
    )

    init {
        getLiveWallpapersUseCase(GetLiveWallpapersUseCase.Param(byLanguage(fileName)))
    }

    private fun byLanguage(fileName: String): String {
        return when (appLanguage) {
            "ar" -> fileName.replace("lwp", "lwp_ar")
            "fr" -> fileName.replace("lwp", "lwp_fr")
            else -> fileName
        }
    }
}