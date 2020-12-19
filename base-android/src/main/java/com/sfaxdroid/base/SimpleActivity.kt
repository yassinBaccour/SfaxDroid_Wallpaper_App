package com.sfaxdroid.base

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by yassine on 11/10/17.
 */
abstract class SimpleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
        onViewCreated()
        initEventAndData()
    }

    open fun onViewCreated() {}
    abstract fun initEventAndData()
    abstract val layout: Int

}