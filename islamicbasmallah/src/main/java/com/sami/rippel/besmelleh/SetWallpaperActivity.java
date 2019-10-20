package com.sami.rippel.besmelleh;

import com.sami.rippel.besmelleh.R;
import android.R.bool;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.service.wallpaper.WallpaperService;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class SetWallpaperActivity extends Activity {
	private static long back_pressed;
	Button b1, btotherapp, btrating;
	public Boolean isAdsShow = false;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.principale);
		b1 = (Button) findViewById(R.id.button1);
		b1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				isAdsShow = true;
				try {

					Intent intent = new Intent(
							WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
					intent.putExtra(
							WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
							new ComponentName(getApplicationContext(),
									MyWallpaper.class));
					startActivity(intent);

				} catch (Exception e) {

					Intent intent = new Intent();
					intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
					startActivity(intent);
					Toast.makeText(getApplicationContext(), R.string.txt1,
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		btrating = (Button) findViewById(R.id.btRating);
		btrating.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				isAdsShow = true;
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri
						.parse("market://details?id=com.sami.rippel.besmelleh"));
				startActivity(intent);
			}

		});

		btotherapp = (Button) findViewById(R.id.btotherapp);
		btotherapp.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				isAdsShow = true;
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("market://search?q=pub:SFAXDROID"));
				startActivity(intent);
			}
		});
		
		
		ImageView imageViewPub = (ImageView) findViewById(R.id.imageViewPub);
		imageViewPub.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri
						.parse("market://details?id=com.sami.rippel.allah"));
				startActivity(intent);
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (back_pressed + 2000 > System.currentTimeMillis())
			super.onBackPressed();

		else {
			RateUs.app_launched(this);
			Toast.makeText(getBaseContext(), R.string.txtrate6,
					Toast.LENGTH_SHORT).show();

		}
		back_pressed = System.currentTimeMillis();

	}

	@Override
	protected void onStart() {
		super.onStart();
		if (isAdsShow == true)
		{
		isAdsShow = false;
		}
	}

}