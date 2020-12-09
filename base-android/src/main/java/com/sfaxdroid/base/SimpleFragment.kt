package com.sfaxdroid.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Created by yassine on 11/10/17.
 */
abstract class SimpleFragment : Fragment() {
    @JvmField
    protected var rootView: View? = null
    protected var mActivity: Activity? = null

    abstract val fragmentId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(fragmentId, null)
        return rootView
    }
}