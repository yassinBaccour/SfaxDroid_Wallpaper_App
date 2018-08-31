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
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.sami.rippel.allah.R;
import com.sami.rippel.labs.framecollage.ChooseActivity;
import com.sami.rippel.model.Constants;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.model.entity.IntentTypeEnum;
import com.sami.rippel.model.listner.DeviceListner;
import com.sami.rippel.ui.activity.ViewPagerWallpaperActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by yassin baccour on 30/10/2016.
 */

public class DeviceUtils {

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
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

    public void setmDeviceListner(DeviceListner mDeviceListner) {
        this.mDeviceListner = mDeviceListner;
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

    public void checkNativeLibraryLoaded() {
        try {
            Set<String> mLibs = new HashSet<String>();
            String mFile = "/proc/" + android.os.Process.myPid() + "/maps";
            BufferedReader mReader = new BufferedReader(new FileReader(mFile));
            String line;
            while ((line = mReader.readLine()) != null) {
                if (line.endsWith(".so")) {
                    int n = line.lastIndexOf(" ");
                    mLibs.add(line.substring(n + 1));
                }
            }
            Log.d("Ldd", mLibs.size() + " libraries:");
            for (String lib : mLibs) {
                if (lib.contains("libbufferutil"))
                    Log.d("Ldd", lib);
            }
        } catch (IOException e) {
            // Do some error handling...
        }
    }

    public void checkDeviceMemory(ViewPagerWallpaperActivity.LwpTypeEnum typeLwp) {
        Double mTotalMemoryMegaByte = 0.0;
        ActivityManager mActManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            mActManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        }
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        mActManager.getMemoryInfo(memInfo);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            mTotalMemoryMegaByte = ((memInfo.totalMem / 1024.0) / 1024.0);
            if (mTotalMemoryMegaByte < 500.0 && mTotalMemoryMegaByte > 100.00) {
                Log.d("TotalMemory :", mTotalMemoryMegaByte + "");
            }
        }
        Log.d("Total Memory :", mTotalMemoryMegaByte + "");
    }

    /*
    public void openRippleLwp(Activity ac) {
        try {
            Intent intent = new Intent(
                    WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
            intent.putExtra(
                    WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                    new ComponentName(mContext, IslamicWallpaper.class));
            ac.startActivity(intent);
        } catch (Exception e) {
            try {
                Intent intent = new Intent();
                intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
                ac.startActivity(intent);
            } catch (Exception ignored) {
            }
        }
    }
    */

    public void shareFileAll(Activity ac, File file) {
        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
        intent.setDataAndType(Uri.fromFile(file), "image/jpg");
        intent.putExtra("mimeType", "image/jpg");
        ac.startActivityForResult(Intent.createChooser(intent, mContext.getString(R.string.setAs)), 200);
    }

    public Boolean ShareFileWithIntentType(Activity ac, File file, IntentTypeEnum intentType) {
        if (appInstalledOrNot(ViewModel.Current.GetIntentNameFromType(intentType))) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("image/*");
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

    public void openChooseActivityWithPhoto(Activity ac, Intent dataIntent) {
        if (ac != null && dataIntent.getData() != null) {
            Constants.currentBitmaps = ViewModel.Current.bitmapUtils.getPreview(ViewModel.Current.fileUtils.getPath(ac, dataIntent.getData()));
            Intent intent = new Intent(ac, ChooseActivity.class);
            ac.startActivity(intent);
        }
    }

    public void openChooseActivityFromCamera(Activity ac) {
        File file = new File(Environment.getExternalStorageDirectory()
                + File.separator + "image.jpg");
        Constants.currentBitmaps = ViewModel.Current.bitmapUtils.decodeSampledBitmapFromFile(
                file.getAbsolutePath(), 1000, 700);
        file.delete();
        Intent intent1 = new Intent(ac, ChooseActivity.class);
        ac.startActivity(intent1);
    }

    public void openFileChooser(Activity ac, int requestCode) {
        if (Environment.getExternalStorageState().equals("mounted")) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            ac.startActivityForResult(
                    Intent.createChooser(intent, "Select Picture:"),
                    requestCode);
        }
    }

    public void openCameraChooser(Activity ac, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory()
                + File.separator + "image.jpg");
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

    public void checkPermission(Activity ac, String permissionName) {
        if (ContextCompat.checkSelfPermission(ac,
                permissionName)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ac,
                    permissionName)) {
                showMessageOKCancel(mContext.getString(R.string.savepermissiondesc),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mDeviceListner != null)
                                    mDeviceListner.onRequestPermissions();
                            }
                        }, ac);
            } else {
                requestPermissions(ac, REQUEST_CODE_ASK_PERMISSIONS);
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

    public Boolean isDeviceCompatibleWaterRipple() {
        String deviceName = getDeviceName();
        for (String x : mNotCompatibleRippleDeviceList) {
            if (x.equals(deviceName))
                return false;
        }
        return true;
    }

    private String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }

    public int[] getRealScreenSize(Activity activity) {
        int[] size = new int[2];
        int screenWidth = 0, screenHeight = 0;
        WindowManager w = activity.getWindowManager();
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try {
                screenWidth = (Integer) Display.class.getMethod("getRawWidth")
                        .invoke(d);
                screenHeight = (Integer) Display.class
                        .getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored) {
            }
        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17)
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d,
                        realSize);
                screenWidth = realSize.x;
                screenHeight = realSize.y;
            } catch (Exception ignored) {
            }
        size[0] = screenWidth;
        size[1] = screenHeight;
        return size;
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

    public int getCellWidht() {
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

    public String GetBytesDownloaded(int progress, long totalBytes) {
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
