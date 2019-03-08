package com.attribe.waiterapp.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.attribe.waiterapp.models.Category;

import java.util.ArrayList;

/**
 * Created by Sabih Ahmed on 09-Sep-15.
 */
public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private CarouselImageView[] mImages;
    private ArrayList<Category> categoriesList;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public ImageAdapter(Context mContext, ArrayList<Category> categoriesList) {
        this.mContext = mContext;
        this.categoriesList = categoriesList;
    }

    @SuppressWarnings("unused")
    public void SetImages(TypedArray array) {
        SetImages(array, true);
    }

    public void SetImages(TypedArray array, boolean reflected) {

        final int reflectionGap = 4;

        Drawable[] drawables = new Drawable[array.length()];
        mImages = new CarouselImageView[array.length()];

        for (int i = 0; i < array.length(); i++) {
            drawables[i] = array.getDrawable(i);
            Bitmap originalImage = ((BitmapDrawable) drawables[i]).getBitmap();

            if (reflected) {
                int width = originalImage.getWidth();
                int height = originalImage.getHeight();

                // This will not scale but will flip on the Y axis
                Matrix matrix = new Matrix();
                matrix.preScale(1, -1);

                // Create a Bitmap with the flip matrix applied to it.
                // We only want the bottom half of the image
                Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
                        height / 2, width, height / 2, matrix, false);

                // Create a new bitmap with same width but taller to fit
                // reflection
                Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
                        (height + height / 2), Bitmap.Config.ARGB_8888);

                // Create a new Canvas with the bitmap that's big enough for
                // the image plus gap plus reflection
                Canvas canvas = new Canvas(bitmapWithReflection);
                // Draw in the original image
                canvas.drawBitmap(originalImage, 0, 0, null);
                // Draw in the gap
                Paint deafaultPaint = new Paint();
                canvas.drawRect(0, height, width, height + reflectionGap,
                        deafaultPaint);
                // Draw in the reflection
                canvas.drawBitmap(reflectionImage, 0, height + reflectionGap,
                        null);

                // Create a shader that is a linear gradient that covers the
                // reflection
                Paint paint = new Paint();

                LinearGradient shader = new LinearGradient(0,
                        originalImage.getHeight(), 0,
                        bitmapWithReflection.getHeight() + reflectionGap,
                        0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP);
                // Set the paint to use this shader (linear gradient)
                paint.setShader(shader);
                // Set the Transfer mode to be porter duff and destination in
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                // Draw a rectangle using the paint with our linear gradient
                canvas.drawRect(0, height, width,
                        bitmapWithReflection.getHeight() + reflectionGap, paint);

                originalImage = bitmapWithReflection;
            }

            CarouselImageView imageView = new CarouselImageView(mContext);
            imageView.setImageBitmap(originalImage);
            imageView.setIndex(i);


            ////////imageView.setScaleType(ScaleType.MATRIX);
            mImages[i] = imageView;


        }


    }

    public int getCount() {
        if (mImages == null)
            return 0;
        else
            return mImages.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return categoriesList.get(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        return mImages[position];
    }

    public void SetImages(ArrayList<Category> mCategoriesList) {

        categoriesList = mCategoriesList;
        int size = mCategoriesList.size();
        Drawable[] drawables = new Drawable[size];
        mImages = new CarouselImageView[size];

        for (int i = 0; i < size  ; i++) {

            drawables[i] = mCategoriesList.get(i).getCarouselImage();
            Bitmap originalImage = ((BitmapDrawable) drawables[i]).getBitmap();

            CarouselImageView imageView = new CarouselImageView(mContext);
            imageView.setSelected(false);
            imageView.setImageBitmap(originalImage);
            imageView.setIndex(i);
            imageView.setFocusable(false);
            mImages[i] = imageView;



        }




    }
}
