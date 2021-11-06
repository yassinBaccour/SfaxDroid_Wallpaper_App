package com.yassin.sfax.tawakkolalaallah

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SfaxDroidThemes {
                MainScreen()
            }
        }
    }

    @Preview
    @Composable
    private fun Preview() {
        SfaxDroidThemes {
            MainScreen()
        }
    }

}
