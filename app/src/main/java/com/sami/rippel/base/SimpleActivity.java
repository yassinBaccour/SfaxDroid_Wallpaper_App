package com.sami.rippel.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.sami.rippel.allah.WallpaperApplication;

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

    protected void setToolBar(Toolbar toolbar, String title) {
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
