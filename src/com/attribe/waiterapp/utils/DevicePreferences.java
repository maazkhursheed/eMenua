package com.attribe.waiterapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.Settings;
import com.attribe.waiterapp.R;

/**
 * Created by Sabih Ahmed on 5/22/2015.
 */
public class DevicePreferences {

    private static final String KEY_WAITER_NAME = "waiterName";
    private static final String KEY_SECTION_NAME = "sectionName";
    private static Context mContext;
    private static DevicePreferences mInstance;
    private SharedPreferences mPrefs;
    private static String DEVICE_REGISTRATION = "deviceRegistrationFlag" ;
    private static String DEVICE_VERIFICATION = "deviceVerificationFlag";
    private static String CLIENT_KEY="clientSecret";
    private static String KEY_TABLE_NUMBER="tableNumber";


    public static DevicePreferences getInstance(){
        if(mInstance == null){

            mInstance =new DevicePreferences();
        }

        return mInstance;

    }
    private DevicePreferences(){

    }

    public void init(Context context){

        this.mContext = context;

        mPrefs= mContext.getSharedPreferences("clientPrefs", Context.MODE_PRIVATE);
    }


    public void setClientKey(String clientSecret){

        SharedPreferences.Editor editor = mPrefs.edit();


        editor.putString(CLIENT_KEY , clientSecret);

        editor.commit();

    }


    public String getClientKey(){
        //SharedPreferences prefs = mContext.getSharedPreferences("clientPrefs", Context.MODE_PRIVATE);


        String clientSecret = mPrefs.getString(CLIENT_KEY, null);

        return clientSecret;

    }

    public void setDeviceRegistrationStatus(Boolean isRegistered){

        SharedPreferences.Editor editor = mPrefs.edit();

        editor.putBoolean(DEVICE_REGISTRATION, isRegistered);

        editor.commit();
    }

    public Boolean isDeviceRegistered(){

        boolean isRegistered;

        isRegistered = mPrefs.getBoolean(DEVICE_REGISTRATION, false);

        return isRegistered;

    }

    public void setDeviceVerificationStatus(boolean verificationStatus){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(DEVICE_VERIFICATION, verificationStatus);

        editor.commit();

    }

    public boolean isDeviceVerified(){
        boolean verificationFlag;

        verificationFlag = mPrefs.getBoolean(DEVICE_VERIFICATION,false);

        return verificationFlag;
    }



    public String getDeviceId(){

       String deviceId = Settings.Secure.getString(this.mContext.getContentResolver(), Settings.Secure.ANDROID_ID);

//        Math.random();
//        int random= (int) (Math.random()*100);
//
//        deviceId = Integer.toString(random);
//        String deviceId = "QWERTY111";

        return deviceId;
    }

    public void setTableNumber(int tableNumber){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(KEY_TABLE_NUMBER, tableNumber);

        editor.commit();
    }

    public int getTableNumber(){
        int tableNumber = mPrefs.getInt(KEY_TABLE_NUMBER, 0);
        return tableNumber;
    }

    public void setRtlLayout(boolean rtlFlag){
        SharedPreferences.Editor editor=mPrefs.edit();
        editor.putBoolean("rtlFlag",true);

        editor.commit();
    }

    public boolean isRtlLayout(){
        SharedPreferences.Editor editor = mPrefs.edit();

        return mPrefs.getBoolean("rtlFlag",false);

    }

    public boolean isNetworkAvailable(){

        boolean networkInfo;

        ConnectivityManager connectivityManager= (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo!=null && activeNetworkInfo.isConnected();
    }

    public static void showNoConnectionDialog()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(true);
        builder.setMessage(R.string.no_connection);
        builder.setTitle(R.string.no_connection_title);
        builder.setPositiveButton(R.string.settings_button_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which)
            {
                mContext.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            }
        });

        builder.setNegativeButton(R.string.cancel_button_text, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                return;
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            public void onCancel(DialogInterface dialog) {
                return;
            }
        });

        builder.show();
    }

    public void setWaiterName(String waiterName) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(KEY_WAITER_NAME,waiterName);

        editor.commit();

    }
    public String getWaiterName() {
        SharedPreferences.Editor editor = mPrefs.edit();
        String waiterName = mPrefs.getString(KEY_WAITER_NAME, "");

        return waiterName;
    }

    public void setSectionName(String sectionName) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(KEY_SECTION_NAME,sectionName);
        editor.commit();

    }

    public String getSectionName() {
        SharedPreferences.Editor editor = mPrefs.edit();
        String sectionName = mPrefs.getString(KEY_SECTION_NAME, "");

        return sectionName;
    }


}
