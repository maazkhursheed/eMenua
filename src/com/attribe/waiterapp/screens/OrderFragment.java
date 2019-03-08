package com.attribe.waiterapp.screens;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.attribe.waiterapp.Database.DatabaseHelper;
import com.attribe.waiterapp.R;
import com.attribe.waiterapp.adapters.CategoryItemAdapter;
import com.attribe.waiterapp.adapters.OrderListAdapter;
import com.attribe.waiterapp.interfaces.OnItemRemoveListener;
import com.attribe.waiterapp.interfaces.OnQuantityChangeListener;
import com.attribe.waiterapp.interfaces.PrintOrder;
import com.attribe.waiterapp.models.*;
import com.attribe.waiterapp.network.RestClient;
import com.attribe.waiterapp.utils.Constants;
import com.attribe.waiterapp.utils.CurrencyHelper;
import com.attribe.waiterapp.utils.DevicePreferences;
import com.attribe.waiterapp.utils.OrderContainer;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Sabih Ahmed on 5/26/2015.
 */
public class OrderFragment extends Fragment implements GridView.OnItemClickListener,
        OnItemRemoveListener,
        OnQuantityChangeListener,PrintOrder{

    public RecyclerView orderlist;
    private OrderListAdapter orderListAdapter;
    private CopyOnWriteArrayList<Item> orderList;
    private TextView totalText, totalPrice ,currencyLabel;
    private TextView totalItemsQuantity;
    private Button confirmButton;
    View view;
    private ArrayList<Item> itemArrayList;
    private static OrderFragment orderFragment;
    private Order order;
    LinearLayoutManager mLayoutmanager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_order, container, false);

        initContents(view);
        return view;
    }

    private void initContents(View view) {

        long total = 0;
        totalText= (TextView) view.findViewById(R.id.fragment_order_textTotal);
        currencyLabel = (TextView)view.findViewById(R.id.order_currency);
        totalPrice = (TextView) view.findViewById(R.id.fragment_order_textTotalPrice);
        orderlist = (RecyclerView) view.findViewById(R.id.itemOrderList);
        confirmButton =(Button)view.findViewById(R.id.fragment_order_confirmButton);
        orderList = OrderContainer.getInstance().getOrderableItems();
        totalItemsQuantity = (TextView) view.findViewById(R.id.grid_TotalItemsQuantity);

        mLayoutmanager = new LinearLayoutManager(getActivity());
        orderlist.setLayoutManager(mLayoutmanager);

        orderListAdapter = new OrderListAdapter(getActivity(),orderList);
        orderlist.setAdapter(orderListAdapter);
        orderListAdapter.setQuantityChangeListener(this);

        currencyLabel.setText(CurrencyHelper.getCurrencyPrefix());
        String totalAmount = rupeeFormat(Integer.toString((int)(OrderContainer.getInstance().getOrderTotal())));
        totalPrice.setText(totalAmount);
//        totalPrice.setText(Double.toString(computeTotalPrice()));

        //totalItemsQuantity.setText(Integer.toString((int)OrderContainer.getInstance().getOrderableItems().size())+ " " + "item(s)") ;

        totalItemsQuantity.setText(Integer.toString((OrderContainer.getInstance().getTotalItemQuantity())) + " " + "item(s)");


        confirmButton.setOnClickListener(new ComfirmButtonClick());
        orderListAdapter.setOnItemRemoveListener(this);

        OrderDialogScreen dialogScreen=new OrderDialogScreen();
        dialogScreen.setQuantityChangeListener(this);


//        OrderTakingScreen orderTakingScreen = new OrderTakingScreen();
//        orderTakingScreen.setOrderPrintInterface(this);

        TableReferenceFragment tableReferenceFragment = new TableReferenceFragment();
        tableReferenceFragment.setOrderPrintInterface(this);

    }

    public static OrderFragment getInstance(){
        orderFragment = new OrderFragment();

        return orderFragment;
    }

    public static String rupeeFormat(String value){
        value=value.replace(",","");
        char lastDigit=value.charAt(value.length()-1);
        String result = "";
        int len = value.length()-1;
        int nDigits = 0;

        for (int i = len - 1; i >= 0; i--)
        {
            result = value.charAt(i) + result;
            nDigits++;
            if (((nDigits % 2) == 0) && (i > 0))
            {
                result = "," + result;
            }
        }
        return (result+lastDigit);
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


        Order orderItem = (Order) adapterView.getItemAtPosition(i);
        showOrderDialog(orderItem.getItem());
    }


    private void showOrderDialog(Item item){

        Intent intent = new Intent(getActivity(),OrderDialogScreen.class);
        intent.putExtra(Constants.KEY_SERIALIZEABLE_ITEM_OBJECT, item);
        intent.putExtra("fromOrder",true);
        startActivity(intent);

    }

    @Override
    public void onItemRemoved() {

//        String totalAmount = rupeeFormat(Double.toString(computeTotalPrice()));
        String totalAmount = rupeeFormat(Integer.toString((int)(OrderContainer.getInstance().getOrderTotal())));
        totalPrice.setText(totalAmount);
//        totalPrice.setText(Double.toString(computeTotalPrice()));
        totalItemsQuantity.setText(Integer.toString((int) OrderContainer.getInstance().getOrderableItems().size())+" " + "item(s)") ;
    }


    @Override
    public void onQuantityChanged() {
       // String totalAmount = rupeeFormat(Double.toString(computeTotalPrice()));
        String totalAmount = rupeeFormat(Integer.toString((int)(OrderContainer.getInstance().getOrderTotal())));
        totalPrice.setText(totalAmount);
//        totalPrice.setText(Double.toString(computeTotalPrice()));
        orderListAdapter.notifyDataSetChanged();


    }



    @Override
    public void onOrderSentToPrint() {

        hideOrderFragment();
        OrderContainer.getInstance().setTableNumber("");
        OrderContainer.getInstance().setTableCover(0);
        OrderContainer.getInstance().getOrderableItems().clear();
    }

    public class ComfirmButtonClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if(OrderContainer.getInstance().getOrderableItems().isEmpty()){
                Toast.makeText(getActivity(),getString(R.string.select_items_prompt),Toast.LENGTH_SHORT).show();
            }
            else{
                //ArrayList<order_detail> order_detail=getOrderDetail(OrderContainer.getInstance().getOrderableItems());
                showOrderPage();

            }
        }




    }



    private void showOrderPage() {

        Intent intent = new Intent(getActivity(),OrderTakingScreen.class);
        startActivity(intent);
    }

    private void hideOrderFragment() {
        view.findViewById(R.id.fragment_order_thankyouFrame).setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.findViewById(R.id.fragment_order_parent).setVisibility(View.GONE);

            }
        }, 6000);


    }



    public void updateFragment(long id){


        DatabaseHelper mDatabaseHelper=new DatabaseHelper(getActivity());
        itemArrayList = mDatabaseHelper.getItemsWithImages(id);


        orderList = OrderContainer.getInstance().getOrderableItems();
        if(! orderList.isEmpty()){

            for(int i = 0 ; i<orderList.size(); i++){
                if(orderList.get(i).getCategory_id()== id){
                    for(int x = 0; x < itemArrayList.size() ; x++){

                        if(itemArrayList.get(x).getId() == orderList.get(i).getId()){
                            itemArrayList.get(x).setSelected(true);
                            continue;
                        }
                    }
                }
            }
        }

        CategoryItemAdapter adapter = new CategoryItemAdapter(getActivity(), itemArrayList);


    }

    public static ArrayList<order_detail> getOrderDetail(CopyOnWriteArrayList<Item> itemList){
        ArrayList<order_detail> order_detail = new ArrayList<>();

        for(int i =  0;i < itemList.size(); i++){


            int itemId = itemList.get(i).getId();
            int quantityValue = itemList.get(i).getDesiredQuantity();
            String itemName = itemList.get(i).getName();
            double item_price = itemList.get(i).getPrice();

            order_detail order = new order_detail(itemId,itemName,quantityValue,item_price);
            order_detail.add(order);
        }

        return order_detail;
    }

    public interface OnActionBarValuesUpdated{

        void onActionBarValuesUpdate();
    }

}