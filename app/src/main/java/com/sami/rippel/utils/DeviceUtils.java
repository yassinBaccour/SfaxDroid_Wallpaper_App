package com.sami.rippel.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;

import android.util.DisplayMetrics;

import android.view.WindowManager;

import com.sami.rippel.allah.R;
import com.sami.rippel.labs.framecollage.ChooseActivity;
import com.sami.rippel.model.Constants;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.model.entity.IntentTypeEnum;
import com.sami.rippel.model.listner.DeviceListner;
import com.sfaxdroid.base.BitmapUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by yassin baccour on 30/10/2016.
 */

public class DeviceUtils {

    public WindowManager mWindowManager;
    private Context mContext;
    private DeviceListner mDeviceListner;
    private String[] mNotCompatibleRippleDeviceList = {"XIAOMI REDMI 3S",
            "LGE LGLS755", "HUAWEI ALE-L21", "XIAOMI REDMI NOTE 3",
            "ASUS ASUS_Z010D", "LGE LG-K430", "LGE LG-K430", "ONEPLUS ONEPLUS A3003",
            "GENERAL MOBILE GM 5 PLUS", "HUAWEI HUAWEI NXT-L29", "HUAWEI HUAWEI RIO-L01", "HUAWEI HUAWEI CAM-L21",
            "QMOBILE Z14", "HUAWEI ALE-L21", "LENOVO LENOVO PB1-750M", "WIKO LENNY3", "HUAWEI HUAWEI KII-L21",
            "QMOBILE QMOBILE S2 PLUS", "SONY D6503", "LGE LG-H502"};

    public DeviceUtils(Context mContext) {
        this.mContext = mContext;
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
    }

    public void setDeviceListner(DeviceListner listner) {
        this.mDeviceListner = listner;
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

    private boolean appInstalledOrNot(String uri) {
        PackageManager mPackageManager = mContext.getPackageManager();
        boolean app_installed;
        try {
            mPackageManager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public boolean decodeBitmapAndSetAsLiveWallpaper(File file) {
        BitmapFactory.Options mOptions = new BitmapFactory.Options();
        mOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        if (file.exists()) {
            Bitmap mBitmap = BitmapFactory.decodeFile(file.getPath(),
                    mOptions);
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(mContext);
            try {
                if (mBitmap != null) {
                    Bitmap mBackground = Bitmap.createScaledBitmap(mBitmap, ViewModel.Current.device.getScreenWidthPixels(), ViewModel.Current.device.getScreenHeightPixels(), true);
                    if (mBackground != null)
                        wallpaperManager.setBitmap(mBackground);
                    mBitmap.recycle();
                    return true;
                } else
                    return false;
            } catch (IOException ignored) {
                return false;
            }
        } else {
            return false;
        }
    }

    public void shareAllFile(Activity ac, File file) {
        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
        intent.setDataAndType(FileProvider.getUriForFile(mContext,
                mContext.getApplicationContext().getPackageName()
                        + ".provider", file), "ic_icon_image/jpg");
        intent.putExtra("mimeType", "ic_icon_image/jpg");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        ac.startActivityForResult(Intent.createChooser(intent, mContext.getString(R.string.setAs)), 200);
    }

    public Boolean shareFileWithIntentType(Activity ac, File file, IntentTypeEnum intentType) {
        if (appInstalledOrNot(ViewModel.Current.GetIntentNameFromType(intentType))) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("ic_icon_image/*");
            Uri photoURI = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", file);
            shareIntent.putExtra(Intent.EXTRA_STREAM, photoURI);
            shareIntent.setPackage(ViewModel.Current.GetIntentNameFromType(intentType));
            ac.startActivity(shareIntent);
            return true;
        } else
            return false;
    }

    public void showSnackMessage(CoordinatorLayout mRootLayout, String message) {
        Snackbar snackbar = Snackbar
                .make(mRootLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void openChooseActivityWithPhoto(Activity activity, Intent dataIntent) {
        if (activity != null && dataIntent.getData() != null) {
            Constants.currentBitmaps = BitmapUtils.getPreview(ViewModel.Current.fileUtils.getPath(activity, dataIntent.getData()));
            Intent intent = new Intent(activity, ChooseActivity.class);
            activity.startActivity(intent);
        }
    }

    public void openChooseActivityFromCamera(Activity ac) {
        File file = new File(Environment.getExternalStorageDirectory()
                + File.separator + "ic_icon_image.jpg");
        Constants.currentBitmaps = BitmapUtils.decodeSampledBitmapFromFile(
                file.getAbsolutePath(), 1000, 700);
        file.delete();
        Intent intent1 = new Intent(ac, ChooseActivity.class);
        ac.startActivity(intent1);
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

    private void requestPermissions(Activity ac, int REQUEST_CODE_ASK_PERMISSIONS) {
        ActivityCompat.requestPermissions(ac,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE_ASK_PERMISSIONS);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener, Activity ac) {
        new AlertDialog.Builder(ac)
                .setMessage(message)
                .setPositiveButton(mContext.getString(R.string.permissionok), okListener)
                .setNegativeButton(mContext.getString(R.string.permissionnon), null)
                .create()
                .show();
    }

    public void checkPermission(Activity activity, String permissionName) {
        if (ContextCompat.checkSelfPermission(activity,
                permissionName)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    permissionName)) {
                showMessageOKCancel(mContext.getString(R.string.savepermissiondesc),
                        (dialog, which) -> {
                            if (mDeviceListner != null)
                                mDeviceListner.onRequestPermissions();
                        }, activity);
            } else {
                int REQUEST_CODE_ASK_PERMISSIONS = 123;
                requestPermissions(activity, REQUEST_CODE_ASK_PERMISSIONS);
            }
        }
    }

    public boolean isMyServiceRunning(Class<?> serviceClass, Activity ac) {
        ActivityManager manager = (ActivityManager) ac.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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

    public String getBytesDownloaded(int progress, long totalBytes) {
        long bytesCompleted = (progress * totalBytes) / 100;
        if (totalBytes >= 1000000) {
            return ("" + (String.format("%.1f", (float) bytesCompleted / 1000000)) + "/" + (String.format("%.1f", (float) totalBytes / 1000000)) + "MB");
        }
        if (totalBytes >= 1000) {
            return ("" + (String.format("%.1f", (float) bytesCompleted / 1000)) + "/" + (String.format("%.1f", (float) totalBytes / 1000)) + "Kb");

        } else {
            return ("" + bytesCompleted + "/" + totalBytes);
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

    public boolean setWallpaper(Bitmap wallpaper) {
        try {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(mContext);
            int MinimumWidth = wallpaperManager.getDesiredMinimumWidth();
            int MinimumHeight = wallpaperManager.getDesiredMinimumHeight();
            if (MinimumWidth > wallpaper.getWidth() &&
                    MinimumHeight > wallpaper.getHeight() && ViewModel.Current.device.getScreenWidthPixels() < Constants.MIN_WIDHT) {
                int xPadding = Math.max(0, MinimumWidth - wallpaper.getWidth()) / 2;
                int yPadding = Math.max(0, MinimumHeight - wallpaper.getHeight()) / 2;
                Bitmap paddedWallpaper = Bitmap.createBitmap(MinimumWidth, MinimumHeight, Bitmap.Config.ARGB_8888);
                int[] pixels = new int[wallpaper.getWidth() * wallpaper.getHeight()];
                wallpaper.getPixels(pixels, 0, wallpaper.getWidth(), 0, 0, wallpaper.getWidth(), wallpaper.getHeight());
                paddedWallpaper.setPixels(pixels, 0, wallpaper.getWidth(), xPadding, yPadding, wallpaper.getWidth(), wallpaper.getHeight());
                wallpaperManager.setBitmap(paddedWallpaper);
                return true;
            } else {
                wallpaperManager.setBitmap(wallpaper);
                return true;
            }
        } catch (IOException e) {
            return false;
        }
    }
}
