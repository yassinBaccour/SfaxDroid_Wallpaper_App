package com.sfaxman.islamwallpaper;

import com.app.girlsgallery.four.MainActivity;
import com.sfaxman.islamwallpaper.R;
//import com.startapp.android.publish.StartAppAd;
//import com.startapp.android.publish.StartAppSDK;


import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SetWallpaperActivity extends Activity {

	Button b1, b2, b3, b4, b5, b6, b7, b8, buttonGallery;
	TextView mySelection;
	public Boolean isAdsShow = false;
	//private StartAppAd startAppAd = new StartAppAd(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//StartAppSDK.init(this, "210170015", true);
		//StartAppAd.showSplash(this, savedInstanceState);
		setContentView(R.layout.principale);
		b1 = (Button) findViewById(R.id.button1);
		b2 = (Button) findViewById(R.id.ButtonKadir);
		b3 = (Button) findViewById(R.id.ButtonSobhhanRabi);
		b4 = (Button) findViewById(R.id.ButtonSobhanaAllah);
		b5 = (Button) findViewById(R.id.ButtonTawakkal);
		b6 = (Button) findViewById(R.id.ButtonSobhanrabialaadim);
		b7 = (Button) findViewById(R.id.ButtonBesmelleh);
		b8 = (Button) findViewById(R.id.Buttonrate);
		 
		b1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				try {
					isAdsShow = true;
					Intent intent = new Intent(
							WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
					intent.putExtra(
							WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
							new ComponentName(getApplicationContext(),
									MangaServiceWallpaper.class));
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

		b2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				intent.putExtra("array", 1);
				startActivity(intent);
			}
		});

		b3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				intent.putExtra("array", 2);
				startActivity(intent);
			}
		});

		b4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				intent.putExtra("array", 3);
				isAdsShow = true;
				startActivity(intent);
			}
		});

		b5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				intent.putExtra("array", 4);
				startActivity(intent);
			}
		});

		b6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				intent.putExtra("array", 5);
				isAdsShow = true;
				startActivity(intent);
			}
		});

		b7.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				intent.putExtra("array", 6);
				startActivity(intent);
			}
		});

		b8.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri
						.parse("market://details?id=com.sfaxman.islamwallpaper"));
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (isAdsShow == true)
		{
			//startAppAd.showAd();
			//startAppAd.loadAd();
			isAdsShow = false;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
}