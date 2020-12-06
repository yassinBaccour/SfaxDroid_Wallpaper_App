package com.sami.rippel.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

import com.sami.rippel.allah.R;
import com.sami.rippel.base.BaseFragment;
import com.sami.rippel.model.Constants;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.model.entity.StateEnum;
import com.sami.rippel.model.entity.TypeCellItemEnum;
import com.sami.rippel.model.entity.WallpaperObject;
import com.sami.rippel.model.listner.OnStateChangeListener;
import com.sami.rippel.utils.RecyclerItemClickListener;
import com.sami.rippel.presenter.Contract.WallpaperFragmentContract;
import com.sami.rippel.presenter.RecentWallpaperPresenter;
import com.sami.rippel.ui.activity.DetailsActivity;
import com.sami.rippel.ui.activity.HomeActivity;
import com.sami.rippel.ui.adapter.GalleryAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecentFragment extends BaseFragment<RecentWallpaperPresenter> implements WallpaperFragmentContract.View, OnStateChangeListener {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewModel.Current.registerOnStateChangeListener(this);
    }

    @Override
    public Activity getFragmentActivity() {
        return getActivity();
    }

    @Override
    public void fillForm() {

    }

    @Override
    protected RecentWallpaperPresenter instantiatePresenter() {
        return new RecentWallpaperPresenter(null); //FIXME VIEW MODEL NULL
    }

    @Override
    public void showContent(List<WallpaperObject> mList) {
        if (ViewModel.Current.isWallpapersLoaded() && mList != null) {
            mData.clear();
            mData = new ArrayList<>(mList);
            if (getActivity() != null && ViewModel.Current.device.isConnected(getActivity()) && mData != null && mData.size() > 0) {
                mRecyclerView.setAdapter(new GalleryAdapter(getActivity(), mData, TypeCellItemEnum.GALLERY_CELL));
                mRecyclerView
                        .addOnItemTouchListener(new RecyclerItemClickListener(
                                getActivity(),
                                (view, position) -> {
                                    if (getView() != null && position >= 0 && mData.size() > 0) {
                                        if (mListener != null) {
                                            mListener.onTrackAction("RecentFragment", "OpenWallpapers");
                                        }
                                        HomeActivity.nbOpenAds = HomeActivity.nbOpenAds + 1;
                                        Intent intent = new Intent(
                                                getActivity(),
                                                DetailsActivity.class);
                                        intent.putParcelableArrayListExtra(
                                                Constants.LIST_FILE_TO_SEND_TO_DETAIL_VIEW_PAGER, mData);
                                        intent.putExtra(Constants.DETAIL_IMAGE_POS, position);
                                        startActivity(intent);
                                    }
                                }));
            } else {
                if (getView() != null) {
                    Toast.makeText(getActivity(), getString(R.string.NoConnection),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ViewModel.Current.unregisterOnStateChangeListener(this);
    }

    @Override
    public int getFragmentId() {
        return R.layout.fragment_recent;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(
                getFragmentActivity(), 3);
    }

    @Override
    protected void initEventAndData() {
        mPresenter.getWallpaper();
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
