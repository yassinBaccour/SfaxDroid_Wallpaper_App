package com.sami.rippel.model.listner;

import com.sami.rippel.model.entity.ServiceErrorFromEnum;

/**
 * Created by yassin baccour on 13/04/2017.
 */

public interface RetrofitDeviceListner {
    void onFillAdapterWithServiceData();

    void onStartCheckUpdateNewWallpapersDataBase();

    void onUpdateNotNeeded();

    void onRestartCheckIfCallError(ServiceErrorFromEnum error);
}
