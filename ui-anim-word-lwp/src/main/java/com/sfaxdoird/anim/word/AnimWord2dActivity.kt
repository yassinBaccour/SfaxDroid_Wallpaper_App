package com.sfaxdoird.anim.word

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.sfaxdroid.app.downloadsystem.*
import com.sfaxdroid.base.*
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.Utils.Companion.getBytesDownloaded
import kotlinx.android.synthetic.main.activity_anim_word.*
import java.io.File

class AnimWord2dActivity : SimpleActivity(), DownloadStatusListenerV1 {

    private var toolbar: Toolbar? = null
    private var clickable = false
    private var downloadId2 = 0
    private var backgroundFile: File? = null
    private val downloadManager =
        ThinDownloadManager(Constants.DOWNLOAD_THREAD_POOL_SIZE)

    private var fontButtonList = listOf<TextView>(
        txtfont1,
        txtfont2,
        txtfont3,
        txtfont4,
        txtfont5,
        txtfont6,
        txtfont7,
        txtfont8
    )

    private var buttonSizeList = listOf<Button>(
        buttonSizeSmall,
        buttonSizeMeduim,
        buttonSizeBig,
        buttonSizeFullScreen
    )

    var pref: SharedPrefsUtils? = null

    override val layout: Int
        get() = R.layout.activity_anim_word

    override fun initEventAndData() {

        pref = SharedPrefsUtils(this)
        toolbar = findViewById(R.id.toolbar)

        setTextViewTypeFace()
        initSizeListener()
        initTextViewListener()
        initToolbar()
        initTextSize()

        buttonColor?.setOnClickListener { chooseColor() }
        fab.setOnClickListener { openLiveWallpapers() }

        val filesDir = getExternalFilesDir("")
        startDownload(filesDir!!)
        backgroundFile =
            File(filesDir, Constants.DOUA_PNG_BACKFROUND_FILE_NAME)
        backgroundFile?.delete()?.takeIf { !it }.also {
            Utils.showSnackMessage(rootLayout, "Error deleteing temps file")
        }
    }

    private fun startDownload(file: File) {
        val requestWallpaper =
            DownloadRequest(
                Uri.parse(intent.getStringExtra(Constants.URL_TO_DOWNLOAD)
                    .orEmpty()
                    .apply {
                        if (Utils.isSmallScreen(this@AnimWord2dActivity)
                        ) {
                            replace("islamicimages", "islamicimagesmini")
                        }
                    })
            )
                .setDestinationURI(
                    Uri.parse(
                        file
                            .toString() + "/" + Constants.DOUA_PNG_BACKFROUND_FILE_NAME
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

    private fun getDrawableBySize(): Int {
        return when (pref?.GetSetting("2dwallpapertextsize", 1)) {
            1 -> R.mipmap.ic_size_small_on
            2 -> R.mipmap.ic_size_meduim_on
            3 -> R.mipmap.ic_size_big_on
            4 -> R.mipmap.ic_size_full_on
            else -> R.mipmap.ic_size_small_on
        }
    }

    private fun setButtonDrawable(button: Button?) {
        button?.setCompoundDrawablesWithIntrinsicBounds(
            null,
            resources.getDrawable(getDrawableBySize()), null, null
        )
    }

    private fun initTextSize() {
        buttonSizeList.forEach {
            setButtonDrawable(it)
        }
    }

    private fun setTextViewTypeFace() {
        fontButtonList.forEachIndexed { index, button ->
            button.typeface = Typeface.createFromAsset(assets, "arabicfont$index.otf")
        }
    }

    private fun resetTextViewBackground() {
        fontButtonList.forEach { it.setTextColor(Color.WHITE) }
    }

    private fun initTextViewListener() {
        fontButtonList.forEachIndexed { index, button ->
            button.setOnClickListener {
                saveFontStyle(index, button)
            }
        }
    }

    private fun saveFontStyle(style: Int, textView: TextView) {
        pref?.SetSetting("2dwallpaperfontstyle", style)
        resetTextViewBackground()
        textView.setTextColor(Color.GREEN)
    }

    private fun saveTextSize(size: Int, button: Button?) {
        pref!!.SetSetting("2dwallpapertextsize", size)
        resetBtnSizeBackground()
        setButtonDrawable(button)
    }

    private fun initSizeListener() {
        buttonSizeList.forEachIndexed { index, button ->
            saveTextSize(index, button)
        }
    }

    private fun resetBtnSizeBackground() {
        buttonSizeSmall.setCompoundTopDrawables(getDrawableByVersion(R.mipmap.ic_size_small))
        buttonSizeMeduim.setCompoundTopDrawables(getDrawableByVersion(R.mipmap.ic_size_meduim))
        buttonSizeBig.setCompoundTopDrawables(getDrawableByVersion(R.mipmap.ic_size_big))
        buttonSizeFullScreen.setCompoundTopDrawables(getDrawableByVersion(R.mipmap.ic_size_full))
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            supportActionBar?.title = getString(R.string.title_word_anim_lwp)
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun chooseColor() {
        ColorPickerDialogBuilder
            .with(this)
            .setTitle(getString(R.string.choose_color))
            .initialColor(Color.BLUE)
            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
            .density(12)
            .setOnColorSelectedListener { }
            .setPositiveButton(
                getString(R.string.btn_ok)
            ) { _: DialogInterface?, selectedColor: Int, _: Array<Int?>? ->
                pref!!.SetSetting("2dwallpaperColor", selectedColor)
                BitmapUtils.changeDrawableButtonColor(buttonColor, this, R.mipmap.ic_palette)
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

    private fun openLiveWallpapers() {
        if (clickable) {
            backgroundFile = null
            Constants.ifBackgroundChanged = true
            Constants.nbIncrementationAfterChange = 0
            Utils.openLiveWallpaper<AnimWord2dWallpaper>(this)
        } else {
            Utils.showSnackMessage(rootLayout, getString(R.string.waiting_downlaod))
        }
    }

    private fun setProgressView(progress: Int, byte: Long) {
        if (progress != 0) {
            progress1?.progress = progress
            progressTxt?.text = ("$progress%  "
                    + getBytesDownloaded(
                progress,
                byte
            ))
        } else {
            progress1?.progress = 0
            progressTxt?.text = getString(R.string.failed_dwn)
        }

    }

    override fun onDownloadComplete(request: DownloadRequest) {
        fab?.isEnabled = true
        progressTxt?.text = getString(R.string.download_terminated_sucessful)
        txtstatusDownload?.text = getString(R.string.download_completed)
        clickable = true
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

}