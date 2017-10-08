package com.sami.rippel.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import com.sami.rippel.base.BaseFragment;
import com.sami.rippel.model.Constants;
import com.sami.rippel.model.entity.TypeCellItemEnum;
import com.sami.rippel.ui.activity.GalleryWallpaperActivity;
import com.sami.rippel.allah.R;
import com.sami.rippel.ui.activity.ViewPagerWallpaperActivity;
import com.sami.rippel.model.listner.RecyclerItemClickListener;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.ui.adapter.GalleryAdapter;

import java.util.ArrayList;

public class CategoryFragment extends BaseFragment {

    GalleryAdapter mAdapter;
    CategoryFragment mFragment;

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
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
    public void fillForm() {
        if (ViewModel.Current.isWallpapersLoaded()) {
            //WallpaperCategory wallpaperCategory = ViewModel.Current.retrofitWallpObject.getCategoryList().stream().filter(x -> x.getTitle().equals("ImageCategoryNew")).findFirst().orElse(null);
            mData.clear();
            mData = new ArrayList<>(ViewModel.Current.getWallpaperCategoryFromName("ImageCategoryNew").getGetWallpapersList());
            if (getActivity() != null && ViewModel.Current.fileUtils.isConnected(getActivity()) && mData != null && mData.size() > 0) {
                mAdapter = new GalleryAdapter(getActivity(), mData, TypeCellItemEnum.CATEGORY_NEW_FORMAT);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView
                        .addOnItemTouchListener(new RecyclerItemClickListener(
                                getActivity(),
                                (view, position) -> {
                                    if (position >= 0) {
                                        String CategorName = mAdapter.GetName(position);
                                        if (mListener != null) {
                                            ViewPagerWallpaperActivity.nbOpenAds++;
                                            mListener.onOpenScreenTracker("CategoryGallery");
                                            mListener.onTrackAction("CategoryOpen", CategorName);
                                        }
                                        Intent intent = new Intent(
                                                getActivity(),
                                                GalleryWallpaperActivity.class);
                                        intent.putParcelableArrayListExtra(
                                                Constants.LIST_FILE_TO_SEND_TO_DETAIL_VIEW_PAGER, mData);

                                        if (!CategorName.isEmpty())
                                            intent.putExtra(Constants.DETAIL_IMAGE_POS, CategorName);
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
        return R.layout.fragment_category;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }
}
