package com.yassin.wallpaper

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.sfaxdroid.engine.Constants
import com.sfaxdroid.mini.base.BaseConstants
import com.sfaxdroid.mini.base.BaseMiniAppActivity

class MainActivity : BaseMiniAppActivity() {

    @Preview
    @Composable
    fun Preview() {
        Main(::changeThemes)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SfaxDroidThemes {
                Main(::changeThemes)
            }
        }
    }

    private fun changeThemes(value: String) {
        val sharedPref = getSharedPreferences(
            BaseConstants.PREF_NAME,
            Context.MODE_PRIVATE
        )
        sharedPref.edit().run {
            {
                putString(
                    Constants.CHANGE_IMAGE_KEY,
                    value
                )
                apply()
            }
        }
    }


    @Composable
    fun SfaxDroidThemes(content: @Composable () -> Unit) {
        MaterialTheme(
            colors = FlowerColor,
            typography = AppTypography
        ) {
            content()
        }
    }

}
