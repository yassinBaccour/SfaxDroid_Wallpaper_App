package com.sami.rippel.besmelleh;
 

import com.sami.rippel.besmelleh.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RateUs {
	
	private final static String TITLE = "أسماء الله الحسنى متغيرة";

	
	private final static String PACKAGE_NAME = "com.sami.rippel.besmelleh";

	
	private final static int DAYS_UNTIL_PROMPT = 0;

	private final static int LAUNCHES_UNTIL_PROMPT = 2;

	public static void app_launched(Context mContext) {
		SharedPreferences prefs = mContext.getSharedPreferences("rateus", 0);
		if (prefs.getBoolean("dontshxcdowagain", false)) {
			return;
		}

		SharedPreferences.Editor editor = prefs.edit();

		
		long launch_count = prefs.getLong("launch_count", 0) + 1;
		editor.putLong("launch_count", launch_count);

		
		Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
		if (date_firstLaunch == 0) {
			date_firstLaunch = System.currentTimeMillis();
			editor.putLong("date_firstlaunch", date_firstLaunch);
		}

		
		if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
			if (System.currentTimeMillis() >= date_firstLaunch
					+ (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
				showRateDialog(mContext, editor);
			}
		}

		editor.commit();
	}

	public static void showRateDialog(final Context mContext,
			final SharedPreferences.Editor editor) {
		final Dialog dialog = new Dialog(mContext);
		
		dialog.setCanceledOnTouchOutside(true);
		
		dialog.setTitle(R.string.txtrate2);

		LinearLayout ll = new LinearLayout(mContext);
		ll.setOrientation(LinearLayout.VERTICAL);

		TextView tv = new TextView(mContext);
		tv.setTextColor(Color.WHITE);
		tv.setTextSize(16);
		tv.setText(R.string.txtrate1);
		tv.setWidth(240);
		tv.setPadding(20, 0, 4, 10);
		ll.addView(tv);

		// First Button
		Button b1 = new Button(mContext);
		b1.setText(R.string.txtrate5);
		b1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse("market://details?id=" + PACKAGE_NAME)));
				dialog.dismiss();
			}
		});
		ll.addView(b1);

		Button b2 = new Button(mContext);
		b2.setText(R.string.txtrate3);
		b2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		ll.addView(b2);

	
		Button b3 = new Button(mContext);
		b3.setText(R.string.txtrate4);
		b3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (editor != null) {
					editor.putBoolean("dontshxcdowagain", true);
					editor.commit();
				}
				dialog.dismiss();
			}
		});
		ll.addView(b3);
		
		

		dialog.setContentView(ll);

		// Show Dialog
		dialog.show();
	}public RateUs() {
		// TODO Auto-generated constructor stub
	}
}