package com.sami.rippel.feature.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sami.rippel.feature.home.AllInOneFragment
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
                AllInOneFragment.newInstance("lwp.json", "LWP")
            }
            1 -> {
                AllInOneFragment.newInstance("new.json", "NEW")
            }
            2 -> {
                AllInOneFragment.newInstance("category.json", "CAT")
            }
            else -> {
                AllInOneFragment.newInstance("lab.json", "LAB")
            }
        }
    }

    override fun getCount() = NB_FRAGMENT

    override fun getPageTitle(position: Int) = titleList[position]

    companion object {
        const val NB_FRAGMENT = 4
    }
}