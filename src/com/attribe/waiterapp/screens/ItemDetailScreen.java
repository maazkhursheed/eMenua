package com.attribe.waiterapp.screens;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.attribe.waiterapp.Database.DatabaseHelper;
import com.attribe.waiterapp.Print.PrintUtils;
import com.attribe.waiterapp.R;
import com.attribe.waiterapp.adapters.ImageAdapter;
import com.attribe.waiterapp.adapters.ItemAdapter;
import com.attribe.waiterapp.adapters.SwipeImageAdapter;
import com.attribe.waiterapp.interfaces.QuantityPicker;
import com.attribe.waiterapp.learningengine.LearningStatus;
import com.attribe.waiterapp.models.Image;
import com.attribe.waiterapp.models.Item;
import com.attribe.waiterapp.utils.AnimationUtils;
import com.attribe.waiterapp.utils.CurrencyHelper;
import com.attribe.waiterapp.utils.OrderContainer;
import it.sephiroth.android.library.widget.HListView;

import java.util.List;

/** This is embedded Detail Fragment
 *  for 10 inch tab
 */
public class ItemDetailScreen extends Fragment implements QuantityPicker{

    @Bind(R.id.item_detail_price) TextView labelItemPrice;
    @Bind(R.id.dialog_order_itemName) TextView labelItemName;
    @Bind(R.id.item_detail_desc) TextView labelItemDesc;
    @Bind(R.id.pager) ViewPager itemViewSlider;
    @Bind(R.id.back_to_category) Button  btnBackToCategory;
    @Bind(R.id.imageGalleryViewList) HListView horizontalImageList;
    @Bind(R.id.buttonDecrement) Button buttonDecrement;
    @Bind(R.id.buttonIncrement) Button buttonIncrement;
    @Bind(R.id.textViewQuantity) TextView labelItemQuantity;

    private Item item;
    public DatabaseHelper databaseHelper;
    private List<Image> item_imageArrayList;
    private ImageAdapter imageAdapter;
    private SwipeImageAdapter swipeImageAdapter;
    private int oldSelectedPosition = -1;       // no item selected by default
    private CategoryScreen.OnCategorySelectListener categorySelectListener;
    private OnActionBarValuesUpdated actionBarListener;
    private OnFragmentInteractionListener mListener;
    private int initialQuantity = 0;
    private  int maxInputLenth = 21;


    public ItemDetailScreen() {
        // Required empty public constructor

    }

    public static ItemDetailScreen getInstance(){
        ItemDetailScreen itemDetailScreen = new ItemDetailScreen();
        return itemDetailScreen;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.item_detail_screen, container, false);

        ButterKnife.bind(this,view);

        if(getArguments() != null){

            initContent();
        }

        return view;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void initContent() {
        databaseHelper = new DatabaseHelper(getActivity());
        item = (Item)getArguments().getSerializable("Item");

        setQuantityInUI(initialQuantity);
        setDataForItem();
        setItemImages();
        setPricePickers();

    }

    /** updates UI of Quantity
     *
     * @param desiredQuantity
     */
    private void setQuantityInUI(int desiredQuantity) {

        if(OrderContainer.getInstance().containsItem(item.getId())){

            initialQuantity = OrderContainer.getInstance().getItemQuantity(item.getId());
            labelItemQuantity.setText(String.valueOf(initialQuantity));

        }

        else{
            labelItemQuantity.setText(String.valueOf(initialQuantity));
        }



    }

    private void setPricePickers() {
        AnimationUtils.setAnimation(getActivity(),buttonIncrement,0,LearningStatus.isAddOrderButtonClicked());
        buttonIncrement.setOnClickListener(new IncrementPriceListener());
        buttonDecrement.setOnClickListener(new DecrementPriceListener());
    }

    private void setItemImages() {
        //set Item images in this method, both main image and horizontal list

        swipeImageAdapter = new SwipeImageAdapter(getActivity(),item.getImages(),item.getName(),item.getCreated_at());
        itemViewSlider.setAdapter(swipeImageAdapter);

        itemViewSlider.setOnPageChangeListener(new SwipeImageListener());

        //populating horizontal list of item's images
        item_imageArrayList = item.getImages();
        imageAdapter =new ImageAdapter(getActivity(),item_imageArrayList,item.getName(),item.getCreated_at());

        horizontalImageList.setAdapter(imageAdapter);

        horizontalImageList.setOnItemClickListener(new GalleryClickListener());
    }

    private void setDataForItem() {
        //this method sets Item name, Item price ,item desc & Item Category Name in Back Button
        int new_max_inputLength = 30;
        String itemName = PrintUtils.addLinebreaks(item.getName(),new_max_inputLength);
        if(itemName.length()>21){
            labelItemName.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
            labelItemName.setText(itemName);
        }
        else{
            labelItemName.setText(PrintUtils.addLinebreaks(item.getName(),maxInputLenth));
        }
        labelItemPrice.setText(String.valueOf(item.getPrice()));
        labelItemDesc.setText(item.getDescription());

        String categoryName = databaseHelper.getCategoryName(item.getCategory_id());
        btnBackToCategory.setText("Back to "+categoryName);
        btnBackToCategory.setOnClickListener(new BackNavigationListener());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
            categorySelectListener = (CategoryScreen.OnCategorySelectListener )context;
            actionBarListener = (OnActionBarValuesUpdated)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onQuantityValueChange(int oldVal, int newVal) {

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
                imageAdapter.notifyDataSetChanged();
            }
            oldSelectedPosition=position;

        }

        @Override
        public void onPageScrollStateChanged(int i) {}

    }

    private class GalleryClickListener implements it.sephiroth.android.library.widget.AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(it.sephiroth.android.library.widget.AdapterView<?> adapterView, View view, int position, long l) {

            itemViewSlider.setCurrentItem(position, true);

            if(oldSelectedPosition != position) {
                if(oldSelectedPosition != -1){

                    item_imageArrayList.get(oldSelectedPosition).setSelected(false);
                }
                item_imageArrayList.get(position).setSelected(true);
                imageAdapter.notifyDataSetChanged();
            }
            oldSelectedPosition=position;

            imageAdapter.notifyDataSetChanged();

        }
    }

    private class BackNavigationListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            getActivity().getSupportFragmentManager().popBackStackImmediate();
            //categorySelectListener.onCategorySelected(item.getCategory_id());
        }
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private class IncrementPriceListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {


            if(initialQuantity>=0){
                initialQuantity++;

                //set incremented price in price view
                labelItemQuantity.setText(String.valueOf(initialQuantity));

                OrderContainer.getInstance().addOrUpdateOrder(item,initialQuantity);

                actionBarListener.onActionBarValuesUpdate();
                LearningStatus.setIsAddOrderButtonClicked(true);

                AnimationUtils.setAnimation(ItemDetailScreen.this.getActivity(),buttonIncrement,false);
            }

        }


    }

    private class DecrementPriceListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {


            if(initialQuantity > 0){

                initialQuantity--;
                labelItemQuantity.setText(String.valueOf(initialQuantity));
                OrderContainer.getInstance().addOrUpdateOrder(item,initialQuantity);
                actionBarListener.onActionBarValuesUpdate();

            }

        }
    }

    public interface OnActionBarValuesUpdated{

        void onActionBarValuesUpdate();
    }
}
