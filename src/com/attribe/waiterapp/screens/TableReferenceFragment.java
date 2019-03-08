package com.attribe.waiterapp.screens;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.attribe.waiterapp.Database.DatabaseHelper;
import com.attribe.waiterapp.Print.PrintHelper;
import com.attribe.waiterapp.Print.PrintStatusNotifier;
import com.attribe.waiterapp.R;
import com.attribe.waiterapp.interfaces.PrintOrder;
import com.attribe.waiterapp.interfaces.onTableNumberDialogDismissed;
import com.attribe.waiterapp.learningengine.LearningStatus;
import com.attribe.waiterapp.models.Item;
import com.attribe.waiterapp.models.Order;
import com.attribe.waiterapp.utils.OrderContainer;
import com.attribe.waiterapp.utils.Utils;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Sabih Ahmed on 28-Jan-16.
 */
public class TableReferenceFragment extends DialogFragment {

    private onTableNumberDialogDismissed tableDialogListener = null;
    private EditText tableCover;
    private EditText tableNumber;
    private Button cancelButton;
    private Button printButton;
    private Button addOrderButton;
    private DatabaseHelper databaseHelper;
    private static PrintOrder printOrder;
    private View parentView;

    public TableReferenceFragment(){}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        parentView = inflater.inflate(R.layout.table_refernce_final, null);
        builder.setView(parentView);

        init();
        initializePrintHandler();
        PrintHelper.connectPrinter();

        setListeners();



        return builder.create();
    }

    private void setListeners() {

        setTableNumberEditorListener();
        setTableCoverEditorListener();

        printButton.setOnClickListener(new PrintClickListener());
        addOrderButton.setOnClickListener(new AddOrderClickListner());
        cancelButton.setOnClickListener(new CancelListener());

    }


    private void setTableCoverEditorListener() {

        tableCover.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView tv, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    if (!tableCover.getText().toString().isEmpty()) {
                        OrderContainer.getInstance().setTableCover(Integer.parseInt(tv.getText().toString()));

                        InputMethodManager imm = (InputMethodManager) tv.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(tv.getWindowToken(), 0);

                    }
                }

                return false;

        }

    });

}


    private void setTableNumberEditorListener() {
        tableNumber.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {

                if(actionID == EditorInfo.IME_ACTION_DONE){

                    if(!tableNumber.getText().toString().isEmpty()){
                        OrderContainer.getInstance().setTableNumber(textView.getText().toString());

//                    TableReferenceFragment.this.dismiss();

                        if(tableDialogListener!=null){
                            tableDialogListener.onDialogDismissed();
                        }
                    }


                    InputMethodManager imm = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    return true;
                }

                return false;
            }
        });
    }

    private void init() {
        databaseHelper = new DatabaseHelper(getActivity());
        databaseHelper.getWritableDatabase();

        tableCover = (EditText)parentView.findViewById(R.id.table_cover);
        tableNumber = (EditText)parentView.findViewById(R.id.table_number);
        cancelButton = (Button)parentView.findViewById(R.id.cancelFragment);
        printButton = (Button) parentView.findViewById(R.id.printOrders);
        addOrderButton = (Button) parentView.findViewById(R.id.addOrders);



        tableNumber.setText(OrderContainer.getInstance().getTableNumber());
        tableCover.setText(Long.toString(OrderContainer.getInstance().getTableCover()));

        hidePrintButton();
        hideAddOrderButton();
    }

    private void initializePrintHandler() {

        PrintHelper.initializePrinter(new PrintStatusNotifier() {
            @Override
            public void readyForPrint() {
                showPrintButton();

            }

            @Override
            public void printerDicsonnected() {
                Toast.makeText(getActivity() , "Printer Disconnected",
                        Toast.LENGTH_SHORT).show();
                hidePrintButton();
                showAddOrderButton();
            }

            @Override
            public void sentToPrintFailed() {
                Toast.makeText(getActivity(), "Print Failed,please reconnect",
                        Toast.LENGTH_SHORT).show();
                hidePrintButton();
                showAddOrderButton();
            }

            @Override
            public void printerConnectionError() {

                Toast.makeText(getActivity(), "Printer Connection error",
                        Toast.LENGTH_SHORT).show();
                hidePrintButton();
                showAddOrderButton();
            }

            @Override
            public void printerConnectionLost() {
                Toast.makeText(getActivity(), "Printer Connection lost,please reconnect",
                        Toast.LENGTH_SHORT).show();
                hidePrintButton();
                showAddOrderButton();
            }
        });

    }

    private void showPrintButton() {
        printButton.setVisibility(View.VISIBLE);
    }

    private void hidePrintButton(){
        printButton.setVisibility(View.GONE);
    }

    private void showAddOrderButton() {
        addOrderButton.setVisibility(View.VISIBLE);
    }

    private void hideAddOrderButton(){
        addOrderButton.setVisibility(View.GONE);
    }

    public static void setOrderPrintInterface(PrintOrder printOrder){

        TableReferenceFragment.printOrder = printOrder;
    }

//======================================Inner Classes / Listeners=======================================================

    private class PrintClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            String TableNumber ="";
            int TableCover =0;

            if(!tableNumber.getText().toString().isEmpty()){

                TableNumber = tableNumber.getText().toString();

            }



            if(!tableCover.getText().toString().isEmpty()){

                TableCover = Integer.parseInt(tableCover.getText().toString());
            }


            OrderContainer.getInstance().setTableNumber(TableNumber);
            OrderContainer.getInstance().setTableCover(TableCover);

            CopyOnWriteArrayList<Item> tempItemsList = new CopyOnWriteArrayList<>();
            for(Item eachItem:OrderContainer.getInstance().getOrderableItems()){
                tempItemsList.add(eachItem);
            }

            Order order = new Order(tempItemsList,
                    TableCover,
                    TableNumber,
                    (int)(long)Utils.getCurrentTime());

            databaseHelper.addOrders(order);


            PrintHelper.printOrder(order);

//            OrderTakingScreen.this.finish();
            getDialog().dismiss();
            getActivity().finish();
            printOrder.onOrderSentToPrint();
            LearningStatus.resetLearning();
        }
    }

    private class AddOrderClickListner implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            OrderContainer.getInstance().setTableNumber(tableNumber.getText().toString());
            OrderContainer.getInstance().setTableCover(Integer.parseInt(tableCover.getText().toString()));

            CopyOnWriteArrayList<Item> tempItemsList = new CopyOnWriteArrayList<>();
            for(Item eachItem:OrderContainer.getInstance().getOrderableItems()){
                tempItemsList.add(eachItem);
            }

            Order order = new Order(tempItemsList,
                    Integer.parseInt(tableCover.getText().toString()),
                    tableNumber.getText().toString(),
                    Utils.getCurrentTime());

            databaseHelper.addOrders(order);

            getDialog().dismiss();
            getActivity().finish();
            printOrder.onOrderSentToPrint();
            Intent orderLogScreen = new Intent(getActivity(),OrderLogScreen.class);
            startActivity(orderLogScreen);
        }
    }


    private class CancelListener implements View.OnClickListener {


        @Override
        public void onClick(View view) {
            getDialog().dismiss();
        }
    }
}
