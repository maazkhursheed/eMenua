package com.attribe.waiterapp.screens;





import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.attribe.waiterapp.R;

/**
 * Created by Sabih Ahmed on 6/1/2015.
 */
public class PassCodeDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    //builder.setView(R.layout.dialog_pass_code);
        return  builder.create();
    }
}