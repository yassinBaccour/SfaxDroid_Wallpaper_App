package com.sfaxdroid.detail;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.flipboard.bottomsheet.commons.MenuSheetView.OnMenuItemClickListener;

import com.google.android.material.snackbar.Snackbar;
import com.sfaxdroid.bases.ActionTypeEnum;
import com.sfaxdroid.base.Constants;
import com.sfaxdroid.bases.DeviceListner;
import com.sfaxdroid.base.FileUtils;
import com.sfaxdroid.bases.IntentTypeEnum;
import com.sfaxdroid.base.Utils;
import com.sfaxdroid.bases.WallpaperListener;
import com.sfaxdroid.base.WallpaperObject;
import com.soundcloud.android.crop.Crop;


import java.io.File;
import java.util.ArrayList;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DetailsActivity extends BaseActivity<DetailPresenter> implements WallpaperListener, DeviceListner, DetailContract.View {

    public Toolbar mToolbar;

    public ViewPager mViewPager;

    public ProgressBar mProgressBar;

    public BottomSheetLayout mBottomSheet;

    public CoordinatorLayout mRootLayout;

    public FloatingActionButton mFab;

    private int mPos;

    private String mFrom = "";

    private boolean mFromRipple = false;

    private DetailPagerAdapter mAdapter;

    private ArrayList<WallpaperObject> mPagerData = new ArrayList<>();

    private void initView() {
        mFab = findViewById(R.id.fab);
        mRootLayout = findViewById(R.id.rootLayout);
        mBottomSheet = findViewById(R.id.bottomsheetLayout);
        mProgressBar = findViewById(R.id.progressBar);
        mViewPager = findViewById(R.id.viewpager);
        mToolbar = findViewById(R.id.toolbar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
        mFab = findViewById(R.id.fab);
        mPagerData = getIntent().getParcelableArrayListExtra(Constants.LIST_FILE_TO_SEND_TO_DETAIL_VIEW_PAGER);
        mPos = getIntent().getIntExtra(Constants.DETAIL_IMAGE_POS, 0);
        mFrom = getIntent().getStringExtra(Constants.KEY_LWP_NAME);
        initToolbar();
        setFabImageResource();
        mFab.setOnClickListener(e -> fabClick());
        setupViewPager();
        if (Build.VERSION.SDK_INT >= 23) {
            Utils.Companion.checkPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, this);
        }
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

    @Override
    protected DetailPresenter instantiatePresenter() {
        return new DetailPresenter();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_details;
    }

    @Override
    protected void initEventAndData() {
    }

    private void fabClick() {
        if (mFrom != null && !mFrom.isEmpty()) {
            switch (mFrom) {
                case Constants.KEY_ADD_TIMER_LWP:
                    mPresenter.saveTempsDorAndDoAction(ActionTypeEnum.MOVE_PERMANENT_DIR, getCurrentUrl(), this, getString(R.string.app_namenospace));
                    break;
                case Constants.KEY_ADDED_LIST_TIMER_LWP:
                    mPresenter.saveTempsDorAndDoAction(ActionTypeEnum.DELETE_CURRENT_PICTURE, getCurrentUrl(), this, getString(R.string.app_namenospace));
                    break;
                case Constants.KEY_RIPPLE_LWP:
                    mPresenter.saveTempsDorAndDoAction(ActionTypeEnum.SEND_LWP, getCurrentUrl(), this, getString(R.string.app_namenospace));
                    break;
            }
        } else {
            showMenuSheet(MenuSheetView.MenuType.LIST);
        }
    }

    public void setFabImageResource() {
        if (mFrom != null && !mFrom.isEmpty()) {
            switch (mFrom) {
                case com.sfaxdroid.base.Constants.KEY_ADD_TIMER_LWP:
                    mFab.setImageResource(R.mipmap.ic_download);
                    break;
                case com.sfaxdroid.base.Constants.KEY_ADDED_LIST_TIMER_LWP:
                    mFab.setImageResource(R.mipmap.ic_delete);
                    break;
                case Constants.KEY_RIPPLE_LWP:
                    mFab.setImageResource(R.mipmap.ic_ripple_fab);
                    break;
            }
        }
    }

    private void checkForCrashes() {
    }

    public void deleteCurrentPicture() {
        if (FileUtils.Companion.deleteFile(getCurrentUrl())) {
            finishWithResult();
        } else {
            showSnackMessage(mRootLayout, "Error Deleting file");
        }
    }

    public void showSnackMessage(CoordinatorLayout mRootLayout, String message) {
        Snackbar snackbar = Snackbar
                .make(mRootLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void finishWithResult() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
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
                mPagerData, this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mPos);
    }

    public OnMenuItemClickListener getMenuSheetListener() {
        return item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.buttonWallpaper) {
                mPresenter.saveTempsDorAndDoAction(ActionTypeEnum.CROP, getCurrentUrl(), this, getString(R.string.app_namenospace));
            } else if (itemId == R.id.buttonChooser) {
                mPresenter.saveTempsDorAndDoAction(ActionTypeEnum.OPEN_NATIV_CHOOSER, getCurrentUrl(), this, getString(R.string.app_namenospace));
            } else if (itemId == R.id.buttonSave) {
                mPresenter.saveTempsDorAndDoAction(ActionTypeEnum.MOVE_PERMANENT_DIR, getCurrentUrl(), this, getString(R.string.app_namenospace));
            } else if (itemId == R.id.buttonSareInsta) {
                mPresenter.saveTempsDorAndDoAction(ActionTypeEnum.SHARE_INSTA, getCurrentUrl(), this, getString(R.string.app_namenospace));
            } else if (itemId == R.id.buttonSareFb) {
                mPresenter.saveTempsDorAndDoAction(ActionTypeEnum.SHARE_FB, getCurrentUrl(), this, getString(R.string.app_namenospace));
            }
            dismissMenuSheet();
            return true;
        };
    }

    @Override
    public void onSaveTempsDorAndDoAction(Boolean aBoolean, ActionTypeEnum actionToDo) {
        if (aBoolean) {
            if (actionToDo == ActionTypeEnum.OPEN_NATIV_CHOOSER) {
                shareAll();
            }
            if (actionToDo == ActionTypeEnum.SHARE_FB) {
                createIntent(IntentTypeEnum.FACEBOOKINTENT);
            }
            if (actionToDo == ActionTypeEnum.SHARE_INSTA) {
                createIntent(IntentTypeEnum.INTAGRAMINTENT);
            }
            if (actionToDo == ActionTypeEnum.SEND_LWP) {
                //sendToRippleLwp();
            }
            if (actionToDo == ActionTypeEnum.CROP) {
                beginCrop();
            }
            if (actionToDo == ActionTypeEnum.MOVE_PERMANENT_DIR) {
                mPresenter.saveFileToPermanentGallery(getCurrentUrl(), this, getString(R.string.app_namenospace), this);
            }
            if (actionToDo == ActionTypeEnum.DELETE_CURRENT_PICTURE) {
                deleteCurrentPicture();
            }
            if (actionToDo == ActionTypeEnum.JUST_WALLPAPER) {
                mPresenter.setAsWallpaper(getCurrentUrl(), this, getString(R.string.app_namenospace));
            }
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
            Utils.Companion.saveToFileToTempsDirAndChooseAction(getCurrentUrl(), actionToDo, Utils.Companion.getScreenHeightPixels(this), Utils.Companion.getScreenWidthPixels(this), this, getString(R.string.app_namenospace), this, null);
        }
    }

    private String getCurrentUrl() {
        return mAdapter.getItem(mViewPager.getCurrentItem()).getUrl();
    }

    private void beginCrop() {
        hideLoading();
        String url = getCurrentUrl();
        int screenWidth = getScreenPoint().x;
        int screenHeight = getScreenPoint().y;
        Uri source = Uri.fromFile(FileUtils.Companion.getTemporaryFile(FileUtils.Companion.getFileName(url), this, getString(R.string.app_namenospace)));
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).withAspect(screenWidth, screenHeight)
                .start(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void createIntent(IntentTypeEnum intentType) {
        hideLoading();
        if (!Utils.Companion.shareFileWithIntentType(this,
                FileUtils.Companion.getTemporaryFile(FileUtils.Companion.getFileName(getCurrentUrl()), this, getString(R.string.app_namenospace)),
                intentType)) {
            showSnackMessage(mRootLayout, getString(R.string.appNotInstalled));
        }
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
            mPresenter.handleCrop(result, this);
        } else if (resultCode == Crop.RESULT_ERROR) {
            showSnackMessage(mRootLayout, Crop.getError(result).getMessage());
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
        super.onActivityResult(requestCode, resultCode, result);
        if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) {
            beginCrop();
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    public Point getScreenPoint() {
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public void shareAll() {
        hideLoading();
        Utils.Companion.shareAllFile(this,
                FileUtils.Companion.
                        getTemporaryFile(FileUtils.Companion.getFileName(getCurrentUrl()), this, getString(R.string.app_namenospace)));
    }

    @Override
    public void onGoToCropActivity() {
        addSubscribe(Flowable.fromCallable(() -> FileUtils.Companion.isSavedToStorage(
                FileUtils.Companion.getFileName(getCurrentUrl()), this, getString(R.string.app_namenospace)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        beginCrop();
                    } else {
                        finish();
                    }
                }));
    }

    @Override
    public void onMoveFileToPermanentGallery() {
        mPresenter.saveFileToPermanentGallery(getCurrentUrl(), this, getString(R.string.app_namenospace), this);
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
        //sendToRippleLwp();
    }

    @Override
    public void onShareWhitApplication() {
        createIntent(IntentTypeEnum.SHNAPCHATINTENT);
    }

    @Override
    public void onFinishActivity() {
        showSnackMessage(mRootLayout, "Error Reading Storage");
        hideLoading();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForCrashes();
        if (mFromRipple) {
            mFromRipple = false;
        }
    }

    @Override
    public void showSnackMsg(String msg) {
        showSnackMessage(mRootLayout, msg);
    }

    @Override
    public void showLoading() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initInject() {

    }
}
