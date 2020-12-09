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

    public static void openChooseActivityWithPhoto(Activity activity, Intent dataIntent) {
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

    public static void openChooseActivityFromCamera(Activity activity) {
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
}
