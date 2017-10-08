package com.sami.rippel.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.flipboard.bottomsheet.commons.MenuSheetView.OnMenuItemClickListener;
import com.google.android.gms.analytics.Tracker;
import com.sami.rippel.allah.R;
import com.sami.rippel.allah.WallpaperApplication;
import com.sami.rippel.base.BaseActivity;
import com.sami.rippel.model.Constants;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.model.entity.ActionTypeEnum;
import com.sami.rippel.model.entity.IntentTypeEnum;
import com.sami.rippel.model.entity.WallpaperObject;
import com.sami.rippel.model.listner.DeviceListner;
import com.sami.rippel.model.listner.WallpaperListner;
import com.sami.rippel.ui.adapter.DetailPagerAdapter;
import com.soundcloud.android.crop.Crop;

import net.hockeyapp.android.CrashManager;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DetailsActivity extends BaseActivity implements WallpaperListner, DeviceListner {
    private DetailPagerAdapter mAdapter;
    private ArrayList<WallpaperObject> mPagerData = new ArrayList<>();
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private ProgressBar mProgressBar;
    private BottomSheetLayout mBottomSheet;
    private CoordinatorLayout mRootLayout;
    private Tracker mTracker;
    private int mPos;
    private String mFrom = "";
    public boolean mFromRipple = false;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
        setContentView(R.layout.activity_details);
        WallpaperApplication application = (WallpaperApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mBottomSheet = (BottomSheetLayout) findViewById(R.id.bottomsheetLayout);
        mRootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        ViewModel.Current.fileUtils.SetListner(this);
        ViewModel.Current.device.setmDeviceListner(this);
        mPagerData = getIntent().getParcelableArrayListExtra(Constants.LIST_FILE_TO_SEND_TO_DETAIL_VIEW_PAGER);
        mPos = getIntent().getIntExtra(Constants.DETAIL_IMAGE_POS, 0);
        mFrom = getIntent().getStringExtra(Constants.KEY_LWP_NAME);
        initToolbar();
        setFabImageResource();
        mFab.setOnClickListener(e -> fabClick());
        setupViewPager();
        if (Build.VERSION.SDK_INT >= 23)
            ViewModel.Current.device.checkPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void fabClick() {
        if (mFrom != null && !mFrom.isEmpty()) {
            switch (mFrom) {
                case Constants.KEY_ADD_TIMER_LWP:
                    saveTempsDorAndDoAction(ActionTypeEnum.MOVE_PERMANENT_DIR);
                    break;
                case Constants.KEY_ADDED_LIST_TIMER_LWP:
                    saveTempsDorAndDoAction(ActionTypeEnum.DELETE_CURRENT_PICTURE);
                    break;
                case Constants.KEY_RIPPLE_LWP:
                    saveTempsDorAndDoAction(ActionTypeEnum.SEND_LWP);
                    break;
            }
        } else {
            showMenuSheet(MenuSheetView.MenuType.LIST);
        }
    }

    public void setFabImageResource() {
        if (mFrom != null && !mFrom.isEmpty()) {
            switch (mFrom) {
                case Constants.KEY_ADD_TIMER_LWP:
                    mFab.setImageResource(R.drawable.ic_download);
                    break;
                case Constants.KEY_ADDED_LIST_TIMER_LWP:
                    mFab.setImageResource(R.drawable.ic_delete);
                    break;
                case Constants.KEY_RIPPLE_LWP:
                    mFab.setImageResource(R.drawable.ic_ripple_fab);
                    break;
            }
        }
    }

    private void checkForCrashes() {
        CrashManager.register(this);
    }

    public void deleteCurrentPicture() {
        if (ViewModel.Current.fileUtils.deleteFile(getCurrentUrl())) {
            finishWithResult();
        } else {
            ViewModel.Current.device.showSnackMessage(mRootLayout, "Error Deleting file");
        }
    }

    private void finishWithResult() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    public void initToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setTitle(
                    getString(R.string.SetWall));
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void showMenuSheet(MenuSheetView.MenuType menuType) {
        MenuSheetView menuSheetView = new MenuSheetView(this, menuType, "", getMenuSheetListener());
        menuSheetView.inflateMenu(R.menu.menu_details);
        mBottomSheet.showWithSheetView(menuSheetView);
    }

    private void dismissMenuSheet() {
        if (mBottomSheet.isSheetShowing()) {
            mBottomSheet.dismissSheet();
        }
    }

    private void setupViewPager() {
        mAdapter = new DetailPagerAdapter(this, R.layout.layout_detail_pager,
                mPagerData);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mPos);
    }

    public OnMenuItemClickListener getMenuSheetListener() {
        return item -> {
            switch (item.getItemId()) {
                case R.id.buttonWallpaper:
                    saveTempsDorAndDoAction(ActionTypeEnum.CROP);
                    break;
                case R.id.buttonChooser:
                    saveTempsDorAndDoAction(ActionTypeEnum.OPEN_NATIV_CHOOSER);
                    break;
                case R.id.buttonSave:
                    saveTempsDorAndDoAction(ActionTypeEnum.MOVE_PERMANENT_DIR);
                    break;
                case R.id.buttonSareInsta:
                    saveTempsDorAndDoAction(ActionTypeEnum.SHARE_INSTA);
                    break;
                case R.id.buttonSareFb:
                    saveTempsDorAndDoAction(ActionTypeEnum.SHARE_FB);
                    break;
                case R.id.buttonShare:
                    saveTempsDorAndDoAction(ActionTypeEnum.SHARE_SNAP_CHAT);
                    break;
                case R.id.buttonRipple:
                    saveTempsDorAndDoAction(ActionTypeEnum.SEND_LWP);
                    break;
                //case R.id.buttonFastWallpaper:
                //saveTempsDorAndDoAction(ActionTypeEnum.JUST_WALLPAPER);
                //break;
            }
            dismissMenuSheet();
            return true;
        };
    }

    public void saveTempsDorAndDoAction(ActionTypeEnum actionToDo) {
        String url = getCurrentUrl();
        addSubscribe(Flowable.fromCallable(() -> ViewModel.Current.fileUtils.isSavedToStorage(ViewModel.Current.fileUtils
                .getFileName(url)))
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        if (actionToDo == ActionTypeEnum.OPEN_NATIV_CHOOSER)
                            shareAll();
                        if (actionToDo == ActionTypeEnum.SHARE_FB)
                            createIntent(IntentTypeEnum.FACEBOOKINTENT);
                        if (actionToDo == ActionTypeEnum.SHARE_INSTA)
                            createIntent(IntentTypeEnum.INTAGRAMINTENT);
                        if (actionToDo == ActionTypeEnum.SEND_LWP)
                            sendToRippleLwp();
                        if (actionToDo == ActionTypeEnum.SHARE_SNAP_CHAT)
                            createIntent(IntentTypeEnum.SHNAPCHATINTENT);
                        if (actionToDo == ActionTypeEnum.CROP)
                            beginCrop();
                        if (actionToDo == ActionTypeEnum.MOVE_PERMANENT_DIR)
                            saveFileToPermanentGallery();
                        if (actionToDo == ActionTypeEnum.DELETE_CURRENT_PICTURE)
                            deleteCurrentPicture();
                        if (actionToDo == ActionTypeEnum.JUST_WALLPAPER)
                            setAsWallpaper();
                    } else {
                        mProgressBar.setVisibility(View.VISIBLE);
                        ViewModel.Current.fileUtils.saveToFileToTempsDirAndChooseAction(url, actionToDo);
                    }
                })
        );
    }

    private String getCurrentUrl() {
        return mAdapter.getItem(mViewPager.getCurrentItem()).getUrl();
    }

    private void beginCrop() {
        hideProgressBar();
        String url = getCurrentUrl();
        int screenWidth = getScreenPoint().x;
        int screenHeight = getScreenPoint().y;
        Uri source = Uri.fromFile(ViewModel.Current.fileUtils.getTemporaryFile(ViewModel.Current.fileUtils
                .getFileName(url)));
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).withAspect(screenWidth, screenHeight)
                .start(this);
    }

    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void createIntent(IntentTypeEnum intentType) {
        hideProgressBar();
        if (!ViewModel.Current.device.ShareFileWithIntentType(this,
                ViewModel.Current.fileUtils.getTemporaryFile(ViewModel.Current.fileUtils.getFileName(getCurrentUrl())),
                intentType));
        ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.appNotInstalled));
    }

    public void setAsWallpaper() {
        hideProgressBar();
        addSubscribe(Flowable.fromCallable(() -> ViewModel.Current.device.decodeBitmapAndSetAsLiveWallpaper(ViewModel.Current.fileUtils.
                getTemporaryFile(ViewModel.Current.fileUtils.getFileName(getCurrentUrl()))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> {
                    if (t)
                        ViewModel.Current.device.showSnackMessage(mRootLayout, "Success");
                    else
                        ViewModel.Current.device.showSnackMessage(mRootLayout, "Error");
                })
        );
    }


    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {

            addSubscribe(Flowable.fromCallable(() -> ViewModel.Current.fileUtils.setAsWallpaper(MediaStore.Images.Media.getBitmap(
                    this.getContentResolver(), Crop.getOutput(result))))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorReturn(error -> false)
                    .subscribe(setSuccess -> {
                        if (setSuccess) {
                            ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.detail_snack_set_success));
                            showADS();
                        } else {
                            ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.detail_snack_set_failure));
                        }
                    })
            );

        } else if (resultCode == Crop.RESULT_ERROR) {
            ViewModel.Current.device.showSnackMessage(mRootLayout, Crop.getError(result).getMessage());
        }
    }


    @Override
    public void onRequestPermissions() {
        final int REQUEST_CODE_ASK_PERMISSIONS = 123;
        ActivityCompat.requestPermissions(DetailsActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE_ASK_PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop();
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        } else {
            showADS();
        }
    }

    public void showADS() {
        ViewModel.Current.device.trackAction(mTracker, "ADS", "ShowAdsAfterImageAction");
    }

    public Point getScreenPoint() {
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public void saveFileToPermanentGallery() {
        addSubscribe(Flowable.fromCallable(() -> ViewModel.Current.fileUtils.savePermanentFile(getCurrentUrl()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        ViewModel.Current.device.showSnackMessage(mRootLayout,
                                getString(R.string.detail_snack_save_success));
                    } else {
                        ViewModel.Current.device.showSnackMessage(mRootLayout,
                                getString(R.string.detail_snack_save_failure));
                        ViewModel.Current.device.checkPermission(this,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                    hideProgressBar();
                })
        );
    }

    public void shareAll() {
        hideProgressBar();
        ViewModel.Current.device.shareFileAll(this,
                ViewModel.Current.fileUtils.
                        getTemporaryFile(ViewModel.Current.fileUtils.getFileName(getCurrentUrl())));
    }

    public void sendToRippleLwp() {
        hideProgressBar();
        mFromRipple = true;
        //ViewModel.Current.device.clearCurrentWallpaper();
        ViewModel.Current.dataUtils
                .SetSetting(Constants.CHANGE_IMAGE_KEY, Constants.CUSTOM_LWP);
        ViewModel.Current.device.openRippleLwp(this);
        Constants.FilePath = ViewModel.Current.fileUtils
                .getTemporaryFile(ViewModel.Current.fileUtils.getFileName(getCurrentUrl())).getPath();
        ViewModel.Current.dataUtils.SetSetting(Constants.CHANGE_IMAGE_KEY,
                Constants.CUSTOM_LWP);
    }

    @Override
    public void onGoToCropActivity() {
        addSubscribe(Flowable.fromCallable(() -> ViewModel.Current.fileUtils.isSavedToStorage(ViewModel.Current.fileUtils
                .getFileName(getCurrentUrl())))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(aBoolean -> {
            if (aBoolean)
                beginCrop();
        }));
    }

    @Override
    public void onMoveFileToPermanentGallery() {
        saveFileToPermanentGallery();
    }

    @Override
    public void onOpenNativeSetWallChoose() {
        shareAll();
    }

    @Override
    public void onOpenWithFaceBook() {
        createIntent(IntentTypeEnum.FACEBOOKINTENT);
    }

    @Override
    public void onOpenWithInstagram() {
        createIntent(IntentTypeEnum.INTAGRAMINTENT);
    }

    @Override
    public void onSendToRippleLwp() {
        sendToRippleLwp();
    }

    @Override
    public void onShareWhitApplication() {
        createIntent(IntentTypeEnum.SHNAPCHATINTENT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForCrashes();
        if (mFromRipple) {
            if (GalleryWallpaperActivity.isAdsShowedFromRipple) {
                showADS();
            }
            mFromRipple = false;
        }
    }
}
