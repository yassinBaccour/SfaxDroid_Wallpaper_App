package com.sfaxdroid.detail

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.flipboard.bottomsheet.commons.MenuSheetView
import com.flipboard.bottomsheet.commons.MenuSheetView.MenuType
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.DeviceManager
import com.sfaxdroid.base.FileManager
import com.sfaxdroid.base.extension.getFileName
import com.sfaxdroid.base.utils.Utils
import com.sfaxdroid.detail.utils.DetailUtils
import com.sfaxdroid.detail.utils.DetailUtils.Companion.decodeBitmapAndSetAsLiveWallpaper
import com.sfaxdroid.detail.utils.DetailUtils.Companion.setWallpaper
import com.sfaxdroid.detail.utils.TouchImageView
import com.soundcloud.android.crop.Crop
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_details.*
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var mCompositeDisposable: CompositeDisposable? = null
    private var from: String = ""
    private var currentUrl: String = ""
    private var fromRipple = false

    @Inject
    lateinit var fileManager: FileManager

    @Inject
    lateinit var deviceManager: DeviceManager

    private fun unSubscribe() {
        mCompositeDisposable?.dispose()
    }

    private fun addSubscribe(subscription: Disposable?) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable?.add(subscription!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        unSubscribe()
    }

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
        initFabButton()
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
        Glide.with(this).load(currentUrl)
            .into(object : SimpleTarget<Drawable?>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable?>?
                ) {
                    detailImage.setImageDrawable(resource)
                    hideLoading()
                }
            })
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
        addSubscribe(Flowable.fromCallable {
            fileManager.getTemporaryDirWithFile(url.getFileName()).exists()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { aBoolean: Boolean ->
                onSaveTempsDorAndDoAction(
                    aBoolean,
                    actionToDo
                )
            }
        )
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

    private fun initFabButton() {
        if (from.isNotEmpty()) {
            when (from) {
                Constants.KEY_ADD_TIMER_LWP -> fab?.setImageResource(R.mipmap.ic_download)
                Constants.KEY_ADDED_LIST_TIMER_LWP -> fab?.setImageResource(R.mipmap.ic_delete)
                Constants.KEY_RIPPLE_LWP -> fab?.setImageResource(R.mipmap.ic_ripple_fab)
            }
        }
        fab.setOnClickListener { fabClick() }
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
        bottomsheetLayout?.showWithSheetView(
            MenuSheetView(
                requireContext(),
                MenuType.LIST,
                "",
                menuSheetListener
            ).apply {
                inflateMenu(R.menu.menu_details)
            })
    }

    private fun dismissMenuSheet() {
        if (bottomsheetLayout?.isSheetShowing == true) {
            bottomsheetLayout?.dismissSheet()
        }
    }

    private val menuSheetListener: MenuSheetView.OnMenuItemClickListener
        get() = MenuSheetView.OnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
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
            true
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
                createIntent(IntentType.facebook)
            }
            if (actionToDo == ActionTypeEnum.ShareInstagram) {
                createIntent(IntentType.instagram)
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
                is ActionTypeEnum.ShareFacebook -> createIntent(IntentType.instagram)
                is ActionTypeEnum.ShareInstagram -> createIntent(IntentType.instagram)
                is ActionTypeEnum.SendLwp -> onSendToRippleLwp()
                is ActionTypeEnum.ShareSnap -> createIntent(IntentType.snap)
            }
        } else {
            onSaveError()
        }
    }

    private fun setAsWallpaper(url: String, context: Context) {
        hideLoading()
        addSubscribe(Flowable.fromCallable {
            decodeBitmapAndSetAsLiveWallpaper(
                fileManager.getTemporaryDirWithFile(
                    url.getFileName()
                ),
                context,
                deviceManager.getScreenWidthPixels(),
                deviceManager.getScreenHeightPixels()
            )
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { t: Boolean ->
                if (t) showSnackMsg("Success") else showSnackMsg(
                    "Error"
                )
            }
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
        addSubscribe(Flowable.fromCallable {
            setWallpaper(
                MediaStore.Images.Media.getBitmap(
                    context.contentResolver, Crop.getOutput(result)
                ),
                context, deviceManager.getScreenWidthPixels(),
            )
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorReturn { _: Throwable? -> false }
            .subscribe { setSuccess: Boolean ->
                if (setSuccess) {
                    showSnackMsg(context.getString(R.string.set_wallpaper_success_message))
                } else {
                    showSnackMsg(context.getString(R.string.set_wallpaper_not_success_message))
                }
            }
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
        addSubscribe(Flowable.fromCallable {
            fileManager.getTemporaryDirWithFile(currentUrl.getFileName()).exists()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { aBoolean: Boolean ->
                if (aBoolean) {
                    beginCrop()
                } else {
                    requireActivity().onBackPressed()
                }
            }
        )
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
        addSubscribe(Flowable.fromCallable {
            fileManager.savePermanentFile(url)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { aBoolean: Boolean ->
                if (aBoolean) {
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
        )
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

    fun showLoading() {
        progressBar?.visibility = View.VISIBLE
    }

    fun hideLoading() {
        progressBar?.visibility = View.GONE
    }

    companion object {
        const val REQUEST_CODE_ASK_PERMISSION = 123
    }
}