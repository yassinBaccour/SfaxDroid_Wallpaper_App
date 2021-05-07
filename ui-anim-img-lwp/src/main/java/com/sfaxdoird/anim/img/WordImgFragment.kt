package com.sfaxdoird.anim.img

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.SharedPrefsUtils
import com.sfaxdroid.base.extension.changeDrawableButtonColor
import com.sfaxdroid.base.utils.Utils
import com.sfaxdroid.base.utils.Utils.Companion.getBytesDownloaded
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WordImgFragment : Fragment() {

    private lateinit var chooseColorBtn: Button
    private lateinit var fabBtn: Button
    private lateinit var toolbar: Toolbar
    private lateinit var progressInfoTxt: TextView
    private lateinit var downloadInfoTxt: TextView
    private lateinit var progressBar: ProgressBar

    private var isClickable = false

    private val viewModel: WordImgViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_word_img_lwp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chooseColorBtn = view.findViewById(R.id.button_choose_color)
        fabBtn = view.findViewById(R.id.fab)
        toolbar = view.findViewById(R.id.toolbar)
        progressInfoTxt = view.findViewById(R.id.progress_information)
        progressBar = view.findViewById(R.id.progress_bar_information)
        downloadInfoTxt = view.findViewById(R.id.download_status_information_text)
        initEventAndData()
    }

    private fun initEventAndData() {
        fabBtn.setOnClickListener { openLwp() }
        chooseColorBtn.setOnClickListener { chooseColor() }

        val screenName = requireArguments().getString(Constants.EXTRA_SCREEN_NAME)
        initToolbar(screenName)

        viewModel.progressInfo.observe(
            viewLifecycleOwner,
            {
                setProgressInformation(it)
            }
        )

        viewModel.isCompleted.observe(
            viewLifecycleOwner,
            {
                if (it) {
                    downloadInfoTxt.text =
                        getString(R.string.download_completed)
                    isClickable = it
                    fabBtn.isEnabled = it
                }
            }
        )

        viewModel.progressValue.observe(
            viewLifecycleOwner,
            {
                setProgressBytes(it.first, it.second)
            }
        )
    }

    private fun initToolbar(screeName: String?) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity?)?.supportActionBar?.apply {
            title = screeName
            setHomeButtonEnabled(false)
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
            .setPositiveButton(
                getString(R.string.btn_ok)
            ) { _: DialogInterface?, selectedColor: Int, _: Array<Int?>? ->

                val pref = SharedPrefsUtils(requireContext())
                pref.SetSetting(com.sfaxdroid.bases.Constants.WALLPAPER_COLOR, selectedColor)

                chooseColorBtn.changeDrawableButtonColor(
                    selectedColor,
                    ResourcesCompat.getDrawable(
                        resources,
                        com.sfaxdroid.base.R.mipmap.ic_palette,
                        requireContext().theme
                    )
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
            activity?.onBackPressed()
        }
        return super.onOptionsItemSelected(menuItem)
    }

    private fun openLwp() {
        if (isClickable) {
            Constants.ifBackgroundChanged = true
            Constants.nbIncrementationAfterChange = 0
            Utils.openLiveWallpaper<WordImgLiveWallpaper>(requireContext())
        }
    }

    private fun setProgressInformation(info: WordImgViewModel.ProgressionInfo) {
        progressInfoTxt.text = when (info) {
            is WordImgViewModel.ProgressionInfo.IdOneCompleted -> info.info + context?.getString(R.string.download_completed)
            is WordImgViewModel.ProgressionInfo.IdTwoCompleted -> context?.getString(R.string.download_terminated_sucessful)
        }
    }

    private fun setProgressBytes(progress: Int, byte: Long) {
        if (progress != 0) {
            progressBar.progress = progress
            progressInfoTxt.text = (
                "$progress%  " +
                    getBytesDownloaded(
                        progress,
                        byte
                    )
                )
        } else {
            progressBar.progress = 0
            progressInfoTxt.text = getString(R.string.failed_dwn)
        }
    }
}
