package com.sfaxdoird.anim.word

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.sfaxdoird.anim.word.databinding.FragmentAnimWordBinding
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.extension.changeDrawableButtonColor
import com.sfaxdroid.base.extension.getDrawableWithTheme
import com.sfaxdroid.base.extension.setCompoundDrawableFromId
import com.sfaxdroid.base.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AnimWord2dFragment : Fragment(R.layout.fragment_anim_word) {

    private var isClickable = false
    private var fontButtonList = arrayListOf<TextView>()
    private var buttonSizeList = arrayListOf<Button>()
    private lateinit var binding: FragmentAnimWordBinding
    private val viewModel: AnimWord2dViewModel by viewModels()

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
                binding.progressBarInformation.progress = it.first
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
                    binding.btnOpenLwp.isEnabled = isCompleted
                    isClickable = isCompleted
                    binding.btnChangeColor.changeDrawableButtonColor(
                        color,
                        requireContext().getDrawableWithTheme(com.sfaxdroid.base.R.mipmap.ic_palette)
                    )
                    setSizeDrawable(size)
                    setFontDrawable(style)
                    binding.progressBarInformation.visibility =
                        if (isCompleted) View.INVISIBLE else View.VISIBLE
                }
            }
        }
    }


    private fun setOnclickToALlButton() {
        binding.btnChangeColor.setOnClickListener { chooseColor() }
        binding.btnOpenLwp.setOnClickListener { viewModel.submitAction(AnimWorldAction.OpenLiveWallpaper) }
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
            binding.buttonSizeSmall,
            binding.buttonSizeMeduim,
            binding.buttonSizeBig,
            binding.buttonSizeFullScreen
        )
        resetBtnSizeBackground()
    }

    private fun setTextViewTypeFace() {
        fontButtonList = arrayListOf(
            binding.txtFont1,
            binding.txtFont2,
            binding.txtFont3,
            binding.txtFont4,
            binding.txtFont5,
            binding.txtFont6,
            binding.txtFont7,
            binding.txtFont8
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
        binding.buttonSizeSmall.setCompoundTopDrawables(requireContext().getDrawableWithTheme(R.mipmap.ic_size_small))
        binding.buttonSizeMeduim.setCompoundTopDrawables(requireContext().getDrawableWithTheme(R.mipmap.ic_size_meduim))
        binding.buttonSizeBig.setCompoundTopDrawables(requireContext().getDrawableWithTheme(R.mipmap.ic_size_big))
        binding.buttonSizeFullScreen.setCompoundTopDrawables(requireContext().getDrawableWithTheme(R.mipmap.ic_size_full))
    }

    private fun initToolbar() {
        val screenName = requireArguments().getString(Constants.EXTRA_SCREEN_NAME)
        (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbar)
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
        if (isClickable) {
            Constants.ifBackgroundChanged = true
            Constants.nbIncrementationAfterChange = 0
            Utils.openLiveWallpaper<AnimWord2dWallpaper>(requireContext())
        }
    }
}
