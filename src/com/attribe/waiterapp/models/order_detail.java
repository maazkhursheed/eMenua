package com.attribe.waiterapp.models;

/**
 * Created by Sabih Ahmed on 5/30/2015.
 */
public class order_detail {

    private int menu_id;
    private int quantity;
    private String item_name;
    private double item_price;
   // private double item_total;

    public order_detail(int menu_id, String item_name, int quantity, double item_price) {
        this.menu_id = menu_id;
        this.item_name = item_name;
        this.quantity = quantity;
        this.item_price = item_price;

    }



    public int getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(int menu_id) {
        this.menu_id = menu_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

//    public double getItem_total() {
//        return item_total;
//    }

//    public void setItem_total(long item_total) {
//        this.item_total = item_total;
//    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public double getItem_price() {
        return item_price;
    }

    public void setItem_price(double item_price) {
        this.item_price = item_price;
    }

    public class Response{
        String message;
        int status;
    }

}




