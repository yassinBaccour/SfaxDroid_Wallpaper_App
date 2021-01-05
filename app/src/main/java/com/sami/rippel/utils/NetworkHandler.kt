package com.sami.rippel.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.sfaxdroid.data.DeviceNetworkHandler
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkHandler
@Inject constructor(@ApplicationContext private val context: Context) : DeviceNetworkHandler {

    private fun isReachable(context: Context): Boolean {
        val manager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT < 23) {
            val networkInfo = manager.activeNetworkInfo
            if (networkInfo != null) {
                return networkInfo.isConnected && (networkInfo.type == ConnectivityManager.TYPE_WIFI || networkInfo.type == ConnectivityManager.TYPE_MOBILE)
            }
        } else {
            val network = manager.activeNetwork
            if (network != null) {
                val capabilities = manager.getNetworkCapabilities(network)
                return if (capabilities != null)
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || capabilities.hasTransport(
                        NetworkCapabilities.TRANSPORT_WIFI
                    ) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                else
                    false
            }
        }
        return false
    }

    override fun isConnected(): Boolean {
        return isReachable(context)
    }
}