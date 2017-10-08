package com.sami.rippel.utils;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.bumptech.glide.request.transition.Transition;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.model.Constants;
import com.sami.rippel.allah.R;
import com.sami.rippel.utils.downloadsystem.DecompressZip;
import com.sami.rippel.model.listner.LwpListner;
import com.sami.rippel.model.listner.WallpaperListner;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sami.rippel.model.entity.ActionTypeEnum;

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

/**
 * Created by yassin baccour on 30/10/2016.
 */

public class FileUtils {
    private static final int SAVE_TEMPORARY = 0;
    private static final int SAVE_PERMANENT = 1;
    private Context mContext;
    private WallpaperListner mFileListner;
    private LwpListner mLwpListner;
    private boolean mSavedPermanent = false;

    public FileUtils(Context context) {
        this.mContext = context;
    }

    public void SetListner(WallpaperListner fileListner) {
        this.mFileListner = fileListner;
    }

    public void SetLwpListner(LwpListner lwpListner)
    {
        this.mLwpListner = lwpListner;
    }

    public String getFileName(String path) {
        return path.substring(path.lastIndexOf('/') + 1, path.length());
    }

    private File getPermanentFile(String fileName) {
        return new File(getPermanentDir(), fileName);
    }

    public File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File image = File.createTempFile(imageFileName,".jpg", getTemporaryDir());
        return image;
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

    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        if (!source.isRecycled()) {
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                    source.getHeight(), matrix, true);
        }else
            return null;
    }

    public boolean savePermanentFile(final String url) {
        final boolean savedFinal;
        File temp = getTemporaryFile(getFileName(url));
        if (temp != null) {
            try {
                copyFile(temp, getPermanentFile(getFileName(url)));
                mSavedPermanent = true;
            } catch (IOException ignored) {
            }
        } else {
            Glide.with(mContext).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> glideAnimation) {
                    mSavedPermanent = saveBitmapToStorage(resource, getFileName(url), SAVE_PERMANENT);
                }
            });
        }
        return mSavedPermanent;
    }

    public boolean deleteFile(final String url)
    {
        File file = new File(url);
        return file.delete();
    }

    private List<File> getListFiles(File parentDir)
    {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    inFiles.addAll(getListFiles(file));
                } else {
                    inFiles.add(file);
                }
            }
        }
        return inFiles;
    }

    public List<File> getPermanentDirListFiles()
    {
        return getListFiles(getPermanentDir());
    }

    public List<File> getBasmalaStickersFileList()
    {
        return getListFiles(getBasmalaFileDirDir());
    }


    public boolean saveBitmapToStorage(Bitmap bitmap, String fileName,
                                       int saveOption) {
        File temporaryDir = getTemporaryDir();
        File permanentDir = getPermanentDir();
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

    private String getUrlByScreen(String url) {
        String finalUrl = "";
        if (ViewModel.Current.device.getScreenHeightPixels() < 820 && ViewModel.Current.device.getScreenWidthPixels() < 500)
            finalUrl = url.replace("/islamicimages/", "/islamicimagesmini/");
        else
            finalUrl = url;
        return finalUrl;
    }

    public void saveToFileToTempsDirAndChooseAction(String url, final ActionTypeEnum action) {
        final String fileName = getFileName(url);
        Glide.with(mContext).asBitmap().load(getUrlByScreen(url))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                Transition<? super Bitmap> glideAnimation) {
                        saveBitmapToStorage(resource, fileName, SAVE_TEMPORARY);
                        if (action == ActionTypeEnum.CROP)
                            mFileListner.onGoToCropActivity();
                        else if (action == ActionTypeEnum.OPEN_NATIV_CHOOSER)
                            mFileListner.onOpenNativeSetWallChoose();
                        else if (action == ActionTypeEnum.MOVE_PERMANENT_DIR)
                            mFileListner.onMoveFileToPermanentGallery();
                        else if (action == ActionTypeEnum.SHARE_FB)
                            mFileListner.onOpenWithFaceBook();
                        else if (action == ActionTypeEnum.SHARE_INSTA)
                            mFileListner.onOpenWithInstagram();
                        else if (action == ActionTypeEnum.SEND_LWP)
                            mFileListner.onSendToRippleLwp();
                        else if (action == ActionTypeEnum.SHARE_SNAP_CHAT)
                            mFileListner.onShareWhitApplication();
                        else if (action == ActionTypeEnum.SKYBOX_LWP)
                            mLwpListner.onSendToLwp();
                        resource.recycle();
                    }
                });
    }

    public boolean isSavedToStorage(String fileName) {
        File temporaryFile = getTemporaryFile(fileName);
        return temporaryFile.exists();
    }

    private File getTemporaryDir() {
        File temporaryDir = new File(Environment.getExternalStorageDirectory(),
                mContext.getString(R.string.app_namenospace) + "/temp");
        if (!temporaryDir.exists()) {
            temporaryDir.mkdirs();
        }
        return temporaryDir;
    }

    public File getTemporaryFile(String fileName) {
        return new File(getTemporaryDir(), fileName);
    }

    private File getPermanentDir() {
        File permanentDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                mContext.getString(R.string.app_namenospace));
        if (!permanentDir.exists()) {
            permanentDir.mkdirs();
        }
        return permanentDir;
    }

    private File getBasmalaFileDirDir() {

        File zipDestination = new File( mContext.getExternalFilesDir(""), Constants.KEY_BASMALA_FOLDER_CONTAINER);
        if (!zipDestination.exists()) {
            zipDestination.mkdirs();
        }
        return zipDestination;
    }

    private File getTemporaryDouaDir()
    {
        File temporaryDir = new File(Environment.getExternalStorageDirectory(),
            mContext.getString(R.string.app_namenospace) + "/temp");
        if (!temporaryDir.exists()) {
            temporaryDir.mkdirs();
        }
        return temporaryDir;
    }

    private void scanFile(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        mContext.sendBroadcast(intent);
    }

    private boolean setWallpaper(Bitmap wallpaper) {
        try {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(mContext);
            int MinimumWidth = wallpaperManager.getDesiredMinimumWidth();
            int MinimumHeight =  wallpaperManager.getDesiredMinimumHeight();
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

    public boolean setAsWallpaper(Bitmap bitmap) {
        return setWallpaper(bitmap);
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

    public void unzipFile(File zipFile, File destination) {
        DecompressZip decomp = new DecompressZip(zipFile.getPath(),
                destination.getPath() + File.separator);
        decomp.unzip();
    }

    static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth,
                                              int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        int inSampleSize = 1;
        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;
        if (expectedWidth > reqWidth) {
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    String getPath(Activity ac, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        if (projection != null) {
            Cursor cursor = ac.managedQuery(uri, projection, null, null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }else{
            return "";
        }
    }

    Bitmap getPreview(String fileName) {
        File image = new File(fileName);
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(image.getPath(), bounds);
        if ((bounds.outWidth == -1) || (bounds.outHeight == -1)) {
            return null;
        }
        //int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight
        //        : bounds.outWidth;
        //BitmapFactory.Options opts = new BitmapFactory.Options();
        //opts.inSampleSize = originalSize / 20;
        return BitmapFactory.decodeFile(image.getPath());
    }

    public Boolean isFileExistInDataFolder(String fileName)
    {
        File filesDir = mContext.getExternalFilesDir("");
        File ZipFile = new File(filesDir, fileName);
        return ZipFile.exists();

    }

    public Bitmap changeImageColor(Bitmap sourceBitmap, int color) {
        try {
            Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0,
                    sourceBitmap.getWidth() - 1, sourceBitmap.getHeight() - 1);
            Paint p = new Paint();
            ColorFilter filter = new LightingColorFilter(color, 1);
            p.setColorFilter(filter);
            Canvas canvas = new Canvas(resultBitmap);
            canvas.drawBitmap(resultBitmap, 0, 0, p);
            return resultBitmap;
        } catch (Exception e) {
           return null;
        }
    }


    public Drawable covertBitmapToDrawable(Context context, Bitmap bitmap) {
        Drawable d = new BitmapDrawable(context.getResources(), bitmap);
        return d;
    }

    public Bitmap convertDrawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
