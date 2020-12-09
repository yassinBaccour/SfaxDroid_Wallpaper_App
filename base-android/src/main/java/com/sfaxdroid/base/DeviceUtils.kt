package com.sfaxdroid.base

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager

class DeviceUtils {

    companion object {
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