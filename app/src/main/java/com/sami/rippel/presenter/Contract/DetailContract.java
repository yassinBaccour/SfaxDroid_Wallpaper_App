package com.sami.rippel.presenter.Contract;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.sami.rippel.base.BasePresenter;
import com.sami.rippel.base.BaseView;
import com.sami.rippel.model.entity.ActionTypeEnum;

/**
 * Created by yassine on 12/10/17.
 */

public interface DetailContract {
    interface View extends BaseView {
        void onSaveTempsDorAndDoAction(Boolean aBoolean, ActionTypeEnum actionToDo);
    }

    interface Presenter extends BasePresenter<View> {

        void setAsWallpaper(String url, Context context, String appName);

        void handleCrop(Intent result, Context context);

        void saveFileToPermanentGallery(String url, Activity context);

        void saveTempsDorAndDoAction(ActionTypeEnum actionToDo, String url, Context context, String appName);
    }
}
