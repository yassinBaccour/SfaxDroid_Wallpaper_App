package com.sami.rippel.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import com.sami.rippel.allah.R;
import com.sami.rippel.allah.WallpaperApplication;
import com.sami.rippel.base.BaseActivity;
import com.sami.rippel.model.Constants;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.model.entity.ActionTypeEnum;
import com.sami.rippel.model.entity.IntentTypeEnum;
import com.sami.rippel.model.entity.WallpaperObject;
import com.sami.rippel.model.listner.DeviceListner;
import com.sami.rippel.model.listner.WallpaperListener;
import com.sami.rippel.presenter.Contract.DetailContract;
import com.sami.rippel.presenter.DetailPresenter;
import com.sami.rippel.ui.adapter.DetailPagerAdapter;
import com.soundcloud.android.crop.Crop;

import net.hockeyapp.android.CrashManager;

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
        WallpaperApplication application = (WallpaperApplication) getApplication();
        mFab = findViewById(R.id.fab);
        ViewModel.Current.fileUtils.SetListner(this);
        ViewModel.Current.device.setmDeviceListner(this);
        mPagerData = getIntent().getParcelableArrayListExtra(Constants.LIST_FILE_TO_SEND_TO_DETAIL_VIEW_PAGER);
        mPos = getIntent().getIntExtra(Constants.DETAIL_IMAGE_POS, 0);
        mFrom = getIntent().getStringExtra(Constants.KEY_LWP_NAME);
        initToolbar();
        setFabImageResource();
        mFab.setOnClickListener(e -> fabClick());
        setupViewPager();
        if (Build.VERSION.SDK_INT >= 23) {
            ViewModel.Current.device.checkPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
        return new DetailPresenter(null);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_details;
    }

    @Override
    protected void initEventAndData() {
//Nothing
    }

    private void fabClick() {
        if (mFrom != null && !mFrom.isEmpty()) {
            switch (mFrom) {
                case Constants.KEY_ADD_TIMER_LWP:
                    mPresenter.saveTempsDorAndDoAction(ActionTypeEnum.MOVE_PERMANENT_DIR, getCurrentUrl());
                    break;
                case Constants.KEY_ADDED_LIST_TIMER_LWP:
                    mPresenter.saveTempsDorAndDoAction(ActionTypeEnum.DELETE_CURRENT_PICTURE, getCurrentUrl());
                    break;
                case Constants.KEY_RIPPLE_LWP:
                    mPresenter.saveTempsDorAndDoAction(ActionTypeEnum.SEND_LWP, getCurrentUrl());
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
                    mFab.setImageResource(R.mipmap.ic_download);
                    break;
                case Constants.KEY_ADDED_LIST_TIMER_LWP:
                    mFab.setImageResource(R.mipmap.ic_delete);
                    break;
                case Constants.KEY_RIPPLE_LWP:
                    mFab.setImageResource(R.mipmap.ic_ripple_fab);
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
            switch (item.getItemId()) {
                case R.id.buttonWallpaper:
                    mPresenter.saveTempsDorAndDoAction(ActionTypeEnum.CROP, getCurrentUrl());
                    break;
                case R.id.buttonChooser:
                    mPresenter.saveTempsDorAndDoAction(ActionTypeEnum.OPEN_NATIV_CHOOSER, getCurrentUrl());
                    break;
                case R.id.buttonSave:
                    mPresenter.saveTempsDorAndDoAction(ActionTypeEnum.MOVE_PERMANENT_DIR, getCurrentUrl());
                    break;
                case R.id.buttonSareInsta:
                    mPresenter.saveTempsDorAndDoAction(ActionTypeEnum.SHARE_INSTA, getCurrentUrl());
                    break;
                case R.id.buttonSareFb:
                    mPresenter.saveTempsDorAndDoAction(ActionTypeEnum.SHARE_FB, getCurrentUrl());
                    break;
                case R.id.buttonShare:
                    mPresenter.saveTempsDorAndDoAction(ActionTypeEnum.SHARE_SNAP_CHAT, getCurrentUrl());
                    break;
                case R.id.buttonRipple:
                    mPresenter.saveTempsDorAndDoAction(ActionTypeEnum.SEND_LWP, getCurrentUrl());
                    break;
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
                mPresenter.saveFileToPermanentGallery(getCurrentUrl(), this);
            }
            if (actionToDo == ActionTypeEnum.DELETE_CURRENT_PICTURE) {
                deleteCurrentPicture();
            }
            if (actionToDo == ActionTypeEnum.JUST_WALLPAPER) {
                mPresenter.setAsWallpaper(getCurrentUrl());
            }
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
            ViewModel.Current.fileUtils.saveToFileToTempsDirAndChooseAction(getCurrentUrl(), actionToDo);
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
        Uri source = Uri.fromFile(ViewModel.Current.fileUtils.getTemporaryFile(ViewModel.Current.fileUtils
                .getFileName(url)));
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
        if (!ViewModel.Current.device.ShareFileWithIntentType(this,
                ViewModel.Current.fileUtils.getTemporaryFile(ViewModel.Current.fileUtils.getFileName(getCurrentUrl())),
                intentType)) {
            ViewModel.Current.device.showSnackMessage(mRootLayout, getString(R.string.appNotInstalled));
        }
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            mPresenter.handleCrop(result, this);
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
        ViewModel.Current.device.shareFileAll(this,
                ViewModel.Current.fileUtils.
                        getTemporaryFile(ViewModel.Current.fileUtils.getFileName(getCurrentUrl())));
    }

    /*
    public void sendToRippleLwp() {
        hideLoading();
        mFromRipple = true;
        //ViewModel.Current.device.clearCurrentWallpaper();
        ViewModel.Current.sharedPrefsUtils
                .SetSetting(Constants.CHANGE_IMAGE_KEY, Constants.CUSTOM_LWP);
        ViewModel.Current.device.openRippleLwp(this);
        Constants.FilePath = ViewModel.Current.fileUtils
                .getTemporaryFile(ViewModel.Current.fileUtils.getFileName(getCurrentUrl())).getPath();
        ViewModel.Current.sharedPrefsUtils.SetSetting(Constants.CHANGE_IMAGE_KEY,
                Constants.CUSTOM_LWP);
    }
    */

    @Override
    public void onGoToCropActivity() {
        addSubscribe(Flowable.fromCallable(() -> ViewModel.Current.fileUtils.isSavedToStorage(ViewModel.Current.fileUtils
                .getFileName(getCurrentUrl())))
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
        mPresenter.saveFileToPermanentGallery(getCurrentUrl(), this);
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
        ViewModel.Current.device.showSnackMessage(mRootLayout, "Error Reading Storage");
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
        ViewModel.Current.device.showSnackMessage(mRootLayout, msg);
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
