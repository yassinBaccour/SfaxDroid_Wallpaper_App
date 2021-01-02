package com.sfaxdoird.anim.word

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.Button


fun Context.getDrawableByVersion(id: Int): Drawable {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        resources.getDrawable(id, this.theme);
    } else {
        resources.getDrawable(id);
    }
}

fun Button.setCompoundTopDrawables(drawable: Drawable) {
    this.setCompoundDrawablesWithIntrinsicBounds(
        null,
        drawable, null, null
    )
}

