package com.attribe.waiterapp.models;

import java.util.ArrayList;

/**
 * Created by Sabih Ahmed on 5/30/2015.
 */
public class OrderDetail {

    private String device_id;
    private double order_total;
    private String table_no;
    private ArrayList<order_detail> order_detail;
    private int table_cover;
    private int order_time;

    public OrderDetail(String deviceId,
                       ArrayList<order_detail> order_detail,
                       double order_total,
                       String table_no,
                       int tableCover ,
                       int orderTime) {
        this.device_id = deviceId;
        this.order_detail = order_detail;
        this.order_total = order_total;
        this.table_no = table_no;
        this.table_cover = tableCover;
        this.order_time = orderTime;
    }




}


