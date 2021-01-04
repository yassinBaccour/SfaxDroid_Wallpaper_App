package com.sfaxdoird.anim.img

import android.content.DialogInterface
import android.graphics.Color
import android.net.Uri
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.sfaxdoird.anim.img.Utils.getTemporaryDir
import com.sfaxdroid.app.ZipUtils.Companion.unzipFile
import com.sfaxdroid.app.downloadsystem.*
import com.sfaxdroid.base.utils.BitmapUtils
import com.sfaxdroid.base.SharedPrefsUtils
import com.sfaxdroid.base.SimpleActivity
import com.sfaxdroid.base.utils.Utils
import com.sfaxdroid.base.utils.Utils.Companion.getBytesDownloaded
import kotlinx.android.synthetic.main.activity_word_img_lwp.*
import java.io.File

class WordImgActivity : SimpleActivity(), DownloadStatusListenerV1 {

    private var downloadManager: ThinDownloadManager =
        ThinDownloadManager(com.sfaxdroid.base.Constants.DOWNLOAD_THREAD_POOL_SIZE)

    private var requestWallpaper: DownloadRequest? = null

    private var zipFile: File? = null
    private var zipDestination: File? = null
    private var backgroundFile: File? = null

    private var mDownloadId1 = 0
    private var mDownloadId2 = 0

    private var isClickable = false


    override val layout: Int
        get() = R.layout.activity_word_img_lwp

    override fun initEventAndData() {

        fab?.setOnClickListener { openLwp() }
        button_choose_color?.setOnClickListener { chooseColor() }

        initToolbar()

        val lwpFolder = getTemporaryDir(
            this,
            Constants.KEY_LWP_FOLDER_CONTAINER,
            getString(R.string.app_namenospace)
        )

        zipFile = File(lwpFolder, Constants.PNG_ZIP_FILE_NAME)

        backgroundFile = File(lwpFolder, com.sfaxdroid.base.Constants.PNG_BACKFROUND_FILE_NAME)
        zipDestination = File(lwpFolder, com.sfaxdroid.base.Constants.ZIP_FOLDER_NAME)

        val requestZipFile =
            DownloadRequest(Uri.parse(if (Utils.isSmallScreen(this)) Constants.URL_ZIP_FILE_MINI_PNG else Constants.URL_ZIP_FILE_PNG))
                .setDestinationURI(Uri.parse(lwpFolder.toString() + "/" + Constants.PNG_ZIP_FILE_NAME))
                .setPriority(DownloadRequest.Priority.LOW)
                .setRetryPolicy(DefaultRetryPolicy())
                .setDownloadContext("LWP Resources")
                .setStatusListener(this)

        requestWallpaper = DownloadRequest(
            Uri.parse(
                Utils.getUrlByScreenSize(
                    intent.getStringExtra(com.sfaxdroid.base.Constants.EXTRA_URL_TO_DOWNLOAD)
                        .orEmpty(), this@WordImgActivity
                )
            )
        )
            .setDestinationURI(Uri.parse(lwpFolder.toString() + "/" + com.sfaxdroid.base.Constants.PNG_BACKFROUND_FILE_NAME))
            .setPriority(DownloadRequest.Priority.LOW)
            .setRetryPolicy(DefaultRetryPolicy())
            .setDownloadContext("Doua LWP Background")
            .setStatusListener(this)

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

    private fun initToolbar() {
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.apply {
            title = getString(R.string.title_word_anim_lwp)
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun chooseColor() {
        ColorPickerDialogBuilder
            .with(this)
            .setTitle(getString(R.string.choose_color))
            .initialColor(Color.BLUE)
            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
            .density(12)
            .setPositiveButton(
                getString(R.string.btn_ok)
            ) { _: DialogInterface?, selectedColor: Int, _: Array<Int?>? ->

                val pref = SharedPrefsUtils(this)
                pref.SetSetting(com.sfaxdroid.bases.Constants.WALLPAPER_COLOR, selectedColor)

                BitmapUtils.changeDrawableButtonColor(
                    button_choose_color,
                    this@WordImgActivity,
                    selectedColor
                )

            }
            .setNegativeButton(
                getString(R.string.btn_cancel)
            ) { _: DialogInterface?, _: Int -> }
            .build()
            .show()
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(menuItem)
    }

    private fun openLwp() {
        if (isClickable) {
            zipFile = null
            zipDestination = null
            backgroundFile = null
            com.sfaxdroid.base.Constants.ifBackgroundChanged = true
            com.sfaxdroid.base.Constants.nbIncrementationAfterChange = 0
            Utils.openLiveWallpaper<WordImgLiveWallpaper>(this)
        } else {
            Utils.showSnackMessage(rootLayout, getString(R.string.download_resource_txt_witing))
        }
    }

    private fun unzipAndDeleteZip() {
        unzipFile(zipFile!!, zipDestination!!)
        if (zipFile?.exists() == true) {
            backgroundFile?.delete()
        }
    }

    override fun onDownloadComplete(request: DownloadRequest) {

        val id = request.downloadId

        if (id == mDownloadId1) {
            setProgressInformation(request.downloadContext.toString() + getString(R.string.download_completed))
            unzipAndDeleteZip()
            isClickable = true
            mDownloadId2 = downloadManager.add(requestWallpaper)
        }

        if (id == mDownloadId2) {
            fab?.isEnabled = true
            setProgressInformation(getString(R.string.download_terminated_sucessful))
            download_status_information_text?.text = getString(R.string.download_completed)
            isClickable = true
        }

    }

    private fun setProgressInformation(info: String) {
        progress_information?.text = info
    }

    override fun onDownloadFailed(
        request: DownloadRequest,
        errorCode: Int,
        errorMessage: String
    ) {
        setProgressView(0, 0)
    }

    override fun onProgress(
        request: DownloadRequest,
        totalBytes: Long,
        downloadedBytes: Long,
        progress: Int
    ) {
        setProgressView(progress, totalBytes)
    }

    private fun setProgressView(progress: Int, byte: Long) {
        if (progress != 0) {
            progress_bar_information?.progress = progress
            progress_information?.text = ("$progress%  "
                    + getBytesDownloaded(
                progress,
                byte
            ))
        } else {
            progress_bar_information?.progress = 0
            progress_information?.text = getString(R.string.failed_dwn)
        }

    }

}