package com.sami.rippel.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sami.rippel.labs.stickers.FlowerStickersImageFragment;
import com.sami.rippel.labs.stickers.FrameImageFragment;
import com.sami.rippel.labs.stickers.NonVectorStickersImageFragment;
import com.sami.rippel.labs.stickers.TextStickersImageFragment;
import com.sami.rippel.labs.stickers.VectorStickersImageFragment;
import com.sami.rippel.model.listner.StickersListener;

public class StickersFragmentPagerAdapter extends FragmentPagerAdapter {

    private StickersListener stickersListener;

    public StickersFragmentPagerAdapter(FragmentManager fragmentManager,
                                        StickersListener stickersListener) {
        super(fragmentManager);
        this.stickersListener = stickersListener;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                NonVectorStickersImageFragment mNonVectorFragment = NonVectorStickersImageFragment.newInstance();
                mNonVectorFragment.setListener(stickersListener);
                return mNonVectorFragment;
            case 1:
                VectorStickersImageFragment mVectorFragment = VectorStickersImageFragment.newInstance();
                mVectorFragment.setListener(stickersListener);
                return mVectorFragment;
            case 2:
                FlowerStickersImageFragment mFlowerFragment = FlowerStickersImageFragment.newInstance();
                mFlowerFragment.setListener(stickersListener);
                return mFlowerFragment;
            case 3:
                TextStickersImageFragment mTextFragment = TextStickersImageFragment.newInstance();
                mTextFragment.setListener(stickersListener);
                return mTextFragment;
            case 4:
                FrameImageFragment mFrameImageFragment = FrameImageFragment.newInstance();
                mFrameImageFragment.setListener(stickersListener);
                return mFrameImageFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return " ";
    }
}
