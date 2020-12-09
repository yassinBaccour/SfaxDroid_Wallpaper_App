package com.sami.rippel.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

import com.sami.rippel.allah.R;
import com.sami.rippel.base.BaseFragment;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.model.entity.StateEnum;
import com.sami.rippel.model.entity.TypeCellItemEnum;
import com.sami.rippel.model.listner.OnStateChangeListener;
import com.sami.rippel.utils.RecyclerItemClickListener;
import com.sami.rippel.presenter.CategoryWallpaperPresenter;
import com.sami.rippel.presenter.Contract.WallpaperFragmentContract;
import com.sami.rippel.ui.activity.GalleryActivity;
import com.sami.rippel.ui.activity.HomeActivity;
import com.sami.rippel.ui.adapter.GalleryAdapter;
import com.sfaxdroid.base.Constants;
import com.sfaxdroid.base.DeviceUtils;
import com.sfaxdroid.base.WallpaperObject;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends BaseFragment<CategoryWallpaperPresenter> implements WallpaperFragmentContract.View, OnStateChangeListener {

    private GalleryAdapter mAdapter;
    private CategoryFragment mFragment;

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewModel.Current.registerOnStateChangeListener(this);
        mFragment = this;
    }

    @Override
    public Activity getFragmentActivity() {
        return getActivity();
    }

    @Override
    public void fillForm() {

    }

    @Override
    public int getFragmentId() {
        return R.layout.fragment_category;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    @Override
    protected void initEventAndData() {
        mPresenter.getWallpaper();
    }

    @Override
    protected CategoryWallpaperPresenter instantiatePresenter() {
        return new CategoryWallpaperPresenter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ViewModel.Current.unregisterOnStateChangeListener(this);
    }

    @Override
    public void showContent(List<WallpaperObject> mList) {
        if (ViewModel.Current.isWallpapersLoaded()) {
            mData.clear();
            mData = new ArrayList<>(mList);
            if (getActivity() != null && DeviceUtils.Companion.isConnected(getActivity()) && mData != null && mData.size() > 0) {
                mAdapter = new GalleryAdapter(getActivity(), mData, TypeCellItemEnum.CATEGORY_NEW_FORMAT);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView
                        .addOnItemTouchListener(new RecyclerItemClickListener(
                                getActivity(),
                                (view, position) -> {
                                    if (position >= 0) {
                                        String categorName = mAdapter.GetName(position);
                                        if (mListener != null) {
                                            HomeActivity.nbOpenAds++;
                                            mListener.onOpenScreenTracker("CategoryGallery");
                                            mListener.onTrackAction("CategoryOpen", categorName);
                                        }
                                        Intent intent = new Intent(
                                                getActivity(),
                                                GalleryActivity.class);
                                        intent.putParcelableArrayListExtra(
                                                com.sfaxdroid.base.Constants.LIST_FILE_TO_SEND_TO_DETAIL_VIEW_PAGER, mData);

                                        if (!categorName.isEmpty())
                                            intent.putExtra(Constants.DETAIL_IMAGE_POS, categorName);
                                        startActivity(intent);
                                    }
                                }));
            } else {
                if (mFragment.getView() != null) {
                    Toast.makeText(getActivity(), getString(R.string.NoConnection),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onStateChange(@NonNull StateEnum state) {
        mPresenter.getWallpaper();
    }

    @Override
    public void showSnackMsg(String msg) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
