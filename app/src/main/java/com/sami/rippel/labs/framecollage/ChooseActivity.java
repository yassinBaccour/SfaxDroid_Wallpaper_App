package com.sami.rippel.labs.framecollage;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import com.sami.rippel.ui.adapter.GalleryAdapter;
import com.sami.rippel.utils.FileUtils;
import com.sami.rippel.allah.R;
import com.sami.rippel.model.entity.TypeCellItemEnum;
import com.sami.rippel.model.listner.RecyclerItemClickListener;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.model.entity.WallpaperObject;

public class ChooseActivity extends ActivityLabBase
{
 	private ProgressBar mProgressLoader;
	private ArrayList<WallpaperObject> mData = new ArrayList<>();
	private RecyclerView mGridView;
 	private Toolbar mToolbar;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
					WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		}
		setContentView(R.layout.grid_view_activity);
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mProgressLoader = (ProgressBar) findViewById(R.id.progressBar);
 		InitToolbar();
		ApplicationHandler applicationHandler = ApplicationHandler.getInstance();
		File makeImageFolder = applicationHandler.getOrCreateFolder(Environment
				.getExternalStorageDirectory().getAbsolutePath(),
				ApplicationHandler.IMAGES.FrameImages);
		makeImageFolder = applicationHandler.getOrCreateFolder(
				makeImageFolder.getAbsolutePath(), ApplicationHandler.IMAGES.Cache);
		mGridView = (RecyclerView) findViewById(R.id.gridview);
		FillForm();
	}

	public void InitToolbar() {
		setSupportActionBar(mToolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			getSupportActionBar().setTitle(getString(R.string.gallery));
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

	protected void FillForm() {
        if (ViewModel.Current.isWallpapersLoaded()) {
			FileUtils mFileUtils = new FileUtils(getApplicationContext());
            //WallpaperCategory wallpaperCategory = ViewModel.Current.retrofitWallpObject.getCategoryList().stream().filter(x -> x.getTitle().equals("ImageFrame")).findFirst().orElse(null);
            mData.clear();
            mData = new ArrayList<WallpaperObject>(ViewModel.Current.getWallpaperCategoryFromName("ImageFrame").getGetWallpapersList());
            if (mData != null && mData.size() > 0)
                mGridView.setLayoutManager(new GridLayoutManager(
                        getApplicationContext(), 2));
            mGridView.setHasFixedSize(true);
            if (mFileUtils.isConnected(getApplicationContext()) && mData != null && mData.size() > 0) {
				GalleryAdapter mAdapter = new GalleryAdapter(ChooseActivity.this, mData, TypeCellItemEnum.FRAME_CELL);
                mGridView.setAdapter(mAdapter);
                mGridView
                        .addOnItemTouchListener(new RecyclerItemClickListener(
                                getApplicationContext(),
                                new RecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view,
                                                            int position) {
                                        Intent previewActivity = new Intent(getApplicationContext(),
                                                PreviewActivity.class);
                                        previewActivity.putExtra("ImageUrl",
                                                mData.get(position).getUrl().replace("prev_", "").replace("jpg", "png")
                                                        .trim());
                                        previewActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(previewActivity);
                                    }
                                }));
            }
            mProgressLoader.setVisibility(View.GONE);
        }
    }
}
