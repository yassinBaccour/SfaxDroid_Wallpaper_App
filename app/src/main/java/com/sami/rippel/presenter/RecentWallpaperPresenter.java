package com.sami.rippel.presenter;

import com.sami.rippel.model.ViewModel;
import com.sami.rippel.presenter.Contract.WallpaperFragmentContract;
import com.sfaxdroid.base.WallpaperObject;

import java.util.List;

/**
 * Created by yassine on 11/10/17.
 */

public class RecentWallpaperPresenter extends RxPresenter<WallpaperFragmentContract.View> implements WallpaperFragmentContract.Presenter {

    public RecentWallpaperPresenter() {
    }

    @Override
    public void attachView(WallpaperFragmentContract.View view) {
        super.attachView(view);
    }

    @Override
    public void getWallpaper() {
        //FIXME Load From Retrofit
        if (ViewModel.Current.isWallpapersLoaded()) {
            List<WallpaperObject> wallpaperObjects = ViewModel.Current.getWallpaperCategoryFromName("New").getGetWallpapersList();
            mView.showContent(wallpaperObjects);
        }
    }
}
