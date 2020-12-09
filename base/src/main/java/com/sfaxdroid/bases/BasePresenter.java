package com.sfaxdroid.bases;

/**
 * Created by yassine on 11/10/17.
 */

public interface BasePresenter<T extends BaseView> {
    void attachView(T view);
    void detachView();
}