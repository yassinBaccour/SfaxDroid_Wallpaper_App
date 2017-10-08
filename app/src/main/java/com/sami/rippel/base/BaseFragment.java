package com.sami.rippel.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.sami.rippel.allah.R;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.model.entity.WallpaperObject;
import com.sami.rippel.model.listner.AdsListner;

import java.util.ArrayList;

/**
 * Created by yassin baccour on 16/04/2017.
 */

public abstract class BaseFragment extends Fragment {
    protected AdsListner mListener = null;
    protected ArrayList<WallpaperObject> mData = new ArrayList<>();
    protected RecyclerView mRecyclerView;
    protected ProgressBar mProgressLoader;
    public abstract Activity getFragmentActivity();
    public abstract void fillForm();
    public abstract int getFragmentId();
    public abstract RecyclerView.LayoutManager getLayoutManager();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getFragmentId(), container,false);
        mProgressLoader = (ProgressBar)  getFragmentActivity().findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recentContainer);
        mRecyclerView.setLayoutManager(getLayoutManager());
        mRecyclerView.setHasFixedSize(true);
        return rootView;
    }

    public void setListener(AdsListner l) {
        mListener = l;
    }

    public void downloadPicture()
    {
        if (getFragmentActivity() != null && ViewModel.Current.fileUtils.isConnected(getFragmentActivity()))
            fillForm();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
