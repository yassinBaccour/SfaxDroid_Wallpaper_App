package com.sami.rippel.ui.activity;

import android.app.WallpaperManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.sami.rippel.allah.R;
import com.sfaxdoird.anim.word.NameOfAllah2DActivity;
import com.sfaxdroid.base.FileUtils;
import com.sfaxdroid.base.SimpleActivity;
import com.sami.rippel.model.Constants;
import com.sami.rippel.model.ViewModel;
import com.sfaxdroid.bases.ActionTypeEnum;
import com.sami.rippel.model.entity.StateEnum;
import com.sami.rippel.model.entity.TypeCellItemEnum;
import com.sfaxdroid.bases.LwpListener;
import com.sami.rippel.model.listner.OnStateChangeListener;
import com.sami.rippel.utils.RecyclerItemClickListener;
import com.sami.rippel.ui.adapter.GalleryAdapter;
import com.sfaxdoird.anim.img.DouaLwpActivity;
import com.sfaxdroid.base.Utils;
import com.sfaxdroid.base.WallpaperCategory;
import com.sfaxdroid.base.WallpaperObject;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.sami.rippel.model.ViewModel.Current;

public class GalleryActivity extends SimpleActivity implements LwpListener, OnStateChangeListener {

    private GalleryAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private ArrayList<WallpaperObject> listFileToSendToDetailViewPager = new ArrayList<>();
    private ProgressBar mProgressBar;
    private String mLwpName;
    private String mPos;
    private String mSelectedUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
        super.onCreate(savedInstanceState);
    }

    public void initData() {
        if (mLwpName != null && !mLwpName.isEmpty() && (mLwpName.equals(com.sfaxdroid.base.Constants.KEY_ADDED_LIST_TIMER_LWP) || mLwpName.equals(Constants.KEY_BASMALA_STIKERS))) {
            refreshBitmapAdapter(mLwpName);
            mRecyclerView
                    .addOnItemTouchListener(new RecyclerItemClickListener(
                            getApplicationContext(),
                            (view, position) -> {
                                if (mLwpName.equals(com.sfaxdroid.base.Constants.KEY_ADDED_LIST_TIMER_LWP)) {
                                    Intent intent = null;
                                    try {
                                        intent = new Intent(GalleryActivity.this, Class.forName("com.sfaxdroid.detail.DetailsActivity"));
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    intent.putParcelableArrayListExtra(com.sfaxdroid.base.Constants.LIST_FILE_TO_SEND_TO_DETAIL_VIEW_PAGER, listFileToSendToDetailViewPager);
                                    intent.putExtra(com.sfaxdroid.base.Constants.DETAIL_IMAGE_POS, position);
                                    intent.putExtra(com.sfaxdroid.base.Constants.KEY_LWP_NAME, com.sfaxdroid.base.Constants.KEY_ADDED_LIST_TIMER_LWP);
                                    startActivityForResult(intent, 90);
                                } else if (mLwpName.equals(Constants.KEY_BASMALA_STIKERS)) {
                                    WallpaperObject lwp = listFileToSendToDetailViewPager.get(position);
                                    Intent output = new Intent();
                                    output.putExtra("URL", lwp.getUrl());
                                    setResult(RESULT_OK, output);
                                    finish();
                                }
                            }));
            mProgressBar.setVisibility(View.GONE);
        } else fillData();
    }

    public WallpaperCategory getWallpaperCategory() {
        List<WallpaperCategory> getCategoryList = Current.retrofitWallpObject.getCategoryList();
        for (WallpaperCategory wall : getCategoryList) {
            if (wall.getTitle().equals(getNameFind()))
                return wall;
        }
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ViewModel.Current.unregisterOnStateChangeListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_gellery_wallpaper;
    }

    @Override
    protected void initEventAndData() {
        Current.fileUtils.SetLwpListener(this);
        ViewModel.Current.registerOnStateChangeListener(this);
        mToolbar = findViewById(R.id.toolbar);
        mProgressBar = findViewById(R.id.progressBar);
        mRecyclerView = findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(
                getApplicationContext(), 3));
        mRecyclerView.setHasFixedSize(true);
        mPos = getIntent().getStringExtra(com.sfaxdroid.base.Constants.DETAIL_IMAGE_POS);
        mLwpName = getIntent().getStringExtra(com.sfaxdroid.base.Constants.KEY_LWP_NAME);
        initToolbar(mPos);
        initData();
    }

    public void fillData() {
        if (mPos != null && !mPos.isEmpty() && Current.isWallpapersLoaded()) {
            WallpaperObject wallpaper = ViewModel.Current.GetWallpaperFromCategoryNameAndPos("ImageCategoryNew", mPos);
            listFileToSendToDetailViewPager.clear();
            listFileToSendToDetailViewPager = new ArrayList<>(wallpaper.getSubWallpapersCategoryList());
        } else if (mLwpName != null && !mLwpName.isEmpty() && Current.isWallpapersLoaded()) {
            listFileToSendToDetailViewPager.clear();
            listFileToSendToDetailViewPager = new ArrayList<>(getWallpaperCategory().getGetWallpapersList());
        }
        if (Current.device.isConnected(getApplicationContext()) && listFileToSendToDetailViewPager != null && listFileToSendToDetailViewPager.size() > 0 && Current.isWallpapersLoaded()) {
            mAdapter = new GalleryAdapter(GalleryActivity.this, listFileToSendToDetailViewPager, TypeCellItemEnum.GALLERY_CELL);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView
                    .addOnItemTouchListener(new RecyclerItemClickListener(
                            getApplicationContext(),
                            (view, position) -> {
                                if (mPos != null && !mPos.isEmpty()) {
                                    Intent intent = null;
                                    try {
                                        intent = new Intent(
                                                GalleryActivity.this,
                                                Class.forName("com.sfaxdroid.detail.DetailsActivity"));
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    intent.putParcelableArrayListExtra(
                                            com.sfaxdroid.base.Constants.LIST_FILE_TO_SEND_TO_DETAIL_VIEW_PAGER, listFileToSendToDetailViewPager);
                                    intent.putExtra(com.sfaxdroid.base.Constants.DETAIL_IMAGE_POS, position);
                                    startActivity(intent);
                                } else if (mLwpName != null && !mLwpName.isEmpty()) {
                                    mSelectedUrl = mAdapter.GetFullPictureUrl(position);
                                    switch (mLwpName) {
                                        case Constants.KEY_DOUA_LWP: {
                                            Intent intent = new Intent(
                                                    GalleryActivity.this,
                                                    DouaLwpActivity.class);
                                            intent.putExtra(com.sfaxdroid.base.Constants.URL_TO_DOWNLOAD, mSelectedUrl);
                                            startActivity(intent);
                                            break;
                                        }
                                        case "SkyBoxLwp":
                                            saveTempsDorAndDoAction(ActionTypeEnum.SKYBOX_LWP, mSelectedUrl);
                                            break;
                                        case com.sfaxdroid.base.Constants.KEY_ADD_TIMER_LWP: {
                                            Intent intent = null;
                                            try {
                                                intent = new Intent(
                                                        GalleryActivity.this,
                                                        Class.forName("com.sfaxdroid.detail.DetailsActivity"));
                                            } catch (ClassNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                            intent.putParcelableArrayListExtra(
                                                    com.sfaxdroid.base.Constants.LIST_FILE_TO_SEND_TO_DETAIL_VIEW_PAGER, listFileToSendToDetailViewPager);
                                            intent.putExtra(com.sfaxdroid.base.Constants.DETAIL_IMAGE_POS, position);
                                            intent.putExtra(com.sfaxdroid.base.Constants.KEY_LWP_NAME, com.sfaxdroid.base.Constants.KEY_ADD_TIMER_LWP);
                                            startActivity(intent);
                                            break;
                                        }
                                        case com.sfaxdroid.base.Constants.KEY_RIPPLE_LWP: {
                                            Intent intent = null;
                                            try {
                                                intent = new Intent(
                                                        GalleryActivity.this,
                                                        Class.forName("com.sfaxdroid.detail.DetailsActivity"));
                                            } catch (ClassNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                            intent.putParcelableArrayListExtra(
                                                    com.sfaxdroid.base.Constants.LIST_FILE_TO_SEND_TO_DETAIL_VIEW_PAGER, listFileToSendToDetailViewPager);
                                            intent.putExtra(com.sfaxdroid.base.Constants.DETAIL_IMAGE_POS, position);
                                            intent.putExtra(com.sfaxdroid.base.Constants.KEY_LWP_NAME, com.sfaxdroid.base.Constants.KEY_RIPPLE_LWP);
                                            startActivity(intent);
                                            break;
                                        }
                                        case Constants.KEY_NAME_OF_ALLAH_2_D: {
                                            Intent intent = new Intent(
                                                    GalleryActivity.this,
                                                    NameOfAllah2DActivity.class);
                                            intent.putExtra(com.sfaxdroid.base.Constants.URL_TO_DOWNLOAD, mSelectedUrl);
                                            startActivity(intent);
                                            break;
                                        }
                                        case Constants.KEY_TEXTURE: {
                                            Intent intent = this.getIntent();
                                            this.setResult(RESULT_OK, intent);
                                            intent.putExtra("urltoopen", listFileToSendToDetailViewPager.get(position).getUrl());
                                            intent.putExtra("type", Constants.SQUARE_TYPE);
                                            finish();
                                            break;
                                        }
                                    }
                                }
                            }));
        }
        mProgressBar.setVisibility(View.GONE);
    }

    public String getNameFind() {
        if (mLwpName != null && !mLwpName.isEmpty()) {
            switch (mLwpName) {
                case "Skybox":
                    return "";
                case Constants.KEY_DOUA_LWP:
                    return "ImageDouaLwp";
                case Constants.KEY_NAME_OF_ALLAH_2_D:
                    return "ImageDouaLwp";
                case com.sfaxdroid.base.Constants.KEY_RIPPLE_LWP:
                    return "All";
                //isAdsShowedFromRipple = true;
                case com.sfaxdroid.base.Constants.KEY_ADD_TIMER_LWP:
                    return "All";
                case Constants.KEY_TEXTURE:
                    return "ImageDouaLwp";
                default:
                    return "";
            }
        } else return "";
    }

    public void refreshBitmapAdapter(String fileName) {
        List<File> myFileList = null;
        listFileToSendToDetailViewPager.clear();
        if (fileName.equals(Constants.KEY_BASMALA_STIKERS)) {
            myFileList = FileUtils.Companion.getBasmalaStickersFileList(getBaseContext(), getBaseContext().getString(R.string.app_namenospace));
        } else if (fileName.equals(com.sfaxdroid.base.Constants.KEY_ADDED_LIST_TIMER_LWP)) {
            myFileList = FileUtils.Companion.getPermanentDirListFiles(getBaseContext(), getBaseContext().getString(R.string.app_namenospace));
        }
        if (myFileList != null) {
            for (File file : myFileList) {
                WallpaperObject imageModel = new WallpaperObject();
                imageModel.setName("Image " + file.getName());
                imageModel.setUrl(file.getPath());
                listFileToSendToDetailViewPager.add(imageModel);
            }
            mAdapter = new GalleryAdapter(GalleryActivity.this, listFileToSendToDetailViewPager, TypeCellItemEnum.BITMAP_CELL);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    public String getActivityTitle() {
        if (mLwpName != null && mLwpName.equals(Constants.KEY_DOUA_LWP))
            return getString(R.string.DouaLwpTitle);
        else if (mLwpName != null && mLwpName.equals(com.sfaxdroid.base.Constants.KEY_ADD_TIMER_LWP))
            return getString(R.string.DouaLwpTitle);
        else if (mLwpName != null && mLwpName.equals(com.sfaxdroid.base.Constants.KEY_ADDED_LIST_TIMER_LWP))
            return getString(R.string.TitleActionBarTimerLwpAdded);
        else if (mLwpName != null && mLwpName.equals(com.sfaxdroid.base.Constants.KEY_RIPPLE_LWP))
            return getString(R.string.DouaLwpTitle);
        if (mLwpName != null && mLwpName.equals(Constants.KEY_NAME_OF_ALLAH_2_D))
            return getString(R.string.DouaLwpTitle);
        if (mLwpName != null && mLwpName.equals(Constants.KEY_TEXTURE))
            return getString(R.string.texturetitle);
        else
            return getString(R.string.gallery);
    }

    public void initToolbar(String pos) {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (pos != null && !pos.isEmpty())
                getSupportActionBar().setTitle(pos);
            else
                getSupportActionBar().setTitle(getActivityTitle());
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void saveTempsDorAndDoAction(ActionTypeEnum actionToDo, String url) {
        boolean isSavedToStorage = FileUtils.Companion.isSavedToStorage(
                FileUtils.Companion.getFileName(url), this, getString(com.sfaxdroid.timer.R.string.app_namenospace));
        if (isSavedToStorage) {
            //sendToRippleLwp();
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
            Utils.Companion.saveToFileToTempsDirAndChooseAction(url, actionToDo, ViewModel.Current.device.getScreenHeightPixels(), ViewModel.Current.device.getScreenWidthPixels(), this, "app_namenospace", null, this);
        }
    }

    public void sendToRippleLwp() {
        Constants.FilePath = FileUtils.Companion.getTemporaryFile(FileUtils.Companion.getFileName(mSelectedUrl), this, getString(com.sfaxdroid.timer.R.string.app_namenospace)).getPath();
        try {

        } catch (Exception e) {
            try {
                Intent intent = new Intent();
                intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
                startActivity(intent);
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 90) {
            if (resultCode == RESULT_OK) {
                refreshBitmapAdapter(mLwpName);
            }
        }
    }

    private void checkForCrashes() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onSendToLwp() {
        //sendToRippleLwp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForCrashes();
    }

    @Override
    public void onStateChange(@NonNull StateEnum state) {
        initData();
    }
}