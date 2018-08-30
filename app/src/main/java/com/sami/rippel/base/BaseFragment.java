package com.sami.rippel.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.sami.rippel.allah.R;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.model.entity.WallpaperObject;
import com.sami.rippel.model.listner.AdsListener;

import java.util.ArrayList;

/**
 * Created by yassin baccour on 16/04/2017.
 */

public abstract class BaseFragment<T extends BasePresenter> extends SimpleFragment implements BaseView {
    protected AdsListener mListener = null;
    protected ArrayList<WallpaperObject> mData = new ArrayList<>();
    protected RecyclerView mRecyclerView;
    protected ProgressBar mProgressLoader;
    protected T mPresenter;

    public abstract Activity getFragmentActivity();

    public abstract void fillForm();

    public abstract RecyclerView.LayoutManager getLayoutManager();

    protected T instantiatePresenter() {
        return null;
    }

    protected abstract void initEventAndData();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = instantiatePresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mProgressLoader = (ProgressBar) getFragmentActivity().findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recentContainer);
        mRecyclerView.setLayoutManager(getLayoutManager());
        mRecyclerView.setHasFixedSize(true);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        initEventAndData();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroyView();
    }

    public void setListener(AdsListener l) {
        mListener = l;
    }

    public void downloadPicture() {
        if (getFragmentActivity() != null && ViewModel.Current.device.isConnected(getFragmentActivity()))
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
