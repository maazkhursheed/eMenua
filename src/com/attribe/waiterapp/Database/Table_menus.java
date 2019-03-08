package com.attribe.waiterapp.Database;

public class Table_menus {

	public static String TABLE_MENU_NAME ="menus ";
	public static String COLUMN_ID = "id ";
	public static String COLUMN_NAME = "name ";
	public static String COLUMN_PRICE = "price ";
	public static String COLUMN_DESCRIPTION = "description ";
	public static final String COLUMN_CATEGORY_ID = "category_id ";

	public static String CREATE_MENU_TABLE= "CREATE  TABLE "+ TABLE_MENU_NAME +"("+
											COLUMN_ID+"Integer  Primary Key, "+COLUMN_NAME +Constants.TYPE_TEXT+"," +
											COLUMN_PRICE+ "REAL, "+
											COLUMN_DESCRIPTION +Constants.TYPE_TEXT+Constants.COMMA_SEP+
											COLUMN_CATEGORY_ID +" Integer , "+
											Constants.COLUMN_CREATED_AT + Constants.TYPE_DATETIME+Constants.COMMA_SEP+
											Constants.COLUMN_UPDATED_AT + Constants.TYPE_DATETIME+ ")";


	public static String DROP_TABLE_ITEM = "drop table if exists "+ TABLE_MENU_NAME;
}

