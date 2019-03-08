package com.attribe.waiterapp.Broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.attribe.waiterapp.screens.CarouselScreen;
import com.attribe.waiterapp.screens.MainActivity;
import com.attribe.waiterapp.screens.SplashScreen;
import com.attribe.waiterapp.utils.DeviceInfo;

/**
 * Created by Sabih Ahmed on 5/11/2015.
 */
public class BootReceiver  extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        showStartScreen(context);

    }


    private void showStartScreen(Context context) {
        //check for Screen size
        if(DeviceInfo.getScreen()==DeviceInfo.ScreenSize.SCREEN_LARGE){
            //in case of 7 inch screen
            showMenuScreen(context);
        }

        if(DeviceInfo.getScreen()==DeviceInfo.ScreenSize.SCREEN_XLARGE){
            showCarouselScreen(context);
        }
    }

    private void showMenuScreen(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    private void showCarouselScreen(Context context){
        Intent intent = new Intent(context, CarouselScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
