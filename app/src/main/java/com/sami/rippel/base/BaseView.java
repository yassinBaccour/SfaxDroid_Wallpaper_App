package com.sami.rippel.base;

/**
 * Created by yassine on 11/10/17.
 */

public interface BaseView {

    //Will be Implemented in All activity
    //In part of View
    //can be user in Presenter with mView
    //Declared in RxPresenter With "protected T mView;"

    void showSnackMsg(String msg);

    void showLoading();

    void hideLoading();

    void showADS();

}
