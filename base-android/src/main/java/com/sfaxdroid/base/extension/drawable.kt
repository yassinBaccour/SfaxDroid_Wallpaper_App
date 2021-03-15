package com.sfaxdroid.base.extension

import android.content.Context
import android.graphics.drawable.Drawable

fun Context.getDrawableWithTheme(id: Int): Drawable {
    return resources.getDrawable(id, this.theme);
}