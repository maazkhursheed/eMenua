package com.attribe.waiterapp.Database;

import android.content.Context;
import android.database.Cursor;
import com.attribe.waiterapp.models.Item;
import com.attribe.waiterapp.models.Order;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Maaz on 4/28/2016.
 */
public class OrderService {

    ArrayList<Order> orderList=new ArrayList<>();
    private Context mContext;

    private DatabaseHelper db;
    CopyOnWriteArrayList<Item>  itemInfoList ;

    public OrderService(Context context) {

        this.mContext = context;
        db = new DatabaseHelper(mContext);




    }

    /**This method fetches orders which have not been uploaded on server
     *
     * @return
     */
    public ArrayList<Order> getOrders() {

        Cursor cursor = db.getOrders(false);
        if (cursor.moveToFirst()) {

            do {
                int order_id = cursor.getInt(cursor.getColumnIndexOrThrow(Table_Orders.ORDERS_ID.trim()));
                Integer tableCover = cursor.getInt(cursor.getColumnIndexOrThrow(Table_Orders.TABLE_COVER.trim()));
                String tableNumber = cursor.getString(cursor.getColumnIndexOrThrow(Table_Orders.TABLE_NUMBER.trim()));
                long timeStamp = cursor.getLong(cursor.getColumnIndexOrThrow(Table_Orders.TIME_STAMP.trim()));
                Integer uploadedFlag = cursor.getInt(cursor.getColumnIndexOrThrow(Table_Orders.UPLOADED.trim())) ;

                Cursor cursor1 = db.getOrderItems(order_id);

                itemInfoList = new CopyOnWriteArrayList<>();
                if (cursor1.moveToFirst()) {

                    do {
                        int item_id = cursor1.getInt(cursor1.getColumnIndexOrThrow(Table_OrderDetails.ITEM_ID.trim()));
                        String item_name = cursor1.getString(cursor1.getColumnIndexOrThrow(Table_OrderDetails.ITEM_NAME.trim()));
                        int item_quantity = cursor1.getInt(cursor1.getColumnIndexOrThrow(Table_OrderDetails.QUANTITY.trim()));

                        Item itemsList = new Item(item_id, item_name, item_quantity);
                        itemInfoList.add(itemsList);

                    } while (cursor1.moveToNext());

                }

                Order order = new Order(itemInfoList, tableCover, tableNumber, timeStamp);
                order.setUploaded(uploadedFlag); //setting upload through setter , to maintain consistency in current code base.
                order.setID(order_id);
                orderList.add(order);

            } while (cursor.moveToNext());
        }

        return orderList;

    }


    /**
     * This method fethes all orders from db
     * @return
     */
    public ArrayList<Order> getAllDbOrders() {

        Cursor cursor = db.getAllOrders();
        if (cursor.moveToFirst()) {

            do {
                int order_id = cursor.getInt(cursor.getColumnIndexOrThrow(Table_Orders.ORDERS_ID.trim()));
                Integer tableCover = cursor.getInt(cursor.getColumnIndexOrThrow(Table_Orders.TABLE_COVER.trim()));
                String tableNumber = cursor.getString(cursor.getColumnIndexOrThrow(Table_Orders.TABLE_NUMBER.trim()));
                long timeStamp = cursor.getLong(cursor.getColumnIndexOrThrow(Table_Orders.TIME_STAMP.trim()));
                Integer uploadedFlag = cursor.getInt(cursor.getColumnIndexOrThrow(Table_Orders.UPLOADED.trim())) ;

                Cursor cursor1 = db.getOrderItems(order_id);

                itemInfoList = new CopyOnWriteArrayList<>();
                if (cursor1.moveToFirst()) {

                    do {
                        int item_id = cursor1.getInt(cursor1.getColumnIndexOrThrow(Table_OrderDetails.ITEM_ID.trim()));
                        String item_name = cursor1.getString(cursor1.getColumnIndexOrThrow(Table_OrderDetails.ITEM_NAME.trim()));
                        int item_quantity = cursor1.getInt(cursor1.getColumnIndexOrThrow(Table_OrderDetails.QUANTITY.trim()));

                        Item itemsList = new Item(item_id, item_name, item_quantity);
                        itemInfoList.add(itemsList);

                    } while (cursor1.moveToNext());

                }

                Order order = new Order(itemInfoList, tableCover, tableNumber, timeStamp);
                order.setUploaded(uploadedFlag); //setting upload through setter , to maintain consistency in current code base.
                order.setID(order_id);
                orderList.add(order);

            } while (cursor.moveToNext());
        }

        return orderList;

    }

    public void updateOrder(int orderID,Boolean uploaded ){

        db.updateOrder(orderID,uploaded);
    }
}
