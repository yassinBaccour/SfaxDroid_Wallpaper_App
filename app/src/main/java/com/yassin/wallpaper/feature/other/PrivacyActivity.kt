package com.yassin.wallpaper.feature.other

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.yassin.wallpaper.R
import com.yassin.wallpaper.databinding.ActivityPrivacyBinding

class PrivacyActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPrivacyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.privacyWebView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl("http://androidsporttv.com/privacypolicy/water/privacy.html")
        }
    }
}
