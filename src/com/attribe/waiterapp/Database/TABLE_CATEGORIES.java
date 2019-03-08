package com.attribe.waiterapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.attribe.waiterapp.models.Category;

/**
 * Created by Sabih Ahmed on 5/19/2015.
 */
public class TABLE_CATEGORIES extends DatabaseHelper{

    public TABLE_CATEGORIES(Context context) {
        super(context);

    }
    public static final String TABLE_NAME_CATEGORIES = "categories ";
    public static final String COLUMN_ID = "id ";
    public static final String COLUMN_NAME = "name ";
    public static final String COLUMN_IMAGE ="image ";


    public static final String CREATE_CATEGORY_TABLE = "CREATE TABLE "+ TABLE_NAME_CATEGORIES +"("+
                                COLUMN_ID +"Integer " + Constants.COMMA_SEP+
                                COLUMN_NAME + Constants.TYPE_TEXT +Constants.COMMA_SEP+
                                COLUMN_IMAGE + Constants.TYPE_BYTE +Constants.COMMA_SEP +
                                Constants.COLUMN_CREATED_AT+ Constants.TYPE_DATETIME+Constants.COMMA_SEP+
                                Constants.COLUMN_UPDATED_AT+ Constants.TYPE_DATETIME+ ")";




    public static final String DROP_CATEGORY_TABLE ="Drop table if exists " + TABLE_NAME_CATEGORIES;

    /**
     *
     * @param category
     */
    public void addCategory(Category category){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, category.getId());
        values.put(COLUMN_NAME, category.getName());
        values.put(COLUMN_IMAGE, category.getImageUrl());

        db.insert(TABLE_NAME_CATEGORIES,null,values);
        db.close();
    }

}
