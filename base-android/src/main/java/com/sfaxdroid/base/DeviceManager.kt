package com.sfaxdroid.base

interface DeviceManager {
    fun isSmallScreen(): Boolean
    fun getScreenHeightPixels(): Int
    fun getScreenWidthPixels(): Int
}
