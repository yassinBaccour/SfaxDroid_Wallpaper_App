package com.sfaxdroid.detail

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.flipboard.bottomsheet.commons.MenuSheetView
import com.flipboard.bottomsheet.commons.MenuSheetView.MenuType
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.FileUtils
import com.sfaxdroid.base.FileUtils.Companion.getFileName
import com.sfaxdroid.base.FileUtils.Companion.getTemporaryFile
import com.sfaxdroid.base.FileUtils.Companion.isSavedToStorage
import com.sfaxdroid.base.Utils
import com.sfaxdroid.base.Utils.Companion.checkPermission
import com.sfaxdroid.base.Utils.Companion.getScreenHeightPixels
import com.sfaxdroid.base.Utils.Companion.getScreenWidthPixels
import com.sfaxdroid.base.Utils.Companion.saveToFileToTempsDirAndChooseAction
import com.sfaxdroid.base.Utils.Companion.shareAllFile
import com.sfaxdroid.base.Utils.Companion.shareFileWithIntentType
import com.sfaxdroid.base.WallpaperObject
import com.sfaxdroid.bases.ActionTypeEnum
import com.sfaxdroid.bases.DeviceListner
import com.sfaxdroid.bases.IntentTypeEnum
import com.sfaxdroid.bases.WallpaperListener
import com.soundcloud.android.crop.Crop
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_details.*
import java.io.File
import java.util.*

class DetailsActivity : BaseActivity<DetailPresenter?>(),
    WallpaperListener, DeviceListner, DetailContract.View {
    private var selectedPosInPager = 0
    private var from: String? = ""
    private var fromRipple = false
    private var adapter: DetailPagerAdapter? = null
    private var listOfWallpaper =
        ArrayList<WallpaperObject?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFlags()
        listOfWallpaper =
            intent.getParcelableArrayListExtra(Constants.LIST_FILE_TO_SEND_TO_DETAIL_VIEW_PAGER)
                ?: arrayListOf()
        selectedPosInPager = intent.getIntExtra(Constants.DETAIL_IMAGE_POS, 0)
        from = intent.getStringExtra(Constants.KEY_LWP_NAME)
        initToolbar()
        setFabImageResource()
        setupViewPager()
        checkPermission()
    }

    private fun setFlags() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
            )
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                this
            )
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.title = getString(R.string.SetWall)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun instantiatePresenter() = DetailPresenter()

    override val layout: Int
        get() = R.layout.activity_details

    override fun initEventAndData() {}

    private fun fabClick() {
        if (!from.isNullOrEmpty()) {
            when (from) {
                Constants.KEY_ADD_TIMER_LWP -> mPresenter?.saveTempsDorAndDoAction(
                    ActionTypeEnum.MOVE_PERMANENT_DIR,
                    currentUrl,
                    this,
                    getString(R.string.app_namenospace)
                )
                Constants.KEY_ADDED_LIST_TIMER_LWP -> mPresenter?.saveTempsDorAndDoAction(
                    ActionTypeEnum.DELETE_CURRENT_PICTURE,
                    currentUrl,
                    this,
                    getString(R.string.app_namenospace)
                )
                Constants.KEY_RIPPLE_LWP -> mPresenter?.saveTempsDorAndDoAction(
                    ActionTypeEnum.SEND_LWP,
                    currentUrl,
                    this,
                    getString(R.string.app_namenospace)
                )
            }
        } else {
            showMenuSheet()
        }
    }

    private fun setFabImageResource() {
        if (from?.isEmpty() == false) {
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
            Utils.showSnackMessage(rootLayout, "Error Deleting file")
        }
    }


    private fun finishWithResult() {
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun showMenuSheet() {
        val menuSheetView = MenuSheetView(this, MenuType.LIST, "", menuSheetListener)
        menuSheetView.inflateMenu(R.menu.menu_details)
        bottomsheetLayout?.showWithSheetView(menuSheetView)
    }

    private fun dismissMenuSheet() {
        if (bottomsheetLayout?.isSheetShowing == true) {
            bottomsheetLayout?.dismissSheet()
        }
    }

    private fun setupViewPager() {
        adapter = DetailPagerAdapter(
            this, R.layout.layout_detail_pager,
            listOfWallpaper, this
        )
        viewpager?.adapter = adapter
        viewpager?.currentItem = selectedPosInPager
    }

    private val menuSheetListener: MenuSheetView.OnMenuItemClickListener
        get() = MenuSheetView.OnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.buttonWallpaper -> {
                    mPresenter?.saveTempsDorAndDoAction(
                        ActionTypeEnum.CROP,
                        currentUrl,
                        this,
                        getString(R.string.app_namenospace)
                    )
                }
                R.id.buttonChooser -> {
                    mPresenter?.saveTempsDorAndDoAction(
                        ActionTypeEnum.OPEN_NATIV_CHOOSER,
                        currentUrl,
                        this,
                        getString(R.string.app_namenospace)
                    )
                }
                R.id.buttonSave -> {
                    mPresenter?.saveTempsDorAndDoAction(
                        ActionTypeEnum.MOVE_PERMANENT_DIR,
                        currentUrl,
                        this,
                        getString(R.string.app_namenospace)
                    )
                }
                R.id.buttonSareInsta -> {
                    mPresenter?.saveTempsDorAndDoAction(
                        ActionTypeEnum.SHARE_INSTA,
                        currentUrl,
                        this,
                        getString(R.string.app_namenospace)
                    )
                }
                R.id.buttonSareFb -> {
                    mPresenter?.saveTempsDorAndDoAction(
                        ActionTypeEnum.SHARE_FB,
                        currentUrl,
                        this,
                        getString(R.string.app_namenospace)
                    )
                }
            }
            dismissMenuSheet()
            true
        }

    override fun onSaveTempsDorAndDoAction(
        aBoolean: Boolean,
        actionToDo: ActionTypeEnum
    ) {
        if (aBoolean) {
            if (actionToDo == ActionTypeEnum.OPEN_NATIV_CHOOSER) {
                shareAll()
            }
            if (actionToDo == ActionTypeEnum.SHARE_FB) {
                createIntent(IntentTypeEnum.FACEBOOKINTENT)
            }
            if (actionToDo == ActionTypeEnum.SHARE_INSTA) {
                createIntent(IntentTypeEnum.INTAGRAMINTENT)
            }
            if (actionToDo == ActionTypeEnum.SEND_LWP) {
                //sendToRippleLwp();
            }
            if (actionToDo == ActionTypeEnum.CROP) {
                beginCrop()
            }
            if (actionToDo == ActionTypeEnum.MOVE_PERMANENT_DIR) {
                mPresenter?.saveFileToPermanentGallery(
                    currentUrl,
                    this,
                    getString(R.string.app_namenospace),
                    this
                )
            }
            if (actionToDo == ActionTypeEnum.DELETE_CURRENT_PICTURE) {
                deleteCurrentPicture()
            }
            if (actionToDo == ActionTypeEnum.JUST_WALLPAPER) {
                mPresenter?.setAsWallpaper(
                    currentUrl,
                    this,
                    getString(R.string.app_namenospace)
                )
            }
        } else {
            progressBar?.visibility = View.VISIBLE
            saveToFileToTempsDirAndChooseAction(
                currentUrl,
                actionToDo,
                getScreenHeightPixels(this),
                getScreenWidthPixels(this),
                this,
                getString(R.string.app_namenospace),
                this,
                null
            )
        }
    }

    private val currentUrl: String
        get() = adapter?.getItem(viewpager.currentItem)?.url.orEmpty()

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

    private fun createIntent(intentType: IntentTypeEnum) {
        hideLoading()
        if (shareFileWithIntentType(
                this,
                getTemporaryFile(
                    getFileName(
                        currentUrl
                    ), this, getString(R.string.app_namenospace)
                ),
                intentType
            ) == false
        ) {
            Utils.showSnackMessage(rootLayout, getString(R.string.app_not_installer_message))
        }
    }

    private fun handleCrop(resultCode: Int, result: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            mPresenter?.handleCrop(result, this)
        } else if (resultCode == Crop.RESULT_ERROR) {
            Utils.showSnackMessage(rootLayout, Crop.getError(result).message.orEmpty())
        }
    }

    override fun onRequestPermissions() {
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
        shareAllFile(
            this,
            getTemporaryFile(
                getFileName(
                    currentUrl
                ), this, getString(R.string.app_namenospace)
            )
        )
    }

    override fun onGoToCropActivity() {
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

    override fun onMoveFileToPermanentGallery() {
        mPresenter?.saveFileToPermanentGallery(
            currentUrl,
            this,
            getString(R.string.app_namenospace),
            this
        )
    }

    override fun onOpenNativeSetWallChoose() {
        shareAll()
    }

    override fun onOpenWithFaceBook() {
        createIntent(IntentTypeEnum.FACEBOOKINTENT)
    }

    override fun onOpenWithInstagram() {
        createIntent(IntentTypeEnum.INTAGRAMINTENT)
    }

    override fun onSendToRippleLwp() {
        //sendToRippleLwp();
    }

    override fun onShareWhitApplication() {
        createIntent(IntentTypeEnum.SHNAPCHATINTENT)
    }

    override fun onFinishActivity() {
        Utils.showSnackMessage(rootLayout, "Error Reading Storage")
        hideLoading()
    }

    override fun onResume() {
        super.onResume()
        if (fromRipple) {
            fromRipple = false
        }
    }

    override fun showSnackMsg(msg: String) {
        Utils.showSnackMessage(rootLayout, msg)
    }

    override fun showLoading() {
        progressBar?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar?.visibility = View.GONE
    }

    companion object {
        const val REQUEST_CODE_ASK_PERMISSION = 123
    }
}