package com.app.girlsgallery.four;

import java.io.IOException;

import com.sfaxman.islamwallpaper.R;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class MainActivity extends Activity {
	private int currentPhoto;
	private TouchImageView imgViewPhoto;
	private LayoutInflater mInflater;
	private View mainView = null;
	Handler handler;
	public String currentpicture;
	int j = 0;
	public static int[] photoArrayPicked = new int[10];
	static {

		photoArrayPicked[0] = R.drawable.islam1_00000;
		photoArrayPicked[1] = R.drawable.islam1_00001;
		photoArrayPicked[2] = R.drawable.islam1_00002;
		photoArrayPicked[3] = R.drawable.islam1_00003;
		photoArrayPicked[4] = R.drawable.islam1_00004;
		photoArrayPicked[5] = R.drawable.islam1_00005;
		photoArrayPicked[6] = R.drawable.islam1_00006;
		photoArrayPicked[7] = R.drawable.islam1_00007;
		photoArrayPicked[8] = R.drawable.islam1_00008;
		photoArrayPicked[9] = R.drawable.islam1_00009;
	}

	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.flip);
		this.mInflater = getLayoutInflater();
		this.mainView = this.mInflater.inflate(R.layout.flip, null);

		Intent iin = getIntent();
		Bundle b = iin.getExtras();

		if (b != null) {
			j = b.getInt("array");

		}
		if (j == 1) {

			System.arraycopy(myApp.photoArray, 0, photoArrayPicked, 0, 10);

		}

		if (j == 2) {

			System.arraycopy(myApp.photoArray, 10, photoArrayPicked, 0, 10);

		}

		if (j == 3) {

			System.arraycopy(myApp.photoArray, 20, photoArrayPicked, 0, 10);

		}
		if (j == 4) {

			System.arraycopy(myApp.photoArray, 30, photoArrayPicked, 0,10);

		}
		if (j == 5) {

			System.arraycopy(myApp.photoArray, 40, photoArrayPicked, 0, 10);

		}
		if (j == 6) {

			System.arraycopy(myApp.photoArray, 50, photoArrayPicked, 0, 10);

		}
		SetupLayout();
	}

	private void DisplayPhoto() {

		this.imgViewPhoto.setImageResource(photoArrayPicked[this.currentPhoto]);
	}

	private void SetupLayout() {
		handler = new Handler();
		this.imgViewPhoto = ((TouchImageView) this.mainView
				.findViewById(R.id.imageViewPhoto));
		this.currentPhoto = 0;
		DisplayPhoto();
		SeekBar localSeekBar = (SeekBar) this.mainView
				.findViewById(R.id.seekBarPhoto);
		localSeekBar.setMax(-1 + photoArrayPicked.length);
		localSeekBar.setOnSeekBarChangeListener(this.sbLis);
		((ImageView) this.mainView.findViewById(R.id.imageViewPrevPhoto))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View paramAnonymousView) {
						MainActivity.this.prevPhoto();
					}
				});
		((ImageView) this.mainView.findViewById(R.id.imageViewNextPhoto))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View paramAnonymousView) {
						MainActivity.this.nextPhoto();
					}
				});
		((ImageView) this.mainView.findViewById(R.id.imageViewWallpaper))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View paramAnonymousView) {
						wallapepr();
						Toast.makeText(getApplicationContext(), R.string.txt11,
								Toast.LENGTH_SHORT).show();
					}
				});
		setContentView(this.mainView);
	}

	public void wallpaeprNoRATIO() {
		WallpaperManager myWallpaperManager = WallpaperManager
				.getInstance(MainActivity.this);
		int w = myWallpaperManager.getDesiredMinimumWidth();
		int h = myWallpaperManager.getDesiredMinimumHeight();
		final boolean need_w = w <= 0;
		if (need_w || h <= 0) {
			final Rect rect = new Rect();
			getWindowManager().getDefaultDisplay().getRectSize(rect);
			if (need_w) {
				w = rect.width();
			} else {
				h = rect.height();
			}
		}
		Bitmap bitmap = BitmapFactory.decodeStream(getResources()
				.openRawResource(photoArrayPicked[this.currentPhoto]));
		final Bitmap resized = Bitmap.createScaledBitmap(bitmap, w, h, false);
		try {
			myWallpaperManager.setBitmap(resized);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void wallapepr() {
		WallpaperManager wallpaperManager = WallpaperManager
				.getInstance(MainActivity.this);
		Bitmap bitmap = BitmapFactory.decodeStream(getResources()
				.openRawResource(photoArrayPicked[this.currentPhoto]));
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels;
		int width = displaymetrics.widthPixels;
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height,
				true);
		try {
			wallpaperManager.setBitmap(scaledBitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
		scaledBitmap.recycle();
	}

	public void nextPhoto() {
		if (this.currentPhoto < -1 + photoArrayPicked.length) {
			this.currentPhoto = (1 + this.currentPhoto);
			DisplayPhoto();
			((SeekBar) this.mainView.findViewById(R.id.seekBarPhoto))
					.setProgress(this.currentPhoto);
		}
	}

	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		getWindow().setFormat(1);
	}

	public void onConfigurationChanged(Configuration paramConfiguration) {
		super.onConfigurationChanged(paramConfiguration);
	}

	public void onPause() {
		super.onPause();
	}

	public Object onRetainNonConfigurationInstance() {
		return null;
	}

	public void prevPhoto() {
		if (this.currentPhoto > 0) {
			this.currentPhoto = (-1 + this.currentPhoto);
			DisplayPhoto();
			((SeekBar) this.mainView.findViewById(R.id.seekBarPhoto))
					.setProgress(this.currentPhoto);
		}
	}

	private SeekBar.OnSeekBarChangeListener sbLis = new SeekBar.OnSeekBarChangeListener() {
		public void onProgressChanged(SeekBar paramAnonymousSeekBar,
				int paramAnonymousInt, boolean paramAnonymousBoolean) {
		}

		public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {
		}

		public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {
			MainActivity.this.currentPhoto = paramAnonymousSeekBar
					.getProgress();
			MainActivity.this.DisplayPhoto();
		}
	};

}
