package com.attribe.waiterapp.customUI;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Sabih Ahmed on 21-Oct-15.
 */
public class CustomFont extends TextView{

    public CustomFont(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public CustomFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public CustomFont(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {

        Typeface typeface = FontCache.getTypeface("TalkingToTheMoon.ttf",context);
        setTypeface(typeface);
    }
}
