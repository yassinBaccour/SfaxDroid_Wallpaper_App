package com.sami.rippel.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sami.rippel.allah.R;
import com.sami.rippel.model.listner.AdsListner;
import com.sami.rippel.ui.fragments.AllBackgroundFragment;
import com.sami.rippel.ui.fragments.CategoryFragment;
import com.sami.rippel.ui.fragments.LabFragment;
import com.sami.rippel.ui.fragments.LwpFragment;
import com.sami.rippel.ui.fragments.RecentFragment;

import java.util.ArrayList;

public class CatalogPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<String> listeTitre = new ArrayList<>();
    private AdsListner ads;
    private LwpFragment mLwpFragment;
    private AllBackgroundFragment mAllBackgroundFragment;
    private RecentFragment mRecentFragment;
    private CategoryFragment mCategoryFragment;

    public CatalogPagerAdapter(FragmentManager fragmentManager, Context context, AdsListner adsListner) {
        super(fragmentManager);
        this.ads = adsListner;
        listeTitre.add(context.getString(R.string.catalog_LWP));
        listeTitre.add(context.getString(R.string.catalog_New));
        listeTitre.add(context.getString(R.string.catalog_All));
        listeTitre.add(context.getString(R.string.catalog_Category));
        listeTitre.add(context.getString(R.string.catalog_Lab));
    }

    public void ChooseFragmentToExcecuteAction(int position) {
        switch (position) {
            case 0:
                break;
            case 1:
                if (mRecentFragment != null)
                    mRecentFragment.downloadPicture();
                break;
            case 2:
                if (mAllBackgroundFragment != null)
                    mAllBackgroundFragment.downloadPicture();
                break;
            case 3:
                if (mCategoryFragment != null)
                    mCategoryFragment.downloadPicture();
                break;
            case 4:
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
                mRecentFragment = RecentFragment.newInstance();
                mRecentFragment.setListener(ads);
                return mRecentFragment;
            case 2:
                mAllBackgroundFragment = AllBackgroundFragment.newInstance();
                mAllBackgroundFragment.setListener(ads);
                return mAllBackgroundFragment;
            case 3:
                mCategoryFragment = CategoryFragment.newInstance();
                mCategoryFragment.setListener(ads);
                return mCategoryFragment;
            case 4:
                LabFragment lab = LabFragment.newInstance();
                lab.setListener(ads);
                return lab;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
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
