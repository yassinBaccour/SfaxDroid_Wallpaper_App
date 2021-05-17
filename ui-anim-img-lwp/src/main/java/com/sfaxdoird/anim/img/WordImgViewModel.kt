package com.sfaxdoird.anim.img

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.app.ZipUtils
import com.sfaxdroid.base.*
import com.sfaxdroid.base.Constants
import com.thin.downloadmanager.DefaultRetryPolicy
import com.thin.downloadmanager.DownloadManager
import com.thin.downloadmanager.DownloadRequest
import com.thin.downloadmanager.DownloadStatusListenerV1
import com.thin.downloadmanager.ThinDownloadManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
internal class WordImgViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    fileManager: FileManager,
    deviceManager: DeviceManager,
    @Named("domain-url") var domainUrl: String,
    @ApplicationContext context: Context
) :
    BaseViewModel<AnimImgViewState>(AnimImgViewState()), DownloadStatusListenerV1 {

    //TODO MVI change with Flow appropriate function
    var progressValue = MutableLiveData<Pair<Int, Long>>()

    private val pendingActions = MutableSharedFlow<AnimImgAction>()
    private val _uiEffects = MutableSharedFlow<AnimImgEffect>(extraBufferCapacity = 100)

    val uiEffects: Flow<AnimImgEffect>
        get() = _uiEffects.asSharedFlow()

    private var downloadManager: ThinDownloadManager =
        ThinDownloadManager(Constants.DOWNLOAD_THREAD_POOL_SIZE)
    private var requestWallpaper: DownloadRequest? = null
    private var zipFile: File? = null
    private var zipDestination: File? = null
    private var backgroundFile: File? = null
    private var mDownloadId1 = 0
    private var mDownloadId2 = 0

    //TODO MVI add lazy
    var pref: SharedPrefsUtils? = SharedPrefsUtils(context)

    init {
        viewModelScope.launch {
            pendingActions.collect {
                when (it) {
                    is AnimImgAction.ChangeColor -> {
                        saveColor(it.color)
                        setState { copy(color = it.color) }
                    }
                    AnimImgAction.OpenLiveWallpaper -> {
                        viewModelScope.launch {
                            _uiEffects.emit(AnimImgEffect.GoToLiveWallpaper)
                        }
                    }
                }
            }
        }

        val lwpFolder =
            fileManager.getTemporaryDirWithFolder(com.sfaxdoird.anim.img.Constants.KEY_LWP_FOLDER_CONTAINER)

        zipDestination = File(lwpFolder, Constants.ZIP_FOLDER_NAME)

        val requestZipFile =
            DownloadRequest(Uri.parse(if (deviceManager.isSmallScreen()) domainUrl + com.sfaxdoird.anim.img.Constants.URL_ZIP_FILE_MINI_PNG else domainUrl + com.sfaxdoird.anim.img.Constants.URL_ZIP_FILE_PNG))
                .setDestinationURI(Uri.parse(lwpFolder.toString() + "/" + com.sfaxdoird.anim.img.Constants.PNG_ZIP_FILE_NAME))
                .setPriority(DownloadRequest.Priority.LOW)
                .setRetryPolicy(DefaultRetryPolicy())
                .setDownloadContext("LWP Resources")
                .setStatusListener(this)

        requestWallpaper = DownloadRequest(
            Uri.parse(savedStateHandle.get<String>(Constants.EXTRA_URL_TO_DOWNLOAD).orEmpty())
        )
            .setDestinationURI(Uri.parse(lwpFolder.toString() + "/" + Constants.PNG_BACKFROUND_FILE_NAME))
            .setPriority(DownloadRequest.Priority.LOW)
            .setRetryPolicy(DefaultRetryPolicy())
            .setDownloadContext("Doua LWP Background")
            .setStatusListener(this)

        backgroundFile = File(lwpFolder, Constants.PNG_BACKFROUND_FILE_NAME)
        zipFile = File(lwpFolder, com.sfaxdoird.anim.img.Constants.PNG_ZIP_FILE_NAME)

        if (zipFile?.exists() == true) {
            backgroundFile?.delete()
            if (downloadManager.query(mDownloadId2) == DownloadManager.STATUS_NOT_FOUND) {
                mDownloadId2 = downloadManager.add(requestWallpaper)
            }
        } else {
            if (downloadManager.query(mDownloadId1) == DownloadManager.STATUS_NOT_FOUND) {
                mDownloadId1 = downloadManager.add(requestZipFile)
            }
        }
    }

    override fun onDownloadComplete(request: DownloadRequest?) {
        val id = request?.downloadId

        if (id == mDownloadId1) {
            viewModelScope.launch {
                setState { copy(progressionInfo = ProgressionInfo.IdOneCompleted) }
            }
            unzipAndDeleteZip()
            mDownloadId2 = downloadManager.add(requestWallpaper)
        }

        if (id == mDownloadId2) {
            viewModelScope.launch {
                setState {
                    copy(
                        progressionInfo = ProgressionInfo.IdTwoCompleted,
                        isCompleted = true
                    )
                }
            }
        }
    }

    override fun onDownloadFailed(
        downloadRequest: DownloadRequest?,
        errorCode: Int,
        errorMessage: String?
    ) {
        progressValue.value = Pair(0, 0)
        viewModelScope.launch {
            setState { copy(isCompleted = false) }
        }
    }

    override fun onProgress(
        downloadRequest: DownloadRequest?,
        totalBytes: Long,
        downloadedBytes: Long,
        progress: Int
    ) {
        progressValue.value = Pair(progress, totalBytes)
    }

    private fun unzipAndDeleteZip() {
        ZipUtils.unzipFile(zipFile, zipDestination)
        if (zipFile?.exists() == true) {
            backgroundFile?.delete()
        }
    }

    override fun onCleared() {
        super.onCleared()
        zipFile = null
        zipDestination = null
        backgroundFile = null
    }

    fun submitAction(action: AnimImgAction) {
        viewModelScope.launch { pendingActions.emit(action) }
    }

    private fun saveColor(color: Int) {
        pref?.setSetting(com.sfaxdroid.bases.Constants.WALLPAPER_COLOR, color)
    }

}
