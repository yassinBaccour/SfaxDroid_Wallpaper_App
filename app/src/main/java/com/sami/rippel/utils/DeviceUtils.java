package com.sami.rippel.utils;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

import android.util.DisplayMetrics;

import android.view.WindowManager;

import com.sami.rippel.model.Constants;
import com.sami.rippel.model.ViewModel;
import com.sfaxdroid.bases.DeviceListner;
import com.sfaxdroid.base.BitmapUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by yassin baccour on 30/10/2016.
 */

public class DeviceUtils {

    public WindowManager mWindowManager;
    private Context mContext;
    private DeviceListner deviceListner;

    public DeviceUtils(Context mContext) {
        this.mContext = mContext;
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
    }

    public void setDeviceListner(DeviceListner listner) {
        this.deviceListner = listner;
    }

    public void clearCurrentWallpaper() {
        WallpaperManager mWallpaperManager = WallpaperManager
                .getInstance(mContext);
        try {
            mWallpaperManager.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showSnackMessage(CoordinatorLayout mRootLayout, String message) {
        Snackbar snackbar = Snackbar
                .make(mRootLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void openChooseActivityWithPhoto(Activity activity, Intent dataIntent) {
        if (activity != null && dataIntent.getData() != null) {
            Constants.currentBitmaps = BitmapUtils.getPreview(ViewModel.Current.fileUtils.getPath(activity, dataIntent.getData()));
            try {
                Intent intent = new Intent(activity, Class.forName("com.sfaxdroid.framecollage.ChooseActivity"));
                activity.startActivity(intent);
                activity.startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public void openChooseActivityFromCamera(Activity activity) {
        File file = new File(Environment.getExternalStorageDirectory()
                + File.separator + "ic_icon_image.jpg");
        Constants.currentBitmaps = BitmapUtils.decodeSampledBitmapFromFile(
                file.getAbsolutePath(), 1000, 700);
        file.delete();
        try {
            Intent intent1 = new Intent(activity, Class.forName("com.sfaxdroid.framecollage.ChooseActivity"));
            activity.startActivity(intent1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void openFileChooser(Activity ac, int requestCode) {
        if (Environment.getExternalStorageState().equals("mounted")) {
            Intent intent = new Intent();
            intent.setType("ic_icon_image/*");
            intent.setAction(Intent.ACTION_PICK);
            ac.startActivityForResult(
                    Intent.createChooser(intent, "Select Picture:"),
                    requestCode);
        }
    }

    public void openCameraChooser(Activity ac, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory()
                + File.separator + "ic_icon_image.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        ac.startActivityForResult(intent, requestCode);
    }


    private DisplayMetrics getDisplayMetrics() {
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        if (mWindowManager != null) {
            mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics;
        } else return null;
    }

    public int getScreenHeightPixels() {
        return getDisplayMetrics().heightPixels;
    }

    public int getScreenWidthPixels() {
        return getDisplayMetrics().widthPixels;
    }

    public int getCellWidth() {
        if (ViewModel.Current.device.getScreenHeightPixels() < 820 && ViewModel.Current.device.getScreenWidthPixels() < 500) {
            return 133;
        } else {
            return 200;
        }
    }

    public int getCellHeight() {
        if (ViewModel.Current.device.getScreenHeightPixels() < 820 && ViewModel.Current.device.getScreenWidthPixels() < 500) {
            return 133;
        } else {
            return 200;
        }
    }


    public Boolean isConnected(Context con) {

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (wifiInfo.isConnected() || mobileInfo.isConnected()) {
                return true;
            }

        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
        }

        return false;
    }


}
