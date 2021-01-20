package com.sfaxdroid.base.extension

import android.graphics.drawable.Drawable
import android.widget.Button
import com.sfaxdroid.base.utils.BitmapUtils

fun Button.changeDrawableButtonColor(selectedColor: Int, drawable: Drawable) {
    this.setCompoundDrawablesWithIntrinsicBounds(
        null,
        BitmapUtils.covertToDrawableWithColorChange(
            context,
            selectedColor,
            drawable
        ), null, null
    )
}