package com.attribe.waiterapp.screens;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.*;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.attribe.waiterapp.Database.DatabaseHelper;
import com.attribe.waiterapp.R;
import com.attribe.waiterapp.adapters.ItemAdapter;
import com.attribe.waiterapp.interfaces.OnInstantOrder;
import com.attribe.waiterapp.interfaces.OnItemAddedToOrder;
import com.attribe.waiterapp.learningengine.LearningStatus;
import com.attribe.waiterapp.models.Item;
import com.attribe.waiterapp.utils.DeviceInfo;
import com.attribe.waiterapp.utils.OrderContainer;
import com.attribe.waiterapp.utils.ResizingHelper;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Created by Sabih Ahmed on 5/12/2015.
 */

public class CategoryItemScreen extends Fragment implements
        FragmentManager.OnBackStackChangedListener,
        OnItemAddedToOrder,
        OnInstantOrder,
        ItemAdapter.MenuItemListener{


    private static final String TAG = CategoryItemScreen.class.getSimpleName();
    private View listView;
    private GridView gridView;
    Fragment detailFragment;
    private OrderFragment orderFragment;
    private ArrayList<Item> itemArrayList;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private CopyOnWriteArrayList<Item> orderList;
    private static Activity mActivity;
    private static View view;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;
    Runnable runView ;


    public CategoryItemScreen(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_catergory_items, container, false);

        itemArrayList = new ArrayList<Item>();
        gridLayoutManager = new GridLayoutManager(getActivity(), ResizingHelper.getGridColumnCount());

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview_item_grid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);

        ItemAdapter adapter = new ItemAdapter(getActivity(),itemArrayList,
                                              LearningStatus.isGridIemClicked());
        recyclerView.setAdapter(adapter);
//        adapter.notifyItemRangeChanged(0,adapter.getItemCount());

        OrderDialogScreen orderDialogScreen = new OrderDialogScreen();
        orderDialogScreen.setOnItemAddedToOrderListener(this);

        updateFragment(getArguments().getLong(com.attribe.waiterapp.Database.Constants.EXTRA_CATEGORY_ID));

        View actionBarLayout = getActivity().getActionBar().getCustomView().findViewById(R.id.ab_parent);

        if(actionBarLayout.getVisibility() == View.GONE){
            actionBarLayout.setVisibility(View.VISIBLE);

            setActionBarValues();

        }

        return view;
    }



    public void updateFragment(long id){

        if(getActivity() != null){
            DatabaseHelper mDatabaseHelper=new DatabaseHelper(getActivity());
            itemArrayList = mDatabaseHelper.getItemsWithImages(id);
            orderList = OrderContainer.getInstance().getOrderableItems();
            if(! orderList.isEmpty()){

                for(int i = 0 ; i<orderList.size(); i++){
                    if(orderList.get(i).getCategory_id()== id){
                        for(int x = 0; x < itemArrayList.size() ; x++){

                            if(itemArrayList.get(x).getId() == orderList.get(i).getId()){
                                itemArrayList.get(x).setSelected(true);
                                continue;
                            }


                        }


                    }


                }


            }

//            CategoryItemAdapter adapter = new CategoryItemAdapter(getActivity(), itemArrayList);
//            adapter.setInstantOrderListener(this);

            ItemAdapter adapter = new ItemAdapter(getActivity(),itemArrayList, LearningStatus.isGridIemClicked());
            adapter.setInstantOrderListener(this);
            adapter.setMenuItemListener(this);
            if(detailFragment!=null){
                if(detailFragment.getView()!=null){
                    detailFragment.getView().setVisibility(View.GONE);
                }
            }

//            gridView.setAdapter(adapter);
//            gridView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);


        }

    }



    @Override
    public void onBackStackChanged() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;
    }



    @Override
    public void onItemAdded (int position,int itemQuantity) {

        itemArrayList.get(position).setSelected(true);
        itemArrayList.get(position).setDesiredQuantity(itemQuantity);
    }

    @Override
    public void onItemAddedInstantly() {
        setActionBarValues();

    }

    private void setActionBarValues(){
        TextView quantityView= (TextView)getActivity().getActionBar().getCustomView().findViewById(R.id.ab_quantity);
        quantityView.setText(Integer.toString(OrderContainer.getInstance().getTotalItemQuantity()) + " " + "item(s)");


        TextView totalView = (TextView)getActivity().getActionBar().getCustomView().findViewById(R.id.ab_total);
        String totalAmount = rupeeFormat(Integer.toString((int) (OrderContainer.getInstance().getOrderTotal())));
        totalView.setText(totalAmount + " " + getString(R.string.currency));
      //  totalView.setText(Integer.toString((int) (OrderContainer.getInstance().getOrderTotal())) + " " + getString(R.string.currency));
    }
    public static CategoryItemScreen getInstance() {

        CategoryItemScreen categoryItemScreen = new CategoryItemScreen();

        return categoryItemScreen;

    }

    public static String rupeeFormat(String value){
        value=value.replace(",","");
        char lastDigit=value.charAt(value.length()-1);
        String result = "";
        int len = value.length()-1;
        int nDigits = 0;

        for (int i = len - 1; i >= 0; i--)
        {
            result = value.charAt(i) + result;
            nDigits++;
            if (((nDigits % 2) == 0) && (i > 0))
            {
                result = "," + result;
            }
        }
        return (result+lastDigit);
    }


    @Override
    public void OnMenuItemClick(Item item) {

        showDetailScreen(item);


    }

    private void showDetailScreen(Item item) {

        if(DeviceInfo.getScreen() == DeviceInfo.ScreenSize.SCREEN_LARGE){
            // 7 inch
            showExternalDetail(item);


        }

        if(DeviceInfo.getScreen() == DeviceInfo.ScreenSize.SCREEN_XLARGE){
            // 10inch
            showEmbeddedDetail(item);
        }
    }

    private void showEmbeddedDetail(Item item){
        ItemDetailScreen detailScreen = ItemDetailScreen.getInstance();
        fragmentManager = getFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Item",item);
        detailScreen.setArguments(bundle);

        fragmentTransaction.addToBackStack("itemList");
        fragmentTransaction.replace(R.id.fragment_container_items, detailScreen);
        fragmentTransaction.commit();
    }

    private void showExternalDetail(Item item){

        Intent intent = new Intent(getActivity(),DetailScreen.class);
        intent.putExtra("Item",item);
        startActivity(intent);
        getActivity().finish();


    }


}