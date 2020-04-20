package com.sami.rippel.base;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sami.rippel.WallpaperApplication;

/**
 * Created by yassine on 11/10/17.
 */

public abstract class SimpleActivity extends AppCompatActivity {

    protected Activity mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        mContext = this;
        onViewCreated();
        WallpaperApplication.getInstance().addActivity(this);
        initEventAndData();
    }

    protected void onViewCreated() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WallpaperApplication.getInstance().removeActivity(this);
    }

    protected abstract int getLayout();

    protected abstract void initEventAndData();
}
