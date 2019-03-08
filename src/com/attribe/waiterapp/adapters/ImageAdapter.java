package com.attribe.waiterapp.adapters;

/**
 * Created by M.Maaz on 9/22/2015.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import com.attribe.waiterapp.R;
import com.attribe.waiterapp.models.Image;
import com.attribe.waiterapp.models.Item;


public class ImageAdapter extends BaseAdapter {
    	
	private int itemBackground;
	private Context mContext;
    private List<Image> item_imageArrayList;
    private LayoutInflater inflater = null;
    
	 private File cacheDir;
	 private String filePath;
	 private File cacheFile;
	 private Uri uri;
	 private InputStream fileInputStream;
     private String itemName;
     private String itemCreatedAt;
	
	
	public ImageAdapter(Context context , List<Image> itemList,String itemName, String itemCreatedAt){
	
        this.mContext = context;
        this.item_imageArrayList = itemList;
        this.itemName = itemName;
        this.itemCreatedAt = itemCreatedAt;

	}
	
	

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
	public View getView(int position , View view, ViewGroup viewGroup) {

		ViewHolder viewHolder ;

		if(view == null){

			LayoutInflater inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view  = inflater.inflate(R.layout.item_order_list, null);

			viewHolder =new ViewHolder();
			viewHolder.imageViewItem = (ImageView) view.findViewById(R.id.item_order_list_image);
			viewHolder.listItemLayout = (RelativeLayout) view.findViewById(R.id.item_order_list_parent);

			view.setTag(viewHolder);

		}

		else{

			viewHolder = (ViewHolder) view.getTag();
		}

		cacheDir = mContext.getCacheDir();
		filePath =  itemName+ item_imageArrayList.get(position).getCreated_at();
		cacheFile = new File(cacheDir, filePath);
		uri = Uri.fromFile(cacheFile);

		try {
			fileInputStream = new FileInputStream(cacheFile);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

        viewHolder.imageViewItem.setImageURI(uri);


		if(item_imageArrayList.get(position).isSelected()){

			highlightItem(position,viewHolder);
		}
		else{

			unHighlightItem(position,viewHolder);
		}

		return view;
	}

	private void highlightItem(int position, ViewHolder viewHolder) {

		viewHolder.listItemLayout.setBackground(mContext.getResources().getDrawable(R.drawable.dialog_order_image_bg));
	}

	private void unHighlightItem(int position , ViewHolder viewHolder){

		viewHolder.listItemLayout.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
	}


	private static class ViewHolder{

		ImageView imageViewItem ;
		RelativeLayout listItemLayout;
	}

}
