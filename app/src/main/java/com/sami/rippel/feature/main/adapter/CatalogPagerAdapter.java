package com.sami.rippel.feature.main.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sami.rippel.allah.R;
import com.sami.rippel.model.listner.AdsListener;
import com.sami.rippel.feature.main.fragments.AllBackgroundFragment;
import com.sami.rippel.feature.main.fragments.CategoryFragment;
import com.sami.rippel.feature.main.fragments.LabFragment;
import com.sami.rippel.feature.main.fragments.LwpFragment;

import java.util.ArrayList;

public class CatalogPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<String> listeTitre = new ArrayList<>();
    private AdsListener ads;
    private LwpFragment mLwpFragment;
    private AllBackgroundFragment mAllBackgroundFragment;
    private CategoryFragment mCategoryFragment;

    public CatalogPagerAdapter(FragmentManager fragmentManager, Context context, AdsListener adsListner) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.ads = adsListner;
        listeTitre.add(context.getString(R.string.catalog_LWP));
        listeTitre.add(context.getString(R.string.catalog_All));
        listeTitre.add(context.getString(R.string.catalog_Category));
        listeTitre.add(context.getString(R.string.catalog_Lab));
    }

    public void ChooseFragmentToExcecuteAction(int position) {
        switch (position) {
            case 0:
                break;
            case 1:
                if (mAllBackgroundFragment != null)
                    mAllBackgroundFragment.downloadPicture();
                break;
            case 2:
                if (mCategoryFragment != null)
                    mCategoryFragment.downloadPicture();
                break;
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                mLwpFragment = LwpFragment.newInstance();
                mLwpFragment.setListener(ads);
                return mLwpFragment;
            case 1:
                mAllBackgroundFragment = AllBackgroundFragment.newInstance();
                mAllBackgroundFragment.setListener(ads);
                return mAllBackgroundFragment;
            case 2:
                mCategoryFragment = CategoryFragment.newInstance();
                mCategoryFragment.setListener(ads);
                return mCategoryFragment;
            default:
                LabFragment lab = LabFragment.newInstance();
                lab.setListener(ads);
                return lab;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listeTitre.get(position);
    }

    public void ChangeAdapterFragmentViewState(boolean mState) {
        if (mLwpFragment != null)
            mLwpFragment.changeButtonSate(mState);
    }
}
