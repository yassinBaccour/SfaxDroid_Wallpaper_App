package com.sami.rippel.feature.main.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.kobakei.ratethisapp.RateThisApp;
import com.sami.rippel.allah.BuildConfig;
import com.sami.rippel.allah.R;
import com.sami.rippel.base.BaseActivity;
import com.sami.rippel.model.Constants;
import com.sami.rippel.model.ViewModel;
import com.sfaxdroid.bases.DeviceListner;
import com.sami.rippel.feature.main.adapter.CatalogPagerAdapter;
import com.sami.rippel.utils.RxUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;


public class HomeActivity extends BaseActivity implements DeviceListner {

    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private CoordinatorLayout mRootLayout;

    private Toolbar mToolbar;

    private TabLayout mTabLayout;

    private ViewPager mViewPager;

    private ProgressBar mProgressLoader;

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    public static Boolean isAdsShow = false;

    public static int nbOpenAds = 0;

    private static long back_pressed;

    private RxPermissions rxPermissions;

    private CatalogPagerAdapter mAdapter;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rxPermissions = new RxPermissions(this);
        setupAds();
        ViewModel.Current.sharedPrefsUtils.SetSetting("IsTheFirstRun", false);
        initView();
        setupToolBar();
        setupViewPager();
        initRatingApp();
        manageNbRunApp();
        mAdapter.changeButtonSate(false);
        loadAndSetWallpaperToViewModel();
        checkPermission();
    }

    private void initView() {
        mCollapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        mRootLayout = findViewById(R.id.rootLayout);
        mToolbar = findViewById(R.id.toolbar);
        mTabLayout = findViewById(R.id.toolbarTabs);
        mViewPager = findViewById(R.id.viewpager);
        mProgressLoader = findViewById(R.id.progressBar);
    }

    private void setupAds() {
        MobileAds.initialize(this, initializationStatus -> {
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6263632629106733/6333632738");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.activity_home;
    }

    @Override
    public void initEventAndData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        showFirstTimeAndOneTimeAds();
        showTimedAdsWhenIOpenPicture();
    }

    public void loadAndSetWallpaperToViewModel() {
        addSubscribe(ViewModel.Current.service.mRetrofitHelper.getWallpapersList()
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(wallpapersRetrofitObject -> {
                    if (wallpapersRetrofitObject.getCategoryList().size() > 0) {
                        ViewModel.Current.setRetrofitWallpObject(wallpapersRetrofitObject);
                        onFillAdapterWithServiceData();
                        mAdapter.changeButtonSate(true);
                    }
                }, throwable -> {
                    showSnackMessage(mRootLayout, "Parsing Wallpaper Data Error" + throwable.getMessage());
                })
        );
    }

    public void showSnackMessage(CoordinatorLayout mRootLayout, String message) {
        Snackbar snackbar = Snackbar
                .make(mRootLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
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
                RateThisApp.stopRateDialog(HomeActivity.this);
                ViewModel.Current.sharedPrefsUtils.SetSetting(Constants.RATING_MESSAGE,
                        Constants.RATING_NON);
            }

            @Override
            public void onNoClicked() {
                ViewModel.Current.sharedPrefsUtils.SetSetting(Constants.RATING_MESSAGE,
                        Constants.RATING_NON);
            }

            @Override
            public void onCancelClicked() {
            }
        });

        RateThisApp.init(config);
    }

    public void manageNbRunApp() {
        int nbRun = ViewModel.Current.sharedPrefsUtils.GetSetting("NbRun", 0);
        if (ViewModel.Current.sharedPrefsUtils.GetSetting("IsSecondRun", "null").equals("null")) {
            ViewModel.Current.sharedPrefsUtils.SetSetting("IsSecondRun",
                    "Second");
            nbRun = nbRun + 1;
            ViewModel.Current.sharedPrefsUtils.SetSetting("NbRun",
                    nbRun);
        } else if (nbRun == 3) {
            ViewModel.Current.sharedPrefsUtils.SetSetting("NbRun",
                    0);
            showRunAppADS();
        } else {
            nbRun = nbRun + 1;
            ViewModel.Current.sharedPrefsUtils.SetSetting("NbRun",
                    nbRun);
        }
    }

    @SuppressLint("CheckResult")
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

        ArrayList titleList = new ArrayList<String>();
        titleList.add(getString(R.string.catalog_LWP));
        titleList.add(getString(R.string.catalog_All));
        titleList.add(getString(R.string.catalog_Category));
        titleList.add(getString(R.string.catalog_Lab));

        mAdapter = new CatalogPagerAdapter(getSupportFragmentManager(), titleList);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (mAdapter != null) {
                    mAdapter.downloadPicture(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void rateApplication() {
        if (ViewModel.Current.sharedPrefsUtils.GetSetting(Constants.RATING_MESSAGE, Constants.RATING_YES).equals(Constants.RATING_YES)) {
            RateThisApp.showRateDialog(this);
        }
    }

    public void onTrackAction(String category, String name) {
    }

    public void showFirstTimeAndOneTimeAds() {
        if (isAdsShow) {
            isAdsShow = false;
            onTrackAction("ADS", AdsType.ShowAds.toString());
            showInterstitial();
        }
    }

    public void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    public void showTimedAdsWhenIOpenPicture() {

        if (nbOpenAds == 4) {
            nbOpenAds = 0;
            showInterstitial();
            onTrackAction("ADS", AdsType.ShowTimedAds.toString());
        }

        if (nbOpenAds == 7) {
            rateApplication();
        }
    }

    public void checkUpdateNewWallpapers() {
        loadAndSetWallpaperToViewModel();
    }


    @Override
    protected void onStart() {
        super.onStart();
        RateThisApp.onStart(this);
    }

    @Override
    public void onBackPressed() {
        if (!BuildConfig.DEBUG) {
            if (back_pressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                rateApplication();
                Toast.makeText(getBaseContext(), R.string.txtrate6,
                        Toast.LENGTH_SHORT).show();
            }
            back_pressed = System.currentTimeMillis();
        }
    }

    @Override
    public void onRequestPermissions() {
        ActivityCompat.requestPermissions(HomeActivity.this,
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


    @Override
    public void showSnackMsg(String s) {

    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    public enum AdsType {
        ShowAds,
        ShowTimedAds,
    }

}
