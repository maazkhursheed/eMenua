package com.attribe.waiterapp.Broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import com.attribe.waiterapp.appcontroller.AppController;

/**
 * Created by Sabih Ahmed on 5/11/2015.
 */
public class OnScreenOffReceiver extends BroadcastReceiver {

    private static final String PREF_KIOSK_MODE = "pref_kiosk_mode";

    @Override
    public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
                AppController ctx = (AppController) context.getApplicationContext();

                if(isKioskModeActive(context)) {
                    wakeUpDevice(ctx);
                }

            }
    }

    private void wakeUpDevice(AppController context) {
        PowerManager.WakeLock wakeLock  =context.getWakeLock();

        if (wakeLock.isHeld()) {
            wakeLock.release(); // release old wake lock
        }

        // create a new wake lock...
        wakeLock.acquire();

        // ... and release again
        wakeLock.release();
    }

    private boolean isKioskModeActive(final Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(PREF_KIOSK_MODE, false);
    }
}
