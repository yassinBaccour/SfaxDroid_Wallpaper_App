package com.sfaxdroid.bases

/**
 * Created by yassine on 11/10/17.
 */
interface BaseView {
    fun showSnackMsg(msg: String)
    fun showLoading()
    fun hideLoading()
}