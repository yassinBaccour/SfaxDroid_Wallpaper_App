package com.sami.rippel.base;

/**
 * Created by yassine on 11/10/17.
 */

public interface BaseView {

    void showErrorMsg(String msg);

    void useNightMode(boolean isNight);

    //=======  State  =======
    void stateError();

    void stateEmpty();

    void stateLoading();

    void stateMain();
}
