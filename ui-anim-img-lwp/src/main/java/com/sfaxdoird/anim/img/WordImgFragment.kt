package com.sfaxdoird.anim.img

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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
import kotlinx.android.synthetic.main.fragment_word_img_lwp.*

@AndroidEntryPoint
class WordImgFragment : Fragment() {

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
        initEventAndData()
    }

    private fun initEventAndData() {
        fab?.setOnClickListener { openLwp() }
        button_choose_color?.setOnClickListener { chooseColor() }

        val screenName = requireArguments().getString(Constants.EXTRA_SCREEN_NAME)
        initToolbar(screenName)

        viewModel.progressInfo.observe(viewLifecycleOwner, {
            setProgressInformation(it)
        })

        viewModel.isCompleted.observe(viewLifecycleOwner, {
            if (it) {
                download_status_information_text?.text = getString(R.string.download_completed)
                isClickable = it
                fab?.isEnabled = it
            }
        })

        viewModel.progressValue.observe(viewLifecycleOwner, {
            setProgressBytes(it.first, it.second)
        })
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

                button_choose_color.changeDrawableButtonColor(
                    selectedColor, resources.getDrawable(com.sfaxdroid.base.R.mipmap.ic_palette)
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
        progress_information?.text = when (info) {
            is WordImgViewModel.ProgressionInfo.IdOneCompleted -> info.info + context?.getString(R.string.download_completed)
            is WordImgViewModel.ProgressionInfo.IdTwoCompleted -> context?.getString(R.string.download_terminated_sucessful)
        }
    }

    private fun setProgressBytes(progress: Int, byte: Long) {
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