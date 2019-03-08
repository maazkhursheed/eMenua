package com.attribe.waiterapp.customUI;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import com.attribe.waiterapp.R;

/**
 * Created by Sabih Ahmed on 5/14/2015.
 */
public class TextViewProxiOrange extends TextView {
    public TextViewProxiOrange(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        Typeface  fontProxi=Typeface.createFromAsset(getContext().getAssets(),"fonts/TalkingToTheMoon.ttf");
        setTypeface(fontProxi);
        setTextColor(getResources().getColor(R.color.orange));

    }

}
