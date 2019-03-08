package com.attribe.waiterapp.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Maaz on 4/7/2016.
 */
public class Utils {


    public static long getCurrentTime(){

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(TimeZone.getDefault().getID()));
        long localTime = cal.getTimeInMillis() / 1000L;


//        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:00"));
//
//        Date currentLocalTime = cal.getTime();
//
//        DateFormat date = new SimpleDateFormat("HH:mm ss");
//        date.setTimeZone(TimeZone.getTimeZone("GMT+5:00"));
//        String localTime = date.format(currentLocalTime);

        return localTime;
    }

    public static String getCurrentTime(long unixTime){
        long unixSeconds = 1372339860;
        Date date = new Date(unixTime*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm "); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getID())); // give a timezone reference for formating (see comment at the bottom
        String formattedDate = sdf.format(date);


        return formattedDate;


    }
}
