package com.sami.rippel.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.kobakei.ratethisapp.RateThisApp;
import com.sami.rippel.allah.BuildConfig;
import com.sami.rippel.allah.R;
import com.sami.rippel.allah.WallpaperApplication;
import com.sami.rippel.base.BaseActivity;
import com.sami.rippel.model.Constants;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.model.entity.ServiceErrorFromEnum;
import com.sami.rippel.model.entity.UpdateApp;
import com.sami.rippel.model.entity.WallpapersRetrofitObject;
import com.sami.rippel.model.listner.AdsListner;
import com.sami.rippel.model.listner.DeviceListner;
import com.sami.rippel.ui.adapter.CatalogPagerAdapter;
import com.sami.rippel.utils.RxUtil;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;
import com.tbruyelle.rxpermissions2.RxPermissions;

import net.hockeyapp.android.CrashManager;

import butterknife.BindView;

public class ViewPagerWallpaperActivity extends BaseActivity implements AdsListner, DeviceListner {

    @Nullable
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Nullable
    @BindView(R.id.rootLayout)
    CoordinatorLayout mRootLayout;
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @Nullable
    @BindView(R.id.toolbarTabs)
    TabLayout mTabLayout;
    @Nullable
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @Nullable
    @BindView(R.id.progressBar)
    ProgressBar mProgressLoader;

    private static final int PICK_FROM_FILE = 3;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    public static Boolean isAdsShow = false;
    public static int nbOpenAds = 0;
    public static boolean stat = false;
    private static long back_pressed;
    public boolean isFirstLaunch = false;
    RxPermissions rxPermissions;
    private Tracker mTracker;
    private CatalogPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForCrashes();
        if (!BuildConfig.DEBUG) {
            StartAppSDK.init(this, "211624686", true);
        }
        rxPermissions = new RxPermissions(this);
        ViewModel.Current.dataUtils.SetSetting("IsTheFirstRun", false);
        isFirstLaunch = true;
        WallpaperApplication application = (WallpaperApplication) getApplication();
        mTracker = application.getDefaultTracker();
        setupToolBar();
        setupViewPager();
        initRatingApp();
        initAdsSDK();
        manageNbRunApp();
        //startUpdateAppIfNeeded();
        mAdapter.ChangeAdapterFragmentViewState(false);
        loadAndSetWallpaperToViewModel();
        checkPermission();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_view_pager;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForCrashes();
        ViewModel.Current.device.setmDeviceListner(this);
        showFirstTimeAndOneTimeAds();
        showTimedAdsWhenIOpenPicture();
        onOpenScreenTracker("ViewPager");
    }

    public void onOpenScreenTracker(String screenName) {
        mTracker.setScreenName(screenName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void startUpdateAppIfNeeded() {
        assert mProgressLoader != null;
        mProgressLoader.setVisibility(View.VISIBLE);
        addSubscribe(ViewModel.Current.service.mRetrofitHelper.getUpdateObject()
                .compose(RxUtil.<UpdateApp>rxSchedulerHelper())
                .subscribe(updateApp -> {
                    if (updateApp.isUpdateAppNeeded()) {
                        onStartCheckUpdateNewWallpapersDataBase();
                    } else {
                        onUpdateNotNeeded();
                    }
                }, throwable -> {
                })
        );
    }

    public void loadAndSetWallpaperToViewModel() {
        addSubscribe(ViewModel.Current.service.mRetrofitHelper.getWallpapersList()
                .compose(RxUtil.<WallpapersRetrofitObject>rxSchedulerHelper())
                .subscribe(wallpapersRetrofitObject -> {
                    if (wallpapersRetrofitObject.getCategoryList().size() > 0) {
                        ViewModel.Current.setRetrofitWallpObject(wallpapersRetrofitObject);
                        onFillAdapterWithServiceData();
                        mAdapter.ChangeAdapterFragmentViewState(true);
                    }
                }, throwable -> {
                    ViewModel.Current.device.showSnackMessage(mRootLayout, "Parsing Wallpaper Data Error" + throwable.getMessage());
                })
        );
    }

    public void initAdsSDK() {
    }

    public void initRatingApp() {
        RateThisApp.Config config = new RateThisApp.Config(3, 5);
        config.setTitle(R.string.txtrate2);
        config.setMessage(R.string.txtrate1);
        config.setYesButtonText(R.string.txtrate5);
        config.setNoButtonText(R.string.txtrate4);
        config.setCancelButtonText(R.string.txtrate3);
        RateThisApp.setCallback(new RateThisApp.Callback() {
            @Override
            public void onYesClicked() {
                RateThisApp.stopRateDialog(ViewPagerWallpaperActivity.this);
                ViewModel.Current.dataUtils.SetSetting(Constants.RATING_MESSAGE,
                        Constants.RATING_NON);
            }

            @Override
            public void onNoClicked() {
                ViewModel.Current.dataUtils.SetSetting(Constants.RATING_MESSAGE,
                        Constants.RATING_NON);
            }

            @Override
            public void onCancelClicked() {
            }
        });

        RateThisApp.init(config);
    }

    public void manageNbRunApp() {
        int nbRun = ViewModel.Current.dataUtils.GetSetting("NbRun", 0);
        if (ViewModel.Current.dataUtils.GetSetting("IsSecondRun", "null").equals("null")) {
            ViewModel.Current.dataUtils.SetSetting("IsSecondRun",
                    "Second");
            nbRun = nbRun + 1;
            ViewModel.Current.dataUtils.SetSetting("NbRun",
                    nbRun);
        } else if (nbRun == 3) {
            ViewModel.Current.dataUtils.SetSetting("NbRun",
                    0);
            showRunAppADS();
        } else {
            nbRun = nbRun + 1;
            ViewModel.Current.dataUtils.SetSetting("NbRun",
                    nbRun);
        }
    }

    public void checkPermission() {

        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_SETTINGS
                )
                .subscribe(granted -> {
                });
    }

    public void showRunAppADS() {
        onTrackAction("ADS", "RunApplication");
    }

    private void setupToolBar() {
        setSupportActionBar(mToolbar);
        mCollapsingToolbarLayout.setTitle(" ");
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.TRANSPARENT);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    private void setupViewPager() {
        mAdapter = new CatalogPagerAdapter(getSupportFragmentManager(), this, this);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (mAdapter != null)
                    mAdapter.ChooseFragmentToExcecuteAction(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void rateApplication() {
        if (ViewModel.Current.dataUtils.GetSetting(Constants.RATING_MESSAGE, Constants.RATING_YES).equals(Constants.RATING_YES)) {
            RateThisApp.showRateDialog(this);
        }
    }

    public void onTrackAction(String category, String name) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(name)
                .build());
    }

    public void showFirstTimeAndOneTimeAds() {
        if (isAdsShow) {
            isAdsShow = false;
            onTrackAction("ADS", AdsType.ShowAds.toString());
            showInterstial();
        }
    }

    public void showInterstial() {
        StartAppAd.showAd(this);
    }

    public void showTimedAdsWhenIOpenPicture() {

        if (nbOpenAds == 3) {

        }

        if (nbOpenAds == 4) {
            nbOpenAds = 0;
            showInterstial();
            onTrackAction("ADS", AdsType.ShowTimedAds.toString());
        }

        if (nbOpenAds == 7) {
            rateApplication();
            onOpenScreenTracker("RatingDialog");
        }
    }

    public void checkUpdateNewWallpapers() {
        loadAndSetWallpaperToViewModel();
    }

    private void checkForCrashes() {
        CrashManager.register(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case PICK_FROM_FILE:
                stat = true;
                ViewModel.Current.device.openChooseActivityWithPhoto(this, data);
                break;

            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    stat = true;
                    ViewModel.Current.device.openChooseActivityFromCamera(this);
                }
                break;
            case 123:
                Log.d("permission", "ok");
                break;
        }
    }

    @Override
    public void onOpenGalleryChooser() {
        ViewModel.Current.device.openFileChooser(this, PICK_FROM_FILE);
    }

    @Override
    public void onOpenCameraChooser() {
        ViewModel.Current.device.openCameraChooser(this, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        RateThisApp.onStart(this);
    }

    @Override
    public void onBackPressed() {
        if (!BuildConfig.DEBUG) {
            if (back_pressed + 2000 > System.currentTimeMillis())
                super.onBackPressed();
            else {
                rateApplication();
                Toast.makeText(getBaseContext(), R.string.txtrate6,
                        Toast.LENGTH_SHORT).show();
            }
            back_pressed = System.currentTimeMillis();
        }
    }

    @Override
    public void onRequestPermissions() {
        ActivityCompat.requestPermissions(ViewPagerWallpaperActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.SET_WALLPAPER_HINTS,
                        Manifest.permission.WRITE_SETTINGS,
                        Manifest.permission.SET_WALLPAPER,
                        Manifest.permission.RECEIVE_BOOT_COMPLETED},

                REQUEST_CODE_ASK_PERMISSIONS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onFillAdapterWithServiceData() {
        mProgressLoader.setVisibility(View.GONE);
    }

    public void onStartCheckUpdateNewWallpapersDataBase() {

        mProgressLoader.setVisibility(View.VISIBLE);
        checkUpdateNewWallpapers();
    }

    public void onUpdateNotNeeded() {
        mProgressLoader.setVisibility(View.GONE);
    }

    public void onRestartCheckIfCallError(ServiceErrorFromEnum errorFromEnum) {
        if (errorFromEnum == ServiceErrorFromEnum.UPDATE_APPLICATION_CALL)
            startUpdateAppIfNeeded();
        else if (errorFromEnum == ServiceErrorFromEnum.GET_WALLPAPER_LIST_CALL)
            checkUpdateNewWallpapers();
    }

    @Override
    protected void initInject() {

    }

    @Override
    public void showSnackMsg(String msg) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showADS() {

    }

    public enum AdsType {
        ShowAds,
        ShowTimedAds,
    }

    public enum LwpTypeEnum {
        RIPPLE_TYPE,
        SKYBOX_TYPE,
        DOUA_TYPE,
        TIMER_TYPE,
    }
}
