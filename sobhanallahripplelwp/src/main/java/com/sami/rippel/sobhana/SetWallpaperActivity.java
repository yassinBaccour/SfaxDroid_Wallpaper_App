package com.sami.rippel.sobhana;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
 import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class SetWallpaperActivity extends Activity {
	private static long back_pressed;
 
	Button b1;
	public Boolean isAdsShow = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 
		setContentView(R.layout.principale);
		b1 = (Button) findViewById(R.id.button1);
		b1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					isAdsShow = true;
					Intent intent = new Intent(
							WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
					intent.putExtra(
							WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
							new ComponentName(getApplicationContext(),
									IslamicWallpaper.class));
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
	protected void onStart() {
		super.onStart();
		if (isAdsShow == true)
		{ 
		isAdsShow = false;
		}
	}

	@Override
	public void onBackPressed() {
		if (back_pressed + 2000 > System.currentTimeMillis())
			super.onBackPressed();
		else 
		{
			RateUs.app_launched(this);
			Toast.makeText(getBaseContext(), R.string.txtrate6,
					Toast.LENGTH_SHORT).show();
		}
		back_pressed = System.currentTimeMillis();
	}
}