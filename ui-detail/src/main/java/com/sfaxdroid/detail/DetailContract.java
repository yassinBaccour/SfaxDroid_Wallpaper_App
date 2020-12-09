package com.sfaxdroid.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.sfaxdroid.bases.ActionTypeEnum;
import com.sfaxdroid.bases.BasePresenter;
import com.sfaxdroid.bases.BaseView;
import com.sfaxdroid.bases.DeviceListner;

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

        void saveFileToPermanentGallery(String url, Activity context, String appName, DeviceListner deviceListner);

        void saveTempsDorAndDoAction(ActionTypeEnum actionToDo, String url, Context context, String appName);
    }
}
