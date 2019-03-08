package com.attribe.waiterapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import android.widget.Toast;
import com.attribe.waiterapp.interfaces.onDbCreate;
import com.attribe.waiterapp.models.Category;
import com.attribe.waiterapp.models.Item;
import com.attribe.waiterapp.models.Image;
import com.attribe.waiterapp.models.Order;
import com.attribe.waiterapp.utils.ExceptionHanlder;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Sabih Ahmed and Maaz Khursheed on 5/19/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 33;
    private static final String DATABASE_NAME = "restaurant-menu";

    private onDbCreate callBack;
    private ExceptionHanlder exceptionHanlder;
    public void setOnDbCreateListener(onDbCreate listener){
        callBack=listener;
    }
    private static Context mContext;
    CopyOnWriteArrayList<Item>  itemInfoList ;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext=context;


        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHanlder(mContext));

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(TABLE_CATEGORIES.CREATE_CATEGORY_TABLE);
            db.execSQL(Table_menus.CREATE_MENU_TABLE);
            db.execSQL(Table_MenuImages.CREATE_IMAGES_TABLE);
            db.execSQL(Table_Orders.CREATE_ORDERS_TABLE);
            db.execSQL(Table_OrderDetails.CREATE_ORDER_DETAILS_TABLE);

        }catch(SQLiteException e){
            Log.d(DatabaseHelper.class.getSimpleName(),e.toString());
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL(TABLE_CATEGORIES.DROP_CATEGORY_TABLE);
        db.execSQL(Table_menus.DROP_TABLE_ITEM);
        db.execSQL(Table_MenuImages.DROP_TABLE_IMAGES);
        db.execSQL(Table_Orders.DROP_TABLE_ORDERS);
        db.execSQL(Table_OrderDetails.DROP_TABLE_ORDER_DETAILS);

        onCreate(db);
    }

    public void addCategory(Category category){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TABLE_CATEGORIES.COLUMN_ID, category.getId());
        values.put(TABLE_CATEGORIES.COLUMN_NAME, category.getName());
        values.put(TABLE_CATEGORIES.COLUMN_IMAGE, category.getImageBlob());
        values.put(Constants.COLUMN_CREATED_AT, category.getCreated_at());
        values.put(Constants.COLUMN_UPDATED_AT, category.getUpdated_at());
        try {
        	db.insert(TABLE_CATEGORIES.TABLE_NAME_CATEGORIES, null, values);	
		} catch (SQLiteException e) {
			Log.d(this.getClass().getSimpleName(), e.toString());
		}
        
        db.close();
    }

    public void addItem(Item item){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Table_menus.COLUMN_ID, item.getId());
        values.put(Table_menus.COLUMN_NAME, item.getName());
        values.put(Table_menus.COLUMN_PRICE, item.getPrice());
        values.put(Table_menus.COLUMN_DESCRIPTION,item.getDescription());
        values.put(Table_menus.COLUMN_CATEGORY_ID, item.getCategory_id());
        values.put(Constants.COLUMN_CREATED_AT, item.getCreated_at());
        values.put(Constants.COLUMN_UPDATED_AT, item.getUpdated_at());
        try {
        	db.insert(Table_menus.TABLE_MENU_NAME,null,values);	
		} catch (SQLiteException e) {
			Log.d(this.getClass().getSimpleName(), e.toString());
		}
        
        db.close();
    }


    public void addImages(int itemId,/*byte[] image*/ String imageUrl, String createdAt, String updatedAt){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Table_MenuImages.COLUMN_MENU_ID, itemId);
        values.put(Table_MenuImages.COLUMN_IMAGE, imageUrl);
        values.put(Constants.COLUMN_CREATED_AT, createdAt);
        values.put(Constants.COLUMN_UPDATED_AT, updatedAt);
        try {
        	 db.insert(Table_MenuImages.TABLE_MENU_IMAGES_NAME, null, values);
        } catch (SQLiteException e) {
        	Log.d(this.getClass().getSimpleName(), e.toString());
        }
       
        db.close();
    }

    public void addOrders(Order order){

        SQLiteDatabase db = this.getWritableDatabase();
        CopyOnWriteArrayList<Item> itemsList = order.getItemsList();
        ContentValues values = new ContentValues();

        values.put(Table_Orders.TABLE_NUMBER, order.getTableNumber());
        values.put(Table_Orders.TABLE_COVER, order.getTableCover());
        values.put(Table_Orders.TIME_STAMP, order.getTimeStamp());
        try {
              long orderId = db.insert(Table_Orders.TABLE_NAME_ORDERS,null,values);

                for (int i = 0; i < itemsList.size(); i++) {

                    ContentValues orderDetails = new ContentValues();
                    orderDetails.put(Table_OrderDetails.ORDERS_ID, orderId);
                    orderDetails.put(Table_OrderDetails.ITEM_ID, itemsList.get(i).getId());
                    orderDetails.put(Table_OrderDetails.ITEM_NAME, itemsList.get(i).getName());
                    orderDetails.put(Table_OrderDetails.QUANTITY, itemsList.get(i).getDesiredQuantity());
                    try {
                        db.insert(Table_OrderDetails.TABLE_NAME_ORDER_DETAILS, null, orderDetails);

                    } catch (SQLiteException sqlException) {
                        //TODO: Exception of inserting order detail -notify UI about excep

                    }
                }

        } catch (SQLiteException e) {
            //TODO: exception about Order Crieation .notify UI about excep
        }

        db.close();
    }

//    public ArrayList<Order> getAllOrders(){
//
//        ArrayList<Order> orderList=new ArrayList<>();
////        CopyOnWriteArrayList<Item>  itemInfoList = new CopyOnWriteArrayList<>();
//
//        String selectQueryOrder="Select * from "+Table_Orders.TABLE_NAME_ORDERS;
//
//
//        SQLiteDatabase db=this.getWritableDatabase();
//
//        Cursor cursor = db.rawQuery(selectQueryOrder,null);
//
//
//        if(cursor.moveToFirst()){
//
//            do{
//                int orderID = cursor.getInt(cursor.getColumnIndexOrThrow(Table_Orders.ORDERS_ID.trim()));
//                String tableCover = cursor.getString(cursor.getColumnIndexOrThrow(Table_Orders.TABLE_COVER.trim()));
//                String tableNumber = cursor.getString(cursor.getColumnIndexOrThrow(Table_Orders.TABLE_NUMBER.trim()));
//                String timeStamp = cursor.getString(cursor.getColumnIndexOrThrow(Table_Orders.TIME_STAMP.trim()));
//
//                String selectQueryOrderItems="Select * from "+Table_OrderDetails.TABLE_NAME_ORDER_DETAILS+" where "+Table_OrderDetails.ORDERS_ID+" = "+orderID;
//                Cursor cursor1 = db.rawQuery(selectQueryOrderItems,null);
//                itemInfoList = new CopyOnWriteArrayList<>();
//                if(cursor1.moveToFirst()){
//
//                    do{
////                        int _id = cursor1.getInt(cursor1.getColumnIndexOrThrow(Table_OrderDetails.ORDER_DETS_ID.trim()));
////                        int orders_id = cursor1.getInt(cursor1.getColumnIndexOrThrow(Table_OrderDetails.ORDERS_ID.trim()));
//                        int item_id = cursor1.getInt(cursor1.getColumnIndexOrThrow(Table_OrderDetails.ITEM_ID.trim()));
//                        String item_name = cursor1.getString(cursor1.getColumnIndexOrThrow(Table_OrderDetails.ITEM_NAME.trim()));
//                        int item_quantity = cursor1.getInt(cursor1.getColumnIndexOrThrow(Table_OrderDetails.QUANTITY.trim()));
//
//                        Item itemsList = new Item(item_id,item_name,item_quantity);
//                        itemInfoList.add(itemsList);
////                        itemInfoList.add(item_id);
////                        itemInfoList.add(item_name);
////                        itemInfoList.add(item_quantity);
////                        itemInfoList.add(orders_id);
//
//                    }while (cursor1.moveToNext());
//
//                }
//
//                Order order = new Order(itemInfoList,tableCover,tableNumber,timeStamp);
//                order.setID(orderID);
//                orderList.add(order);
//
//            }while(cursor.moveToNext());
//        }
//
//        return orderList;
//    }

    /**This method fetches orders from db
     * which are updated or not
     *
     *
     * @param updated
     */
    public Cursor getOrders(Boolean updated){

        int booleanFlag =0;

        if(updated){
            booleanFlag =1;
        }
        String query = "Select * from "+Table_Orders.TABLE_NAME_ORDERS+" where "+Table_Orders.UPLOADED+" = "+booleanFlag;

        SQLiteDatabase db=this.getWritableDatabase();

        //TODO: rectify the error of query builder qeury

        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){

            do{

            }while(cursor.moveToNext());

        }
        return cursor;

    }

    public Cursor getAllOrders(){

        String query = "Select * from "+Table_Orders.TABLE_NAME_ORDERS;
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){

            do{

            }while(cursor.moveToNext());

        }
        return cursor;
    }

    public Cursor getOrderItems(int order_id){

        String selectQueryOrderItems = "Select * from " + Table_OrderDetails.TABLE_NAME_ORDER_DETAILS + " where " + Table_OrderDetails.ORDERS_ID + " = " + order_id;
        SQLiteDatabase db=this.getWritableDatabase();

        Cursor cursorOfItems = db.rawQuery(selectQueryOrderItems,null);

        if(cursorOfItems.moveToFirst()){

            do{
                //TODO: Refactor this
            }while(cursorOfItems.moveToNext());

        }
        return cursorOfItems;

    }


    public ArrayList<Category> getAllCategories(){
        ArrayList<Category> categoryList=new ArrayList<Category>();

        String selectQuery="Select * from "+TABLE_CATEGORIES.TABLE_NAME_CATEGORIES;
        SQLiteDatabase db=this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                String[] columnNames = cursor.getColumnNames();
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(TABLE_CATEGORIES.COLUMN_ID.trim()));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_CATEGORIES.COLUMN_NAME.trim()));
                byte[] image=cursor.getBlob(cursor.getColumnIndexOrThrow(TABLE_CATEGORIES.COLUMN_IMAGE.trim()));
                String createdAt=cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_CREATED_AT.trim()));
                String updatedAt=cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_UPDATED_AT.trim()));

                Category category=new Category(id,name,image,createdAt,updatedAt);

                categoryList.add(category);
            }while(cursor.moveToNext());

        }


        return categoryList;
    }

    public ArrayList<Item> getItemsWithImages(long categoryId){
        ArrayList<Item> itemsList = new ArrayList<Item>();
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "Select * from "+ Table_menus.TABLE_MENU_NAME +"where "+ Table_menus.COLUMN_CATEGORY_ID+" = "+categoryId;

        String selectQuery2 ="SELECT menus.id, menus.category_id, menus.name, " +
                "menus.price, menus.description, menus.created_at, " +
                "menus.updated_at," +
                "menu_images.menu_id, menu_images.image\n" +
                "FROM\n" +
                "  menu_images\n" +
                "  INNER JOIN menus ON (menu_images.menu_id = menus.id) " +
                "where menus.category_id = "+categoryId;

        String selectQueryLeftJoin="SELECT m.*, mi.* from menus m  left join menu_images mi on m.id = mi.menu_id\n" +
                "where m.category_id = "+categoryId;

        String revisedQuery="SELECT m.*, mi.menu_id, mi.image from menus m  left join menu_images mi on m.id = mi.menu_id\n" +
                "where m.category_id = "+categoryId;

        Cursor cursor = db.rawQuery(selectQuery, null);



        if(cursor.moveToFirst()){

            do{
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(Table_menus.COLUMN_ID.trim()));
                String imageQuery="Select * from menu_images where menu_id = "+ id;
                ArrayList<Image> itemImages = new ArrayList<>();
                Cursor cursorOfImage = db.rawQuery(imageQuery, null);
                if(cursorOfImage.moveToFirst()){
                    do{

                        Integer imageId=cursorOfImage.getInt(cursor.getColumnIndexOrThrow(Table_MenuImages.COLUMN_ID.trim()));
                        int menu_id=cursorOfImage.getInt(cursorOfImage.getColumnIndexOrThrow(Table_MenuImages.COLUMN_MENU_ID.trim()));
                        String imageUrl = cursorOfImage.getString(cursorOfImage.getColumnIndexOrThrow(Table_MenuImages.COLUMN_IMAGE.trim()));

                        String createdAt= cursorOfImage.getString(cursorOfImage.getColumnIndexOrThrow(Constants.COLUMN_CREATED_AT.trim()));;
                        String updatedAt = cursorOfImage.getString(cursorOfImage.getColumnIndexOrThrow(Constants.COLUMN_UPDATED_AT.trim()));;
                        Image itemImage = new Image(imageId, menu_id, imageUrl,createdAt,updatedAt);
                        itemImages.add(itemImage);

                    }while(cursorOfImage.moveToNext());

                }


                int category_id = cursor.getInt(cursor.getColumnIndexOrThrow(Table_menus.COLUMN_CATEGORY_ID.trim()));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(Table_menus.COLUMN_NAME.trim()));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(Table_menus.COLUMN_PRICE.trim()));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(Table_menus.COLUMN_DESCRIPTION.trim()));
                String created_at = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_CREATED_AT.trim()));
                String updated_at = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_UPDATED_AT.trim()));
               // String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(Table_MenuImages.COLUMN_IMAGE.trim()));


                //byte [] image = cursor.getBlob(cursor.getColumnIndexOrThrow(Table_MenuImages.COLUMN_IMAGE.trim()));

                Item item = new Item(id,name,description,price,category_id,created_at,updated_at,itemImages);
                itemsList.add(item);

            }while(cursor.moveToNext());
        }

        cursor.close();
        return itemsList;
    }


    public String getCategoryName(int categoryId){

        String categoryName = "";
        SQLiteDatabase db = this.getWritableDatabase();
        String query="Select "+TABLE_CATEGORIES.COLUMN_NAME+" "+"from "+TABLE_CATEGORIES.TABLE_NAME_CATEGORIES+" "+
                "where "+TABLE_CATEGORIES.COLUMN_ID+" "+"= "+categoryId;

        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){

            categoryName = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_CATEGORIES.COLUMN_NAME.trim()));


        }

        cursor.close();

        return categoryName;
    }

	public void clearCategoryTable() {
		
		deleteQuery(TABLE_CATEGORIES.TABLE_NAME_CATEGORIES);
		
	}
	private void deleteQuery(String tableName) {
		SQLiteDatabase db=this.getWritableDatabase();
		String deleteQuery = "delete from "+tableName.trim();
		try {
            db.delete(tableName.trim(),null,null);

			//db.rawQuery(deleteQuery, null);
		} catch (SQLiteException e) {
			Log.d(this.getClass().getSimpleName(), e.toString());
		}
		
		db.close();
		
	}
	public void clearMenuTable() {
		deleteQuery(Table_menus.TABLE_MENU_NAME);
		
	}
	public void clearImagesTable() {
		deleteQuery(Table_MenuImages.TABLE_MENU_IMAGES_NAME);
		
	}

    public void clearOrdersTable() {
        deleteQuery(Table_Orders.TABLE_NAME_ORDERS);

    }

    public void clearOrderDetailsTable() {
        deleteQuery(Table_OrderDetails.TABLE_NAME_ORDER_DETAILS);

    }

    public void updateOrder(int orderID, Boolean uploaded) {
        int booleanFlag =0;

        if(uploaded){
            booleanFlag =1;
        }

        String query = "Update "+Table_Orders.TABLE_NAME_ORDERS+" SET "+ Table_Orders.UPLOADED+" = '" +booleanFlag+ "' where "+Table_Orders.ORDERS_ID+" = "+orderID;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor updateCursor = db.rawQuery(query,null);
        try {
            updateCursor.moveToFirst();
        }

        catch (SQLiteException ex){
            Log.d("",ex.toString());
        }
        updateCursor.close();

    }

}
