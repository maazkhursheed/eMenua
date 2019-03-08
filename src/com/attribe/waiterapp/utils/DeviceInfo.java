package com.attribe.waiterapp.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.widget.Toast;

/**
 * Created by Sabih Ahmed on 04-Mar-16.
 */
public class DeviceInfo {


    private static int screen;

    public static int setScreenSize(Context context){

        int screenSize = context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        String toastMsg = "";
        switch(screenSize) {

            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                screen = ScreenSize.SCREEN_XLARGE;
                toastMsg = "Xlarge";
                break;

            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                screen = ScreenSize.SCREEN_LARGE;
                toastMsg = "Large screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                screen = ScreenSize.SCREEN_NORMAL;
                toastMsg = "Normal screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                screen = ScreenSize.SCREEN_SMALL;
                toastMsg = "Small screen";
                break;
            default:
                screen =ScreenSize.SCREEN_OTHER;
                toastMsg = "Screen size is neither large, normal or small";
        }


        return screen;
    }

    public static int getScreen() {
        return screen;
    }

    //=============================================Inner Classes=============================================
    public static class ScreenSize {
        public static int SCREEN_OTHER  =-1;
        public static int SCREEN_XLARGE = 3;
        public static int SCREEN_LARGE  = 2;
        public static int SCREEN_NORMAL = 1;
        public static int SCREEN_SMALL  = 0;
    }
}
