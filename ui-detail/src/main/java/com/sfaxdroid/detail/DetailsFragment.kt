package com.sfaxdroid.detail

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.DeviceManager
import com.sfaxdroid.base.FileManager
import com.sfaxdroid.base.extension.getFileName
import com.sfaxdroid.base.extension.loadUrlWithAction
import com.sfaxdroid.base.utils.Utils
import com.sfaxdroid.detail.databinding.ActivityDetailsBinding
import com.sfaxdroid.detail.utils.DetailUtils
import com.sfaxdroid.detail.utils.DetailUtils.Companion.decodeBitmapAndSetAsLiveWallpaper
import com.sfaxdroid.detail.utils.DetailUtils.Companion.saveToFileToTempsDirAndChooseAction
import com.sfaxdroid.detail.utils.DetailUtils.Companion.setWallpaper
import com.sfaxdroid.detail.utils.TouchImageView
import com.soundcloud.android.crop.Crop
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.reflect.Method
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.activity_details) {

    private var from: String = ""
    private var currentUrl: String = ""
    private var fromRipple = false
    private var isFullScreen = false

    private var bottomSheetBehavior: BottomSheetBehavior<*>? = null

    @Inject
    lateinit var fileManager: FileManager

    @Inject
    lateinit var deviceManager: DeviceManager

    @Inject
    @Named("app-id")
    lateinit var appId: String

    private lateinit var binding: ActivityDetailsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ActivityDetailsBinding.bind(view)
        initToolbar()
        initFabButtonAndBottomSheet()
        checkPermission()
        showLoading()
        loadWallpaper()
        addStrictMode()
        hideSystemUI()
    }

    private fun hideSystemUI() {
        isFullScreen = requireArguments().getBoolean(Constants.KEY_IS_FULL_SCREEN, false)
        if (isFullScreen)
            requireActivity().window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    private fun addStrictMode() {
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                val m: Method = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
                m.invoke(null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        currentUrl = requireArguments().getString(Constants.EXTRA_IMG_URL).orEmpty()
        from = arguments?.getString(Constants.KEY_LWP_NAME).orEmpty()
    }

    private fun loadWallpaper() {
        val detailImage: TouchImageView = binding.imgWallpaper
        detailImage.loadUrlWithAction(currentUrl, ::hideLoading)
        detailImage.scaleType = ImageView.ScaleType.CENTER_CROP
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            onRequestPermissions()
        }
    }

    private fun initToolbar() {
        (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity?)?.supportActionBar?.apply {
            title = ""
            setHomeButtonEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun saveTempsDorAndDoAction(
        actionToDo: ActionTypeEnum,
        url: String
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            onSaveTempsDorAndDoAction(
                saveFile(url),
                actionToDo
            )
        }
    }

    private suspend fun saveFile(url: String) = withContext(Dispatchers.IO) {
        fileManager.getTemporaryDirWithFile(url.getFileName()).exists()
    }

    private fun fabClick() {
        if (from.isNotEmpty()) {
            saveAndGoToLiveWallpaper()
        } else {
            showMenuSheet()
        }
    }

    private fun saveAndGoToLiveWallpaper() {
        when (from) {
            Constants.KEY_ADD_TIMER_LWP -> saveTempsDorAndDoAction(
                ActionTypeEnum.MovePerDir,
                currentUrl
            )

            Constants.KEY_ADDED_LIST_TIMER_LWP -> saveTempsDorAndDoAction(
                ActionTypeEnum.Delete,
                currentUrl
            )

            Constants.KEY_RIPPLE_LWP -> saveTempsDorAndDoAction(
                ActionTypeEnum.SendLwp,
                currentUrl
            )
        }
    }

    private fun initFabButtonAndBottomSheet() {
        if (from.isNotEmpty()) {
            when (from) {
                Constants.KEY_ADD_TIMER_LWP -> binding.buttonFab.setImageResource(R.mipmap.ic_download)
                Constants.KEY_ADDED_LIST_TIMER_LWP -> binding.buttonFab.setImageResource(R.mipmap.ic_delete)
                Constants.KEY_RIPPLE_LWP -> binding.buttonFab.setImageResource(R.mipmap.ic_ripple_fab)
            }
        }
        binding.buttonFab.setOnClickListener { fabClick() }

        bottomSheetBehavior =
            BottomSheetBehavior.from<View>(binding.sheetView.root)
        bottomSheetBehavior?.apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            peekHeight = 0
            isHideable = true
            addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_EXPANDED)
                        binding.buttonFab.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24)
                    else
                        binding.buttonFab.setImageResource(R.mipmap.ic_add_white)
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }
            })
        }
        binding.sheetView.buttonCrop.setOnClickListener { view -> menuSheetClick(view.id) }
        binding.sheetView.buttonChooser.setOnClickListener { view -> menuSheetClick(view.id) }
        binding.sheetView.buttonShare.setOnClickListener { view -> menuSheetClick(view.id) }
        binding.buttonShowSize.setOnClickListener { changeFullScreenImageSize() }
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun changeFullScreenImageSize() {
        if (binding.imgWallpaper.scaleType == ImageView.ScaleType.CENTER_CROP) {
            binding.imgWallpaper.scaleType = ImageView.ScaleType.FIT_START
            binding.buttonShowSize.setImageResource(R.drawable.ic_fullscreen_size)
        } else {
            binding.imgWallpaper.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.buttonShowSize.setImageResource(R.drawable.ic_real_size)
        }
    }

    private fun deleteFile(url: String): Boolean {
        return File(url).delete()
    }

    private fun deleteCurrentPicture() {
        if (deleteFile(currentUrl)) {
            finishWithResult()
        } else {
            Utils.showSnackMessage(
                binding.rootLayout,
                getString(R.string.ui_detail_failure_delete)
            )
        }
    }

    private fun finishWithResult() {
        requireActivity().setResult(Activity.RESULT_OK, Intent())
        requireActivity().onBackPressed()
    }

    private fun showMenuSheet() {
        if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        } else {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun dismissMenuSheet() {
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun menuSheetClick(id: Int) {
        when (id) {
            R.id.button_crop -> {
                saveTempsDorAndDoAction(
                    ActionTypeEnum.Crop,
                    currentUrl
                )
            }

            R.id.button_chooser -> {
                saveTempsDorAndDoAction(
                    ActionTypeEnum.OpenNativeChooser,
                    currentUrl
                )
            }

            R.id.button_share -> {
                saveTempsDorAndDoAction(
                    ActionTypeEnum.Share,
                    currentUrl
                )
            }
        }
        dismissMenuSheet()
    }

    private fun onSaveTempsDorAndDoAction(
        aBoolean: Boolean,
        actionToDo: ActionTypeEnum
    ) {
        if (aBoolean) {
            when (actionToDo) {
                ActionTypeEnum.OpenNativeChooser -> {
                    openNativeChooser()
                }

                ActionTypeEnum.Share -> {
                    createIntent()
                }

                ActionTypeEnum.MovePerDir -> {
                    saveFileToPermanentGallery(
                        currentUrl,
                        requireActivity(),
                        ::onRequestPermissions
                    )
                }

                ActionTypeEnum.Delete -> {
                    deleteCurrentPicture()
                }

                ActionTypeEnum.SendLwp -> {
                    // sendToRippleLwp();
                }

                else -> beginCrop()
            }
        } else {
            hideLoading()
            saveToFileToTempsDirAndChooseAction(
                currentUrl,
                actionToDo,
                requireContext(),
                fileManager,
                ::doActionAfterSave
            )
        }
    }

    private fun doActionAfterSave(isSaved: Boolean, action: ActionTypeEnum) {
        if (isSaved) {
            when (action) {
                is ActionTypeEnum.OpenNativeChooser -> openNativeChooser()
                is ActionTypeEnum.MovePerDir -> onMoveFileToPermanentGallery()
                is ActionTypeEnum.Share -> createIntent()
                is ActionTypeEnum.SendLwp -> onSendToRippleLwp()
                else -> onGoToCropActivity()
            }
        } else {
            onSaveError()
        }
    }

    private suspend fun decodeAndSet(context: Context, url: String) = withContext(Dispatchers.IO) {
        decodeBitmapAndSetAsLiveWallpaper(
            fileManager.getTemporaryDirWithFile(
                url.getFileName()
            ),
            context,
            deviceManager.getScreenWidthPixels(),
            deviceManager.getScreenHeightPixels()
        )
    }

    private fun beginCrop() {
        hideLoading()
        Crop.of(
            Uri.fromFile(fileManager.getTemporaryDirWithFile(currentUrl.getFileName())),
            Uri.fromFile(File(requireContext().cacheDir, "cropped"))
        ).withAspect(screenPoint.x, screenPoint.y)
            .start(context, this)
    }

    private fun createIntent() {
        hideLoading()
        if (!DetailUtils.shareFileWithIntentType(
                requireActivity(),
                fileManager.getTemporaryDirWithFile(currentUrl.getFileName()), appId
            )
        ) {
            Utils.showSnackMessage(
                binding.rootLayout,
                getString(R.string.app_not_installer_message)
            )
        }
    }

    private fun handleCropResult(resultCode: Int, result: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            handleCropSuccess(result, requireContext())
        } else if (resultCode == Crop.RESULT_ERROR) {
            Utils.showSnackMessage(binding.rootLayout, Crop.getError(result).message.orEmpty())
        }
    }

    private fun handleCropSuccess(result: Intent?, context: Context) {
        viewLifecycleOwner.lifecycleScope.launch {
            if (setWallpaper(result, context)) {
                showSnackMsg(context.getString(R.string.set_wallpaper_success_message))
            } else {
                showSnackMsg(context.getString(R.string.set_wallpaper_not_success_message))
            }
        }
    }

    private suspend fun setWallpaper(result: Intent?, context: Context) =
        withContext(Dispatchers.IO) {
            setWallpaper(
                MediaStore.Images.Media.getBitmap(
                    context.contentResolver, Crop.getOutput(result)
                ),
                context, deviceManager.getScreenWidthPixels(),
            )
        }

    private fun onRequestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_CODE_ASK_PERMISSION
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        result: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, result)
        if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) {
            beginCrop()
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCropResult(resultCode, result)
        } else if (requestCode == 200 && resultCode == Activity.RESULT_CANCELED) {
            //beginCrop()
        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
        }
    }

    private val screenPoint: Point
        get() {
            val wm = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            return size
        }

    private fun openNativeChooser() {
        hideLoading()
        DetailUtils.openNativeChooser(
            this, requireContext(),
            fileManager.getTemporaryDirWithFile(currentUrl.getFileName()), appId
        )
    }

    private fun onGoToCropActivity() {
        viewLifecycleOwner.lifecycleScope.launch {
            val result = getTempFile()
            if (result) {
                beginCrop()
            } else {
                requireActivity().onBackPressed()
            }
        }
    }

    private suspend fun getTempFile() = withContext(Dispatchers.IO) {
        fileManager.getTemporaryDirWithFile(currentUrl.getFileName()).exists()
    }

    private fun onMoveFileToPermanentGallery() {
        saveFileToPermanentGallery(
            currentUrl,
            requireActivity(),
            ::onRequestPermissions
        )
    }

    private fun saveFileToPermanentGallery(
        url: String,
        context: Activity,
        onRequestPermissions: () -> Unit
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            val result = savePermanentFile(url)
            if (result) {
                showSnackMsg(context.getString(R.string.set_wallpaper_success_message))
            } else {
                showSnackMsg(context.getString(R.string.set_wallpaper_not_success_message))
                DetailUtils.checkPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, onRequestPermissions
                )
            }
            hideLoading()
        }
    }

    private suspend fun savePermanentFile(url: String) = withContext(Dispatchers.IO) {
        fileManager.savePermanentFile(url)
    }

    private fun onSendToRippleLwp() {
        // sendToRippleLwp();
    }

    private fun onSaveError() {
        Utils.showSnackMessage(binding.rootLayout, getString(R.string.ui_detail_failure_delete))
        hideLoading()
    }

    override fun onResume() {
        super.onResume()
        if (fromRipple) {
            fromRipple = false
        }
    }

    private fun showSnackMsg(msg: String) {
        Utils.showSnackMessage(binding.rootLayout, msg)
    }

    private fun showLoading() {
        binding.progressBarHorizontal.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBarHorizontal.visibility = View.GONE
    }

    companion object {
        const val REQUEST_CODE_ASK_PERMISSION = 123
    }
}
