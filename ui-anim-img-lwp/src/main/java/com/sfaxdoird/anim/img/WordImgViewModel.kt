package com.sfaxdoird.anim.img

import android.net.Uri
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.sfaxdroid.app.ZipUtils
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.DeviceManager
import com.sfaxdroid.base.FileManager
import com.thin.downloadmanager.DefaultRetryPolicy
import com.thin.downloadmanager.DownloadManager
import com.thin.downloadmanager.DownloadRequest
import com.thin.downloadmanager.DownloadStatusListenerV1
import com.thin.downloadmanager.ThinDownloadManager
import java.io.File
import javax.inject.Named

class WordImgViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    fileManager: FileManager,
    deviceManager: DeviceManager,
    @Named("domain-url") var domainUrl: String
) :
    ViewModel(), DownloadStatusListenerV1 {

    var progressInfo = MutableLiveData<ProgressionInfo>()
    var progressValue = MutableLiveData<Pair<Int, Long>>()
    var isCompleted = MutableLiveData<Boolean>()

    private var downloadManager: ThinDownloadManager =
        ThinDownloadManager(Constants.DOWNLOAD_THREAD_POOL_SIZE)
    private var requestWallpaper: DownloadRequest? = null
    private var zipFile: File? = null
    private var zipDestination: File? = null
    private var backgroundFile: File? = null
    private var mDownloadId1 = 0
    private var mDownloadId2 = 0

    init {
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
            progressInfo.value = ProgressionInfo.IdOneCompleted(request.downloadContext.toString())
            unzipAndDeleteZip()
            mDownloadId2 = downloadManager.add(requestWallpaper)
        }

        if (id == mDownloadId2) {
            progressInfo.value = ProgressionInfo.IdTwoCompleted
            isCompleted.value = true
        }
    }

    override fun onDownloadFailed(
        downloadRequest: DownloadRequest?,
        errorCode: Int,
        errorMessage: String?
    ) {
        progressValue.value = Pair(0, 0)
        isCompleted.value = false
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

    sealed class ProgressionInfo {
        class IdOneCompleted(var info: String) : ProgressionInfo()
        object IdTwoCompleted : ProgressionInfo()
    }
}