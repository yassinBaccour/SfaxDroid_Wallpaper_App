package com.yassin.sfax.tawakkolalaallah;


//import com.startapp.android.publish.StartAppAd;
//import com.startapp.android.publish.StartAppSDK;
import com.yassin.sfax.tawakkolalaallah.R;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class SetWallpaperActivity extends Activity {
	Button b1, b2;
	public Boolean isAdsShow = false;
	private static long back_pressed;
	//private StartAppAd startAppAd = new StartAppAd(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 
		setContentView(R.layout.principale);
		b1 = (Button) findViewById(R.id.button1);
		b2 = (Button) findViewById(R.id.Buttongallery);
		b1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					isAdsShow = true;
					Intent intent = new Intent(
							WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
					intent.putExtra(
							WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
							new ComponentName(getApplicationContext(),
									LiveWallpaper.class));
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


		Button btrating = (Button) findViewById(R.id.btRating);
		btrating.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri
						.parse("market://details?id=com.yassin.sfax.tawakkolalaallah"));
				startActivity(intent);
			}
		});

		Button btotherapp = (Button) findViewById(R.id.btotherapp);
		btotherapp.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
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
		
		//startAppAd.loadAd(); 
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
		if (back_pressed + 2000 > System.currentTimeMillis())
			super.onBackPressed();
		else {
			 
			Toast.makeText(getBaseContext(), R.string.txtrate6,
					Toast.LENGTH_SHORT).show();
		}
		back_pressed = System.currentTimeMillis();
	}

}