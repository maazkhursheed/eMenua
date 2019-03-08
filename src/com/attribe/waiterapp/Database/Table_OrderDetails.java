package com.attribe.waiterapp.Database;

/**
 * Created by Maaz on 4/20/2016.
 */
public class Table_OrderDetails {

    public static String TABLE_NAME_ORDER_DETAILS ="order_details ";
    public static String ORDER_DETS_ID = "id ";
    public static String ITEM_ID = "item_id ";
    public static String ITEM_NAME = "item_name ";
    public static String QUANTITY = "quantity ";
    public static String ORDERS_ID = "order_id ";

    public static String CREATE_ORDER_DETAILS_TABLE= "CREATE  TABLE "+ TABLE_NAME_ORDER_DETAILS +"("+
                                                      ORDER_DETS_ID+" Integer  Primary Key  Autoincrement, "+
                                                      ITEM_ID +" Integer " + Constants.COMMA_SEP+
                                                      ITEM_NAME + Constants.TYPE_TEXT +Constants.COMMA_SEP+
                                                      QUANTITY +" Integer " + Constants.COMMA_SEP+
                                                      ORDERS_ID +" Integer " + ")";

    public static String DROP_TABLE_ORDER_DETAILS = "drop table if exists "+ TABLE_NAME_ORDER_DETAILS;



}
