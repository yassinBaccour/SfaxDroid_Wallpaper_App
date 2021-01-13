package com.sfaxdoird.anim.img

import android.content.Context
import android.net.Uri
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.sfaxdroid.app.ZipUtils
import com.sfaxdroid.app.downloadsystem.*
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.utils.Utils
import java.io.File

class WordImgViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle
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

    fun downloadResource(context: Context) {
        val lwpFolder = com.sfaxdoird.anim.img.Utils.getTemporaryDirWithFolder(
            context,
            folder = com.sfaxdoird.anim.img.Constants.KEY_LWP_FOLDER_CONTAINER,
            context.getString(R.string.app_namenospace)
        )

        zipDestination = File(lwpFolder, Constants.ZIP_FOLDER_NAME)

        val requestZipFile =
            DownloadRequest(Uri.parse(if (Utils.isSmallScreen(context)) com.sfaxdoird.anim.img.Constants.URL_ZIP_FILE_MINI_PNG else com.sfaxdoird.anim.img.Constants.URL_ZIP_FILE_PNG))
                .setDestinationURI(Uri.parse(lwpFolder.toString() + "/" + com.sfaxdoird.anim.img.Constants.PNG_ZIP_FILE_NAME))
                .setPriority(DownloadRequest.Priority.LOW)
                .setRetryPolicy(DefaultRetryPolicy())
                .setDownloadContext("LWP Resources")
                .setStatusListener(this)

        requestWallpaper = DownloadRequest(
            Uri.parse(
                Utils.getUrlByScreenSize(
                    savedStateHandle.get<String>(Constants.EXTRA_URL_TO_DOWNLOAD)
                        .orEmpty(), context
                )
            )
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