package com.sfaxdoird.anim.word

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.sfaxdroid.app.downloadsystem.*
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.SharedPrefsUtils
import com.sfaxdroid.base.utils.BitmapUtils
import com.sfaxdroid.base.utils.Utils
import com.sfaxdroid.base.utils.Utils.Companion.getBytesDownloaded
import kotlinx.android.synthetic.main.fragment_anim_word.*
import java.io.File


class AnimWord2dFragment : Fragment(), DownloadStatusListenerV1 {

    private var toolbar: Toolbar? = null
    private var clickable = false
    private var downloadId2 = 0
    private var backgroundFile: File? = null
    private val downloadManager =
        ThinDownloadManager(Constants.DOWNLOAD_THREAD_POOL_SIZE)

    private var fontButtonList = arrayListOf<TextView>()

    private var buttonSizeList = arrayListOf<Button>()

    var pref: SharedPrefsUtils? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_anim_word, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEventAndData()
    }

    private fun initEventAndData() {

        pref = SharedPrefsUtils(requireContext())
        toolbar = view?.findViewById(R.id.toolbar)

        fontButtonList = arrayListOf(
            txtfont1,
            txtfont2,
            txtfont3,
            txtfont4,
            txtfont5,
            txtfont6,
            txtfont7,
            txtfont8
        )

        buttonSizeList = arrayListOf(
            buttonSizeSmall,
            buttonSizeMeduim,
            buttonSizeBig,
            buttonSizeFullScreen
        )

        setTextViewTypeFace()
        initSizeListener()
        initTextViewListener()
        initToolbar()
        initTextSize()

        buttonColor?.setOnClickListener { chooseColor() }
        fab.setOnClickListener { openLiveWallpapers() }

        val filesDir = requireContext().getExternalFilesDir("")
        startDownload(filesDir!!)
        backgroundFile =
            File(filesDir, Constants.PNG_BACKFROUND_FILE_NAME)
        backgroundFile?.delete()?.takeIf { !it }.also {
            Utils.showSnackMessage(rootLayout, "Error deleteing temps file")
        }
    }

    private fun startDownload(file: File) {
        val requestWallpaper =
            DownloadRequest(
                Uri.parse(arguments?.getString(Constants.EXTRA_URL_TO_DOWNLOAD, "") ?: ""
                    .apply {
                        if (Utils.isSmallScreen(requireContext())
                        ) {
                            replace("islamicimages", "islamicimagesmini")
                        }
                    })
            )
                .setDestinationURI(
                    Uri.parse(
                        file
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
            val ind = index + 1
            try {
                button.typeface =
                    Typeface.createFromAsset(requireContext().assets, "arabicfont$ind.otf")
            } catch (e: Exception) {
                button.typeface =
                    Typeface.createFromAsset(requireContext().assets, "arabicfont$ind.ttf")
            }
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
        pref?.SetSetting("2dwallpapertextsize", size)
        resetBtnSizeBackground()
        setButtonDrawable(button)
    }

    private fun initSizeListener() {
        buttonSizeList.forEachIndexed { index, button ->
            saveTextSize(index, button)
        }
    }

    private fun resetBtnSizeBackground() {
        buttonSizeSmall.setCompoundTopDrawables(requireContext().getDrawableByVersion(R.mipmap.ic_size_small))
        buttonSizeMeduim.setCompoundTopDrawables(requireContext().getDrawableByVersion(R.mipmap.ic_size_meduim))
        buttonSizeBig.setCompoundTopDrawables(requireContext().getDrawableByVersion(R.mipmap.ic_size_big))
        buttonSizeFullScreen.setCompoundTopDrawables(requireContext().getDrawableByVersion(R.mipmap.ic_size_full))
    }

    private fun initToolbar() {
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        (activity as AppCompatActivity?)?.supportActionBar?.apply {
            title = getString(R.string.title_word_anim_lwp)
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun chooseColor() {
        ColorPickerDialogBuilder
            .with(requireContext())
            .setTitle(getString(R.string.choose_color))
            .initialColor(Color.BLUE)
            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
            .density(12)
            .setOnColorSelectedListener { }
            .setPositiveButton(
                getString(R.string.btn_ok)
            ) { _: DialogInterface?, selectedColor: Int, _: Array<Int?>? ->
                pref!!.SetSetting(com.sfaxdroid.bases.Constants.WALLPAPER_COLOR, selectedColor)
                BitmapUtils.changeDrawableButtonColor(
                    buttonColor,
                    requireContext(),
                    R.mipmap.ic_palette
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
            requireActivity().onBackPressed()
        }
        return super.onOptionsItemSelected(menuItem)
    }

    private fun openLiveWallpapers() {
        if (clickable) {
            backgroundFile = null
            Constants.ifBackgroundChanged = true
            Constants.nbIncrementationAfterChange = 0
            Utils.openLiveWallpaper<AnimWord2dWallpaper>(requireContext())
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