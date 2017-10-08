package com.sami.rippel.labs.basmala;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.sami.rippel.model.Constants;

public class ScreenReceiver extends BroadcastReceiver {

	public static int NbOuverture = 0;
	public static int currentOuverture = 0;
	public static boolean wasScreenOn = true;
	public static AnimationServiceApp app;
	
	public static String ListePackage[] = {"com.viber.voip", 
		"com.android.incallui",
		"com.skype.raider",
		"com.whatsapp"
		};
	
	public void StopService(Context paramContext)
	{
		if (app != null)
		{
			app.stopPlayer();
			app.stopAnimationIfRunning();
			app = null;
		}
	}
	
	public void StartService(Context paramContext)
	{
		if (app == null)
		{
		app = new AnimationServiceApp(paramContext);
		app.startAnimation();
		}
	}
	
	public String GetListOfTask(ActivityManager am, Context paramContext)
	{
		return am
				.getRunningAppProcesses().get(0).processName;
	}
	
	@SuppressWarnings("deprecation")
	public String GetComponentInfo(ActivityManager am, Context paramContext)
	{
		return am.getRunningTasks(1).get(0).topActivity.getPackageName().toString();
	}
	
	public Boolean isUserPresentOrScreenOn(Intent paramIntent)
	{
		 return Intent.ACTION_SCREEN_ON.equals(paramIntent.getAction())
		|| Intent.ACTION_USER_PRESENT.equals(paramIntent.getAction());
	}
	
	public Boolean isScreenOff(Intent paramIntent)
	{
		return Intent.ACTION_SCREEN_OFF.equals(paramIntent.getAction());
	}
	
	public void UserPresentAction(Context paramContext)
	{
		if (wasScreenOn) {
			wasScreenOn = false;
			if (NbOuverture == 0) {
				StartService(paramContext);
			} else {
				currentOuverture++;
				if (NbOuverture == currentOuverture) {
					currentOuverture = 0;
					StartService(paramContext);
				}
			}
		}
	}
	
	public void ScreenOffAction(Context paramContext)
	{
		wasScreenOn = true;
		StopService(paramContext);
	}
	
	public void ScreenOnAction(Context paramContext, Intent paramIntent)
	{
		ActivityManager am = (ActivityManager) paramContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (int i = 0; i < ListePackage.length; i++)
		{
			if (ListePackage[i].equals(GetComponentInfo(am, paramContext)) || ListePackage[i].equals(GetListOfTask(am, paramContext)))
				{wasScreenOn = true;
			StopService(paramContext);}
		} 
	    if (Intent.ACTION_USER_PRESENT.equals(paramIntent
				.getAction())) {
			UserPresentAction(paramContext);
		}
	}
	
	public void onReceive(final Context paramContext, Intent paramIntent) {
		try {
			SharedPreferences pref = paramContext.getSharedPreferences(
					Constants.PREFERENCESNAME, Context.MODE_PRIVATE);
			if (pref != null) {
				NbOuverture = pref.getInt("NbOuv", 0);
				if (isUserPresentOrScreenOn(paramIntent)) {
					ScreenOnAction(paramContext, paramIntent);
				}
				if (isScreenOff(paramIntent)) {
					ScreenOffAction(paramContext);
				}
			}
		} catch (Exception e)
		{

		}
	}
}
