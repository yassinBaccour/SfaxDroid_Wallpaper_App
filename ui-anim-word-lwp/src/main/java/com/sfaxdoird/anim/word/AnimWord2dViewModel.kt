package com.sfaxdoird.anim.word

import android.net.Uri
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.DeviceManager
import com.sfaxdroid.base.FileManager
import com.thin.downloadmanager.DefaultRetryPolicy
import com.thin.downloadmanager.DownloadManager
import com.thin.downloadmanager.DownloadRequest
import com.thin.downloadmanager.DownloadStatusListenerV1
import com.thin.downloadmanager.ThinDownloadManager

class AnimWord2dViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    var fileManager: FileManager,
    var deviceManager: DeviceManager
) :
    ViewModel(), DownloadStatusListenerV1 {

    private var downloadId2 = 0
    private val downloadManager =
        ThinDownloadManager(Constants.DOWNLOAD_THREAD_POOL_SIZE)
    var progressValue = MutableLiveData<Pair<Int, Long>>()
    var isCompleted = MutableLiveData<Boolean>()

    init {
        load()
    }

    fun load() {
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
        isCompleted.value = true
    }

    override fun onDownloadFailed(
        request: DownloadRequest,
        errorCode: Int,
        errorMessage: String
    ) {
        progressValue.value = Pair(0, 0)
        isCompleted.value = false
    }

    override fun onProgress(
        request: DownloadRequest,
        totalBytes: Long,
        downloadedBytes: Long,
        progress: Int
    ) {
        progressValue.value = Pair(progress, totalBytes)
    }
}