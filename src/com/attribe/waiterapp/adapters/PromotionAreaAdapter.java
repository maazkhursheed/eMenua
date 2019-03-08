package com.attribe.waiterapp.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.attribe.waiterapp.R;


import java.util.ArrayList;

/**
 * Created by Sabih Ahmed on 19-Oct-15.
 */
public class PromotionAreaAdapter extends PagerAdapter{

    private Context context;
    private ArrayList<Drawable> resourceList;



    public PromotionAreaAdapter(Context context) {

        this.context = context;
        resourceList=new ArrayList<>();
        resourceList.add(context.getResources().getDrawable(R.drawable.promo_1));
        resourceList.add(context.getResources().getDrawable(R.drawable.promo_2));
        resourceList.add(context.getResources().getDrawable(R.drawable.promo_3));
        resourceList.add(context.getResources().getDrawable(R.drawable.promo_4));
        resourceList.add(context.getResources().getDrawable(R.drawable.promo_5));

    }


    @Override
    public int getCount() {
        return resourceList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == ((LinearLayout) o);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view =  inflater.inflate(R.layout.promo_item,null);


        ImageView image = (ImageView)view.findViewById(R.id.promo_item_image);


        image.setImageDrawable(resourceList.get(position));


        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       container.removeView((LinearLayout) object);
    }
}
