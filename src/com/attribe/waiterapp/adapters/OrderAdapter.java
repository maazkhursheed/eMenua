package com.attribe.waiterapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.attribe.waiterapp.R;
import com.attribe.waiterapp.interfaces.OnItemRemoveListener;
import com.attribe.waiterapp.models.Order;
import com.attribe.waiterapp.utils.OrderContainer;


import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Sabih Ahmed on 5/21/2015.
 */
public class OrderAdapter extends BaseAdapter{

    private Context mContext;
    private CopyOnWriteArrayList<Order> orderList;
    private OnItemRemoveListener itemRemoveListener;


    public void setOnItemRemoveListener(OnItemRemoveListener itemRemoveListener ){
        this.itemRemoveListener=itemRemoveListener;

    }
    public OrderAdapter(Context context, CopyOnWriteArrayList<Order> orderList) {
        mContext = context;
        this.orderList = orderList;

    }


    @Override
    public int getCount() {

        return orderList.size();
    }

    @Override
    public Object getItem(int i) {

        return orderList.get(i);

    }

    @Override
    public long getItemId(int i) {
        return orderList.get(i).getItemId();
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder=new ViewHolder();
        if(convertView == null){

            LayoutInflater inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_grid_order_new, null);

            viewHolder = createViewHolder(viewHolder, convertView);

            convertView.setTag(viewHolder);
        }

        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.itemName.setText(orderList.get(i).getItem().getName());
        viewHolder.itemQuantityTotal.setText( computeTotal(orderList.get(i).getQuantityValue(),orderList.get(i).getItem().getPrice()));
        viewHolder.grid_ItemQuantity.setText(Integer.toString(orderList.get(i).getQuantityValue())+ " " + "item(s)");
        viewHolder.itemPrice.setText(Double.toString(orderList.get(i).getItem().getPrice()));

//        viewHolder.itemQuantity.setText("x "+ Integer.toString(itemList.get(i).getQuantityValue())+"\t = "+
//                                        computeTotal(itemList.get(i).getQuantityValue(),itemList.get(i).getItem().getPrice()));

        if(orderList.get(i).getItem().getImageBlob()== null){

            try {
                viewHolder.itemImage.setImageURI(getImage(orderList.get(i)));
            }

            catch (NullPointerException e){
                viewHolder.itemImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.sample_burger));
            }


        }

        viewHolder.crossBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                OrderContainer.getInstance().getOrderableItems().remove(i);
                notifyDataSetChanged();
                itemRemoveListener.onItemRemoved();


            }
        });

        return convertView;
    }

    private String computeTotal(int quantityValue, double price) {
        double total = quantityValue * price;

        return Double.toString(total);
    }

    private ViewHolder createViewHolder(ViewHolder viewHolder, View convertView) {


        viewHolder.itemName= (TextView) convertView.findViewById(R.id.grid_item_itemName);
        viewHolder.itemQuantityTotal=(TextView)convertView.findViewById(R.id.grid_item_order_quantity);
        viewHolder.itemPrice=(TextView)convertView.findViewById(R.id.grid_item_price);
        viewHolder.grid_ItemQuantity=(TextView)convertView.findViewById(R.id.grid_ItemQuantity);
        viewHolder.crossBox=(CheckBox) convertView.findViewById(R.id.grid_item_order_cross);
        viewHolder.itemImage=(ImageView)convertView.findViewById(R.id.grid_item_image);
        return viewHolder;
    }

    private class ViewHolder {
        TextView itemName,itemPrice,itemQuantityTotal,grid_ItemQuantity ;
        CheckBox crossBox;
        ImageView itemImage;

    }

    private Uri getImage(Order order){
        File cacheDir = mContext.getCacheDir();
        Uri uri;
        try {

            String filePath = order.getItem().getName()+order.getItem().getImages().get(0).getCreated_at();
            File imageFile = new File(cacheDir,filePath);

            uri = Uri.fromFile(imageFile);
        }

        catch (IndexOutOfBoundsException e){
            uri = null;
        }


        return uri;

    }

}
