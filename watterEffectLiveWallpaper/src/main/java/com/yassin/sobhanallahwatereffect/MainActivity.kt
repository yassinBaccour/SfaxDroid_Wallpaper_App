package com.yassin.sobhanallahwatereffect

import android.content.Context
import android.os.Bundle
import com.sfaxdroid.base.BaseActivity
import com.sfaxdroid.base.Utils
import com.sfaxdroid.engine.Constants
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSetWallpaper.setOnClickListener {
            ClearCurrentWallpaper()
            Utils.openLiveWallpaper<WallpaperEngine>(this)
        }

        imageViewPub.setOnClickListener {
            Utils.openPub(this)
        }

        initRadioButton()
    }

    private fun initRadioButton() {

        val sharedPref = getSharedPreferences(
            Constants.PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )

        val editor = sharedPref.edit()

        when (sharedPref.getString(Constants.CHANGE_IMAGE_KEY, "")) {
            Constants.CHANGE_IMAGE_VALUE_ONE -> {
                radioChooseWallpaper.check(R.id.wallp1)
            }
            Constants.CHANGE_IMAGE_VALUE_TWO -> {
                radioChooseWallpaper.check(R.id.wallp2)
            }
            Constants.CHANGE_IMAGE_VALUE_THREE -> {
                radioChooseWallpaper.check(R.id.wallp3)
            }
            else -> {
                radioChooseWallpaper.check(R.id.wallp1)
                editor.putString(
                    Constants.CHANGE_IMAGE_KEY,
                    Constants.CHANGE_IMAGE_VALUE_ONE
                )
                editor.apply()
            }
        }
        radioChooseWallpaper
            .setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.wallp1 -> {
                        editor.putString(
                            Constants.CHANGE_IMAGE_KEY,
                            Constants.CHANGE_IMAGE_VALUE_ONE
                        )
                        editor.commit()
                    }
                    R.id.wallp2 -> {
                        editor.putString(
                            Constants.CHANGE_IMAGE_KEY,
                            Constants.CHANGE_IMAGE_VALUE_TWO
                        )
                        editor.commit()
                    }
                    R.id.wallp3 -> {
                        editor.putString(
                            Constants.CHANGE_IMAGE_KEY,
                            Constants.CHANGE_IMAGE_VALUE_THREE
                        )
                        editor.commit()
                    }
                }
            }
    }
}