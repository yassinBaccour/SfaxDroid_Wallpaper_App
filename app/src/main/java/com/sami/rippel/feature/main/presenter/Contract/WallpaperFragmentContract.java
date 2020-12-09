package com.sami.rippel.feature.main.presenter.Contract;

import com.sfaxdroid.bases.BasePresenter;
import com.sfaxdroid.bases.BaseView;
import com.sfaxdroid.base.WallpaperObject;

import java.util.List;

/**
 * Created by yassine on 11/10/17.
 */

public interface WallpaperFragmentContract {

    interface View extends BaseView {
        void showContent(List<WallpaperObject> mList);
    }

    interface Presenter extends BasePresenter<View> {
        void getWallpaper();
    }

}
