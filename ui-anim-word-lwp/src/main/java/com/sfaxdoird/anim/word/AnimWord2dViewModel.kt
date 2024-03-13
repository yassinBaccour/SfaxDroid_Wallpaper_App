package com.sfaxdoird.anim.word

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.base.BaseViewModel
import com.sfaxdroid.base.FileManager
import com.sfaxdroid.base.SharedPrefsUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AnimWord2dViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val savedStateHandle: SavedStateHandle,
    var fileManager: FileManager
) :
    BaseViewModel<AnimWorldViewState>(AnimWorldViewState()) {

    private var downloadId2 = 0
    var progressValue = MutableLiveData<Pair<Int, Long>>()
    private val pref: SharedPrefsUtils = SharedPrefsUtils(context)
    private var pendingActions = MutableSharedFlow<AnimWorldAction>()
    private var _uiEffects = MutableSharedFlow<AnimWorldEffect>(extraBufferCapacity = 100)

    val uiEffects: Flow<AnimWorldEffect>
        get() = _uiEffects.asSharedFlow()

    init {
        viewModelScope.launch {
            pendingActions.collect {
                when (it) {
                    is AnimWorldAction.ChangeColor -> {
                        pref.setSetting(com.sfaxdroid.bases.Constants.WALLPAPER_COLOR, it.color)
                        setState { copy(color = it.color) }
                    }
                    is AnimWorldAction.ChangeFont -> {
                        pref.setSetting("2dwallpaperfontstyle", it.style + 1)
                        setState { copy(style = it.style) }
                    }
                    is AnimWorldAction.ChangeSize -> {
                        pref.setSetting("2dwallpapertextsize", it.size + 1)
                        setState { copy(size = it.size) }
                    }
                    AnimWorldAction.DownloadData -> {
                        load()
                    }
                    AnimWorldAction.OpenLiveWallpaper -> {
                        _uiEffects.emit(OpenLiveWallpaper)
                    }
                }
            }
        }
        load()
    }

    private fun load() {
        val filesDir = fileManager.getExternalFilesDir()
    }

    fun submitAction(action: AnimWorldAction) {
        viewModelScope.launch { pendingActions.emit(action) }
    }
}
