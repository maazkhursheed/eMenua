package com.attribe.waiterapp.Database;

/**
 * Created by Maaz on 4/20/2016.
 */
public class Table_Orders {

    public static String TABLE_NAME_ORDERS ="orders ";
    public static String ORDERS_ID = "id ";
    public static String TABLE_NUMBER = "table_number ";
    public static String TABLE_COVER = "table_cover ";
    public static String TIME_STAMP = "time_stamp ";
    public static String UPLOADED = "uploaded ";

    public static String CREATE_ORDERS_TABLE= "CREATE  TABLE "+ TABLE_NAME_ORDERS +"("+
                                               ORDERS_ID+" Integer  Primary Key  Autoincrement, "+
                                               TABLE_NUMBER +Constants.TYPE_TEXT+"," +
                                               TABLE_COVER +Constants.TYPE_TEXT+"," +
                                               TIME_STAMP +Constants.TYPE_INTEGER+"," +
                                               UPLOADED+" Integer  Default 0 "+")";

    public static String DROP_TABLE_ORDERS = "drop table if exists "+ TABLE_NAME_ORDERS;
}
