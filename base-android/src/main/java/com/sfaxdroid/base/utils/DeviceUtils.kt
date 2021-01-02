package com.sfaxdroid.base.utils

import android.content.Context

class DeviceUtils {

    companion object {

        fun getCellWidth(context: Context): Int {
            return if (Utils.getScreenHeightPixels(context) < 820 && Utils.getScreenWidthPixels(
                    context
                ) < 500
            ) {
                133
            } else {
                200
            }
        }

        fun getCellHeight(context: Context): Int {
            return if (Utils.getScreenHeightPixels(context) < 820 && Utils.getScreenWidthPixels(
                    context
                ) < 500
            ) {
                133
            } else {
                200
            }
        }

    }
}