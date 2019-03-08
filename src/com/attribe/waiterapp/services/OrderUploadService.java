package com.attribe.waiterapp.services;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.attribe.waiterapp.Database.DatabaseHelper;
import com.attribe.waiterapp.Database.OrderService;
import com.attribe.waiterapp.interfaces.OrderUploadResponse;
import com.attribe.waiterapp.models.Order;
import com.attribe.waiterapp.utils.OrderContainer;

import java.util.ArrayList;

/**
 * Created by Maaz on 4/27/2016.
 */
public class OrderUploadService extends IntentService {


    private DatabaseHelper databaseHelper;

    public OrderUploadService() {
        super("OrderUploadService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        //Go to orders table and find orders with uploaded = false,
        //if there are orders, upload them
        //update the db if uploading succeeds

        //if fails, do nothing

        final OrderService orderService = new OrderService(this);


        ArrayList<Order> allOrders = orderService.getOrders();

        for (final Order order : allOrders) {
            try {

                    OrderContainer.getInstance().placeOrder(order, new OrderUploadResponse() {
                        @Override
                        public void success(int orderID) {

                            orderService.updateOrder(orderID, true);
                        }

                        @Override
                        public void failed() {

                        }
                    });
            } catch (Exception ex) {

                Log.d("",ex.toString());
            }


        }
    }
}
