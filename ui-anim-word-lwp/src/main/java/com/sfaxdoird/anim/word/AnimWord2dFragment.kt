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
import androidx.lifecycle.lifecycleScope
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.extension.changeDrawableButtonColor
import com.sfaxdroid.base.extension.getDrawableWithTheme
import com.sfaxdroid.base.extension.setCompoundDrawableFromId
import com.sfaxdroid.base.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_anim_word.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AnimWord2dFragment : Fragment() {

    private var toolbar: Toolbar? = null
    private var clickable = false
    private var fontButtonList = arrayListOf<TextView>()
    private var buttonSizeList = arrayListOf<Button>()

    private val viewModel: AnimWord2dViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_anim_word, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEventAndData()
    }

    private fun initEventAndData() {
        initToolbar()
        setTextViewTypeFace()
        setDefaultSize()
        setOnclickToALlButton()

        //TODO MutableSharedFlow
        viewModel.progressValue.observe(
            viewLifecycleOwner,
            {
                progress_bar_information?.progress = it.first
            }
        )

        lifecycleScope.launchWhenStarted {
            viewModel.uiEffects.collect {
                when (it) {
                    OpenLiveWallpaper -> openLiveWallpapers()
                    Retry -> Utils.showAlert(requireContext(), ::retryDownload)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.uiState.collect {
                it.apply {
                    btnOpenLwp?.isEnabled = isCompleted
                    clickable = isCompleted
                    btnChangeColor.changeDrawableButtonColor(
                        color,
                        requireContext().getDrawableWithTheme(com.sfaxdroid.base.R.mipmap.ic_palette)
                    )
                    setSizeDrawable(size)
                    setFontDrawable(style)
                    progress_bar_information?.visibility =
                        if (isCompleted) View.INVISIBLE else View.VISIBLE
                }
            }
        }
    }


    private fun setOnclickToALlButton() {
        btnChangeColor?.setOnClickListener { chooseColor() }
        btnOpenLwp.setOnClickListener { viewModel.submitAction(AnimWorldAction.OpenLiveWallpaper) }
        fontButtonList.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                viewModel.submitAction(AnimWorldAction.ChangeFont(index, textView))
            }
        }
        buttonSizeList.forEachIndexed { index, button ->
            button.setOnClickListener {
                viewModel.submitAction(AnimWorldAction.ChangeSize(index, button))
            }
        }
    }

    private fun retryDownload() {
        viewModel.submitAction(AnimWorldAction.DownloadData)
    }

    private fun getDrawableBySize(size: Int): Int {
        return when (size) {
            0 -> R.mipmap.ic_size_small_on
            1 -> R.mipmap.ic_size_meduim_on
            2 -> R.mipmap.ic_size_big_on
            3 -> R.mipmap.ic_size_full_on
            else -> R.mipmap.ic_size_small_on
        }
    }

    private fun setButtonDrawable(button: Button?, size: Int) {
        button?.setCompoundDrawableFromId(getDrawableBySize(size))
    }

    private fun setDefaultSize() {
        buttonSizeList = arrayListOf(
            buttonSizeSmall,
            buttonSizeMeduim,
            buttonSizeBig,
            buttonSizeFullScreen
        )
        resetBtnSizeBackground()
    }

    private fun setTextViewTypeFace() {
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


    private fun setFontDrawable(style: Int) {
        resetTextViewBackground()
        fontButtonList[style].setTextColor(Color.GREEN)
    }

    private fun setSizeDrawable(size: Int) {
        resetBtnSizeBackground()
        setButtonDrawable(buttonSizeList.getOrNull(size), size)
    }

    private fun resetBtnSizeBackground() {
        buttonSizeSmall.setCompoundTopDrawables(requireContext().getDrawableWithTheme(R.mipmap.ic_size_small))
        buttonSizeMeduim.setCompoundTopDrawables(requireContext().getDrawableWithTheme(R.mipmap.ic_size_meduim))
        buttonSizeBig.setCompoundTopDrawables(requireContext().getDrawableWithTheme(R.mipmap.ic_size_big))
        buttonSizeFullScreen.setCompoundTopDrawables(requireContext().getDrawableWithTheme(R.mipmap.ic_size_full))
    }

    private fun initToolbar() {
        val screenName = requireArguments().getString(Constants.EXTRA_SCREEN_NAME)
        toolbar = view?.findViewById(R.id.toolbar)
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
                viewModel.submitAction(AnimWorldAction.ChangeColor(selectedColor))
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
