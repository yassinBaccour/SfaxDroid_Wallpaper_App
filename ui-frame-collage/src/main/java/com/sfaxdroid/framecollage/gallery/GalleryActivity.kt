package com.sfaxdroid.framecollage.gallery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import com.sfaxdroid.base.*
import com.sfaxdroid.base.utils.FileUtils.Companion.getBasmalaStickersFileList
import com.sfaxdroid.bases.LwpListener
import kotlinx.android.synthetic.main.activity_gellery_wallpaper.*

class GalleryActivity : SimpleActivity(), LwpListener {

    private var adapter: GalleryAdapter? = null
    private var wallpaperList: ArrayList<WallpaperObject>? = ArrayList()
    private var selectedLwpName: String = ""
    private var mPos: String? = null

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
        return getBitmapListWallpaper(selectedLwpName)
    }

    private fun initData() {
        wallpaperList?.clear()
        wallpaperList = getWallpaperList()
        adapter = GalleryAdapter(
            this@GalleryActivity,
            wallpaperList,
            TypeCellItemEnum.BITMAP_CELL
        )
        list.adapter = adapter
        progressBar.visibility = View.GONE
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
                applicationContext
            ) { _: View?, position: Int ->
                if (!mPos.isNullOrEmpty()) {
                    openDetailActivity(position)
                } else {
                    when (selectedLwpName) {
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


    private fun initToolbar(pos: String?) {
        setSupportActionBar(toolbar as Toolbar)
        if (!pos.isNullOrEmpty()) {
            supportActionBar?.title =
                pos
        } else {
            supportActionBar?.title = getString(
                R.string.texturetitle
            )
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

}