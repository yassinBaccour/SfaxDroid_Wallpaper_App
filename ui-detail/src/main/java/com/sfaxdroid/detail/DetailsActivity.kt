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
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.flipboard.bottomsheet.commons.MenuSheetView
import com.flipboard.bottomsheet.commons.MenuSheetView.MenuType
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.SimpleActivity
import com.sfaxdroid.base.utils.FileUtils
import com.sfaxdroid.base.utils.FileUtils.Companion.getFileName
import com.sfaxdroid.base.utils.FileUtils.Companion.getTemporaryFile
import com.sfaxdroid.base.utils.FileUtils.Companion.isSavedToStorage
import com.sfaxdroid.base.utils.FileUtils.Companion.savePermanentFile
import com.sfaxdroid.base.utils.Utils
import com.sfaxdroid.base.utils.Utils.Companion.checkPermission
import com.sfaxdroid.base.utils.Utils.Companion.getScreenHeightPixels
import com.sfaxdroid.base.utils.Utils.Companion.getScreenWidthPixels
import com.sfaxdroid.detail.utils.DetailUtils
import com.sfaxdroid.detail.utils.DetailUtils.Companion.decodeBitmapAndSetAsLiveWallpaper
import com.sfaxdroid.detail.utils.DetailUtils.Companion.setWallpaper
import com.sfaxdroid.detail.utils.TouchImageView
import com.soundcloud.android.crop.Crop
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_details.*
import java.io.File
import java.util.*

class DetailsActivity : SimpleActivity() {

    private var mCompositeDisposable: CompositeDisposable? = null
    private var from: String = ""
    private var currentUrl: String = ""
    private var fromRipple = false

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentUrl = Utils.getUrlByScreenSize(
            intent.getStringExtra(Constants.EXTRA_IMG_URL).orEmpty(), Utils.isSmallScreen(this)
        )
        from = intent.getStringExtra(Constants.KEY_LWP_NAME).orEmpty()
        initToolbar()
        initFabButton()
        checkPermission()
        showLoading()
        loadWallpaper()
    }

    private fun getFullUrl(url: String): String {
        return url.replace("_preview", "")
    }

    private fun loadWallpaper() {
        val detailImage: TouchImageView = findViewById(R.id.detailImage)
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
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.apply {
            title = ""
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override val layout = R.layout.activity_details

    override fun initEventAndData() {}

    private fun saveTempsDorAndDoAction(
        actionToDo: ActionTypeEnum,
        url: String,
        context: Context,
        appName: String
    ) {
        addSubscribe(Flowable.fromCallable {
            isSavedToStorage(
                getFileName(url),
                context,
                appName
            )
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
                currentUrl,
                this,
                getString(R.string.app_namenospace)
            )
            Constants.KEY_ADDED_LIST_TIMER_LWP -> saveTempsDorAndDoAction(
                ActionTypeEnum.Delete,
                currentUrl,
                this,
                getString(R.string.app_namenospace)
            )
            Constants.KEY_RIPPLE_LWP -> saveTempsDorAndDoAction(
                ActionTypeEnum.SendLwp,
                currentUrl,
                this,
                getString(R.string.app_namenospace)
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

    private fun deleteCurrentPicture() {
        if (FileUtils.deleteFile(currentUrl)) {
            finishWithResult()
        } else {
            Utils.showSnackMessage(rootLayout, getString(R.string.ui_detail_failure_delete))
        }
    }

    private fun finishWithResult() {
        setResult(Activity.RESULT_OK, Intent())
        finish()
    }

    private fun showMenuSheet() {
        bottomsheetLayout?.showWithSheetView(
            MenuSheetView(
                this,
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
                        currentUrl,
                        this,
                        getString(R.string.app_namenospace)
                    )
                }
                R.id.buttonChooser -> {
                    saveTempsDorAndDoAction(
                        ActionTypeEnum.OpenNativeChooser,
                        currentUrl,
                        this,
                        getString(R.string.app_namenospace)
                    )
                }
                R.id.buttonSave -> {
                    saveTempsDorAndDoAction(
                        ActionTypeEnum.MovePerDir,
                        currentUrl,
                        this,
                        getString(R.string.app_namenospace)
                    )
                }
                R.id.buttonSareInsta -> {
                    saveTempsDorAndDoAction(
                        ActionTypeEnum.ShareInstagram,
                        currentUrl,
                        this,
                        getString(R.string.app_namenospace)
                    )
                }
                R.id.buttonSareFb -> {
                    saveTempsDorAndDoAction(
                        ActionTypeEnum.ShareFacebook,
                        currentUrl,
                        this,
                        getString(R.string.app_namenospace)
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
                    this,
                    getString(R.string.app_namenospace),
                    ::onRequestPermissions
                )
            }
            if (actionToDo == ActionTypeEnum.Delete) {
                deleteCurrentPicture()
            }
            if (actionToDo == ActionTypeEnum.JustWallpaper) {
                setAsWallpaper(
                    currentUrl,
                    this,
                    getString(R.string.app_namenospace)
                )
            }
        } else {
            hideLoading()
            DetailUtils.saveToFileToTempsDirAndChooseAction(
                currentUrl,
                actionToDo,
                getScreenHeightPixels(this),
                getScreenWidthPixels(this),
                this,
                getString(R.string.app_namenospace),
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

    private fun setAsWallpaper(url: String, context: Context, appName: String) {
        hideLoading()
        addSubscribe(Flowable.fromCallable {
            decodeBitmapAndSetAsLiveWallpaper(
                getTemporaryFile(
                    getFileName(
                        url
                    ), context, appName
                ), context
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
            Uri.fromFile(
                getTemporaryFile(
                    getFileName(currentUrl),
                    this,
                    getString(R.string.app_namenospace)
                )
            ), Uri.fromFile(File(cacheDir, "cropped"))
        ).withAspect(screenPoint.x, screenPoint.y)
            .start(this)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(menuItem)
    }

    private fun createIntent(intentType: IntentType) {
        hideLoading()
        if (!DetailUtils.shareFileWithIntentType(
                this,
                getTemporaryFile(
                    getFileName(
                        currentUrl
                    ), this, getString(R.string.app_namenospace)
                ),
                intentType
            )
        ) {
            Utils.showSnackMessage(rootLayout, getString(R.string.app_not_installer_message))
        }
    }

    private fun handleCrop(resultCode: Int, result: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            handleCrop(result, this)
        } else if (resultCode == Crop.RESULT_ERROR) {
            Utils.showSnackMessage(rootLayout, Crop.getError(result).message.orEmpty())
        }
    }

    private fun handleCrop(result: Intent?, context: Context) {
        addSubscribe(Flowable.fromCallable {
            setWallpaper(
                MediaStore.Images.Media.getBitmap(
                    context.contentResolver, Crop.getOutput(result)
                ), context
            )
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorReturn { error: Throwable? -> false }
            .subscribe { setSuccess: Boolean ->
                if (setSuccess) {
                    showSnackMsg(context.getString(R.string.set_wallpaper_sucess_message))
                } else {
                    showSnackMsg(context.getString(R.string.set_wallpaper_not_sucess_message))
                }
            }
        )
    }

    private fun onRequestPermissions() {
        ActivityCompat.requestPermissions(
            this@DetailsActivity,
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
            handleCrop(resultCode, result)
        }
    }

    private val screenPoint: Point
        get() {
            val wm = this
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            return size
        }

    private fun shareAll() {
        hideLoading()
        DetailUtils.shareAllFile(
            this,
            getTemporaryFile(
                getFileName(
                    currentUrl
                ), this, getString(R.string.app_namenospace)
            )
        )
    }

    private fun onGoToCropActivity() {
        addSubscribe(Flowable.fromCallable {
            isSavedToStorage(
                getFileName(currentUrl),
                this,
                getString(R.string.app_namenospace)
            )
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { aBoolean: Boolean ->
                if (aBoolean) {
                    beginCrop()
                } else {
                    finish()
                }
            }
        )
    }

    private fun onMoveFileToPermanentGallery() {
        saveFileToPermanentGallery(
            currentUrl,
            this,
            getString(R.string.app_namenospace),
            ::onRequestPermissions
        )
    }

    private fun saveFileToPermanentGallery(
        url: String?,
        context: Activity,
        appName: String?,
        onRequestPermissions: () -> Unit
    ) {
        addSubscribe(Flowable.fromCallable {
            savePermanentFile(
                url, context,
                appName!!
            )
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { aBoolean: Boolean ->
                if (aBoolean) {
                    showSnackMsg(context.getString(R.string.set_wallpaper_sucess_message))
                } else {
                    showSnackMsg(context.getString(R.string.set_wallpaper_not_sucess_message))
                    checkPermission(
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