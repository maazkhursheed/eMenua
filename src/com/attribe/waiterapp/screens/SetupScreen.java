package com.attribe.waiterapp.screens;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import com.attribe.waiterapp.R;
import com.attribe.waiterapp.utils.DevicePreferences;

/**
 * Created by Sabih Ahmed on 5/22/2015.
 */
public class SetupScreen extends Activity {
    EditText secretText;
    Button buttonClear, buttonVerify;
    DevicePreferences devicePreferences;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_setup);

        initContents();
        secretText.getText().toString();




    }

    private void initContents() {
        //devicePreferences =new DevicePreferences(this);



    }
}