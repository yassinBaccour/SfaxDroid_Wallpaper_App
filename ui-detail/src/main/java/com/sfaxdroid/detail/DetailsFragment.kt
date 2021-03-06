package com.sfaxdroid.detail

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.DeviceManager
import com.sfaxdroid.base.FileManager
import com.sfaxdroid.base.extension.getFileName
import com.sfaxdroid.base.extension.loadUrlWithAction
import com.sfaxdroid.base.utils.Utils
import com.sfaxdroid.detail.utils.DetailUtils
import com.sfaxdroid.detail.utils.DetailUtils.Companion.decodeBitmapAndSetAsLiveWallpaper
import com.sfaxdroid.detail.utils.DetailUtils.Companion.setWallpaper
import com.sfaxdroid.detail.utils.TouchImageView
import com.soundcloud.android.crop.Crop
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet.*


@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var from: String = ""
    private var currentUrl: String = ""
    private var fromRipple = false
    private var bottomSheetBehavior: BottomSheetBehavior<*>? = null

    @Inject
    lateinit var fileManager: FileManager

    @Inject
    lateinit var deviceManager: DeviceManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initFabButtonAndBottomSheet()
        checkPermission()
        showLoading()
        loadWallpaper()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        currentUrl = arguments?.getString(Constants.EXTRA_IMG_URL).orEmpty()
        from = arguments?.getString(Constants.KEY_LWP_NAME).orEmpty()
    }

    private fun loadWallpaper() {
        val detailImage: TouchImageView = requireView().findViewById(R.id.detailImage)
        detailImage.loadUrlWithAction(currentUrl, ::hideLoading)
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            onRequestPermissions()
        }
    }

    private fun initToolbar() {
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
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
                Constants.KEY_ADD_TIMER_LWP -> fab?.setImageResource(R.mipmap.ic_download)
                Constants.KEY_ADDED_LIST_TIMER_LWP -> fab?.setImageResource(R.mipmap.ic_delete)
                Constants.KEY_RIPPLE_LWP -> fab?.setImageResource(R.mipmap.ic_ripple_fab)
            }
        }
        fab.setOnClickListener { fabClick() }

        bottomSheetBehavior =
            BottomSheetBehavior.from<View>(bottom_sheet)
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior?.peekHeight = 0
        bottomSheetBehavior?.isHideable = true
        bottomSheetBehavior?.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED)
                    fab?.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24)
                else
                    fab?.setImageResource(R.mipmap.ic_add_white)
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

        })

        buttonWallpaper.setOnClickListener { view -> menuSheetClick(view.id) }
        buttonChooser.setOnClickListener { view -> menuSheetClick(view.id) }
        buttonSave.setOnClickListener { view -> menuSheetClick(view.id) }
        buttonSareInsta.setOnClickListener { view -> menuSheetClick(view.id) }
        buttonSareFb.setOnClickListener { view -> menuSheetClick(view.id) }
    }

    private fun deleteFile(url: String): Boolean {
        val file = File(url)
        return file.delete()
    }

    private fun deleteCurrentPicture() {
        if (deleteFile(currentUrl)) {
            finishWithResult()
        } else {
            Utils.showSnackMessage(rootLayout, getString(R.string.ui_detail_failure_delete))
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
            R.id.buttonWallpaper -> {
                saveTempsDorAndDoAction(
                    ActionTypeEnum.Crop,
                    currentUrl
                )
            }
            R.id.buttonChooser -> {
                saveTempsDorAndDoAction(
                    ActionTypeEnum.OpenNativeChooser,
                    currentUrl
                )
            }
            R.id.buttonSave -> {
                saveTempsDorAndDoAction(
                    ActionTypeEnum.MovePerDir,
                    currentUrl
                )
            }
            R.id.buttonSareInsta -> {
                saveTempsDorAndDoAction(
                    ActionTypeEnum.ShareInstagram,
                    currentUrl
                )
            }
            R.id.buttonSareFb -> {
                saveTempsDorAndDoAction(
                    ActionTypeEnum.ShareFacebook,
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
            if (actionToDo == ActionTypeEnum.OpenNativeChooser) {
                shareAll()
            }
            if (actionToDo == ActionTypeEnum.ShareFacebook) {
                createIntent(IntentType.FACEBOOK)
            }
            if (actionToDo == ActionTypeEnum.ShareInstagram) {
                createIntent(IntentType.INSTAGRAM)
            }
            if (actionToDo == ActionTypeEnum.SendLwp) {
                //sendToRippleLwp();
            }
            if (actionToDo == ActionTypeEnum.Crop) {
                beginCrop()
            }
            if (actionToDo == ActionTypeEnum.MovePerDir) {
                saveFileToPermanentGallery(
                    currentUrl,
                    requireActivity(),
                    ::onRequestPermissions
                )
            }
            if (actionToDo == ActionTypeEnum.Delete) {
                deleteCurrentPicture()
            }
            if (actionToDo == ActionTypeEnum.JustWallpaper) {
                setAsWallpaper(
                    currentUrl,
                    requireContext()
                )
            }
        } else {
            hideLoading()
            DetailUtils.saveToFileToTempsDirAndChooseAction(
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
                is ActionTypeEnum.Crop -> onGoToCropActivity()
                is ActionTypeEnum.OpenNativeChooser -> shareAll()
                is ActionTypeEnum.MovePerDir -> onMoveFileToPermanentGallery()
                is ActionTypeEnum.ShareFacebook -> createIntent(IntentType.INSTAGRAM)
                is ActionTypeEnum.ShareInstagram -> createIntent(IntentType.INSTAGRAM)
                is ActionTypeEnum.SendLwp -> onSendToRippleLwp()
                is ActionTypeEnum.ShareSnap -> createIntent(IntentType.SNAP)
            }
        } else {
            onSaveError()
        }
    }

    private fun setAsWallpaper(url: String, context: Context) {
        hideLoading()
        viewLifecycleOwner.lifecycleScope.launch {
            val result = decodeAndSet(context, url)
            if (result) showSnackMsg("Success") else showSnackMsg(
                "Error"
            )
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

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == android.R.id.home) {
            requireActivity().onBackPressed()
        }
        return super.onOptionsItemSelected(menuItem)
    }

    private fun createIntent(intentType: IntentType) {
        hideLoading()
        if (!DetailUtils.shareFileWithIntentType(
                requireActivity(),
                fileManager.getTemporaryDirWithFile(currentUrl.getFileName()),
                intentType
            )
        ) {
            Utils.showSnackMessage(rootLayout, getString(R.string.app_not_installer_message))
        }
    }

    private fun handleCropResult(resultCode: Int, result: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            handleCropSuccess(result, requireContext())
        } else if (resultCode == Crop.RESULT_ERROR) {
            Utils.showSnackMessage(rootLayout, Crop.getError(result).message.orEmpty())
        }
    }

    private fun handleCropSuccess(result: Intent?, context: Context) {
        viewLifecycleOwner.lifecycleScope.launch {
            val result = setWallpaper(result, context)
            if (result) {
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
        requestCode: Int, resultCode: Int,
        result: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, result)
        if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) {
            beginCrop()
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCropResult(resultCode, result)
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

    private fun shareAll() {
        hideLoading()
        DetailUtils.shareAllFile(
            requireActivity(),
            fileManager.getTemporaryDirWithFile(currentUrl.getFileName())
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
        //sendToRippleLwp();
    }

    private fun onSaveError() {
        Utils.showSnackMessage(rootLayout, getString(R.string.ui_detail_failure_delete))
        hideLoading()
    }

    override fun onResume() {
        super.onResume()
        if (fromRipple) {
            fromRipple = false
        }
    }

    fun showSnackMsg(msg: String) {
        Utils.showSnackMessage(rootLayout, msg)
    }

    private fun showLoading() {
        progressBar?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar?.visibility = View.GONE
    }

    companion object {
        const val REQUEST_CODE_ASK_PERMISSION = 123
    }
}