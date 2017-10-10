package com.sami.rippel.labs.basmala;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.sami.rippel.allah.R;
import com.sami.rippel.model.Constants;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.ui.activity.GalleryWallpaperActivity;
import com.sami.rippel.views.GlideApp;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadManager;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.thin.downloadmanager.RetryPolicy;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ActivityBasmalaScreen extends AppCompatActivity {
    private static final int DOWNLOAD_THREAD_POOL_SIZE = 4;
    private static final int RESULT_CODE = 144;
    private static final int OVERLAY_PERMISSION_REQ_CODE = 1234;
    private Activity mAct;
    private Toolbar mToolbar;
    private CoordinatorLayout mRootLayout;
    private ImageView mImgPreview;
    private Button mButtonActive;
    private Button mButtonGallery;
    private Button mBtnSpeedFast;
    private Button mBtnSpeedSlow;
    private Button mBtnSpeedMeduim;
    private Button mButtonTurbo;
    private Button mButtonSizeFullScreen;
    private Button mButtonSizeBig;
    private Button mButtonSizeMeduim;
    private Button mButtonSizeSmall;
    private Button mButtonColor;
    private int mDownloadId1;
    private CheckBox mSoundCheckbox;
    private CheckBox mPictureCheckbox;
    private CheckBox mPhotoAleatoire;
    private CheckBox mCheckBoxNbUnlock;
    private EditText mEditTextNbOpen;
    private LinearLayout mLinearLayoutChoosePicture;
    private TextView mTextViewServiceInfo;
    private TextView mProgress1Txt;
    private ProgressBar mProgress1;
    private LinearLayout mLayoutDownload;
    private File mZipFile;
    private File mZipDestination;
    private String mZipUrl = "";
    private View mDivinerBottom;
    private FloatingActionButton mFab;
    private boolean mIsServiceCanBeUsed = false;
    private ThinDownloadManager mDownloadManager;
    private ActivityBasmalaScreen.MyDownloadDownloadStatusListenerV1
            mDownloadStatusListener = new ActivityBasmalaScreen.MyDownloadDownloadStatusListenerV1();

    public void initUI() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mBtnSpeedMeduim = (Button) findViewById(R.id.buttonMeduim);
        mBtnSpeedSlow = (Button) findViewById(R.id.buttonShort);
        mBtnSpeedFast = (Button) findViewById(R.id.buttonFast);
        mButtonGallery = (Button) findViewById(R.id.buttonGallery);
        mButtonTurbo = (Button) findViewById(R.id.buttonTurbo);
        mSoundCheckbox = (CheckBox) findViewById(R.id.checkBoxSound);
        mPictureCheckbox = (CheckBox) findViewById(R.id.checkBoxImage);
        mCheckBoxNbUnlock = (CheckBox) findViewById(R.id.checkBoxNbUnlock);
        mImgPreview = (ImageView) findViewById(R.id.imageView1);
        mPhotoAleatoire = (CheckBox) findViewById(R.id.checkBoxAleatoire);
        mLinearLayoutChoosePicture = (LinearLayout) findViewById(R.id.linearLayoutChoosePicture);
        mEditTextNbOpen = (EditText) findViewById(R.id.editTextNbOpen);
        mRootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
        mTextViewServiceInfo = (TextView) findViewById(R.id.textViewServiceInfo);
        mProgress1Txt = (TextView) findViewById(R.id.progressTxt1);
        mLayoutDownload = (LinearLayout) findViewById(R.id.layoutDownload);
        mDivinerBottom = (View) findViewById(R.id.divinerBottom);
        mProgress1 = (ProgressBar) findViewById(R.id.progress1);
        mButtonActive = (Button) findViewById(R.id.button1);
        mButtonSizeFullScreen = (Button) findViewById(R.id.buttonSizeFullScreen);
        mButtonSizeBig = (Button) findViewById(R.id.buttonSizeBig);
        mButtonSizeMeduim = (Button) findViewById(R.id.buttonSizeMeduim);
        mButtonSizeSmall = (Button) findViewById(R.id.buttonSizeSmall);
        mButtonColor = (Button) findViewById(R.id.buttonColor);
        mProgress1.setMax(100);
        mProgress1.setProgress(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basmallah);
        mAct = this;
        initUI();
        initToolbar();
        initPreferences();
        initListner();
        initUIifServiceRunning();
        setSpeedUIDrawableAndListner();
        ViewModel.Current.device.checkPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        mLayoutDownload.setVisibility(View.GONE);
        mDivinerBottom.setVisibility(View.GONE);
    }

    private void initUIifServiceRunning() {
        if (ViewModel.Current.device.isMyServiceRunning(LockService.class, this)) {
            mTextViewServiceInfo.setText(getString(R.string.serviceOn));
            mButtonActive.setText(getString(R.string.closesebasmalae));
            mButtonActive.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.ic_close_basmallah), null, null);
        } else {
            mTextViewServiceInfo.setText(getString(R.string.serviceOff));
            mButtonActive.setText(getString(R.string.activesbasmalah));
            mButtonActive.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.ic_active_besmelleh), null, null);
        }
    }

    private void initListner() {

        mEditTextNbOpen.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0)
                    ViewModel.Current.dataUtils.SetSetting("NbOuv",
                            Integer.parseInt(mEditTextNbOpen.getText().toString()));
            }
        });

        mCheckBoxNbUnlock.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!mCheckBoxNbUnlock.isChecked()) {
                    mEditTextNbOpen.setEnabled(false);
                    mEditTextNbOpen.setText("0");
                    ViewModel.Current.dataUtils.SetSetting("NbOuv", 0);

                } else if (mCheckBoxNbUnlock.isChecked()) {
                    mEditTextNbOpen.setEnabled(true);
                }
            }
        });

        mSoundCheckbox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSoundCheckbox.isChecked()) {
                    ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.toastnosound));
                    ViewModel.Current.dataUtils.SetSetting("sound", "off");
                } else if (mSoundCheckbox.isChecked()) {
                    ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.toastwithsound));
                    ViewModel.Current.dataUtils.SetSetting("sound", "on");
                }
            }
        });

        mPictureCheckbox.setOnClickListener(v -> {
            if (!mPictureCheckbox.isChecked()) {
                ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.toastsanspicture));
                ViewModel.Current.dataUtils.SetSetting("eye", "off");
            } else if (mPictureCheckbox.isChecked()) {
                if (ViewModel.Current.fileUtils.isFileExistInDataFolder(Constants.PNG_BASMALA_STICKERS_FILE_NAME)) {
                    ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.toastwithpicture));
                    ViewModel.Current.dataUtils.SetSetting("eye", "on");
                } else {
                    mPictureCheckbox.setChecked(false);
                    ViewModel.Current.device.showSnackMessage(mRootLayout,
                            getString(R.string.openBasmalaNeedAddPicture));
                }
            }
        });

        mButtonActive.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLockRunning = ViewModel.Current.device.isMyServiceRunning(LockService.class, mAct);
                if (!isLockRunning && mIsServiceCanBeUsed) {
                    startService(new Intent(getApplicationContext(),
                            LockService.class));
                    mTextViewServiceInfo.setText(getString(R.string.serviceOn));
                    mButtonActive.setText(getString(R.string.closesebasmalae));
                    mButtonActive.setCompoundDrawablesWithIntrinsicBounds(null,
                            getResources().getDrawable(R.drawable.ic_close_basmallah), null, null);

                } else if (isLockRunning) {
                    stopService(new Intent(getApplicationContext(),
                            LockService.class));
                    mTextViewServiceInfo.setText(getString(R.string.serviceOff));
                    mButtonActive.setText(getString(R.string.activesbasmalah));
                    mButtonActive.setCompoundDrawablesWithIntrinsicBounds(null,
                            getResources().getDrawable(R.drawable.ic_active_besmelleh), null, null);
                } else {
                    ViewModel.Current.device.showSnackMessage(mRootLayout, "Permission not accepted");
                }
            }
        });

        mPhotoAleatoire.setOnClickListener(v -> {
            if (!mPhotoAleatoire.isChecked()) {
                ViewModel.Current.dataUtils.SetSetting("random", "off");
                mLinearLayoutChoosePicture.setVisibility(View.VISIBLE);
            } else if (mPhotoAleatoire.isChecked()) {
                if (ViewModel.Current.fileUtils.isFileExistInDataFolder(Constants.PNG_BASMALA_STICKERS_FILE_NAME)) {
                    ViewModel.Current.dataUtils.SetSetting("random", "on");
                    mLinearLayoutChoosePicture.setVisibility(View.INVISIBLE);
                } else {
                    mPhotoAleatoire.setChecked(false);
                    ViewModel.Current.device.showSnackMessage(mRootLayout,
                            getString(R.string.openBasmalaNeedAddPicture));
                }
            }
        });

        mFab.setOnClickListener(v -> {
            AnimationServiceApp app = new AnimationServiceApp(getApplicationContext());
            app.stopAnimationIfRunning();
            app.startPreviewAnimation();
            ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.speedInfoFast));
        });

        mButtonGallery.setOnClickListener(v -> startDownloadIfNeed());

        mImgPreview.setOnClickListener(v -> openGallery());

        mButtonColor.setOnClickListener(v -> chooseColor());

        initSizeListner();
    }

    private void initSizeListner() {
        mButtonSizeSmall.setOnClickListener(v -> {
            ViewModel.Current.dataUtils.SetSetting("size", 0);
            resetBtnSizeBackground();
            mButtonSizeSmall.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.ic_size_small_on), null, null);
        });

        mButtonSizeMeduim.setOnClickListener(v -> {
            ViewModel.Current.dataUtils.SetSetting("size", 1);
            resetBtnSizeBackground();
            mButtonSizeMeduim.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.ic_size_meduim_on), null, null);
        });

        mButtonSizeBig.setOnClickListener(v -> {
            ViewModel.Current.dataUtils.SetSetting("size", 2);
            resetBtnSizeBackground();
            mButtonSizeBig.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.ic_size_big_on), null, null);
        });

        mButtonSizeFullScreen.setOnClickListener(v -> {
            ViewModel.Current.dataUtils.SetSetting("size", 3);
            resetBtnSizeBackground();
            mButtonSizeFullScreen.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.ic_size_full_on), null, null);
        });
    }

    private void initPreferences() {
        String soundPref = ViewModel.Current.dataUtils.GetSetting("sound", "on");
        String picturePref = ViewModel.Current.dataUtils.GetSetting("eye", "off");
        String randomPref = ViewModel.Current.dataUtils.GetSetting("random", "off");
        int nbOuverture = ViewModel.Current.dataUtils.GetSetting("NbOuv", 0);
        int speed = ViewModel.Current.dataUtils.GetSetting("speed", 1);
        int size = ViewModel.Current.dataUtils.GetSetting("size", 3);

        mEditTextNbOpen.setText(nbOuverture + "");
        if (soundPref.equals("on")) {
            mSoundCheckbox.setChecked(true);
        } else if (soundPref.equals("off")) {
            mSoundCheckbox.setChecked(false);
        }
        // Picture pref
        if (picturePref.equals("on")) {
            mPictureCheckbox.setChecked(true);
        } else if (picturePref.equals("off")) {
            mPictureCheckbox.setChecked(false);
        }
        // Random pref
        if (randomPref.equals("on")) {
            mPhotoAleatoire.setChecked(true);
            mLinearLayoutChoosePicture.setVisibility(View.INVISIBLE);
        } else if (randomPref.equals("off")) {
            mPhotoAleatoire.setChecked(false);
            mLinearLayoutChoosePicture.setVisibility(View.VISIBLE);
        }
        // Nb open Pref
        if (nbOuverture > 0) {
            mCheckBoxNbUnlock.setChecked(true);
            mEditTextNbOpen.setEnabled(true);
        } else {
            mCheckBoxNbUnlock.setChecked(false);
            mEditTextNbOpen.setEnabled(false);
            mEditTextNbOpen.setText("0");
        }

        //button Speed
        resetBtnSpeedBackground();
        if (speed == 0)
            mBtnSpeedSlow.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.ic_speed_slow_on), null, null);
        else if (speed == 1)
            mBtnSpeedMeduim.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.ic_speed_meduim_on), null, null);
        else if (speed == 2)
            mBtnSpeedFast.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.ic_speed_fast_on), null, null);
        else if (speed == 3)
            mButtonTurbo.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.ic_turbo_on), null, null);

        resetBtnSizeBackground();
        if (size == 0)
            mButtonSizeSmall.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.ic_size_small_on), null, null);
        else if (size == 1)
            mButtonSizeMeduim.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.ic_size_meduim_on), null, null);
        else if (size == 2)
            mButtonSizeBig.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.ic_size_big_on), null, null);
        else if (size == 3)
            mButtonSizeFullScreen.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.ic_size_full_on), null, null);
    }

    private void setSpeedUIDrawableAndListner() {

        mBtnSpeedSlow.setOnClickListener(v -> {
            ViewModel.Current.dataUtils.SetSetting("speed", 0);
            resetBtnSpeedBackground();
            mBtnSpeedSlow.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.ic_speed_slow_on), null, null);
        });

        mBtnSpeedMeduim.setOnClickListener(v -> {
            ViewModel.Current.dataUtils.SetSetting("speed", 1);
            resetBtnSpeedBackground();
            mBtnSpeedMeduim.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.ic_speed_meduim_on), null, null);
        });


        mBtnSpeedFast.setOnClickListener(v -> {
            ViewModel.Current.dataUtils.SetSetting("speed", 2);
            resetBtnSpeedBackground();
            mBtnSpeedFast.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.ic_speed_fast_on), null, null);
        });

        mButtonTurbo.setOnClickListener(v -> {
            ViewModel.Current.dataUtils.SetSetting("speed", 3);
            resetBtnSpeedBackground();
            mButtonTurbo.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.ic_turbo_on), null, null);
        });
    }

    public void resetBtnSpeedBackground() {
        mBtnSpeedSlow.setCompoundDrawablesWithIntrinsicBounds(null,
                getResources().getDrawable(R.drawable.ic_speed_slow), null, null);
        mBtnSpeedMeduim.setCompoundDrawablesWithIntrinsicBounds(null,
                getResources().getDrawable(R.drawable.ic_speed_meduim), null, null);
        mBtnSpeedFast.setCompoundDrawablesWithIntrinsicBounds(null,
                getResources().getDrawable(R.drawable.ic_speed_fast), null, null);
        mButtonTurbo.setCompoundDrawablesWithIntrinsicBounds(null,
                getResources().getDrawable(R.drawable.ic_turbo), null, null);
    }

    public void resetBtnSizeBackground() {
        mButtonSizeSmall.setCompoundDrawablesWithIntrinsicBounds(null,
                getResources().getDrawable(R.drawable.ic_size_small), null, null);
        mButtonSizeMeduim.setCompoundDrawablesWithIntrinsicBounds(null,
                getResources().getDrawable(R.drawable.ic_size_meduim), null, null);
        mButtonSizeBig.setCompoundDrawablesWithIntrinsicBounds(null,
                getResources().getDrawable(R.drawable.ic_size_big), null, null);
        mButtonSizeFullScreen.setCompoundDrawablesWithIntrinsicBounds(null,
                getResources().getDrawable(R.drawable.ic_size_full), null, null);
    }

    public void onResume() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissionDrawOverlays();
        } else {
            mIsServiceCanBeUsed = true;
        }
        super.onResume();
    }

    @TargetApi(23)
    public void checkPermissionDrawOverlays() {
        if (Settings.canDrawOverlays(getApplicationContext())) {
            mIsServiceCanBeUsed = true;
        } else {
            permissionDilog();
        }
    }

    public void permissionDilog() {
        showMessageOKCancel(getString(R.string.needallowpermission),
                (dialog, which) -> {
                    dialog.dismiss();
                    requestPermissions();
                });
        return;
    }

    public void requestPermissions() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(getString(R.string.permissionok), okListener)
                .setNegativeButton(getString(R.string.permissionnon), null)
                .create()
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK && data != null)
            return;
        switch (requestCode) {
            case RESULT_CODE:
                if (data != null) {
                    String path = data.getStringExtra("URL");
                    GlideApp.with(this).load(path)
                            .transition(withCrossFade())
                            .override(200, 200)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(mImgPreview);
                    ViewModel.Current.dataUtils.SetSetting(Constants.KEY_BASMALA_PREFERENCES_PATH, path);
                    ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.imageChnaged));
                }
                break;
            case OVERLAY_PERMISSION_REQ_CODE:
                mIsServiceCanBeUsed = true;
                break;
            default:
                break;
        }
    }

    public void startDownloadIfNeed() {
        File filesDir = getExternalFilesDir("");
        mZipDestination = new File(filesDir, Constants.KEY_BASMALA_FOLDER_CONTAINER);
        mZipFile = new File(filesDir, Constants.PNG_BASMALA_STICKERS_FILE_NAME);
        if (!mZipFile.exists()) {
            if (ViewModel.Current.device.getScreenHeightPixels() < 820
                    && ViewModel.Current.device.getScreenWidthPixels() < 500) {
                mZipUrl = Constants.URL_BASMALA_PNG.replace("basmala.zip", "basmalamini.zip");
            } else {
                mZipUrl = Constants.URL_BASMALA_PNG;
            }
            mDownloadManager = new ThinDownloadManager(DOWNLOAD_THREAD_POOL_SIZE);
            RetryPolicy retryPolicy = new DefaultRetryPolicy();
            final DownloadRequest downloadRequest1 = new DownloadRequest(Uri.parse(mZipUrl))
                    .setDestinationURI(Uri.parse(filesDir + "/"
                            + Constants.PNG_BASMALA_STICKERS_FILE_NAME)).setPriority(DownloadRequest.Priority.LOW)
                    .setRetryPolicy(retryPolicy)
                    .setDownloadContext("Basmala LWP Resources")
                    .setStatusListener(mDownloadStatusListener);
            if (mDownloadManager.query(mDownloadId1) == DownloadManager.STATUS_NOT_FOUND) {
                mDownloadId1 = mDownloadManager.add(downloadRequest1);
                mLayoutDownload.setVisibility(View.VISIBLE);
                mButtonGallery.setEnabled(false);
            }
        } else {
            openGallery();
        }
    }

    public void openGallery() {
        Intent intent = new Intent(ActivityBasmalaScreen.this, GalleryWallpaperActivity.class);
        intent.putExtra("LwpName", Constants.KEY_BASMALA_STIKERS);
        startActivityForResult(intent, RESULT_CODE);
    }

    public void initToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setTitle(
                    getString(R.string.titleBasmala));
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
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
                    ViewModel.Current.dataUtils.SetSetting("color", selectedColor);
                    mButtonColor.setCompoundDrawablesWithIntrinsicBounds(null,
                            ViewModel.Current.fileUtils.covertBitmapToDrawable(mAct,
                                    ViewModel.Current.fileUtils.
                                            changeImageColor(ViewModel.Current.fileUtils.
                                                            convertDrawableToBitmap(getResources().getDrawable(R.drawable.ic_palette))
                                                    , selectedColor))
                            , null, null);
                })
                .setNegativeButton(getString(R.string.btncancel), (dialog, which) -> {
                })
                .build()
                .show();
    }

    public class MyDownloadDownloadStatusListenerV1 implements DownloadStatusListenerV1 {

        @Override
        public void onDownloadComplete(DownloadRequest request) {
            final int id = request.getDownloadId();
            if (id == mDownloadId1) {
                mProgress1Txt.setText(request.getDownloadContext() + getString(R.string.DouaLwpDownloadCompleted));
                ViewModel.Current.fileUtils.unzipFile(mZipFile, mZipDestination);
                mLayoutDownload.setVisibility(View.GONE);
                mButtonGallery.setEnabled(true);
                openGallery();
            }
        }

        @Override
        public void onDownloadFailed(DownloadRequest request, int errorCode, String errorMessage) {
            final int id = request.getDownloadId();
            if (id == mDownloadId1) {
                mProgress1Txt.setText(" Failed: ErrorCode " + errorCode + ", " + errorMessage);
                mProgress1.setProgress(0);
                mButtonGallery.setEnabled(true);
            }
        }

        @Override
        public void onProgress(DownloadRequest request, long totalBytes, long downloadedBytes, int progress) {
            int id = request.getDownloadId();
            if (id == mDownloadId1) {
                mProgress1Txt.setText(progress + "%" + "  "
                        + ViewModel.Current.GetBytesDownloaded(progress, totalBytes));
                mProgress1.setProgress(progress);
            }
        }
    }
}
