package com.sfaxdroid.list.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.DeviceManager
import com.sfaxdroid.data.entity.Response
import com.sfaxdroid.data.mappers.WallpaperToCategoryMapper
import com.sfaxdroid.domain.GetCategoryUseCase
import com.sfaxdroid.list.ListUtils.getWrappedListWithType
import com.sfaxdroid.list.ScreenType
import com.sfaxdroid.list.WallpaperListState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class CategoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getCategoryUseCase: GetCategoryUseCase,
    var deviceManager: DeviceManager
) : ViewModel() {

    private var fileName = savedStateHandle.get<String>(Constants.EXTRA_JSON_FILE_NAME).orEmpty()

    val state = getCategoryUseCase.flow.map {
        val itemsList = getWrappedListWithType(
            when (it) {
                is Response.FAILURE -> arrayListOf()
                is Response.SUCCESS -> it.response.wallpaperList.wallpapers.map { wall ->
                    WallpaperToCategoryMapper().map(wall, deviceManager.isSmallScreen())
                }
            },
            ScreenType.Cat
        )
        WallpaperListState(itemsList, isRefresh = false)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WallpaperListState(isRefresh = true),
    )

    init {
        getCategoryUseCase(GetCategoryUseCase.Param(fileName))
    }

}