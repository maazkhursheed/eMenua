package com.attribe.waiterapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.attribe.waiterapp.R;
import com.attribe.waiterapp.interfaces.ItemAddedTotalValue;
import com.attribe.waiterapp.interfaces.OnInstantOrder;
import com.attribe.waiterapp.learningengine.LearningStatus;
import com.attribe.waiterapp.learningengine.UXLearning;
import com.attribe.waiterapp.models.Item;
import com.attribe.waiterapp.utils.AnimationUtils;
import com.attribe.waiterapp.utils.CurrencyHelper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Maaz on 2/8/2016.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{


    private Context context;
    private ArrayList<Item> itemList;
    private OnInstantOrder onInstantOrder;
    private ItemAddedTotalValue itemAddedTotalValue;
    private MenuItemListener menuItemListener;
    private int lastPosition = -1;
    private LinearLayout gridItem;
    private boolean isGridItemClicked;

    public void setInstantOrderListener(OnInstantOrder onInstantOrder){
        this.onInstantOrder = onInstantOrder;
    }

    public void setItemAddedTotalValue(ItemAddedTotalValue itemAddedTotalValue){
        this.itemAddedTotalValue = itemAddedTotalValue;
    }

    public void setMenuItemListener(MenuItemListener menuItemListener){
        this.menuItemListener = menuItemListener;
    }

    public ItemAdapter(Context context, ArrayList<Item> itemList, boolean gridIemClicked){
        this.context = context;
        this.itemList = itemList;
        this.isGridItemClicked = gridIemClicked;
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

//    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       final View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_grid, null);

        ViewHolder rcv = new ViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.itemName.setText(itemList.get(position).getName());
        holder.itemPrice.setText(CurrencyHelper.getCurrencyPrefix()+" "+String.valueOf(itemList.get(position).getPrice()));

        byte[] imagesBlob = itemList.get(position).getImageBlob();
        holder.position = position;
        AnimationUtils.setAnimation(context, holder.listItemGridLayout,position,isGridItemClicked);
        if(imagesBlob == null){

            //checking if images list of item is not empty
            if(!itemList.get(position).getImages().isEmpty()){
                String imageFilePath = itemList.get(position).getName()+itemList.get(position).getImages().get(0).getCreated_at();

                File cacheFile = new File(context.getCacheDir(), imageFilePath);
                Uri uri = Uri.fromFile(cacheFile);
                Picasso.with(context).load(uri).into(holder.itemImage);

            }

        holder.listItemGridLayout.setOnClickListener(new ItemClickListener(itemList.get(position)));
        }




    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        ((ViewHolder)holder).clearAnimation();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView itemName, itemPrice;

        LinearLayout listItemGridLayout;
        ImageView itemImage;
        public int position;
        View listView;
        private int learningStatus;


        public ViewHolder(View view) {
            super(view);

            itemName = (TextView) view.findViewById(R.id.grid_item_itemName);
            itemPrice = (TextView)view.findViewById(R.id.grid_item_price);

            listItemGridLayout = (LinearLayout) view.findViewById(R.id.list_item_grid_parent);
            itemImage = (ImageView)view.findViewById(R.id.grid_item_image);

            gridItem = listItemGridLayout;
        }

        public void clearAnimation(){
            listItemGridLayout.clearAnimation();
        }

        public void setLearningStatus(int learningStatus) {
            this.learningStatus = learningStatus;
        }
    }

    public interface MenuItemListener{

        void OnMenuItemClick(Item item);
    }

    private static class ThumbnailTask extends AsyncTask {

        private int mPosition;
        private ViewHolder mHolder;
        private Context mContext;
        private ArrayList<Item> mItemList;
        private File cacheDir;
        private String filePath;
        private File cacheFile;
        private Uri uri;
        private InputStream fileInputStream;

        public ThumbnailTask(Context context, int position, ViewHolder viewHolder,
                             String imageFilePath) {
            this.mPosition = position;
            this.mHolder = viewHolder;
            this.mContext=context;
            this.filePath=imageFilePath;
        }


        @Override
        protected Object doInBackground(Object[] objects) {
            cacheDir = mContext.getCacheDir();

            cacheFile = new File(cacheDir, filePath);

            uri = Uri.fromFile(cacheFile);

            try {
                fileInputStream = new FileInputStream(cacheFile);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (mHolder.position == mPosition) {
                //mHolder.itemImage.setImageURI(getImageUri(cacheDir,filePath));
                mHolder.itemImage.setImageBitmap(BitmapFactory.decodeStream(fileInputStream));
            }
        }
    }

    private class ItemClickListener implements View.OnClickListener {

        Item item = null;
        public ItemClickListener(Item item) {
            this.item = item;
        }

        @Override
        public void onClick(View v) {

            menuItemListener.OnMenuItemClick(item);
            LearningStatus.setIsGridIemClicked(true);
            AnimationUtils.setAnimation(context,gridItem,false);
        }
    }
}
