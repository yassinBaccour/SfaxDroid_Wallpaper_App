package com.sami.rippel.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sami.rippel.labs.stickers.FlowerStickersImageFragment;
import com.sami.rippel.labs.stickers.FrameImageFragment;
import com.sami.rippel.labs.stickers.NonVectorStickersImageFragment;
import com.sami.rippel.labs.stickers.TextStickersImageFragment;
import com.sami.rippel.labs.stickers.VectorStickersImageFragment;
import com.sami.rippel.model.listner.StickersListner;

public class StickersFragmentPagerAdapter extends FragmentPagerAdapter {

    private StickersListner stickersListner;

    public NonVectorStickersImageFragment getmNonVectorFragment() {
        return mNonVectorFragment;
    }

    private NonVectorStickersImageFragment mNonVectorFragment;
    private VectorStickersImageFragment mVectorFragment;
    private FlowerStickersImageFragment mFlowerFragment;
    private TextStickersImageFragment mTextFragment;
    private FrameImageFragment mFrameImageFragment;

    public StickersFragmentPagerAdapter(FragmentManager fragmentManager,
                                        Context context, StickersListner stickersListner) {
        super(fragmentManager);
        this.stickersListner = stickersListner;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                mNonVectorFragment = NonVectorStickersImageFragment.newInstance();
                mNonVectorFragment.setListener(stickersListner);
                return mNonVectorFragment;
            case 1:
                mVectorFragment = VectorStickersImageFragment.newInstance();
                mVectorFragment.setListener(stickersListner);
                return mVectorFragment;
            case 2:
                mFlowerFragment = FlowerStickersImageFragment.newInstance();
                mFlowerFragment.setListener(stickersListner);
                return mFlowerFragment;
            case 3:
                mTextFragment = TextStickersImageFragment.newInstance();
                mTextFragment.setListener(stickersListner);
                return mTextFragment;
            case 4:
                mFrameImageFragment = FrameImageFragment.newInstance();
                mFrameImageFragment.setListener(stickersListner);
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
