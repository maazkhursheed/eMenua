package com.attribe.waiterapp.screens;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.attribe.waiterapp.Database.DatabaseHelper;
import com.attribe.waiterapp.R;
import com.attribe.waiterapp.adapters.OrderPageAdapter;
import com.attribe.waiterapp.models.Item;
import com.attribe.waiterapp.utils.OrderContainer;
import com.zj.wfsdk.WifiCommunication;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * Created by Sabih Ahmed on 21-Oct-15.
 */
public class OrderTakingScreen extends Activity implements Serializable {

    private ListView orderPageList;
    private WifiCommunication printerCommunication = null;
    private EditText tableNumberField;
    private TextView tableNumberLabel;
    private TextView tableNumberCaption;
    private Button viewOrderButton;
    private Button proceedButton;
    public static ArrayList<Item> itemList = new ArrayList<>();
    private DatabaseHelper databaseHelper;


    public OrderTakingScreen(){

    }
    public static ArrayList<Item> getItemList() {
        return itemList;
    }

    public static void setItemList(ArrayList<Item> itemList) {
        OrderTakingScreen.itemList = itemList;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_testing);

        initView();
        setOrderList();
    }

    private void setOrderList() {

        OrderPageAdapter adapter = new OrderPageAdapter(this, OrderContainer.getInstance().getOrderableItems());
        orderPageList.setAdapter(adapter);
    }

    private void initView() {

        databaseHelper = new DatabaseHelper(this);
        databaseHelper.getWritableDatabase();

        orderPageList = (ListView)findViewById(R.id.order_page_list);
        viewOrderButton = (Button)findViewById(R.id.orderView);
        proceedButton = (Button) findViewById(R.id.proceedButton);


        viewOrderButton.setOnClickListener(new ViewOrderClickListener());
        proceedButton.setOnClickListener(new ProceedListener());

        tableNumberCaption = (TextView)findViewById(R.id.table_number_caption);
        tableNumberLabel =(TextView)findViewById(R.id.tableNumber_label);
        tableNumberLabel.setText(OrderContainer.getInstance().getTableNumber());
        tableNumberField = (EditText)findViewById(R.id.tableNumber_field);

    }


    private class ViewOrderClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent orderPage = new Intent(getApplicationContext(),OrderLogScreen.class);
            startActivity(orderPage);
        }
    }

    private class ProceedListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            DialogFragment dialog= new TableReferenceFragment(  );
            dialog.show(getFragmentManager(),"");
        }
    }

}