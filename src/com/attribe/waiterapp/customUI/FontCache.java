package com.attribe.waiterapp.customUI;

import android.content.Context;
import android.graphics.Typeface;


import java.util.Hashtable;

/**
 * Created by Sabih Ahmed on 21-Oct-15.
 */
public class FontCache {

    private static Hashtable<String,Typeface> fontCache = new Hashtable<>();

    public static Typeface getTypeface(String fontName, Context context){

        Typeface typeface = fontCache.get(fontName);

        if(typeface == null){

            try{

                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/"+fontName);

            }catch (Exception e){
                return null;
            }

            fontCache.put(fontName,typeface);
        }

        return typeface;
    }

}
