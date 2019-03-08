package com.attribe.waiterapp.adapters;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.attribe.waiterapp.R;
import com.attribe.waiterapp.models.Category;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Sabih Ahmed on 5/12/2015.
 */
public class CategoryAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Category> categoryArrayList;

    public CategoryAdapter(Context context, ArrayList<Category> categoryArrayList) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;

    }

    @Override
    public int getCount() {
        return categoryArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return categoryArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {

        return categoryArrayList.get(i).getId();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = new ViewHolder();

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item_category, null);

            viewHolder = createViewHolder(view);
            view.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) view.getTag();
        }

        setCategoryImage(viewHolder, position);
        setCategoryName(viewHolder, position);

        final ViewHolder finalViewHolder = viewHolder;

        if(categoryArrayList.get(position).isSelected()){


            viewHolder.frameImageContainer.setBackground(context.getResources().getDrawable(R.drawable.shape_circle_white));
            view.setBackgroundDrawable((context.getResources().
                    getDrawable(R.drawable.shape_category_selector)));

        }

        else{
            view.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
            viewHolder.frameImageContainer.setBackground(context.getResources().getDrawable(R.drawable.shape_category_circle));
        }

        return view;
    }

    private void setCategoryName(ViewHolder viewHolder, int position) {
        viewHolder.categoryName.setText(categoryArrayList.get(position).getName());
    }

    private void setCategoryImage(ViewHolder viewHolder, int position) {
        if (categoryArrayList.get(position).getImageBlob() == null) {

            File cacheDir = context.getCacheDir();
            String filePath = categoryArrayList.get(position).getName() + categoryArrayList.get(position).getCreated_at();
            File cacheFile = new File(cacheDir, filePath);
            try {
                InputStream fileInputStream = new FileInputStream(cacheFile);

                viewHolder.imageView.setImageBitmap(BitmapFactory.decodeStream(fileInputStream));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private static class ViewHolder {

        private TextView categoryName;
        private ImageView imageView;
        private LinearLayout frameImageContainer;

    }

    private ViewHolder createViewHolder(View row) {
        ViewHolder viewHolder = new ViewHolder();

        viewHolder.categoryName = (TextView) row.findViewById(R.id.list_item_category);
        viewHolder.imageView = (ImageView) row.findViewById(R.id.list_item_category_image);

        viewHolder.frameImageContainer = (LinearLayout) row.findViewById(R.id.list_item_category_imageFrame);

        return viewHolder;
    }
}