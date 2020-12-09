package com.sfaxdroid.base

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager

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

        @SuppressLint("MissingPermission")
        fun isConnected(con: Context): Boolean? {
            try {
                val connectivityManager =
                    con.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val wifiInfo =
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                val mobileInfo =
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                if (wifiInfo.isConnected || mobileInfo.isConnected) {
                    return true
                }
            } catch (e: Exception) {
                println("CheckConnectivity Exception: " + e.message)
            }
            return false
        }

    }
}