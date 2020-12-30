package com.sami.rippel.feature.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sami.rippel.feature.singleview.AllInOneFragment
import java.util.*

class CatalogPagerAdapter(
    fragmentManager: FragmentManager,
    private var titleList: ArrayList<String>
) : FragmentPagerAdapter(
    fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                AllInOneFragment.newInstance("lwp.json")
            }
            1 -> {
                AllInOneFragment.newInstance("new.json")
            }
            2 -> {
                AllInOneFragment.newInstance("category.json")
            }
            else -> {
                AllInOneFragment.newInstance("lab.json")
            }
        }
    }

    override fun getCount() = NB_FRAGMENT

    override fun getPageTitle(position: Int) = titleList[position]

    companion object {
        const val NB_FRAGMENT = 4
    }
}