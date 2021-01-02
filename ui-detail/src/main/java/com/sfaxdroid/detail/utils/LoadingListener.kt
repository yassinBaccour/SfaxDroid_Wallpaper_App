package com.sfaxdroid.detail.utils

/**
 * Created by yassine on 11/10/17.
 */
interface LoadingListener {
    fun showSnackMsg(msg: String)
    fun showLoading()
    fun hideLoading()
}