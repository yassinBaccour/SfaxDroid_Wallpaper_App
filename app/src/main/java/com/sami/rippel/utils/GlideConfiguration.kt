package com.sami.rippel.utils

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

@GlideModule
class GlideConfiguration : AppGlideModule() {

    override fun applyOptions(
        context: Context,
        mBuilder: GlideBuilder
    ) {
        mBuilder.setDefaultRequestOptions(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
    }

    override fun isManifestParsingEnabled() = false
}