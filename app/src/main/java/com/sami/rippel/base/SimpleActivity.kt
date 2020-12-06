package com.sami.rippel.base

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by yassine on 11/10/17.
 */
abstract class SimpleActivity : AppCompatActivity() {

    protected var mContext: Activity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
        mContext = this
        onViewCreated()
        initEventAndData()
    }

    protected open fun onViewCreated() {}
    protected abstract val layout: Int
    protected abstract fun initEventAndData()
}