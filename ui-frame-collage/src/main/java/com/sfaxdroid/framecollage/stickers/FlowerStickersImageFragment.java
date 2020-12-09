package com.sfaxdroid.framecollage.stickers;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sami.rippel.allah.R;
import com.sami.rippel.model.Constants;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.model.entity.TypeCellItemEnum;
import com.sami.rippel.model.entity.WallpaperObject;
import com.sami.rippel.utils.RecyclerItemClickListener;
import com.sami.rippel.model.listner.StickersListener;
import com.sami.rippel.ui.adapter.GalleryAdapter;

import java.util.ArrayList;
import java.util.List;

public class FlowerStickersImageFragment extends Fragment {

    private StickersListener stickersListner;
    private RecyclerView mListView;
    private GalleryAdapter mAdapter;
    private ArrayList<WallpaperObject> mData = new ArrayList<>();

    public static FlowerStickersImageFragment newInstance() {
        return new FlowerStickersImageFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mListView.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(),
                (view1, pos) -> {
                    if (pos >= 0 && mAdapter != null && mAdapter.getItemCount() >= 0) {

                        String url = mAdapter.GetPreviewUrl(pos).replace(Constants.PREVIEW_JPG, Constants.PNG_FORMAT);
                        if (stickersListner != null && url != null && !url.isEmpty()) {
                            stickersListner.downloadAndPutImageAtScreen(url);
                        }
                    }
                }));
        if (stickersListner != null && this.getView() != null)
            stickersListner.hideProgressLoader();
    }

    public void fillList(String mPrefix) {
        if (ViewModel.Current.isWallpapersLoaded()) {
            mData.clear();
            mData = new ArrayList<>();
            List<WallpaperObject> mImageListByType = ViewModel.Current.getWallpaperCategoryFromName(Constants.CATEGORY_STIKERS)
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_stickers, container, false);
        mListView = (RecyclerView) rootView.findViewById(R.id.list_tatoo);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mListView.setLayoutManager(layoutManager);
        mListView.setHasFixedSize(true);
        fillList(Constants.CATEGORY_FLOWER);
        return rootView;
    }

    public void setListener(StickersListener stickersListner) {
        this.stickersListner = stickersListner;
    }
}