package com.sami.rippel.presenter;

import com.sami.rippel.base.BasePresenter;
import com.sami.rippel.base.BaseView;
import com.sami.rippel.model.entity.WallpaperObject;

import org.rajawali3d.wallpaper.Wallpaper;

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
