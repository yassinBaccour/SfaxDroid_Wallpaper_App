package com.sfaxdoird.anim.word

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.base.BaseViewModel
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.FileManager
import com.sfaxdroid.base.SharedPrefsUtils
import com.thin.downloadmanager.DefaultRetryPolicy
import com.thin.downloadmanager.DownloadManager
import com.thin.downloadmanager.DownloadRequest
import com.thin.downloadmanager.DownloadStatusListenerV1
import com.thin.downloadmanager.ThinDownloadManager
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
    BaseViewModel<AnimWorldViewState>(AnimWorldViewState()), DownloadStatusListenerV1 {

    private var downloadId2 = 0
    private val downloadManager = ThinDownloadManager(Constants.DOWNLOAD_THREAD_POOL_SIZE)
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

        val requestWallpaper =
            DownloadRequest(
                Uri.parse(savedStateHandle.get<String>(Constants.EXTRA_URL_TO_DOWNLOAD).orEmpty())
            )
                .setDestinationURI(
                    Uri.parse(
                        filesDir
                            .toString() + "/" + Constants.PNG_BACKFROUND_FILE_NAME
                    )
                ).setPriority(DownloadRequest.Priority.LOW)
                .setRetryPolicy(DefaultRetryPolicy())
                .setDownloadContext("LWP Background")
                .setStatusListener(this)

        if (downloadManager.query(downloadId2) == DownloadManager.STATUS_NOT_FOUND) {
            downloadId2 =
                downloadManager.add(requestWallpaper)
        }
    }

    override fun onDownloadComplete(request: DownloadRequest) {
        viewModelScope.launch {
            setState { copy(isCompleted = true) }
        }
    }

    override fun onDownloadFailed(
        request: DownloadRequest,
        errorCode: Int,
        errorMessage: String
    ) {
        progressValue.value = Pair(0, 0)
        viewModelScope.launch {
            setState { copy(isCompleted = false) }
            _uiEffects.emit(Retry)
        }
    }

    override fun onProgress(
        request: DownloadRequest,
        totalBytes: Long,
        downloadedBytes: Long,
        progress: Int
    ) {
        progressValue.value = Pair(progress, totalBytes)
    }

    fun submitAction(action: AnimWorldAction) {
        viewModelScope.launch { pendingActions.emit(action) }
    }
}
