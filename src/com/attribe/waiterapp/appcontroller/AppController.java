package com.attribe.waiterapp.appcontroller;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import com.attribe.waiterapp.Broadcasts.OnScreenOffReceiver;
import com.attribe.waiterapp.utils.CurrencyHelper;
import com.attribe.waiterapp.utils.DeviceInfo;
import com.attribe.waiterapp.utils.DevicePreferences;

/**
 * Created by Sabih Ahmed on 5/11/2015.
 */
public class AppController extends Application {

    private PowerManager.WakeLock wakeLock;
    private OnScreenOffReceiver onScreenOffReceiver;


    @Override
    public void onCreate() {
        super.onCreate();
        DevicePreferences.getInstance().init(this);
        CurrencyHelper.setLocale(CurrencyHelper.LocaleHelper.UAE);
        DeviceInfo.setScreenSize(this);
       //registerKioskModeScreenOffReceiver();
    }

    private void registerKioskModeScreenOffReceiver() {
        // register screen off receiver
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        onScreenOffReceiver = new OnScreenOffReceiver();
        registerReceiver(onScreenOffReceiver, filter);
    }

    public PowerManager.WakeLock getWakeLock () {
        if (wakeLock == null) {
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "wakeup");
        }
        return wakeLock;
    }
}
