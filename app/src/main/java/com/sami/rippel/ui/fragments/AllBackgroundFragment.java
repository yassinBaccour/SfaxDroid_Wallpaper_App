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
import com.sami.rippel.model.listner.AdsListener;
import com.sami.rippel.model.listner.OnStateChangeListener;
import com.sami.rippel.utils.RecyclerItemClickListener;
import com.sami.rippel.presenter.AllWallpaperPresenter;
import com.sami.rippel.presenter.Contract.WallpaperFragmentContract;
import com.sami.rippel.ui.activity.DetailsActivity;
import com.sami.rippel.ui.activity.HomeActivity;
import com.sami.rippel.ui.adapter.GalleryAdapter;

import java.util.ArrayList;
import java.util.List;

public class AllBackgroundFragment extends BaseFragment<AllWallpaperPresenter> implements WallpaperFragmentContract.View, OnStateChangeListener {

    private AdsListener mListener = null;
    private AllBackgroundFragment mFragment;

    public static AllBackgroundFragment newInstance() {
        return new AllBackgroundFragment();
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
    protected AllWallpaperPresenter instantiatePresenter() {
        return new AllWallpaperPresenter(null); //FIXME VIEW MODEL NULL
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ViewModel.Current.unregisterOnStateChangeListener(this);
    }

    @Override
    protected void initEventAndData() {
        mPresenter.getWallpaper();
    }

    @Override
    public void showContent(List<WallpaperObject> mList) {
        if (ViewModel.Current.isWallpapersLoaded()) {
            //Fixme retrolabda
            //WallpaperCategory wallpaperCategory = ViewModel.Current.retrofitWallpObject.getCategoryList().stream().filter(x -> x.getTitle().equals("All")).findFirst().orElse(null);
            mData.clear();
            mData = new ArrayList<>(mList);
            if (getActivity() != null && ViewModel.Current.device.isConnected(getActivity()) && mData != null && mData.size() > 0) {
                GalleryAdapter mAdapter = new GalleryAdapter(getActivity(), mData, TypeCellItemEnum.GALLERY_CELL);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView
                        .addOnItemTouchListener(new RecyclerItemClickListener(
                                getActivity(),
                                (view, position) -> {
                                    if (mFragment.getView() != null && position >= 0) {
                                        if (mListener != null) {
                                            HomeActivity.nbOpenAds++;
                                            mListener.onTrackAction("AllFragment", "OpenWallpapers");
                                        }
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
        return R.layout.fragment_all_background;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(
                getFragmentActivity(), 3);
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
