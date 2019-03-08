package com.attribe.waiterapp.screens;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.*;
import com.attribe.waiterapp.Database.DatabaseHelper;
import com.attribe.waiterapp.R;
import com.attribe.waiterapp.adapters.ImageAdapter;
import com.attribe.waiterapp.adapters.SwipeImageAdapter;
import com.attribe.waiterapp.interfaces.OnItemAddedToOrder;
import com.attribe.waiterapp.interfaces.OnQuantityChangeListener;
import com.attribe.waiterapp.interfaces.OrderButtonListener;
import com.attribe.waiterapp.interfaces.QuantityPicker;
import com.attribe.waiterapp.models.Image;
import com.attribe.waiterapp.models.Item;
import com.attribe.waiterapp.models.Order;
import com.attribe.waiterapp.utils.OrderContainer;
import com.attribe.waiterapp.utils.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Sabih Ahmed on 5/14/2015.
 */
public class OrderDialogScreen extends MainActivity implements QuantityPicker{

    private TextView textViewItemName,
            textViewTotalPrice,
            textViewItemPrice,
            textViewQuantity,
            textViewCategoryName;
    private NumberPicker pricePicker;
    private ImageView itemImage;
    private ImageView backButton;
    private TextView  backToMainScreen;
    private Item item;
    private Intent i;
    private int itemQuantity;
    private int position;
    private static CopyOnWriteArrayList<Order> orderList;
    private static OnQuantityChangeListener quantityChangeListener;
    private static OnItemAddedToOrder onItemAddedToOrder;
    private ListView galleryList;
    private List<Image> item_imageArrayList;
    private Button buttonIncrement;
    private Button buttonDecrement;
    private Button btnBackToCategory;

    public Context mContext;
    public File cacheDir;
    public String filePath;
    public File cacheFile;
    public Uri uri;
    public InputStream fileInputStream;
    public DatabaseHelper databaseHelper;
    private QuantityPicker quantityPicker;
    private int initialQuantity;
    private int newQuantity;
    private SwipeImageAdapter swipeImageAdapter;
    private ImageAdapter verticalGalleryAdapter;
    private ViewPager viewPager;
    private LinearLayout reviewArea,postCommentsArea, userReviewsArea;
    private TextView cheffName,cheffComments,comments_area;
    private ActionBar detailScreenBar;
    private static OrderButtonListener orderButtonListener;



    private int oldSelectedPosition = -1;       // no item selected by default

    public OrderDialogScreen(){}

    public void setOnItemAddedToOrderListener(OnItemAddedToOrder onItemAddedToOrderListener){
        this.onItemAddedToOrder = onItemAddedToOrderListener;
    }
    public void setQuantityChangeListener(OnQuantityChangeListener quantityChangeListener){
        this.quantityChangeListener = quantityChangeListener;
    }

    public void setOrderButtonListener(OrderButtonListener orderButtonListener) {

        this.orderButtonListener = orderButtonListener;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.detail_full_screen);

        initContent();

        quantityPicker=this;
        initializeActionBar();



    }

    private void initializeActionBar(){
        // action bar gets initialized in overridden method 'setCustomActionBar'

        Button viewOrderButton = (Button)detailScreenBar.getCustomView().findViewById(R.id.ab_button);
        viewOrderButton.setOnClickListener(new ViewOrderFromDetailListener());
    }

    @Override
    public void setCustomActionBar(ActionBar actionBar) {
        detailScreenBar = getActionBar();
        super.setCustomActionBar(detailScreenBar);


    }

    private void initContent() {

        i = getIntent();
        databaseHelper = new DatabaseHelper(this);
        item = (Item) i.getSerializableExtra(Constants.KEY_SERIALIZEABLE_ITEM_OBJECT);
        position = i.getIntExtra(Constants.KEY_ITEM_POSITION, -1);

        textViewItemName = (TextView)findViewById(R.id.dialog_order_itemName);
        textViewItemPrice = (TextView)findViewById(R.id.item_detail_price);
        textViewTotalPrice = (TextView) findViewById(R.id.item_detail_price);
        textViewQuantity = (TextView)findViewById(R.id.textViewQuantity);
        textViewCategoryName = (TextView)findViewById(R.id.dialog_order_categoryName);

        pricePicker=(NumberPicker)findViewById(R.id.dialog_order_numberPicker);
        pricePicker.setMinValue(1);
        pricePicker.setMaxValue(100);

        buttonDecrement = (Button)findViewById(R.id.buttonDecrement);
        buttonIncrement=(Button)findViewById(R.id.buttonIncrement);

        buttonDecrement.setOnClickListener(new QuantityDecrementListener());
        buttonIncrement.setOnClickListener(new QuantityIncrementListener());

        //////Gallery of images [start]
        //vertical list of item images
        galleryList = (ListView)findViewById(R.id.imageGalleryViewList);
        item_imageArrayList = item.getImages();
        verticalGalleryAdapter=new ImageAdapter(this,item_imageArrayList,item.getName(),item.getCreated_at());

        galleryList.setAdapter(verticalGalleryAdapter);

        cacheDir = OrderDialogScreen.this.getCacheDir();
        galleryList.setOnItemClickListener(new GalleryClickListener());

        //////Gallery Of Images [end]

        backButton =(ImageView)findViewById(R.id.dialog_order_removeButton);
        backButton.setOnClickListener(new BackButtonListener());
        backToMainScreen = (TextView) findViewById(R.id.dialog_order_categoryName);
        backToMainScreen.setOnClickListener(new BackNavigationListener());
        btnBackToCategory = (Button)findViewById(R.id.back_to_category);
        btnBackToCategory.setOnClickListener(new BackNavigationListener());

        viewPager = (ViewPager) findViewById(R.id.pager);
        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);

        reviewArea = (LinearLayout)findViewById(R.id.reviews_area);
        postCommentsArea = (LinearLayout)findViewById(R.id.post_comments_area);
        userReviewsArea = (LinearLayout)findViewById(R.id.user_reviews_area);
        cheffName = (TextView)findViewById(R.id.cheffName);
        cheffComments = (TextView)findViewById(R.id.cheffDescription);
        comments_area = (TextView)findViewById(R.id.comments_area);

        handleReviewArea(position);
        swipeImageAdapter = new SwipeImageAdapter(this,item.getImages(),item.getName(),item.getCreated_at());
        viewPager.setAdapter(swipeImageAdapter);

        viewPager.setOnPageChangeListener(new SwipeImageListener());
        // displaying selected image first
        viewPager.setCurrentItem(position);
        //Initializing values
        initValues();

    }

    private void handleReviewArea(int position) {
        if(position == 0 || position == 1 || position == 2){

            cheffName.setVisibility(View.VISIBLE);
            cheffComments.setVisibility(View.VISIBLE);

            reviewArea.setBackground(null);


            postCommentsArea.setVisibility(View.GONE);
            userReviewsArea.setVisibility(View.VISIBLE);
        }
    }

    /**This method initializes & renders the necessary information
     * to the view such as item name, item price, item images, quantity etc
     *
     */
    private void initValues() {
        ////setting item name and Price
        textViewItemName.setText(item.getName());
        textViewItemPrice.setText(String.valueOf(item.getPrice()));

        if(i.getBooleanExtra("fromOrder",false)){
            textViewCategoryName.setText("Back to order");

        }
        else{
            String categoryName = databaseHelper.getCategoryName(item.getCategory_id());
            textViewCategoryName.setText(categoryName);
            btnBackToCategory.setText("Back to "+categoryName);
        }


        textViewCategoryName.setOnClickListener(new BackNavigationListener());

        //////Iterate through order list to check if current item is already present in order list
        ////// if it is found, set the price and quantity according to that of order
        if(!OrderContainer.getInstance().getOrderableItems().isEmpty()){

            ////iterating through order list
            for(Item eachItem: OrderContainer.getInstance().getOrderableItems() ){

                ////if current item is found in order list
                if(eachItem.getId()== item.getId()){

                    ////set its price & quantity
                    pricePicker.setValue(eachItem.getDesiredQuantity());
                    itemQuantity = eachItem.getDesiredQuantity();
                    setQuantityView(itemQuantity);

                }
            }
        }

        ////if not present in order, set the default price & quantity
        else{
            pricePicker.setValue(1);
            itemQuantity = 1;
            setQuantityView(itemQuantity);
        }

        ////set Total price View according to quantity
        textViewTotalPrice.setText(String.valueOf(getPrice(itemQuantity, item.getPrice())));
    }

    private void setQuantityView(int itemQuantity) {
        textViewQuantity.setText(Integer.toString(itemQuantity) + " " + getString(R.string.items));
    }



    @Override
    public void onQuantityValueChange(int oldVal, int newVal) {

        textViewTotalPrice.setText(String.valueOf(getPrice(newVal, item.getPrice())));
        itemQuantity = newVal;
        setQuantityView(itemQuantity);

    }

    private double getPrice(int newVal, double price) {
        return newVal*price;
    }


    public void addCart(View view){

        Order order = new Order(item,itemQuantity);

        boolean update = false;

        if(! OrderContainer.getInstance().getOrderableItems() .isEmpty()){

            for(Item iterator : OrderContainer.getInstance().getOrderableItems() ){

                if(iterator.getId()== order.getItem().getId()){ // Item already exists in order

                    iterator.setDesiredQuantity(itemQuantity);  //intention of increasing quantity
                    if(quantityChangeListener != null){
                        quantityChangeListener.onQuantityChanged();
                    }
                    update = true;
                }
            }
            if(!update){
                onItemAddedToOrder.onItemAdded(position,itemQuantity);
            }
        }
        else{
              onItemAddedToOrder.onItemAdded(position,itemQuantity);
        }
        this.finish();
    }

    private Uri getImageUri(Item item){

        //Image file is saved in following pattern
        //filepath = ItemName+ImageFileCreationDateTime
        File cacheDir = this.getCacheDir();
        Uri uri = null;
        if(!item.getImages().isEmpty()){
            String filePath = item.getName()+item.getImages().get(0).getCreated_at(); // placing first image of item
            File imageFile = new File(cacheDir, filePath);
            uri= Uri.fromFile(imageFile);
        }
        return uri;
    }

    private void showImages(String filePath){

        cacheFile = new File(cacheDir, filePath);
        uri = Uri.fromFile(cacheFile);

        try {
              fileInputStream = new FileInputStream(cacheFile);

            } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        itemImage.setImageBitmap(BitmapFactory.decodeStream(fileInputStream));
    }




    private class GalleryClickListener implements AdapterView.OnItemClickListener {

        View res;

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            viewPager.setCurrentItem(position, true);

           if(oldSelectedPosition != position) {
               if(oldSelectedPosition != -1){

                   item_imageArrayList.get(oldSelectedPosition).setSelected(false);
               }
               item_imageArrayList.get(position).setSelected(true);
               verticalGalleryAdapter.notifyDataSetChanged();
           }
              oldSelectedPosition=position;

            verticalGalleryAdapter.notifyDataSetChanged();
        }
    }


    private class SwipeImageListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int i, float v, int i1) {}

        @Override
        public void onPageSelected(int position) {

            if(oldSelectedPosition != position) {
                if(oldSelectedPosition != -1){

                    item_imageArrayList.get(oldSelectedPosition).setSelected(false);
                }
                item_imageArrayList.get(position).setSelected(true);
                verticalGalleryAdapter.notifyDataSetChanged();
            }
            oldSelectedPosition=position;

            verticalGalleryAdapter.notifyDataSetChanged();

        }

        @Override
        public void onPageScrollStateChanged(int i) {}

    }

    private class QuantityIncrementListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            initialQuantity=itemQuantity;
            itemQuantity ++;

            newQuantity = itemQuantity;

            quantityPicker.onQuantityValueChange(initialQuantity,newQuantity);
        }
    }

    private class QuantityDecrementListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            if(itemQuantity > 1 && itemQuantity!=0){
                initialQuantity = itemQuantity;

                itemQuantity--;
                newQuantity = itemQuantity;

                quantityPicker.onQuantityValueChange(initialQuantity,newQuantity);
            }
        }
    }

    private class BackButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            finish();
        }
    }

    private class BackNavigationListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            OrderDialogScreen.this.finish();
        }
    }

    private class ViewOrderFromDetailListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {


            OrderDialogScreen.this.finish();
//            Intent intent = new Intent(OrderDialogScreen.this,MainActivity.class);
//            startActivity(intent);
            orderButtonListener.onOrderButtonClick();


        }
    }
}