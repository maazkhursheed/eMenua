package com.attribe.waiterapp.utils;

import com.attribe.waiterapp.interfaces.OrderUploadResponse;
import com.attribe.waiterapp.models.*;
import com.attribe.waiterapp.network.RestClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import com.attribe.waiterapp.models.Item;
import com.attribe.waiterapp.models.Order;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Sabih Ahmed on 5/22/2015.
 */
public class OrderContainer {


    private static OrderContainer orderContainer;
    public static CopyOnWriteArrayList<Item> itemList;
    public ArrayList<Order> selectedOrderList;
    private Integer tableCover=0;
    private String tableNumber="";
    public static ArrayList<Order> orderLogList ;

    public static OrderContainer getInstance(){

        if(orderContainer == null){

            orderContainer = new OrderContainer();
        }

            return orderContainer;
    }

    private OrderContainer(){
        itemList = new CopyOnWriteArrayList<>();
        orderLogList = new ArrayList<>();
    }

    public CopyOnWriteArrayList<Item> getOrderableItems(){

        return itemList;
    }

    public static ArrayList<Order> getOrderLogList() {
        return orderLogList;
    }

    public void setOrderLogList(Order order) {
        this.orderLogList.add(order);
    }


    public void putOrder(Item order){
        this.itemList.add(order);

    }

    public void addOrUpdateOrder(Item item, int desiredQuantity){

        boolean isItemFound = false;

        //iterate through order list & see if item is already present
        if(!OrderContainer.getInstance().getOrderableItems().isEmpty()){
            //this block executes when list is not empty

            for(Item eachItem : OrderContainer.getInstance().getOrderableItems()){

                if(eachItem.getId()== item.getId()){ // if item exists in order list
                    isItemFound = true;
                    if(desiredQuantity==0){
                        //intention to remove item
                        this.itemList.remove(item);
                    }
                    else{
                        eachItem.setDesiredQuantity(desiredQuantity); // update quantity in order container

                    }


                    break;
                }

            }

            if(!isItemFound){// item not found, insert in in order list
                item.setDesiredQuantity(desiredQuantity);
                OrderContainer.getInstance().getOrderableItems().add(item);

            }
        }

        else{
            //this block executes in case of very first time item insertion in order list

            item.setDesiredQuantity(desiredQuantity);
            OrderContainer.getInstance().getOrderableItems().add(item);
        }

    }

    /**
    public void updateTableInfo(String tableCover, String tableNumber){

        //iterate through order list & see if item is already present
        if(!OrderContainer.getInstance().getOrderableItems().isEmpty()){
            //this block executes when list is not empty

            for(Item order : OrderContainer.getInstance().getOrderableItems()){

                order.setTableCover(tableCover);        //  add table cover
                order.setTableNumber(tableNumber);      //  add table number
                order.setTimeStamp(Utils.getCurrentTime());  // add order time stamp
            }

        }

    }**/



//    public void setTableNumber(String tableNumber){
//
//        /**
//        //iterate through order list & see if item is already present
//        if(!OrderContainer.getInstance().getOrderableItems().isEmpty()){
//            //this block executes when list is not empty
//
//            for(Item order : OrderContainer.getInstance().getOrderableItems()){
//                order.setTableNumber(tableNumber);      //  add table number
//
//            }
//
//        }
//        **/
//    }
//
//    public void setTableCover(String tableCover){
//        /**
//        //iterate through order list & see if item is already present
//        if(!OrderContainer.getInstance().getOrderableItems().isEmpty()){
//            //this block executes when list is not empty
//
//            for(Item order : OrderContainer.getInstance().getOrderableItems()){
//
//                order.setTableCover(tableCover);        //  add table cover
//            }
//
//        }
//         **/
//    }





    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public Integer getTableCover() {
        return tableCover;
    }

    public void setTableCover(Integer tableCover) {
        this.tableCover = tableCover;
    }


/**
    public Item getTableInfo(){

            for(Item order : OrderContainer.getInstance().getOrderableItems()){

                order.getTableCover();
                order.getTableNumber();
                order.getTimeStamp();

                return order;
            }



        return null;
    }
 **/



    public void removeOrder(Order order){
        this.itemList.remove(order);

    }

    public int getTotalItemQuantity(){
        return this.itemList.size();
    }

    public double getOrderTotal(){
        double total = 0;
        for(Item eachItem :OrderContainer.getInstance().getOrderableItems()){

            total += eachItem.getPrice() * eachItem.getDesiredQuantity();
        }
        return total;
    }

    public static double getOrderTotal(CopyOnWriteArrayList<Item> itemList){
        double total = 0;
        for(Item eachItem :OrderContainer.getInstance().getOrderableItems()){

            total += eachItem.getPrice() * eachItem.getDesiredQuantity();
        }
        return total;

    }

    public void addOrderToSelectedList(Order order){

        this.selectedOrderList.add(order);
    }



    public boolean containsItem(int itemId){

        boolean contains = false;
        for(Item eachItem :this.itemList){

            if(eachItem.getId()==itemId){

                contains=true;
            }

        }

        return contains;
    }

    public int getItemQuantity(int itemId){

        int itemQuantity = 0;
        for(Item eachItem:this.itemList){

            if(eachItem.getId()== itemId){

                itemQuantity = eachItem.getDesiredQuantity();
            }
        }

        return itemQuantity;
    }

    public static void placeOrder(Order order , final OrderUploadResponse uploadResponse) {


        String deviceID= DevicePreferences.getInstance().getDeviceId();

        CopyOnWriteArrayList<Item> itemsList = order.getItemsList();
        ArrayList<order_detail> order_detail = new ArrayList<>();

        double total = getOrderTotal(itemsList);

        for(Item item: itemsList){

            int id = item.getId();
            String name = item.getName();
            int desiredQuantity = item.getDesiredQuantity();
            double price = item.getPrice();

            order_detail detail = new order_detail(id,name,desiredQuantity,price);
            order_detail.add(detail);


        }

        OrderDetail orderToServer = new OrderDetail(deviceID,
                order_detail,
                total,
                order.getTableNumber(),
                order.getTableCover(),
                (int)order.getTimeStamp());


        final int orderID = order.getID();

        RestClient.getAdapter().placeOrder(orderToServer, new Callback<order_detail.Response>() {

            @Override
            public void success(order_detail.Response response, Response response2) {

                uploadResponse.success(orderID);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                uploadResponse.failed();
            }
        });

    }

    /**
    public void setTimeStamp() {

        if(!OrderContainer.getInstance().getOrderableItems().isEmpty()){
            //this block executes when list is not empty

            for(Item order : OrderContainer.getInstance().getOrderableItems()){

                order.setTimeStamp(Utils.getCurrentTime());
            }

        }

    }
     **/
//    public void logOrder() {
//        //extract object from order list
//        //put it in order log list
//
//        if(!OrderContainer.getInstance().getOrderableItems().isEmpty()){
//
//            for(Item order : OrderContainer.getInstance().getOrderableItems()){
//                orderLogList.add(order);
//            }
//        }
//    }

    public String computeTotal(int quantityValue, double price) {
        double total = quantityValue * price;

        return Double.toString(total);
    }

}
