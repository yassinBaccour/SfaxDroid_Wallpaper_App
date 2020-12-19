package com.sfaxdroid.detail;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.sfaxdroid.bases.BasePresenter;
import com.sfaxdroid.bases.BaseView;
import com.sfaxdroid.base.SimpleActivity;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by yassin baccour on 10/05/2017.
 */

public abstract class BaseActivity<T extends BasePresenter> extends SimpleActivity implements BaseView {
    protected CompositeDisposable mCompositeDisposable;
    protected T mPresenter;

    protected T instantiatePresenter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        mPresenter = instantiatePresenter();
        if (mPresenter != null)
            mPresenter.attachView(this);
    }

    protected void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

    protected void addSubscribe(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }


    @Override
    protected void onDestroy() {
        if (mPresenter != null)
            mPresenter.detachView();
        super.onDestroy();
        unSubscribe();
    }
}
