package com.sfaxdoird.anim.word

import android.graphics.drawable.Drawable
import android.widget.Button


fun Button.setCompoundTopDrawables(drawable: Drawable?) {
    this.setCompoundDrawablesWithIntrinsicBounds(
        null,
        drawable, null, null
    )
}

