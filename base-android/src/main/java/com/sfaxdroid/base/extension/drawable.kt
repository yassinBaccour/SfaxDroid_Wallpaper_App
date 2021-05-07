package com.sfaxdroid.base.extension

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat

fun Context.getDrawableWithTheme(id: Int): Drawable? {
    return ResourcesCompat.getDrawable(resources, id, this.theme)
}
