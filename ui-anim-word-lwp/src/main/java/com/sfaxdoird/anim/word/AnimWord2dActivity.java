package com.sfaxdoird.anim.word;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.sfaxdroid.app.downloadsystem.DefaultRetryPolicy;
import com.sfaxdroid.app.downloadsystem.DownloadManager;
import com.sfaxdroid.app.downloadsystem.DownloadRequest;
import com.sfaxdroid.app.downloadsystem.DownloadStatusListenerV1;
import com.sfaxdroid.app.downloadsystem.RetryPolicy;
import com.sfaxdroid.app.downloadsystem.ThinDownloadManager;
import com.sfaxdroid.base.Constants;
import com.sfaxdroid.base.SharedPrefsUtils;
import com.sfaxdroid.base.SimpleActivity;
import com.sfaxdroid.base.BitmapUtils;
import com.sfaxdroid.base.Utils;

import java.io.File;

public class AnimWord2dActivity extends SimpleActivity {

    private CoordinatorLayout mRootLayout;
    private ProgressBar mProgress1;
    private TextView mProgress1Txt;
    private TextView mTxtstatusDownload;
    private TextView mTxtfont1;
    private TextView mTxtfont2;
    private TextView mTxtfont3;
    private TextView mTxtfont4;
    private TextView mTxtfont5;
    private TextView mTxtfont6;
    private TextView mTxtfont7;
    private TextView mTxtfont8;
    private Toolbar mToolbar;
    private FloatingActionButton mFab;
    private Button mButtonColor;
    private Button mButtonSizeFullScreen;
    private Button mButtonSizeBig;
    private Button mButtonSizeMeduim;
    private Button mButtonSizeSmall;
    private boolean mClickable = false;
    private int mDownloadId2;
    private File mBackgroundFile;
    SharedPrefsUtils pref;

    AnimWord2dActivity.MyDownloadDownloadStatusListenerV1
            mDownloadStatusListener = new AnimWord2dActivity.MyDownloadDownloadStatusListenerV1();

    @Override
    protected int getLayout() {
        return R.layout.activity_nameofallah_live_wallpaper;
    }

    @Override
    protected void initEventAndData() {

        pref = new SharedPrefsUtils(this);

        ThinDownloadManager mDownloadManager = new ThinDownloadManager(Constants.DOWNLOAD_THREAD_POOL_SIZE);

        String mBackgroundUrl = "";
        if (Utils.Companion.getScreenHeightPixels(this) < 820
                && Utils.Companion.getScreenWidthPixels(this) < 500)
            mBackgroundUrl = getIntent().getStringExtra(com.sfaxdroid.base.Constants.URL_TO_DOWNLOAD)
                    .replace("islamicimages", "islamicimagesmini");
        else
            mBackgroundUrl = getIntent().getStringExtra(com.sfaxdroid.base.Constants.URL_TO_DOWNLOAD);

        mToolbar = findViewById(R.id.toolbar);
        mRootLayout = findViewById(R.id.rootLayout);
        mProgress1Txt = findViewById(R.id.progressTxt1);
        mTxtstatusDownload = findViewById(R.id.txtstatusDownload);
        mFab = findViewById(R.id.fab);
        mProgress1 = findViewById(R.id.progress1);
        mButtonColor = findViewById(R.id.buttonColor);
        mProgress1.setMax(100);
        mProgress1.setProgress(0);

        mTxtfont1 = findViewById(R.id.txtfont1);
        mTxtfont2 = findViewById(R.id.txtfont2);
        mTxtfont3 = findViewById(R.id.txtfont3);
        mTxtfont4 = findViewById(R.id.txtfont4);
        mTxtfont5 = findViewById(R.id.txtfont5);
        mTxtfont6 = findViewById(R.id.txtfont6);
        mTxtfont7 = findViewById(R.id.txtfont7);
        mTxtfont8 = findViewById(R.id.txtfont8);

        mButtonSizeFullScreen = findViewById(R.id.buttonSizeFullScreen);
        mButtonSizeBig = findViewById(R.id.buttonSizeBig);
        mButtonSizeMeduim = findViewById(R.id.buttonSizeMeduim);
        mButtonSizeSmall = findViewById(R.id.buttonSizeSmall);
        setTextViewTypeFace();
        initSizeListner();
        initTextViewListner();
        initToolbar();
        mFab.setEnabled(false);
        RetryPolicy mRetryPolicy = new DefaultRetryPolicy();
        File mFilesDir = getExternalFilesDir("");
        mBackgroundFile = new File(mFilesDir, com.sfaxdroid.base.Constants.DOUA_PNG_BACKFROUND_FILE_NAME);
        DownloadRequest mDownloadRequest2 = new DownloadRequest(Uri.parse(mBackgroundUrl))
                .setDestinationURI(Uri.parse(mFilesDir
                        + "/" + com.sfaxdroid.base.Constants.DOUA_PNG_BACKFROUND_FILE_NAME)).setPriority(DownloadRequest.Priority.LOW)
                .setRetryPolicy(mRetryPolicy)
                .setDownloadContext("Doua LWP Background")
                .setStatusListener(mDownloadStatusListener);
        boolean result = mBackgroundFile.delete();
        if (!result) {
            showSnackMessage(mRootLayout, "Error deleteing temps file");
        }
        if (mDownloadManager.query(mDownloadId2) == DownloadManager.STATUS_NOT_FOUND)
            mDownloadId2 = mDownloadManager.add(mDownloadRequest2);

        mProgress1Txt.setText(getString(R.string.DouaLwpProgressDesc));
        mButtonColor.setOnClickListener(x -> chooseColor());
        mFab.setOnClickListener(v -> openLiveWallpapersDoua());
        int size = pref.GetSetting("nameofallahtextsize", 1);
        if (size == 1)
            mButtonSizeSmall.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.ic_size_small_on), null, null);
        else if (size == 2)
            mButtonSizeMeduim.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.ic_size_meduim_on), null, null);
        else if (size == 3)
            mButtonSizeBig.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.ic_size_big_on), null, null);
        else if (size == 4)
            mButtonSizeFullScreen.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.ic_size_full_on), null, null);
    }

    public void setTextViewTypeFace() {
        mTxtfont1.setTypeface(Typeface.createFromAsset(getAssets(), "arabicfont1.otf"));
        mTxtfont2.setTypeface(Typeface.createFromAsset(getAssets(), "arabicfont2.ttf"));
        mTxtfont3.setTypeface(Typeface.createFromAsset(getAssets(), "arabicfont3.ttf"));
        mTxtfont4.setTypeface(Typeface.createFromAsset(getAssets(), "arabicfont4.otf"));
        mTxtfont5.setTypeface(Typeface.createFromAsset(getAssets(), "arabicfont5.ttf"));
        mTxtfont6.setTypeface(Typeface.createFromAsset(getAssets(), "arabicfont6.ttf"));
        mTxtfont7.setTypeface(Typeface.createFromAsset(getAssets(), "arabicfont7.ttf"));
        mTxtfont8.setTypeface(Typeface.createFromAsset(getAssets(), "arabicfont8.ttf"));
    }


    private void initTextViewListner() {
        mTxtfont1.setOnClickListener(v -> {
            pref.SetSetting("nameofallahfontstyle", 1);
            resetTextViewBackground();
            mTxtfont1.setTextColor(Color.GREEN);
        });

        mTxtfont2.setOnClickListener(v -> {
            pref.SetSetting("nameofallahfontstyle", 2);
            resetTextViewBackground();
            mTxtfont2.setTextColor(Color.GREEN);
        });

        mTxtfont3.setOnClickListener(v -> {
            pref.SetSetting("nameofallahfontstyle", 3);
            resetTextViewBackground();
            mTxtfont3.setTextColor(Color.GREEN);
        });

        mTxtfont4.setOnClickListener(v -> {
            pref.SetSetting("nameofallahfontstyle", 4);
            resetTextViewBackground();
            mTxtfont4.setTextColor(Color.GREEN);
        });

        mTxtfont5.setOnClickListener(v -> {
            pref.SetSetting("nameofallahfontstyle", 5);
            resetTextViewBackground();
            mTxtfont5.setTextColor(Color.GREEN);
        });

        mTxtfont6.setOnClickListener(v -> {
            pref.SetSetting("nameofallahfontstyle", 6);
            resetTextViewBackground();
            mTxtfont6.setTextColor(Color.GREEN);
        });

        mTxtfont7.setOnClickListener(v -> {
            pref.SetSetting("nameofallahfontstyle", 7);
            resetTextViewBackground();
            mTxtfont7.setTextColor(Color.GREEN);
        });

        mTxtfont8.setOnClickListener(v -> {
            pref.SetSetting("nameofallahfontstyle", 8);
            resetTextViewBackground();
            mTxtfont8.setTextColor(Color.GREEN);
        });
    }

    private void resetTextViewBackground() {
        mTxtfont1.setTextColor(Color.WHITE);
        mTxtfont2.setTextColor(Color.WHITE);
        mTxtfont3.setTextColor(Color.WHITE);
        mTxtfont4.setTextColor(Color.WHITE);
        mTxtfont5.setTextColor(Color.WHITE);
        mTxtfont6.setTextColor(Color.WHITE);
        mTxtfont7.setTextColor(Color.WHITE);
        mTxtfont8.setTextColor(Color.WHITE);
    }


    private void initSizeListner() {
        mButtonSizeSmall.setOnClickListener(v -> {
            pref.SetSetting("nameofallahtextsize", 1);
            resetBtnSizeBackground();
            mButtonSizeSmall.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.ic_size_small_on), null, null);
        });

        mButtonSizeMeduim.setOnClickListener(v -> {
            pref.SetSetting("nameofallahtextsize", 2);
            resetBtnSizeBackground();
            mButtonSizeMeduim.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.ic_size_meduim_on), null, null);
        });

        mButtonSizeBig.setOnClickListener(v -> {
            pref.SetSetting("nameofallahtextsize", 3);
            resetBtnSizeBackground();
            mButtonSizeBig.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.ic_size_big_on), null, null);
        });

        mButtonSizeFullScreen.setOnClickListener(v -> {
            pref.SetSetting("nameofallahtextsize", 4);
            resetBtnSizeBackground();
            mButtonSizeFullScreen.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.mipmap.ic_size_full_on), null, null);
        });
    }

    public void resetBtnSizeBackground() {
        mButtonSizeSmall.setCompoundDrawablesWithIntrinsicBounds(null,
                getResources().getDrawable(R.mipmap.ic_size_small), null, null);
        mButtonSizeMeduim.setCompoundDrawablesWithIntrinsicBounds(null,
                getResources().getDrawable(R.mipmap.ic_size_meduim), null, null);
        mButtonSizeBig.setCompoundDrawablesWithIntrinsicBounds(null,
                getResources().getDrawable(R.mipmap.ic_size_big), null, null);
        mButtonSizeFullScreen.setCompoundDrawablesWithIntrinsicBounds(null,
                getResources().getDrawable(R.mipmap.ic_size_full), null, null);
    }

    public void initToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setTitle(getString(R.string.TitleDouaLwp));
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void chooseColor() {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle(getString(R.string.choosecolor))
                .initialColor(Color.BLUE)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(selectedColor -> {
                })
                .setPositiveButton(getString(R.string.btnok), (dialog, selectedColor, allColors) -> {
                    pref.SetSetting("DouaLwpColor", selectedColor);
                    mButtonColor.setCompoundDrawablesWithIntrinsicBounds(null,
                            BitmapUtils
                                    .covertBitmapToDrawable(AnimWord2dActivity.this,
                                            BitmapUtils.
                                                    changeImageColor(BitmapUtils.
                                                                    convertDrawableToBitmap(getResources()
                                                                            .getDrawable(R.mipmap.ic_palette))
                                                            , selectedColor))
                            , null, null);
                })
                .setNegativeButton(getString(R.string.btncancel), (dialog, which) -> {
                })
                .build()
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void openLiveWallpapersDoua() {
        if (mClickable) {
            mBackgroundFile = null;
            com.sfaxdroid.base.Constants.ifBackgroundChanged = true;
            com.sfaxdroid.base.Constants.nbIncrementationAfterChange = 0;
            try {
                Intent intent = new Intent(
                        WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                intent.putExtra(
                        WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                        new ComponentName(AnimWord2dActivity.this,
                                AnimWord2dWallpaper.class));
                startActivity(intent);
                //finish();
            } catch (Exception e) {
                try {
                    Intent intent = new Intent();
                    intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
                    startActivity(intent);
                } catch (Exception ignored) {
                }
            }
        } else {
            showSnackMessage(mRootLayout, "Wait for download");
        }
    }

    public void showSnackMessage(CoordinatorLayout mRootLayout, String message) {
        Snackbar snackbar = Snackbar
                .make(mRootLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private class MyDownloadDownloadStatusListenerV1 implements DownloadStatusListenerV1 {

        @Override
        public void onDownloadComplete(DownloadRequest request) {
            mFab.setEnabled(true);
            mProgress1Txt.setText(getString(R.string.downloadterminated));
            mTxtstatusDownload.setText(getString(R.string.txtcompleted));
            mClickable = true;
        }

        @Override
        public void onDownloadFailed(DownloadRequest request, int errorCode, String errorMessage) {
            final int id = request.getDownloadId();
            mProgress1Txt.setText(" Failed: ErrorCode " + errorCode + ", " + errorMessage);
            mProgress1.setProgress(0);
        }

        @Override
        public void onProgress(DownloadRequest request, long totalBytes, long downloadedBytes, int progress) {
            int id = request.getDownloadId();
            mProgress1Txt.setText(progress + "%" + "  " + Utils.Companion.getBytesDownloaded(progress, totalBytes));
            mProgress1.setProgress(progress);
        }
    }
}
