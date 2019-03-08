package com.attribe.waiterapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;
import com.attribe.waiterapp.R;
import com.attribe.waiterapp.models.Item;
import com.attribe.waiterapp.models.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maaz on 4/14/2016.
 */
public class ExpandableOrderListAdapter extends BaseExpandableListAdapter {

    private final List<Order> orderLog;
    public LayoutInflater inflater;
    public Activity activity;

    public ExpandableOrderListAdapter(Activity act, ArrayList<Order> logList) {
        activity = act;
        this.orderLog = logList;
        inflater = act.getLayoutInflater();
    }

    @Override
    public int getGroupCount() {
        return orderLog.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return orderLog.get(groupPosition).getItemsList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return orderLog.get(groupPosition).getTableNumber();
    }

    @Override
    public Item getChild(int groupPosition, int childPosition) {

        return orderLog.get(groupPosition).getItemsList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_group, null);
        }

        ((CheckedTextView) convertView).setText(" Table # "+orderLog.get(groupPosition).getTableNumber()+"\t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t"+"Uploaded :"+orderLog.get(groupPosition).getUploaded()+"\n"+
                                                " Table Cover : "+orderLog.get(groupPosition).getTableCover()+"\n"+
                                                " Order Time : "+Long.toString(orderLog.get(groupPosition).getTimeStamp()));
        ((CheckedTextView) convertView).setChecked(isExpanded);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final Item item = getChild(groupPosition, childPosition);
        TextView text = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.order_log_item, null);
        }
        text = (TextView) convertView.findViewById(R.id.orderListItem);
        text.setText(item.getName()+"  ->  "+item.getDesiredQuantity());

        return convertView;
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
