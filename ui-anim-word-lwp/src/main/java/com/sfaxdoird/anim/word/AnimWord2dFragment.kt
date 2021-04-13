package com.sfaxdoird.anim.word

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.SharedPrefsUtils
import com.sfaxdroid.base.extension.changeDrawableButtonColor
import com.sfaxdroid.base.extension.getDrawableWithTheme
import com.sfaxdroid.base.extension.setCompoundDrawableFromId
import com.sfaxdroid.base.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_anim_word.*

@AndroidEntryPoint
class AnimWord2dFragment : Fragment() {

    private var toolbar: Toolbar? = null
    private var clickable = false
    private var fontButtonList = arrayListOf<TextView>()
    private var buttonSizeList = arrayListOf<Button>()
    var pref: SharedPrefsUtils? = null

    private val viewModel: AnimWord2dViewModel by viewModels()

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

        val screenName = requireArguments().getString(Constants.EXTRA_SCREEN_NAME)

        setTextViewTypeFace()
        initSizeListener()
        initTextViewListener()
        initToolbar(screenName)
        initTextSize()

        buttonColor?.setOnClickListener { chooseColor() }
        fab.setOnClickListener { openLiveWallpapers() }

        viewModel.isCompleted.observe(viewLifecycleOwner, {
            if (it) {
                fab?.isEnabled = true
                clickable = true
            } else {
                Utils.showAlert(requireContext(), ::retryDownload)
            }
        })

        viewModel.progressValue.observe(viewLifecycleOwner, {
            setProgressBytes(it.first, it.second)
        })

    }

    private fun setProgressBytes(progress: Int, byte: Long) {
        if (progress == 100) {
            progress_bar_information?.visibility = View.INVISIBLE
        }
        if (progress != 0) {
            progress_bar_information?.progress = progress
        } else {
            progress_bar_information?.progress = 0
        }
    }

    private fun retryDownload() {
        viewModel.load()
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
        button?.setCompoundDrawableFromId(getDrawableBySize())
    }

    private fun initTextSize() {
        resetBtnSizeBackground()
        val textSize = pref?.GetSetting("2dwallpapertextsize", 1) ?: 2
        setButtonDrawable(buttonSizeList[textSize - 1])
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

        val fontStyle = pref?.GetSetting("2dwallpaperfontstyle", 1) ?: 2
        fontButtonList[fontStyle - 1].setTextColor(Color.GREEN)
    }

    private fun saveFontStyle(style: Int, textView: TextView) {
        pref?.SetSetting("2dwallpaperfontstyle", style + 1)
        resetTextViewBackground()
        textView.setTextColor(Color.GREEN)
    }

    private fun saveTextSize(size: Int, button: Button?) {
        pref?.SetSetting("2dwallpapertextsize", size + 1)
        resetBtnSizeBackground()
        setButtonDrawable(button)
    }

    private fun initSizeListener() {
        buttonSizeList.forEachIndexed { index, button ->
            button.setOnClickListener {
                saveTextSize(index, button)
            }
        }
    }

    private fun resetBtnSizeBackground() {
        buttonSizeSmall.setCompoundTopDrawables(requireContext().getDrawableWithTheme(R.mipmap.ic_size_small))
        buttonSizeMeduim.setCompoundTopDrawables(requireContext().getDrawableWithTheme(R.mipmap.ic_size_meduim))
        buttonSizeBig.setCompoundTopDrawables(requireContext().getDrawableWithTheme(R.mipmap.ic_size_big))
        buttonSizeFullScreen.setCompoundTopDrawables(requireContext().getDrawableWithTheme(R.mipmap.ic_size_full))
    }

    private fun initToolbar(screenName: String?) {
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        (activity as AppCompatActivity?)?.supportActionBar?.apply {
            title = screenName
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
                pref?.SetSetting(com.sfaxdroid.bases.Constants.WALLPAPER_COLOR, selectedColor)
                buttonColor.changeDrawableButtonColor(
                    selectedColor,
                    requireContext().getDrawableWithTheme(com.sfaxdroid.base.R.mipmap.ic_palette)
                )
            }
            .setNegativeButton(
                getString(R.string.btn_cancel)
            ) { _: DialogInterface?, _: Int -> }
            .build()
            .show()
    }

    private fun openLiveWallpapers() {
        if (clickable) {
            Constants.ifBackgroundChanged = true
            Constants.nbIncrementationAfterChange = 0
            Utils.openLiveWallpaper<AnimWord2dWallpaper>(requireContext())
        }
    }


}