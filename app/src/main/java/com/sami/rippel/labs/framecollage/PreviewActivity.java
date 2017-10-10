package com.sami.rippel.labs.framecollage;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.myandroid.views.MultiTouchListener;
import com.sami.rippel.allah.R;
import com.sami.rippel.model.Constants;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.views.GlideApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class PreviewActivity extends ActivityLabBase implements OnTouchListener {
    private enum ShareTo {
        facebook,
        instagram,
        AppSystem,
        ImgSystem
    }

    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private int key = 0;
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private float d = 0f;
    private float newRot = 0f;
    private float[] lastEvent = null;
    private ImageView mView1, mView2;
    private FrameLayout mFramelayout;
    private ProgressBar mProgressBar;
    private String mImageUrl;
    private BottomSheetLayout mBottomSheet;
    private CoordinatorLayout mRootLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_create_frame);
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            mImageUrl = extras.getString("ImageUrl");
        Initializetion();
    }

    private void Initializetion() {
        mView1 = (ImageView) findViewById(R.id.imageView);
        mView2 = (ImageView) findViewById(R.id.imageView2);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mBottomSheet = (BottomSheetLayout) findViewById(R.id.bottomsheetLayout);
        mRootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenuSheet(MenuSheetView.MenuType.LIST);
            }
        });
        mFramelayout = (FrameLayout) findViewById(R.id.frame);
        mView1.setImageBitmap(Constants.currentBitmaps);
        mView1.setScaleType(ImageView.ScaleType.CENTER);
        mProgressBar.setVisibility(View.VISIBLE);
        GlideApp.with(this).load(GetUrlByScreen(mImageUrl)).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource,
                                        Transition<? super Drawable> glideAnimation) {
                mView2.setImageDrawable(resource);
                mProgressBar.setVisibility(View.GONE);
            }
        });
        mView1.setOnTouchListener(new MultiTouchListener());
    }

    public String GetUrlByScreen(String urlToChange) {
        String url = "";
        if (ViewModel.Current.device.getScreenHeightPixels() < Constants.MIN_HEIGHT && ViewModel.Current.device.getScreenWidthPixels() < Constants.MIN_WIDHT)
            url = urlToChange.replace("/islamicimages/", "/islamicimagesmini/");
        else
            url = urlToChange;
        return url;
    }

    private void showMenuSheet(MenuSheetView.MenuType menuType) {
        MenuSheetView menuSheetView = new MenuSheetView(this, menuType, "", getMenuSheetListener());
        menuSheetView.inflateMenu(R.menu.menu_preview);
        mBottomSheet.showWithSheetView(menuSheetView);
    }

    private void dismissMenuSheet() {
        if (mBottomSheet.isSheetShowing()) {
            mBottomSheet.dismissSheet();
        }
    }

    public MenuSheetView.OnMenuItemClickListener getMenuSheetListener() {
        return new MenuSheetView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.buttonWallpaper:
                        SaveOrSetWallpapers(true);
                        break;
                    case R.id.buttonChooser:
                        ShareToSocialMedia(ShareTo.ImgSystem);
                    case R.id.buttonSave:
                        SaveOrSetWallpapers(false);
                        break;
                    case R.id.buttonSareInsta:
                        ShareToSocialMedia(ShareTo.instagram);
                        break;
                    case R.id.buttonSareFb:
                        ShareToSocialMedia(ShareTo.facebook);
                        break;
                    case R.id.buttonShare:
                        ShareToSocialMedia(ShareTo.AppSystem);
                }
                dismissMenuSheet();
                return true;
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void ShowSnackMessage(String message) {
        Snackbar snackbar = Snackbar
                .make(mRootLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @SuppressLint("NewApi")
    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) mView1;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                mode = DRAG;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                d = rotation(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    float dx = event.getX() - start.x;
                    float dy = event.getY() - start.y;
                    matrix.postTranslate(dx, dy);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = (newDist / oldDist);
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                    if (lastEvent != null && event.getPointerCount() == 2) {
                        newRot = rotation(event);
                        float r = newRot - d;
                        float[] values = new float[9];
                        matrix.getValues(values);
                        matrix.postRotate(r, view.getMeasuredWidth() / 2,
                                view.getMeasuredHeight() / 2);
                    }
                }
                break;
        }

        view.setImageMatrix(matrix);
        return true;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    private void SaveOrSetWallpapers(Boolean boolean1) {
        mFramelayout.setDrawingCacheEnabled(true);
        Bitmap bitmap = mFramelayout.getDrawingCache();
        File newDir = new File(Constants.GT_FOLDER_PATH);
        newDir.mkdirs();
        Random gen = new Random();
        int n = 10000;
        n = gen.nextInt(n);
        String fotoname = n + ".jpg";
        File file = new File(newDir, fotoname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            if (boolean1) {
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int height = metrics.heightPixels / 2;
                int width = metrics.widthPixels / 2;
                WallpaperManager wallpaperManager = WallpaperManager
                        .getInstance(this);
                try {
                    wallpaperManager.setBitmap(bitmap);
                    wallpaperManager.suggestDesiredDimensions(width, height);
                    ShowSnackMessage(getString(R.string.setSwallSucess));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (boolean1) {
                ShowSnackMessage(getString(R.string.setSwallSucess));
            } else {
                ShowSnackMessage(getString(R.string.setSwallNotSucess));
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse("file://"
                                + Environment.getExternalStorageDirectory())));
            }
            out.flush();
            out.close();
        } catch (Exception e) {
        }
    }

    private void ShareToSocialMedia(ShareTo shareto) {
        mFramelayout.setDrawingCacheEnabled(true);
        Bitmap bitmap = mFramelayout.getDrawingCache();
        File newDir = new File(Constants.GT_FOLDER_PATH);
        newDir.mkdirs();
        Random gen = new Random();
        int n = 10000;
        n = gen.nextInt(n);
        String fotoname = n + ".jpg";
        File file = new File(newDir, fotoname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
        }
        if (shareto == ShareTo.ImgSystem) {
            Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
            intent.setDataAndType(Uri.fromFile(file), "image/jpg");
            intent.putExtra("mimeType", "image/jpg");
            startActivityForResult(Intent.createChooser(intent, "Set As"), 200);
        } else {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            if (shareto == ShareTo.AppSystem) {
                startActivity(Intent.createChooser(shareIntent, "ShareFileWithIntentType Image"));
            } else if (shareto == ShareTo.instagram && appInstalledOrNot("com.instagram.android")) {
                shareIntent.setPackage("com.instagram.android");
                startActivity(shareIntent);
            } else if (shareto == ShareTo.facebook && appInstalledOrNot("com.facebook.katana")) {
                shareIntent.setPackage("com.facebook.katana");
                startActivity(shareIntent);
            } else {
                ShowSnackMessage(getString(R.string.appNotInstalled));
            }
        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),
                ChooseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}

