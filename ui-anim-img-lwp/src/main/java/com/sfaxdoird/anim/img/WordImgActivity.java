package com.sfaxdoird.anim.img;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
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
import com.sfaxdroid.app.ZipUtils;
import com.sfaxdroid.app.downloadsystem.DefaultRetryPolicy;
import com.sfaxdroid.app.downloadsystem.DownloadManager;
import com.sfaxdroid.app.downloadsystem.DownloadRequest;
import com.sfaxdroid.app.downloadsystem.DownloadStatusListenerV1;
import com.sfaxdroid.app.downloadsystem.RetryPolicy;
import com.sfaxdroid.app.downloadsystem.ThinDownloadManager;
import com.sfaxdroid.base.BitmapUtils;
import com.sfaxdroid.base.SharedPrefsUtils;
import com.sfaxdroid.base.SimpleActivity;

import java.io.File;

import static com.sfaxdroid.base.Constants.URL_TO_DOWNLOAD;

public class WordImgActivity extends SimpleActivity {

    public CoordinatorLayout mRootLayout;
    public ProgressBar mProgress1;
    public TextView mProgress1Txt;
    public TextView mTxtStatusDownload;
    public Toolbar mToolbar;
    public FloatingActionButton mFab;
    public Button mButtonColor;
    private ThinDownloadManager mDownloadManager;
    private File mZipFile;
    private File mZipDestination;
    private File mBackgroundFile;
    private int mDownloadId1;
    private int mDownloadId2;
    private DownloadRequest mDownloadRequest2;
    private boolean mIsClickable = false;
    private MyDownloadDownloadStatusListenerV1
            myDownloadStatusListener = new MyDownloadDownloadStatusListenerV1();

    @Override
    protected void onViewCreated() {
        mRootLayout = findViewById(R.id.rootLayout);
        mProgress1 = findViewById(R.id.progress1);
        mProgress1Txt = findViewById(R.id.progressTxt1);
        mTxtStatusDownload = findViewById(R.id.txtstatusDownload);
        mToolbar = findViewById(R.id.toolbar);
        mFab = findViewById(R.id.fab);
        mButtonColor = findViewById(R.id.buttonColor);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_doua_live_wallpaper;
    }

    @Override
    protected void initEventAndData() {
        String mZipUrl = "";
        String mBackgroundUrl = "";
        if (com.sfaxdroid.base.Utils.Companion.getScreenHeightPixels(this) < 820
                && com.sfaxdroid.base.Utils.Companion.getScreenWidthPixels(this) < 500) {
            mBackgroundUrl = getIntent().getStringExtra(URL_TO_DOWNLOAD)
                    .replace("islamicimages", "islamicimagesmini");
            mZipUrl = Constants.URL_DOUA_PNG.replace(Constants.PNG_ZIP_FILE_NAME, "douamini.zip");
        } else {
            mBackgroundUrl = getIntent().getStringExtra(URL_TO_DOWNLOAD);
            mZipUrl = Constants.URL_DOUA_PNG;
        }
        mProgress1.setMax(100);
        mProgress1.setProgress(0);
        mDownloadManager = new ThinDownloadManager(com.sfaxdroid.base.Constants.DOWNLOAD_THREAD_POOL_SIZE);
        initToolbar();
        mFab.setEnabled(false);
        mFab.setOnClickListener(e -> openLiveWallpapersDoua());
        RetryPolicy mRetryPolicy = new DefaultRetryPolicy();
        File mFilesDir = Utils.getTemporaryDouaDir(this, Constants.KEY_DOUA_FOLDER_CONTAINER, getString(R.string.app_namenospace));
        mZipFile = new File(mFilesDir, Constants.PNG_ZIP_FILE_NAME);
        mBackgroundFile = new File(mFilesDir, com.sfaxdroid.base.Constants.DOUA_PNG_BACKFROUND_FILE_NAME);
        mZipDestination = new File(mFilesDir, com.sfaxdroid.base.Constants.DOUA_ZIP_FOLDER_NAME);

        final DownloadRequest mDownloadRequest1 = new DownloadRequest(Uri.parse(mZipUrl))
                .setDestinationURI(Uri.parse(mFilesDir + "/" + Constants.PNG_ZIP_FILE_NAME))
                .setPriority(DownloadRequest.Priority.LOW)
                .setRetryPolicy(mRetryPolicy)
                .setDownloadContext("Doua LWP Resources")
                .setStatusListener(myDownloadStatusListener);

        mDownloadRequest2 = new DownloadRequest(Uri.parse(mBackgroundUrl))
                .setDestinationURI(Uri.parse(mFilesDir + "/" + com.sfaxdroid.base.Constants.DOUA_PNG_BACKFROUND_FILE_NAME))
                .setPriority(DownloadRequest.Priority.LOW)
                .setRetryPolicy(mRetryPolicy)
                .setDownloadContext("Doua LWP Background")
                .setStatusListener(myDownloadStatusListener);

        if (!mZipFile.exists()) {
            if (mDownloadManager.query(mDownloadId1) == DownloadManager.STATUS_NOT_FOUND) {
                mDownloadId1 = mDownloadManager.add(mDownloadRequest1);
            }
        } else {
            if (mZipFile.exists()) {
                mBackgroundFile.delete();
            }
            if (mDownloadManager.query(mDownloadId2) == DownloadManager.STATUS_NOT_FOUND) {
                mDownloadId2 = mDownloadManager.add(mDownloadRequest2);
            }
        }

        mProgress1Txt.setText(getString(R.string.DouaLwpProgressDesc));
        mButtonColor.setOnClickListener(v -> chooseColor());
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
                    SharedPrefsUtils pref = new SharedPrefsUtils(this);
                    pref.SetSetting("DouaLwpColor", selectedColor);
                    mButtonColor.setCompoundDrawablesWithIntrinsicBounds(null,
                            BitmapUtils.covertBitmapToDrawable(WordImgActivity.this,
                                    BitmapUtils.
                                            changeImageColor(BitmapUtils.
                                                            convertDrawableToBitmap(getResources().getDrawable(R.mipmap.ic_palette))
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
        if (mIsClickable) {
            mZipFile = null;
            mZipDestination = null;
            mBackgroundFile = null;
            com.sfaxdroid.base.Constants.ifBackgroundChanged = true;
            com.sfaxdroid.base.Constants.nbIncrementationAfterChange = 0;
            try {
                Intent intent = new Intent(
                        WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                intent.putExtra(
                        WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                        new ComponentName(WordImgActivity.this,
                                WordImgLiveWallpaper.class));
                intent.putExtra("valueStart", "eeee");
                startActivity(intent);
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

    class MyDownloadDownloadStatusListenerV1 implements DownloadStatusListenerV1 {

        @Override
        public void onDownloadComplete(DownloadRequest request) {
            final int id = request.getDownloadId();
            if (id == mDownloadId1) {
                mProgress1Txt.setText(request.getDownloadContext() + getString(R.string.DouaLwpDownloadCompleted));
                ZipUtils.Companion.unzipFile(mZipFile, mZipDestination);
                if (mZipFile.exists()) {
                    mBackgroundFile.delete();
                }
                mIsClickable = true;
                mDownloadId2 = mDownloadManager.add(mDownloadRequest2);
            } else if (id == mDownloadId2) {
                //openLiveWallpapersDoua();
                mFab.setEnabled(true);
                mProgress1Txt.setText(getString(R.string.downloadterminated));
                mTxtStatusDownload.setText(getString(R.string.txtcompleted));
                mIsClickable = true;
            }
        }

        @Override
        public void onDownloadFailed(DownloadRequest request, int errorCode, String errorMessage) {
            final int id = request.getDownloadId();
            if (id == mDownloadId1 && mProgress1Txt != null && mProgress1 != null) {
                mProgress1Txt.setText(" Failed: ErrorCode " + errorCode + ", " + errorMessage);
                mProgress1.setProgress(0);
            }
            if (id == mDownloadId2 && mProgress1Txt != null && mProgress1 != null) {
                mProgress1Txt.setText(" Failed: ErrorCode " + errorCode + ", " + errorMessage);
                mProgress1.setProgress(0);
            }
        }

        @Override
        public void onProgress(DownloadRequest request, long totalBytes, long downloadedBytes, int progress) {
            int id = request.getDownloadId();
            if (id == mDownloadId1 && mProgress1Txt != null && mProgress1 != null) {
                mProgress1Txt.setText(progress + "%" + "  "
                        + com.sfaxdroid.base.Utils.Companion.getBytesDownloaded(progress, totalBytes));
                mProgress1.setProgress(progress);
            }
            if (id == mDownloadId2 && mProgress1Txt != null && mProgress1 != null) {
                mProgress1Txt.setText(progress + "%" + "  "
                        + com.sfaxdroid.base.Utils.Companion.getBytesDownloaded(progress, totalBytes));
                mProgress1.setProgress(progress);
            }
        }
    }
}
