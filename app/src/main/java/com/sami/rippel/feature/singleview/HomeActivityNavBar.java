package com.sami.rippel.feature.singleview;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.kobakei.ratethisapp.RateThisApp;
import com.sami.rippel.allah.R;
import com.sami.rippel.base.BaseActivity;
import com.sami.rippel.model.Constants;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.model.entity.ServiceErrorFromEnum;
import com.sami.rippel.model.listner.AdsListener;
import com.sfaxdroid.bases.DeviceListner;
import com.sami.rippel.utils.RxUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;


public class HomeActivityNavBar extends BaseActivity implements AdsListener, DeviceListner {

    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private CoordinatorLayout mRootLayout;

    private Toolbar mToolbar;

    private ProgressBar mProgressLoader;

    private ImageView mPrivacy;

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    public static Boolean isAdsShow = false;

    public static int nbOpenAds = 0;

    public static boolean stat = false;

    private static long back_pressed;

    private RxPermissions rxPermissions;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForCrashes();
        rxPermissions = new RxPermissions(this);
        setupAds();
        ViewModel.Current.sharedPrefsUtils.SetSetting("IsTheFirstRun", false);
        initView();
        setupToolBar();
        initRatingApp();
        manageNbRunApp();
        loadAndSetWallpaperToViewModel();
        checkPermission();
    }

    private void loadFragment() {
        AllInOneFragment mAllBackgroundFragment = AllInOneFragment.Companion.newInstance();
        mAllBackgroundFragment.setListener(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content_main, mAllBackgroundFragment, "Home.class");
        fragmentTransaction.commit();
    }

    private void initView() {
        mCollapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        mRootLayout = findViewById(R.id.rootLayout);
        mToolbar = findViewById(R.id.toolbar);
        mProgressLoader = findViewById(R.id.progressBar);
        mPrivacy = findViewById(R.id.imgPrivacy);

        mPrivacy.setOnClickListener(x -> {
            Intent intent = new Intent(this,
                    PrivacyActivity.class);
            startActivity(intent);
        });
    }

    private void setupAds() {
        MobileAds.initialize(this, initializationStatus -> {
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.intertitial));
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
        return R.layout.activity_home_nav;
    }

    @Override
    public void initEventAndData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForCrashes();
        showFirstTimeAndOneTimeAds();
        showTimedAdsWhenIOpenPicture();
        onOpenScreenTracker("ViewPager");
    }

    public void onOpenScreenTracker(String screenName) {
    }

    public void startUpdateAppIfNeeded() {
        assert mProgressLoader != null;
        mProgressLoader.setVisibility(View.VISIBLE);
        addSubscribe(ViewModel.Current.service.mRetrofitHelper.getUpdateObject()
                .compose(RxUtil.rxSchedulerHelper())
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
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(wallpapersRetrofitObject -> {
                    if (wallpapersRetrofitObject.getCategoryList().size() > 0) {
                        ViewModel.Current.setRetrofitWallpObject(wallpapersRetrofitObject);
                        loadFragment();
                        onFillAdapterWithServiceData();
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
                RateThisApp.stopRateDialog(HomeActivityNavBar.this);
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
            onOpenScreenTracker("RatingDialog");
        }
    }

    public void checkUpdateNewWallpapers() {
        loadAndSetWallpaperToViewModel();
    }

    private void checkForCrashes() {
    }


    @Override
    protected void onStart() {
        super.onStart();
        RateThisApp.onStart(this);
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            rateApplication();
            Toast.makeText(getBaseContext(), R.string.txtrate6,
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }

    @Override
    public void onRequestPermissions() {
        ActivityCompat.requestPermissions(HomeActivityNavBar.this,
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
        if (errorFromEnum == ServiceErrorFromEnum.UPDATE_APPLICATION_CALL) {
            startUpdateAppIfNeeded();
        } else if (errorFromEnum == ServiceErrorFromEnum.GET_WALLPAPER_LIST_CALL) {
            checkUpdateNewWallpapers();
        }
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
