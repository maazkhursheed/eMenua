package com.attribe.waiterapp.Database;

public class Table_MenuImages {

	public static String TABLE_MENU_IMAGES_NAME = "menu_images ";
	public static String COLUMN_ID="id ";
	public static String COLUMN_MENU_ID = "menu_id ";
	public static String COLUMN_IMAGE="image ";
	public static String TEXT_TYPE = "TEXT ";
	public static String CREATE_IMAGES_TABLE = "CREATE  TABLE "+ TABLE_MENU_IMAGES_NAME +
												" ("+ COLUMN_ID +"Integer Primary Key "+Constants.COMMA_SEP+
												COLUMN_MENU_ID +" int "+Constants.COMMA_SEP+
												COLUMN_IMAGE + Constants.TYPE_TEXT + Constants.COMMA_SEP+
												Constants.COLUMN_CREATED_AT + Constants.TYPE_DATETIME+Constants.COMMA_SEP+
												Constants.COLUMN_UPDATED_AT + Constants.TYPE_DATETIME +
												")";

	public static String DROP_TABLE_IMAGES = "drop table if exists "+ TABLE_MENU_IMAGES_NAME;
}
