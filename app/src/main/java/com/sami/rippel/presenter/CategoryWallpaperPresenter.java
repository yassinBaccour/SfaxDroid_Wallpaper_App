package com.sami.rippel.presenter;

import com.sami.rippel.model.ViewModel;
import com.sami.rippel.presenter.Contract.WallpaperFragmentContract;
import com.sfaxdroid.base.WallpaperObject;

import java.util.List;

/**
 * Created by yassine on 11/10/17.
 */

public class CategoryWallpaperPresenter extends RxPresenter<WallpaperFragmentContract.View> implements WallpaperFragmentContract.Presenter {

    public CategoryWallpaperPresenter() {
    }

    @Override
    public void attachView(WallpaperFragmentContract.View view) {
        super.attachView(view);
    }

    @Override
    public void getWallpaper() {
        if (ViewModel.Current.isWallpapersLoaded()) {
            List<WallpaperObject> wallpaperObjects = ViewModel.Current.getWallpaperCategoryFromName("ImageCategoryNew").getGetWallpapersList();
            mView.showContent(wallpaperObjects);
        }
    }
}
