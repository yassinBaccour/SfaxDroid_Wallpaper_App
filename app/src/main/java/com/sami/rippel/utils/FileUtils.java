package com.sami.rippel.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sami.rippel.allah.R;
import com.sami.rippel.model.Constants;
import com.sami.rippel.model.entity.ActionTypeEnum;
import com.sami.rippel.model.listner.LwpListener;
import com.sami.rippel.model.listner.WallpaperListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by yassin baccour on 30/10/2016.
 */

public class FileUtils {
    private static final int SAVE_TEMPORARY = 0;
    private static final int SAVE_PERMANENT = 1;
    private Context mContext;
    private WallpaperListener wallpaperListener;
    private LwpListener lwpListener;
    private boolean mSavedPermanent = false;

    public FileUtils(Context context) {
        this.mContext = context;
    }

    public void SetListener(WallpaperListener wallpaperListener) {
        this.wallpaperListener = wallpaperListener;
    }

    public void SetLwpListener(LwpListener lwpListener) {
        this.lwpListener = lwpListener;
    }


    private void copyFile(File sourceLocation, File targetLocation)
            throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < sourceLocation.listFiles().length; i++) {

                copyFile(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {
            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }

    public boolean savePermanentFile(final String url) {
        final boolean savedFinal;
        File temp = com.sfaxdroid.base.FileUtils.Companion.getTemporaryFile(com.sfaxdroid.base.FileUtils.Companion.getFileName(url), mContext, mContext.getString(com.sfaxdroid.timer.R.string.app_namenospace));
        if (temp != null) {
            try {
                copyFile(temp, com.sfaxdroid.base.FileUtils.Companion.getPermanentFile(com.sfaxdroid.base.FileUtils.Companion.getFileName(url), mContext, mContext.getString(com.sfaxdroid.timer.R.string.app_namenospace)));
                mSavedPermanent = true;
            } catch (IOException ignored) {
            }
        } else {
            Glide.with(mContext).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> glideAnimation) {
                    mSavedPermanent = saveBitmapToStorage(resource, com.sfaxdroid.base.FileUtils.Companion.getFileName(url), SAVE_PERMANENT);
                }
            });
        }
        return mSavedPermanent;
    }


    public boolean saveBitmapToStorage(Bitmap bitmap, String fileName,
                                       int saveOption) {
        File temporaryDir = com.sfaxdroid.base.FileUtils.Companion.getTemporaryDir(mContext, mContext.getString(com.sfaxdroid.timer.R.string.app_namenospace));
        File permanentDir = com.sfaxdroid.base.FileUtils.Companion.getPermanentDir(mContext, mContext.getString(com.sfaxdroid.timer.R.string.app_namenospace));
        File file;
        switch (saveOption) {
            case SAVE_PERMANENT:
                file = new File(permanentDir, fileName);
                break;
            case SAVE_TEMPORARY:
                file = new File(temporaryDir, fileName);
                break;
            default:
                file = new File(temporaryDir, fileName);
                break;
        }
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            if (saveOption == SAVE_PERMANENT) {
                scanFile(file);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getUrlByScreen(String url, int screenHeightPixels, int screenWidthPixels) {
        String finalUrl = "";
        if (screenHeightPixels < 820 && screenWidthPixels < 500)
            finalUrl = url.replace("/islamicimages/", "/islamicimagesmini/");
        else
            finalUrl = url;
        return finalUrl;
    }

    public void saveToFileToTempsDirAndChooseAction(String url, final ActionTypeEnum action, int screenHeightPixels, int screenWidthPixels) {
        Glide.with(mContext).asBitmap().load(getUrlByScreen(url, screenHeightPixels, screenWidthPixels
        ))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                Transition<? super Bitmap> glideAnimation) {
                        boolean isSaved = saveBitmapToStorage(resource, com.sfaxdroid.base.FileUtils.Companion.getFileName(url), SAVE_TEMPORARY);
                        if (isSaved) {
                            if (action == ActionTypeEnum.CROP)
                                wallpaperListener.onGoToCropActivity();
                            else if (action == ActionTypeEnum.OPEN_NATIV_CHOOSER)
                                wallpaperListener.onOpenNativeSetWallChoose();
                            else if (action == ActionTypeEnum.MOVE_PERMANENT_DIR)
                                wallpaperListener.onMoveFileToPermanentGallery();
                            else if (action == ActionTypeEnum.SHARE_FB)
                                wallpaperListener.onOpenWithFaceBook();
                            else if (action == ActionTypeEnum.SHARE_INSTA)
                                wallpaperListener.onOpenWithInstagram();
                            else if (action == ActionTypeEnum.SEND_LWP)
                                wallpaperListener.onSendToRippleLwp();
                            else if (action == ActionTypeEnum.SHARE_SNAP_CHAT)
                                wallpaperListener.onShareWhitApplication();
                            else if (action == ActionTypeEnum.SKYBOX_LWP)
                                lwpListener.onSendToLwp();
                        } else {
                            wallpaperListener.onFinishActivity();
                            resource.recycle();
                        }
                    }
                });
    }


    private void scanFile(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        mContext.sendBroadcast(intent);
    }

    public String getPath(Activity ac, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        if (projection != null) {
            Cursor cursor = ac.managedQuery(uri, projection, null, null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else {
            return "";
        }
    }
}
