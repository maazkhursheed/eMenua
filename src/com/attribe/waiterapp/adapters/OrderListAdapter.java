package com.attribe.waiterapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.attribe.waiterapp.Print.PrintUtils;
import com.attribe.waiterapp.R;
import com.attribe.waiterapp.interfaces.OnItemRemoveListener;
import com.attribe.waiterapp.interfaces.OnQuantityChangeListener;
import com.attribe.waiterapp.models.Item;
import com.attribe.waiterapp.models.Order;
import com.attribe.waiterapp.screens.MainActivity;
import com.attribe.waiterapp.screens.OrderFragment;
import com.attribe.waiterapp.utils.OrderContainer;


import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;

/**This Adapter binds view for OrderViewing Screen
 *
 * Created by Attribes on 2/15/2016.
 */
public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder>  {

    private Context mContext;
    private CopyOnWriteArrayList<Item> orderList;
    private OnItemRemoveListener itemRemoveListener;
    private OrderFragment.OnActionBarValuesUpdated actionBarListener;

    public void setQuantityChangeListener(OnQuantityChangeListener listener) {
        this.listener = listener;
    }

    private OnQuantityChangeListener listener;



    private  int maxInputLenth = 15;
    private int initialQuantity ;
    private ViewHolder viewHolder ;
    private View layoutView;


    public void setOnItemRemoveListener(OnItemRemoveListener itemRemoveListener ){
        this.itemRemoveListener=itemRemoveListener;

    }
    public OrderListAdapter(Context context, CopyOnWriteArrayList<Item> orderList) {
        this.mContext = context;
        this.orderList = orderList;

    }

    @Override
    public int getItemCount() {

        return orderList.size();
    }



    //    @Override
    public Object getItem(int i) {

        return orderList.get(i);

    }

    @Override
    public long getItemId(int i) {
        return orderList.get(i).getId();
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        layoutView = LayoutInflater.from(mContext).inflate(R.layout.list_item_order_new, parent , false);


            viewHolder = new ViewHolder(layoutView);
            layoutView.setTag(viewHolder);


            viewHolder = (ViewHolder)layoutView.getTag();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

       Item item = orderList.get(position);
        //initialQuantity = itemList.get(position).getQuantityValue();
        if(mContext.getClass().getSimpleName().equals(MainActivity.class.getSimpleName())){
            actionBarListener = (OrderFragment.OnActionBarValuesUpdated)mContext;
        }


        String itemName =  PrintUtils.addLinebreaks( item.getName(),maxInputLenth);

        holder.list_itemName.setText(itemName);
        holder.list_itemPrice.setText(Double.toString( item.getPrice()));
        holder.list_ItemQuantity.setText(Integer.toString( item.getDesiredQuantity()));

        holder.list_itemQuantityTotal.setText( OrderContainer.getInstance().computeTotal(item.getDesiredQuantity(),item.getPrice()));

        if(position % 2 == 0){

            layoutView.setBackgroundResource(R.drawable.row_item_bg);
        }

        //Todo: have to refactor this

        holder.list_itemIncrementBtn.setOnClickListener(new IncrementPriceListener(position,
                                                        item.getDesiredQuantity()));
        holder.list_itemDecrementBtn.setTag(orderList.get(position));
        holder.list_itemDecrementBtn.setOnClickListener(new DecrementPriceListener(position,
                item.getDesiredQuantity()));



        holder.list_crossBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                OrderContainer.getInstance().getOrderableItems().remove(position);
                notifyDataSetChanged();
                itemRemoveListener.onItemRemoved();
                Toast.makeText(mContext, "The item has been removed ....!", Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

//    private String computeTotal(int quantityValue, double price) {
//        double total = quantityValue * price;
//
//        return Double.toString(total);
//    }

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



    private class IncrementPriceListener implements View.OnClickListener {

        private final int position;
        private int chosenQuantity;
        public IncrementPriceListener( int position, int chosenQuantity) {
            this.position = position;
            this.chosenQuantity = chosenQuantity;
        }

        @Override
        public void onClick(View view ) {

            chosenQuantity++;

            OrderContainer.getInstance().addOrUpdateOrder(OrderContainer.getInstance().getOrderableItems().get(position),
                                                           chosenQuantity);
            viewHolder.list_ItemQuantity.setText(Integer.toString(OrderContainer.getInstance().getOrderableItems().
                                                get(position).getDesiredQuantity()));
            notifyDataSetChanged();

            if(actionBarListener !=null)actionBarListener.onActionBarValuesUpdate();

            listener.onQuantityChanged();

        }

    }

    private class DecrementPriceListener implements View.OnClickListener {
        private int position;
        private int chosenQuantity;

        public DecrementPriceListener(int position, int quantityValue) {

            this.position = position;
            this.chosenQuantity = quantityValue;
        }

        @Override
        public void onClick(View view) {


            if(chosenQuantity > 0){
                chosenQuantity--;
                viewHolder.list_ItemQuantity.setText(String.valueOf(chosenQuantity));
                OrderContainer.getInstance().addOrUpdateOrder(OrderContainer.getInstance().getOrderableItems().get(position)
                                                                ,chosenQuantity);
                if(actionBarListener !=null)actionBarListener.onActionBarValuesUpdate();
                notifyDataSetChanged();
                listener.onQuantityChanged();
            }

        }
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        TextView list_itemName, list_itemPrice, list_ItemQuantity, list_itemQuantityTotal;
        CheckBox list_crossBox;
        Button list_itemIncrementBtn , list_itemDecrementBtn;


        public ViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);

            list_itemName= (TextView)view.findViewById(R.id.list_itemName);
            list_itemPrice=(TextView)view.findViewById(R.id.list_itemPrice);
            list_ItemQuantity=(TextView)view.findViewById(R.id.list_ItemQuantity);
            list_itemQuantityTotal=(TextView)view.findViewById(R.id.list_item_quantity_total);
            list_crossBox=(CheckBox)view.findViewById(R.id.list_item_order_cross);
            list_itemIncrementBtn=(Button)view.findViewById(R.id.buttonItemIncrement);
            list_itemDecrementBtn=(Button)view.findViewById(R.id.buttonItemDecrement);
        }


        @Override
        public void onClick(View v) {

        }
    }


}
