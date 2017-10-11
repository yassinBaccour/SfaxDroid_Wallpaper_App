package com.sami.rippel.presenter;

import com.sami.rippel.model.ViewModel;
import com.sami.rippel.model.entity.WallpaperObject;

import java.util.List;

/**
 * Created by yassine on 11/10/17.
 */

public class AllWallpaperPresenter extends RxPresenter<WallpaperFragmentContract.View> implements WallpaperFragmentContract.Presenter {

    private ViewModel mDataManager;

    public AllWallpaperPresenter(ViewModel mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(WallpaperFragmentContract.View view) {
        super.attachView(view);
    }

    @Override
    public void getWallpaper() {
        if (ViewModel.Current.isWallpapersLoaded()) {
            List<WallpaperObject> wallpaperObjects = ViewModel.Current.getWallpaperCategoryFromName("All").getGetWallpapersList();
            mView.showContent(wallpaperObjects);
        }
    }
}
