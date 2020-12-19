package com.sami.rippel.feature.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sami.rippel.feature.main.fragments.AllBackgroundFragment
import com.sami.rippel.feature.main.fragments.CategoryFragment
import com.sami.rippel.feature.main.fragments.LabFragment
import com.sami.rippel.feature.main.fragments.LwpFragment
import java.util.*

class CatalogPagerAdapter(
    fragmentManager: FragmentManager,
    private var titleList: ArrayList<String>
) : FragmentPagerAdapter(
    fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    private var mLwpFragment: LwpFragment? = null
    private var mAllBackgroundFragment: AllBackgroundFragment? = null
    private var mCategoryFragment: CategoryFragment? = null

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                LwpFragment.newInstance().also {
                    mLwpFragment = it
                    return it
                }
            }
            1 -> {
                AllBackgroundFragment.newInstance().also {
                    mAllBackgroundFragment = it
                    return it
                }
            }
            2 -> {
                CategoryFragment.newInstance().also {
                    mCategoryFragment = it
                    return it
                }
            }
            else -> {
                return LabFragment.newInstance()
            }
        }
    }

    override fun getCount() =
        NB_FRAGMENT

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }

    fun changeButtonSate(mState: Boolean) {
        mLwpFragment?.changeButtonSate(mState)
    }

    fun downloadPicture(position: Int) {
        when (position) {
            1 -> mAllBackgroundFragment?.downloadPicture()
            2 -> mCategoryFragment?.downloadPicture()
        }
    }

    companion object {
        const val NB_FRAGMENT = 4
    }
}