package com.attribe.waiterapp.utils;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import com.attribe.waiterapp.screens.OrderTakingScreen;
import com.attribe.waiterapp.screens.SectionNameFragment;
import com.attribe.waiterapp.screens.TableReferenceFragment;

/**
 * Created by Sabih Ahmed on 10-Mar-16.
 */
public class NavigationController {


    public static void showOrderScreen(Context context) {
        Intent intent = new Intent(context, OrderTakingScreen.class);
        context.startActivity(intent);

    }

    public static void showTableNumberScreen(FragmentManager fragmentManager){
        DialogFragment dialog= new TableReferenceFragment();
        dialog.show(fragmentManager,"");
    }

    public static void showSectionNameDialog(FragmentManager fragmentManager){
        DialogFragment dialog= new SectionNameFragment();
        dialog.show(fragmentManager,"");
    }
}
