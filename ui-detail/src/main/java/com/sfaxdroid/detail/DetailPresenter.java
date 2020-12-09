package com.sfaxdroid.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;

import com.sfaxdroid.bases.ActionTypeEnum;
import com.sfaxdroid.bases.DeviceListner;
import com.sfaxdroid.base.FileUtils;
import com.sfaxdroid.base.Utils;
import com.soundcloud.android.crop.Crop;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yassine on 12/10/17.
 */

public class DetailPresenter extends RxPresenter<DetailContract.View> implements DetailContract.Presenter {


    @Override
    public void attachView(DetailContract.View view) {
        super.attachView(view);
    }

    @Override
    public void setAsWallpaper(String url, Context context, String appName) {
        mView.hideLoading();
        addSubscribe(Flowable.fromCallable(() -> DetailUtils.Companion.decodeBitmapAndSetAsLiveWallpaper(FileUtils.Companion.
                getTemporaryFile(FileUtils.Companion.getFileName(url), context, appName), context))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> {
                    if (t)
                        mView.showSnackMsg("Success");
                    else
                        mView.showSnackMsg("Error");
                })
        );
    }

    @Override
    public void handleCrop(Intent result, Context context) {
        addSubscribe(Flowable.fromCallable(() -> DetailUtils.Companion.setWallpaper(MediaStore.Images.Media.getBitmap(
                context.getContentResolver(), Crop.getOutput(result)), context))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(error -> false)
                .subscribe(setSuccess -> {
                    if (setSuccess) {
                        mView.showSnackMsg(context.getString(R.string.setSwallSucess));
                    } else {
                        mView.showSnackMsg(context.getString(R.string.setSwallNotSucess));
                    }
                })
        );

    }

    @Override
    public void saveFileToPermanentGallery(String url, Activity context, String appName, DeviceListner deviceListner) {
        addSubscribe(Flowable.fromCallable(() -> FileUtils.Companion.savePermanentFile(url, context, appName))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        mView.showSnackMsg(context.getString(R.string.setSwallSucess));
                    } else {
                        mView.showSnackMsg(context.getString(R.string.setSwallNotSucess));
                        Utils.Companion.checkPermission(context,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE, deviceListner);
                    }
                    mView.hideLoading();
                })
        );
    }

    @Override
    public void saveTempsDorAndDoAction(ActionTypeEnum actionToDo, String url, Context context, String appName) {
        addSubscribe(Flowable.fromCallable(() -> FileUtils.Companion.isSavedToStorage(
                FileUtils.Companion.getFileName(url), context, appName))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    mView.onSaveTempsDorAndDoAction(aBoolean, actionToDo);
                })
        );
    }
}
