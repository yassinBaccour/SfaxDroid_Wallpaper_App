package com.sami.rippel.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.sfaxdroid.bases.LwpListener;
import com.sfaxdroid.bases.WallpaperListener;


/**
 * Created by yassin baccour on 30/10/2016.
 */

public class FileUtils {

    private Context mContext;
    private WallpaperListener wallpaperListener;
    private LwpListener lwpListener;

    public FileUtils(Context context) {
        this.mContext = context;
    }

    public void SetListener(WallpaperListener wallpaperListener) {
        this.wallpaperListener = wallpaperListener;
    }

    public void SetLwpListener(LwpListener lwpListener) {
        this.lwpListener = lwpListener;
    }

    String getPath(Activity ac, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = ac.managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
