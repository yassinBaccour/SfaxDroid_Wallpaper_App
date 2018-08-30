package com.sami.rippel.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;

import com.sami.rippel.allah.R;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.model.entity.ActionTypeEnum;
import com.sami.rippel.presenter.Contract.DetailContract;
import com.soundcloud.android.crop.Crop;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yassine on 12/10/17.
 */

public class DetailPresenter extends RxPresenter<DetailContract.View> implements DetailContract.Presenter {

    private ViewModel mDataManager;

    public DetailPresenter(ViewModel mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(DetailContract.View view) {
        super.attachView(view);
    }

    @Override
    public void setAsWallpaper(String url) {
        mView.hideLoading();
        addSubscribe(Flowable.fromCallable(() -> ViewModel.Current.device.decodeBitmapAndSetAsLiveWallpaper(ViewModel.Current.fileUtils.
                getTemporaryFile(ViewModel.Current.fileUtils.getFileName(url))))
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
        addSubscribe(Flowable.fromCallable(() -> ViewModel.Current.fileUtils.setAsWallpaper(MediaStore.Images.Media.getBitmap(
                context.getContentResolver(), Crop.getOutput(result))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(error -> false)
                .subscribe(setSuccess -> {
                    if (setSuccess) {
                        mView.showSnackMsg(context.getString(R.string.detail_snack_set_success));
                    } else {
                        mView.showSnackMsg(context.getString(R.string.detail_snack_set_failure));
                    }
                })
        );

    }

    @Override
    public void saveFileToPermanentGallery(String url, Activity context) {
        addSubscribe(Flowable.fromCallable(() -> ViewModel.Current.fileUtils.savePermanentFile(url))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        mView.showSnackMsg(context.getString(R.string.detail_snack_save_success));
                    } else {
                        mView.showSnackMsg(context.getString(R.string.detail_snack_save_failure));
                        ViewModel.Current.device.checkPermission(context,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                    mView.hideLoading();
                })
        );
    }

    @Override
    public void saveTempsDorAndDoAction(ActionTypeEnum actionToDo, String url) {
        addSubscribe(Flowable.fromCallable(() -> ViewModel.Current.fileUtils.isSavedToStorage(ViewModel.Current.fileUtils
                .getFileName(url)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    mView.onSaveTempsDorAndDoAction(aBoolean, actionToDo);
                })
        );
    }
}
