package com.sami.rippel.labs.framecollage;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class ActivityLabBase extends AppCompatActivity implements OnClickListener {

    protected Context mContext;
    protected Resources mResources;

    public void initializeBase() {
        mContext = getApplicationContext();
        mResources = getResources();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initializeBase();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View mView = getCurrentFocus();
            int scrcoords[] = new int[2];
            if (mView != null) {
                mView.getLocationOnScreen(scrcoords);
                float x = event.getRawX() + mView.getLeft() - scrcoords[0];
                float y = event.getRawY() + mView.getTop() - scrcoords[1];

                if (event.getAction() == MotionEvent.ACTION_UP
                        && (x < mView.getLeft() || x >= mView.getRight() || y < mView.getTop() || y > mView
                        .getBottom())) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
                            .getWindowToken(), 0);
                }
            }
        }
        return ret;
    }

    @Override
    protected void onDestroy() {
        mContext = null;
        mResources = null;
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
    }

}