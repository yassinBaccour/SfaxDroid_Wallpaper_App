package com.sami.rippel.labs.basmala;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class LockService extends Service {

    IntentFilter localIntentFilter;

    public IBinder onBind(Intent paramIntent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
        localIntentFilter = new IntentFilter("android.intent.action.SCREEN_ON");
        localIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        localIntentFilter.addAction("android.intent.action.USER_PRESENT");
        registerReceiver(new ScreenReceiver(), localIntentFilter);
        return super.onStartCommand(paramIntent, paramInt1, paramInt2);
    }
}