package com.attribe.waiterapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.Touch;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.attribe.waiterapp.R;
import com.attribe.waiterapp.helper.TouchImageView;
import com.attribe.waiterapp.models.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by M.Maaz on 9/22/2015.
 */
public class SwipeImageAdapter extends PagerAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    private List<Image> item_imageArrayList;
    private File cacheDir;
    private String filePath;
    private File cacheFile;
    private Uri uri;
    private InputStream fileInputStream;
    private String itemName;
    private String itemCreatedAt;
    private TouchImageView imgDisplay;

    // constructor
    public SwipeImageAdapter(Context context , List<Image> itemList,String itemName, String itemCreatedAt) {

        this.mContext = context;
        this.item_imageArrayList = itemList;
        this.itemName = itemName;
        this.itemCreatedAt = itemCreatedAt;
    }


    @Override
    public int getCount() {
        return item_imageArrayList.size();
    }

    public Object getItem(int position) {
        return item_imageArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView imgDisplay;

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.new_order_image_screen_dialogue, container, false);

        imgDisplay = (ImageView) viewLayout.findViewById(R.id.iv_new_order_image);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        cacheDir = mContext.getCacheDir();
        filePath =  itemName+ item_imageArrayList.get(position).getCreated_at();
        cacheFile = new File(cacheDir, filePath);
        uri = Uri.fromFile(cacheFile);

        try {
            fileInputStream = new FileInputStream(cacheFile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        imgDisplay.setImageURI(uri);

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
