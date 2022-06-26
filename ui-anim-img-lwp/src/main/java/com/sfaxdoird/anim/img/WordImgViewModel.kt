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
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
internal class WordImgViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val fileManager: FileManager,
    private val deviceManager: DeviceManager,
    @Named("domain-url") var domainUrl: String,
    @ApplicationContext context: Context
) :
    BaseViewModel<AnimImgViewState>(AnimImgViewState()), DownloadStatusListenerV1 {

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

    private val selectedColor = MutableStateFlow(0)
    private val progressValueFlow = MutableStateFlow<Pair<Int, Long>>(Pair(0, 0))
    private val progressInfoFlow = MutableStateFlow<ProgressionInfo>(ProgressionInfo.Idle)

    val state = combine(
        selectedColor,
        progressValueFlow,
        progressInfoFlow
    ) { color, progressValue, progressInfo ->
        AnimImgViewState(
            color = color,
            progressValue = progressValue,
            progressionInfo = progressInfo
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AnimImgViewState.Empty
    )

    init {
        startDownload()
    }

    private fun startDownload() {
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
                progressInfoFlow.emit(ProgressionInfo.IdOneCompleted)
            }
            unzipAndDeleteZip()
            mDownloadId2 = downloadManager.add(requestWallpaper)
        }

        if (id == mDownloadId2) {
            viewModelScope.launch {
                progressInfoFlow.emit(ProgressionInfo.IdTwoCompleted)
            }
        }
    }

    override fun onDownloadFailed(
        downloadRequest: DownloadRequest?,
        errorCode: Int,
        errorMessage: String?
    ) {
        viewModelScope.launch {
            progressInfoFlow.emit(ProgressionInfo.Error)
        }
    }

    override fun onProgress(
        downloadRequest: DownloadRequest?,
        totalBytes: Long,
        downloadedBytes: Long,
        progress: Int
    ) {
        viewModelScope.launch {
            progressValueFlow.emit(Pair(progress, totalBytes))
        }
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

    fun saveColor(color: Int) {
        pref?.setSetting(com.sfaxdroid.bases.Constants.WALLPAPER_COLOR, color)
        viewModelScope.launch {
            selectedColor.emit(color)
        }
    }

}
