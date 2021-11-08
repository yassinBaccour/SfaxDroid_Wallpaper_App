package com.yassin.wallpaper

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
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
        val editor = sharedPref.edit()
        editor.putString(
            Constants.CHANGE_IMAGE_KEY,
            value
        )
        editor.apply()
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
