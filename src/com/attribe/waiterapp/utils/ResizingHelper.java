package com.attribe.waiterapp.utils;

/**
 * Created by Sabih Ahmed on 04-Mar-16.
 */
public class ResizingHelper {


    public static int getGridColumnCount() {

        int columnCount = 0;

        if(DeviceInfo.getScreen() == DeviceInfo.ScreenSize.SCREEN_LARGE){
            //7 inch
            columnCount = 2;

        }

        if(DeviceInfo.getScreen() == DeviceInfo.ScreenSize.SCREEN_XLARGE){

            //greater than 7 inch

            columnCount = 3;
        }

        return columnCount;
    }
}
