package com.sami.rippel.labs.stickers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.sami.rippel.allah.R;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.model.entity.TypeCellItemEnum;
import com.sami.rippel.model.entity.WallpaperObject;
import com.sami.rippel.model.listner.RecyclerItemClickListener;
import com.sami.rippel.model.listner.StickersListner;
import com.sami.rippel.ui.adapter.GalleryAdapter;

import java.util.ArrayList;
import java.util.List;

public class FrameImageFragment extends Fragment {

    StickersListner stickersListner;
    RecyclerView mListView;
    private GalleryAdapter mAdapter;
    private ArrayList<WallpaperObject> mData = new ArrayList<>();

    public static FrameImageFragment newInstance() {
        return new FrameImageFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mListView.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(),
                (view1, pos) -> {

                    if (pos == 0)
                    {
                        stickersListner.RemoveFrame();
                    }
                    if (pos > 0 && mAdapter != null && mAdapter.getItemCount() >= 0) {

                        String url = mAdapter.GetPreviewUrl(pos).replace("_preview.jpg", ".png");
                        if (stickersListner != null && url != null && !url.isEmpty()) {
                            stickersListner.downloadAndPutFrameAtScreen(url, "square");
                        }
                    }
                }));
        if (stickersListner != null)
            stickersListner.hideProgressLoader();
    }

    public void fillList(String mPrefix) {
        if (ViewModel.Current.isWallpapersLoaded()) {
            mData.clear();
            mData = new ArrayList<>();
            List<WallpaperObject> mImageListByType = ViewModel.Current.getWallpaperCategoryFromName("Stikers")
                    .getGetWallpapersList();
            for (WallpaperObject wall : mImageListByType) {
                if (wall.getName() != null && wall.getName().equals(mPrefix))
                    mData.add(wall);
            }
            mAdapter = new GalleryAdapter(getActivity(), mData, TypeCellItemEnum.TYPE_ONE);
            mListView.setAdapter(mAdapter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_stickers, container, false);
        mListView = (RecyclerView) rootView.findViewById(R.id.list_tatoo);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mListView.setLayoutManager(layoutManager);
        mListView.setHasFixedSize(true);
        fillList("frame");
        return rootView;
    }

    public void setListener(StickersListner stickersListner) {
        this.stickersListner = stickersListner;
    }
}
