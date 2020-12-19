package com.sfaxdroid.mini.base;

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

    private final static int LAUNCHES_UNTIL_PROMPT = 2;

    public static void app_launched(Context mContext, String packageName) {
        SharedPreferences prefs = mContext.getSharedPreferences("rateUsSharedPreferences", 0);
        if (prefs.getBoolean("dontshxcdowagain", false)) {
            return;
        }
        SharedPreferences.Editor editor = prefs.edit();
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch) {
                showRateDialog(mContext, editor, packageName);
            }
        }

        editor.commit();
    }

    private static void showRateDialog(final Context context,
                                       final SharedPreferences.Editor editor,
                                       final String packageName) {
        final Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle(R.string.txtrate2);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView textView = new TextView(context);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(16);
        textView.setText(R.string.txtrate1);
        textView.setWidth(240);
        textView.setPadding(20, 0, 4, 10);
        linearLayout.addView(textView);
        Button buttonRate = new Button(context);
        buttonRate.setText(R.string.txtrate5);
        buttonRate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse("market://details?id=" + packageName)));
                dialog.dismiss();
            }
        });
        linearLayout.addView(buttonRate);
        Button buttonRemindLater = new Button(context);
        buttonRemindLater.setText(R.string.txtrate3);
        buttonRemindLater.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        linearLayout.addView(buttonRemindLater);
        final Button buttonDoNotAskMe = new Button(context);
        buttonDoNotAskMe.setText(R.string.txtrate4);
        buttonDoNotAskMe.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshxcdowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
        linearLayout.addView(buttonDoNotAskMe);
        dialog.setContentView(linearLayout);
        dialog.show();
    }
}