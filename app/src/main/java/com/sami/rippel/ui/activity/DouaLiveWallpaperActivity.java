package com.sami.rippel.ui.activity;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.sami.rippel.allah.R;
import com.sami.rippel.base.SimpleActivity;
import com.sami.rippel.livewallpapers.lwpdouachanged.DouaLiveWallpaper;
import com.sami.rippel.model.Constants;
import com.sami.rippel.model.ViewModel;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadManager;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.thin.downloadmanager.RetryPolicy;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;

import butterknife.BindView;

public class DouaLiveWallpaperActivity extends SimpleActivity {

    private static final int DOWNLOAD_THREAD_POOL_SIZE = 4;
    @Nullable
    @BindView(R.id.rootLayout)
    public CoordinatorLayout mRootLayout;
    @Nullable
    @BindView(R.id.progress1)
    public ProgressBar mProgress1;
    @Nullable
    @BindView(R.id.progressTxt1)
    public TextView mProgress1Txt;
    @Nullable
    @BindView(R.id.txtstatusDownload)
    public TextView mTxtstatusDownload;
    @Nullable
    @BindView(R.id.toolbar)
    public Toolbar mToolbar;
    @Nullable
    @BindView(R.id.fab)
    public FloatingActionButton mFab;
    @Nullable
    @BindView(R.id.buttonColor)
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
    protected int getLayout() {
        return R.layout.activity_doua_live_wallpaper;
    }

    @Override
    protected void initEventAndData() {
        String mZipUrl = "";
        String mBackgroundUrl = "";
        if (ViewModel.Current.device.getScreenHeightPixels() < 820
                && ViewModel.Current.device.getScreenWidthPixels() < 500) {
            mBackgroundUrl = getIntent().getStringExtra(Constants.URL_TO_DOWNLOAD)
                    .replace("islamicimages", "islamicimagesmini");
            mZipUrl = Constants.URL_DOUA_PNG.replace("doua.zip", "douamini.zip");
        } else {
            mBackgroundUrl = getIntent().getStringExtra(Constants.URL_TO_DOWNLOAD);
            mZipUrl = Constants.URL_DOUA_PNG;
        }
        mProgress1.setMax(100);
        mProgress1.setProgress(0);
        mDownloadManager = new ThinDownloadManager(DOWNLOAD_THREAD_POOL_SIZE);
        initToolbar();
        mFab.setEnabled(false);
        mFab.setOnClickListener(e -> openLiveWallpapersDoua());
        RetryPolicy mRetryPolicy = new DefaultRetryPolicy();
        File mFilesDir = ViewModel.Current.fileUtils.getTemporaryDouaDir();
        mZipFile = new File(mFilesDir, Constants.PNG_ZIP_FILE_NAME);
        mBackgroundFile = new File(mFilesDir, Constants.DOUA_PNG_BACKFROUND_FILE_NAME);
        mZipDestination = new File(mFilesDir, "DouaFolder");

        final DownloadRequest mDownloadRequest1 = new DownloadRequest(Uri.parse(mZipUrl))
                .setDestinationURI(Uri.parse(mFilesDir + "/" + Constants.PNG_ZIP_FILE_NAME))
                .setPriority(DownloadRequest.Priority.LOW)
                .setRetryPolicy(mRetryPolicy)
                .setDownloadContext("Doua LWP Resources")
                .setStatusListener(myDownloadStatusListener);

        mDownloadRequest2 = new DownloadRequest(Uri.parse(mBackgroundUrl))
                .setDestinationURI(Uri.parse(mFilesDir + "/" + Constants.DOUA_PNG_BACKFROUND_FILE_NAME))
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
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                    }
                })
                .setPositiveButton(getString(R.string.btnok), (dialog, selectedColor, allColors) -> {
                    ViewModel.Current.sharedPrefsUtils.SetSetting("DouaLwpColor", selectedColor);
                    mButtonColor.setCompoundDrawablesWithIntrinsicBounds(null,
                            ViewModel.Current.bitmapUtils.covertBitmapToDrawable(DouaLiveWallpaperActivity.this,
                                    ViewModel.Current.bitmapUtils.
                                            changeImageColor(ViewModel.Current.bitmapUtils.
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
            Constants.ifBackground_changed = true;
            Constants.nb_incrementation_after_change = 0;
            try {
                Intent intent = new Intent(
                        WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                intent.putExtra(
                        WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                        new ComponentName(DouaLiveWallpaperActivity.this,
                                DouaLiveWallpaper.class));
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
            ViewModel.Current.device.showSnackMessage(mRootLayout, "Wait for download");
        }
    }

    class MyDownloadDownloadStatusListenerV1 implements DownloadStatusListenerV1 {

        @Override
        public void onDownloadComplete(DownloadRequest request) {
            final int id = request.getDownloadId();
            if (id == mDownloadId1) {
                mProgress1Txt.setText(request.getDownloadContext() + getString(R.string.DouaLwpDownloadCompleted));
                ViewModel.Current.fileUtils.unzipFile(mZipFile, mZipDestination);
                if (mZipFile.exists()) {
                    mBackgroundFile.delete();
                }
                mIsClickable = true;
                mDownloadId2 = mDownloadManager.add(mDownloadRequest2);
            } else if (id == mDownloadId2) {
                //openLiveWallpapersDoua();
                mFab.setEnabled(true);
                mProgress1Txt.setText(getString(R.string.downloadterminated));
                mTxtstatusDownload.setText(getString(R.string.txtcompleted));
                mIsClickable = true;
            }
        }

        @Override
        public void onDownloadFailed(DownloadRequest request, int errorCode, String errorMessage) {
            final int id = request.getDownloadId();
            if (id == mDownloadId1  && mProgress1Txt != null && mProgress1 != null) {
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
                        + ViewModel.Current.device.GetBytesDownloaded(progress, totalBytes));
                mProgress1.setProgress(progress);
            }
            if (id == mDownloadId2 && mProgress1Txt != null && mProgress1 != null) {
                mProgress1Txt.setText(progress + "%" + "  "
                        + ViewModel.Current.device.GetBytesDownloaded(progress, totalBytes));
                mProgress1.setProgress(progress);
            }
        }
    }
}
