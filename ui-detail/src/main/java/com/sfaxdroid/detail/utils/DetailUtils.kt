package com.sfaxdroid.detail.utils

import android.app.Activity
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.R
import com.sfaxdroid.base.utils.FileUtils
import com.sfaxdroid.base.utils.Utils
import com.sfaxdroid.detail.ActionTypeEnum
import com.sfaxdroid.detail.IntentType
import java.io.File
import java.io.IOException
import kotlin.math.max

class DetailUtils {

    companion object {

        fun saveToFileToTempsDirAndChooseAction(
            url: String?,
            action: ActionTypeEnum,
            screenHeightPixels: Int,
            screenWidthPixels: Int,
            context: Context,
            appName: String,
            doActionAfterSave: (isSaved: Boolean, action: ActionTypeEnum) -> Unit
        ) {
            Glide.with(context).asBitmap().load(
                Utils.getUrlByScreen(
                    url!!, screenHeightPixels, screenWidthPixels
                )
            )
                .into(object : SimpleTarget<Bitmap?>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap?>?
                    ) {
                        val isSaved =
                            FileUtils.saveBitmapToStorage(
                                resource,
                                FileUtils.getFileName(url),
                                FileUtils.SAVE_TEMPORARY,
                                context, appName
                            )
                        doActionAfterSave(isSaved, action)
                        resource.recycle()
                    }
                })
        }

        private fun getIntentNameFromType(intentType: IntentType): String {
            return when (intentType) {
                is IntentType.facebook -> {
                    Constants.FB_PACKAGE
                }
                is IntentType.instagram -> {
                    Constants.INSTAGRAM_PACKAGE
                }
                is IntentType.snap -> {
                    Constants.SNAP_PACKAGE
                }
            }
        }

        fun shareFileWithIntentType(
            activity: Activity,
            file: File?,
            intentType: IntentType
        ): Boolean {
            return if (Utils.appInstalledOrNot(getIntentNameFromType(intentType), activity)
            ) {
                val shareIntent =
                    Intent(Intent.ACTION_SEND)
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
                shareIntent.type = "ic_icon_image/*"
                val photoURI = FileProvider.getUriForFile(
                    activity,
                    activity.applicationContext.packageName + ".provider",
                    file!!
                )
                shareIntent.putExtra(Intent.EXTRA_STREAM, photoURI)
                shareIntent.setPackage(
                    getIntentNameFromType(
                        intentType
                    )
                )
                activity.startActivity(shareIntent)
                true
            } else false
        }

        fun shareAllFile(activity: Activity, file: File?) {
            val intent =
                Intent(Intent.ACTION_ATTACH_DATA)
            intent.setDataAndType(
                FileProvider.getUriForFile(
                    activity, activity.applicationContext.packageName
                            + ".provider", file!!
                ), "ic_icon_image/jpg"
            )
            intent.putExtra("mimeType", "ic_icon_image/jpg")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            activity.startActivityForResult(
                Intent.createChooser(
                    intent,
                    activity.getString(R.string.set_as_dialog_message)
                ), 200
            )
        }

        fun setWallpaper(wallpaper: Bitmap, context: Context): Boolean {
            return try {
                val wallpaperManager =
                    WallpaperManager.getInstance(context)
                val width = wallpaperManager.desiredMinimumWidth
                val height = wallpaperManager.desiredMinimumHeight
                if (width > wallpaper.width && height > wallpaper.height && Utils.getScreenWidthPixels(
                        context
                    ) < Constants.MIN_WIDHT
                ) {
                    val pixels =
                        IntArray(wallpaper.width * wallpaper.height)
                    wallpaper.getPixels(
                        pixels,
                        0,
                        wallpaper.width,
                        0,
                        0,
                        wallpaper.width,
                        wallpaper.height
                    )
                    wallpaperManager.setBitmap(Bitmap.createBitmap(
                        width,
                        height,
                        Bitmap.Config.ARGB_8888
                    ).apply {
                        setPixels(
                            pixels,
                            0,
                            wallpaper.width,
                            max(0, width - wallpaper.width) / 2,
                            max(0, height - wallpaper.height) / 2,
                            wallpaper.width,
                            wallpaper.height
                        )
                    })
                    true
                } else {
                    wallpaperManager.setBitmap(wallpaper)
                    true
                }
            } catch (e: IOException) {
                false
            }
        }

        fun decodeBitmapAndSetAsLiveWallpaper(file: File, context: Context): Boolean {
            if (file.exists()) {
                val wallpaperManager =
                    WallpaperManager.getInstance(context)
                try {
                    BitmapFactory.decodeFile(
                        file.path,
                        BitmapFactory.Options().apply {
                            inPreferredConfig = Bitmap.Config.ARGB_8888
                        }
                    )?.also {
                        val mBackground =
                            Bitmap.createScaledBitmap(
                                it,
                                Utils.getScreenWidthPixels(context),
                                Utils.getScreenHeightPixels(context),
                                true
                            )
                        if (mBackground != null) wallpaperManager.setBitmap(mBackground)
                        it.recycle()
                        return true
                    }
                    return false
                } catch (ignored: IOException) {
                    return false
                }
            } else {
                return false
            }
        }
    }
}