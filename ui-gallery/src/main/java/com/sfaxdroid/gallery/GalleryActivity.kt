package com.sfaxdroid.gallery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import com.sfaxdroid.base.*
import com.sfaxdroid.base.FileUtils.Companion.getBasmalaStickersFileList
import com.sfaxdroid.base.FileUtils.Companion.getPermanentDirListFiles
import com.sfaxdroid.bases.LwpListener
import com.sfaxdroid.bases.OnStateChangeListener
import com.sfaxdroid.bases.StateEnum
import com.sfaxdroid.bases.TypeCellItemEnum
import kotlinx.android.synthetic.main.activity_gellery_wallpaper.*

class GalleryActivity : SimpleActivity(), LwpListener, OnStateChangeListener {

    private var adapter: GalleryAdapter? = null
    private var wallpaperList: ArrayList<WallpaperObject>? = ArrayList()
    private var selectedLwpName: String = ""
    private var mPos: String? = null
    private var selectedUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPos = intent.getStringExtra(Constants.DETAIL_IMAGE_POS)
        selectedLwpName = intent.getStringExtra(Constants.KEY_LWP_NAME)
    }

    private fun openDetailActivity(position: Int, lwpName: String = "") {
        try {
            startActivityForResult(
                Intent(
                    this@GalleryActivity,
                    Class.forName("com.sfaxdroid.detail.DetailsActivity")
                ).apply {
                    putParcelableArrayListExtra(
                        Constants.LIST_FILE_TO_SEND_TO_DETAIL_VIEW_PAGER,
                        wallpaperList
                    )
                    putExtra(
                        Constants.DETAIL_IMAGE_POS,
                        position
                    )
                    if (lwpName.isNotEmpty())
                        putExtra(
                            Constants.KEY_LWP_NAME,
                            lwpName
                        )
                }, 90
            )
        } catch (e: ClassNotFoundException) {
        }
    }

    private fun finishWithResult(url: String?) {
        val output = Intent()
        output.putExtra("URL", url)
        setResult(Activity.RESULT_OK, output)
        finish()
    }

    private fun getWallpaperList(): ArrayList<WallpaperObject> {
        return if (isBitmapList()) {
            getBitmapListWallpaper(selectedLwpName)
        } else {
            getListWallpaper(mPos)
        }
    }

    private fun initData() {
        wallpaperList?.clear()
        wallpaperList = getWallpaperList()
        adapter = GalleryAdapter(
            this@GalleryActivity,
            wallpaperList,
            if (isBitmapList()) TypeCellItemEnum.BITMAP_CELL else TypeCellItemEnum.GALLERY_CELL
        )
        list.adapter = adapter
        progressBar.visibility = View.GONE
    }

    private val wallpaperCategory: WallpaperCategory?
        get() {
            //val getCategoryList = ViewModel.Current.retrofitWallpObject.getCategoryList()
            val getCategoryList = arrayListOf<WallpaperCategory>()
            for (wall in getCategoryList) {
                if (wall.getTitle() == nameFind) return wall
            }
            return null
        }


    override val layout: Int
        get() = R.layout.activity_gellery_wallpaper

    override fun initEventAndData() {
        list.layoutManager = GridLayoutManager(this, 3)
        list.setHasFixedSize(true)
        initToolbar(mPos)
        initListViewClick()
        initData()
    }

    private fun initListViewClick() {
        list.addOnItemTouchListener(
            RecyclerItemClickListener(
                applicationContext,
                RecyclerItemClickListener.OnItemClickListener { _: View?, position: Int ->
                    if (!mPos.isNullOrEmpty()) {
                        openDetailActivity(position)
                    } else {
                        when (selectedLwpName) {
                            Constants.KEY_ADDED_LIST_TIMER_LWP -> {
                                openDetailActivity(
                                    position,
                                    Constants.KEY_ADDED_LIST_TIMER_LWP
                                )
                            }
                            Constants.KEY_ADD_TIMER_LWP -> {
                                openDetailActivity(
                                    position,
                                    Constants.KEY_ADD_TIMER_LWP
                                )
                            }
                            Constants.KEY_RIPPLE_LWP -> {
                                openDetailActivity(
                                    position,
                                    Constants.KEY_RIPPLE_LWP
                                )
                            }
                            Constants.KEY_WORD_IMG_LWP -> {
                                openByClassName("com.sfaxdoird.anim.img.WordImgActivity")
                            }
                            Constants.KEY_ANIM_2D_LWP -> {
                                openByClassName("com.sfaxdoird.anim.word.AnimWord2dActivity")
                            }
                            Constants.KEY_BASMALA_STIKERS -> {
                                finishWithResult(wallpaperList?.get(position)?.url)
                            }
                            Constants.KEY_TEXTURE -> {
                                finishActivityWithResult(position)
                            }
                        }
                    }
                }
            )
        )
    }

    private fun openByClassName(name: String) {
        try {
            startActivity(Intent(
                this@GalleryActivity,
                Class.forName(name)
            ).apply {
                putExtra(
                    Constants.URL_TO_DOWNLOAD,
                    selectedUrl
                )
            })
        } catch (e: Exception) {
        }
    }

    private fun getListWallpaper(mPos: String?): ArrayList<WallpaperObject> {
        return if (!mPos.isNullOrEmpty()) {
            ArrayList(
                GetWallpaperFromCategoryNameAndPos(
                    "ImageCategoryNew",
                    mPos
                )?.subWallpapersCategoryList ?: arrayListOf()
            )
        } else if (!selectedLwpName.isNullOrEmpty()) {
            ArrayList(wallpaperCategory!!.getGetWallpapersList())
        } else {
            arrayListOf()
        }
    }

    private fun GetWallpaperFromCategoryNameAndPos(
        catName: String,
        position: String
    ): WallpaperObject? {
        //val wall: List<WallpaperCategory> = ViewModel.Current.retrofitWallpObject.getCategoryList()
        val wall = arrayListOf<WallpaperCategory>()
        for (wallCat in wall) {
            if (wallCat.getTitle() == catName) {
                for (item in wallCat.getGetWallpapersList()) {
                    if (item.name == position) return item
                }
            }
        }
        return null
    }

    private fun isBitmapList(): Boolean {
        return (selectedLwpName == Constants.KEY_ADDED_LIST_TIMER_LWP || selectedLwpName == Constants.KEY_BASMALA_STIKERS)
    }

    private fun finishActivityWithResult(position: Int) {
        val intent = intent
        setResult(Activity.RESULT_OK, intent)
        intent.putExtra(
            "urltoopen",
            wallpaperList?.get(position)?.url.orEmpty()
        )
        intent.putExtra(
            "type",
            Constants.SQUARE_TYPE
        )
        finish()
    }

    private val nameFind: String
        get() = if (selectedLwpName.isNotEmpty()) {
            when (selectedLwpName) {
                Constants.KEY_WORD_IMG_LWP -> "ImageDouaLwp"
                Constants.KEY_ANIM_2D_LWP -> "ImageDouaLwp"
                Constants.KEY_RIPPLE_LWP -> "All"
                Constants.KEY_ADD_TIMER_LWP -> "All"
                Constants.KEY_TEXTURE -> "ImageDouaLwp"
                else -> ""
            }
        } else ""

    private fun getBitmapListWallpaper(fileName: String?): ArrayList<WallpaperObject> {

        val list: ArrayList<WallpaperObject> = arrayListOf()

        val myFileList = when (fileName) {
            Constants.KEY_BASMALA_STIKERS -> {
                getBasmalaStickersFileList(
                    baseContext,
                    baseContext.getString(R.string.app_namenospace)
                )
            }
            Constants.KEY_ADDED_LIST_TIMER_LWP -> {
                getPermanentDirListFiles(
                    baseContext,
                    baseContext.getString(R.string.app_namenospace)
                )
            }
            else -> {
                arrayListOf()
            }
        }

        myFileList?.forEach {
            list.add(WallpaperObject().apply {
                name = "Image " + it?.name
                url = it?.path
            })
        }

        return list
    }

    private fun activityTitle(): String {
        return when (selectedLwpName) {
            Constants.KEY_ADDED_LIST_TIMER_LWP -> return getString(
                R.string.TitleActionBarTimerLwpAdded
            )
            Constants.KEY_TEXTURE -> return getString(
                R.string.texturetitle
            )
            else -> getString(R.string.choose_background)
        }
    }

    private fun initToolbar(pos: String?) {
        setSupportActionBar(toolbar as Toolbar)
        if (!pos.isNullOrEmpty()) {
            supportActionBar?.title =
                pos
        } else {
            supportActionBar?.title = activityTitle()
        }
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 90) {
            if (resultCode == Activity.RESULT_OK) {
                getBitmapListWallpaper(selectedLwpName)
            }
        }
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(menuItem)
    }

    override fun onSendToLwp() {}
    override fun onStateChange(state: StateEnum) {
        initData()
    }
}