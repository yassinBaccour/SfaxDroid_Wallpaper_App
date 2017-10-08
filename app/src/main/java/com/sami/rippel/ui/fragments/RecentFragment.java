package com.sami.rippel.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sami.rippel.base.BaseFragment;
import com.sami.rippel.model.Constants;
import com.sami.rippel.ui.adapter.GalleryAdapter;
import com.sami.rippel.ui.activity.DetailsActivity;
import com.sami.rippel.allah.R;
import com.sami.rippel.ui.activity.ViewPagerWallpaperActivity;
import com.sami.rippel.model.entity.TypeCellItemEnum;
import com.sami.rippel.model.listner.RecyclerItemClickListener;
import com.sami.rippel.model.ViewModel;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class RecentFragment extends BaseFragment {

    private GalleryAdapter mAdapter;
    private RecentFragment mFragment;

    public static RecentFragment newInstance() {
        Bundle args = new Bundle();
        RecentFragment fragment = new RecentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mFragment = this;
    }

    @Override
    public Activity getFragmentActivity() {
        return getActivity();
    }

    @Override
    public void fillForm()
    {
        if (ViewModel.Current.isWallpapersLoaded()) {
            //WallpaperCategory wallpaperCategory = ViewModel.Current.retrofitWallpObject.getCategoryList().stream().filter(x -> x.getTitle().equals("New")).findFirst().orElse(null);
            mData.clear();
            mData = new ArrayList<>(ViewModel.Current.getWallpaperCategoryFromName("New").getGetWallpapersList());
            if (getActivity() != null && ViewModel.Current.fileUtils.isConnected(getActivity()) && mData != null && mData.size() > 0) {
                mAdapter = new GalleryAdapter(getActivity(), mData, TypeCellItemEnum.GALLERY_CELL);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView
                        .addOnItemTouchListener(new RecyclerItemClickListener(
                                getActivity(),
                                (view, position) -> {
                                    if (mFragment.getView() != null && position >= 0 && mData.size() > 0) {
                                        if (mListener != null) {
                                            mListener.onTrackAction("RecentFragment", "OpenWallpapers");
                                        }
                                        ViewPagerWallpaperActivity.nbOpenAds = ViewPagerWallpaperActivity.nbOpenAds + 1;
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
                if (mFragment.getView() != null) {
                    Toast.makeText(getActivity(), getString(R.string.NoConnection),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
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

}
