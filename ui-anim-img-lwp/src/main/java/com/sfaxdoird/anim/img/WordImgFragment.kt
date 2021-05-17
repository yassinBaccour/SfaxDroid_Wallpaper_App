package com.sfaxdoird.anim.img

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.sfaxdoird.anim.img.databinding.FragmentWordImgLwpBinding
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.extension.changeDrawableButtonColor
import com.sfaxdroid.base.utils.Utils
import com.sfaxdroid.base.utils.Utils.Companion.getBytesDownloaded
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WordImgFragment : Fragment(R.layout.fragment_word_img_lwp) {

    private val viewModel: WordImgViewModel by viewModels()
    private lateinit var binding: FragmentWordImgLwpBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWordImgLwpBinding.bind(view)
        initEventAndData()
    }

    private fun initEventAndData() {

        val screenName = requireArguments().getString(Constants.EXTRA_SCREEN_NAME)
        initToolbar(screenName)

        binding.fab.setOnClickListener { viewModel.submitAction(AnimImgAction.OpenLiveWallpaper) }
        binding.buttonChooseColor.setOnClickListener { chooseColor() }

        //TODO find a solution to create a counter with MutableStateFlow
        viewModel.progressValue.observe(
            viewLifecycleOwner,
            {
                setProgressBytes(it.first, it.second)
            }
        )

        lifecycleScope.launch {
            viewModel.uiState.collect {
                it.apply {
                    binding.fab.isEnabled = isCompleted
                    binding.downloadStatusInformationSubtitle.text =
                        if (isCompleted) getString(R.string.download_completed) else getString(R.string.download_resource_txt_witing)
                    binding.buttonChooseColor.changeDrawableButtonColor(
                        color,
                        ResourcesCompat.getDrawable(
                            resources,
                            com.sfaxdroid.base.R.mipmap.ic_palette,
                            requireContext().theme
                        )
                    )
                    setProgressInformation(progressionInfo)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.uiEffects.collect {
                when (it) {
                    AnimImgEffect.GoToLiveWallpaper -> {
                        openLwp()
                    }
                }
            }
        }
    }

    private fun initToolbar(screeName: String?) {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
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
                viewModel.submitAction(AnimImgAction.ChangeColor(selectedColor))
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
        Constants.ifBackgroundChanged = true
        Constants.nbIncrementationAfterChange = 0
        Utils.openLiveWallpaper<WordImgLiveWallpaper>(requireContext())
    }

    private fun setProgressInformation(info: ProgressionInfo) {
        binding.progressInformation.text = when (info) {
            is ProgressionInfo.IdOneCompleted -> context?.getString(R.string.download_resource_completed)
            is ProgressionInfo.IdTwoCompleted -> context?.getString(R.string.download_terminated_sucessful)
            ProgressionInfo.Idle -> ""
        }
    }

    private fun setProgressBytes(progress: Int, byte: Long) {
        if (progress != 0) {
            binding.progressBarInformation.progress = progress
            binding.progressInformation.text = (
                    "$progress%  " +
                            getBytesDownloaded(
                                progress,
                                byte
                            )
                    )
        } else {
            binding.progressBarInformation.progress = 0
            binding.progressInformation.text = getString(R.string.failed_dwn)
        }
    }
}
