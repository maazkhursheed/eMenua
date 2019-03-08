package com.attribe.waiterapp.screens;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.attribe.waiterapp.R;
import com.attribe.waiterapp.utils.DevicePreferences;

/**
 * Created by Sabih Ahmed on 18-May-16.
 */
public class SectionNameFragment extends DialogFragment {

    public View view;
    private Button registerButton;
    private EditText waiterName;
    private EditText sectionName;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.section_name_fragment, null);

        builder.setView(view);

        init(view);

        return builder.create();
    }

    private void init(View view) {
        waiterName = (EditText)view.findViewById(R.id.waiterName);
        sectionName = (EditText)view.findViewById(R.id.section_name);
        registerButton = (Button)view.findViewById(R.id.register_button);

        if(!DevicePreferences.getInstance().getWaiterName().isEmpty()){

            waiterName.setText(DevicePreferences.getInstance().getWaiterName());
        }

        if(!DevicePreferences.getInstance().getSectionName().isEmpty()){
            sectionName.setText(DevicePreferences.getInstance().getSectionName());
        }



        setRegisterButtonListener();

    }

    private void setRegisterButtonListener() {


        registerButton.setOnClickListener(new RegisterButtonListener());
    }


    private class RegisterButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            DevicePreferences.getInstance().setWaiterName(waiterName.getText().toString());
            DevicePreferences.getInstance().setSectionName(sectionName.getText().toString());

            getDialog().dismiss();

        }
    }
}
