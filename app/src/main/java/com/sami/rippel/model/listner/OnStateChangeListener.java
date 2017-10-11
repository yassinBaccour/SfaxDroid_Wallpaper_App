package com.sami.rippel.model.listner;

import android.support.annotation.NonNull;

import com.sami.rippel.model.ViewModel;
import com.sami.rippel.model.entity.StateEnum;

/**
 * Created by yassine on 11/10/17.
 */

public interface OnStateChangeListener {
    void onStateChange(@NonNull StateEnum state);
}