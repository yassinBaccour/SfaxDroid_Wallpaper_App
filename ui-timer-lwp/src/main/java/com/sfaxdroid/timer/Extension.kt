package com.sfaxdroid.timer

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

inline fun Fragment.loadFragment(
    isAddToBackStack: Boolean = true,
    transitionPairs: Map<String, View> = mapOf(),
    transaction: FragmentTransaction.() -> Unit,
) {
    val beginTransaction = childFragmentManager.beginTransaction()
    beginTransaction.transaction()
    for ((name, view) in transitionPairs) {
        androidx.core.view.ViewCompat.setTransitionName(view, name)
        beginTransaction.addSharedElement(view, name)
    }

    if (isAddToBackStack) beginTransaction.addToBackStack("FragmentWallpaper")
    beginTransaction.commit()
}
