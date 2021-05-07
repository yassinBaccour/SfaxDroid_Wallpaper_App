package com.sfaxdroid.base.extension

import android.graphics.drawable.Drawable
import android.widget.Button
import androidx.core.content.res.ResourcesCompat
import com.sfaxdroid.base.utils.BitmapUtils

fun Button.changeDrawableButtonColor(selectedColor: Int, drawable: Drawable?) {
    drawable?.let {
        this.setCompoundDrawablesWithIntrinsicBounds(
            null,
            BitmapUtils.covertToDrawableWithColorChange(
                context,
                selectedColor,
                drawable
            ), null, null
        )
    }
}

fun Button.setCompoundDrawableFromId(buttonResourceId: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(
        null,
        ResourcesCompat.getDrawable(resources, buttonResourceId, context.theme), null, null
    )
}
