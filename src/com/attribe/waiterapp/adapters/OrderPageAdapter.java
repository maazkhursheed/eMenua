package com.attribe.waiterapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.attribe.waiterapp.R;
import com.attribe.waiterapp.models.Item;
import com.attribe.waiterapp.models.Order;
import com.attribe.waiterapp.screens.OrderTakingScreen;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Sabih Ahmed on 21-Oct-15.
 */
public class OrderPageAdapter extends BaseAdapter{

    private final Context context;
    private final CopyOnWriteArrayList<Item> orderList;


    public OrderPageAdapter(Context context, CopyOnWriteArrayList<Item> orderList) {

        this.context = context;
        this.orderList=orderList;
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.order_page_item,null);

        TextView itemName = (TextView)convertView.findViewById(R.id.item_name);
        TextView itemQuantity = (TextView)convertView.findViewById(R.id.item_quantity);

        itemName.setText(orderList.get(position).getName());
        itemQuantity.setText(Integer.toString(orderList.get(position).getDesiredQuantity()));

        return convertView;
    }
}
