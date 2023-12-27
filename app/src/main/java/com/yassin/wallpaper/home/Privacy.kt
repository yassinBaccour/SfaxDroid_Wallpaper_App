package com.yassin.wallpaper.home

import android.annotation.SuppressLint
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun Privacy() {
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize())
    AndroidView(factory = {
        android.webkit.WebView(context).apply {
            webViewClient = WebViewClient()
            settings.apply {
                loadsImagesAutomatically = true
                javaScriptEnabled = true
                domStorageEnabled = true
            }
            loadUrl("http://androidsporttv.com/privacypolicy/water/privacy.html")
        }
    })
}