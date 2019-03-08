package com.attribe.waiterapp.screens;

import android.app.ActionBar;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.attribe.waiterapp.Database.Constants;
import com.attribe.waiterapp.R;
import android.support.v4.app.FragmentActivity;
import com.attribe.waiterapp.interfaces.OrderButtonListener;
import com.attribe.waiterapp.models.Item;
import com.attribe.waiterapp.models.Order;
import com.attribe.waiterapp.utils.*;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainActivity extends FragmentActivity implements CategoryScreen.OnCategorySelectListener,
        FragmentManager.OnBackStackChangedListener,
        OrderButtonListener,
        ItemDetailScreen.OnFragmentInteractionListener,
        ItemDetailScreen.OnActionBarValuesUpdated ,
        OrderFragment.OnActionBarValuesUpdated{

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private CategoryItemScreen dynamicFragment;
    private ArrayList<Item> itemArrayList;
    private CopyOnWriteArrayList<Order> orderList;
    private ActionBar actionBar;
    private Button showOrderButton;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting Action bar's color programmatically
        actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar_color)));

        setCustomActionBar(actionBar);
        setActionBarValues();

        //Handling Exceptions
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHanlder(this));

        setContentView(R.layout.main);

        // First time when user comes from the carousel view, the code lands here for setting the required category
        CategoryScreen categoryScreen = new CategoryScreen();
        if(getIntent().getExtras() != null){
            categoryScreen.setOnCategorySelecListener(this, (Integer) getIntent().getExtras().get(Constants.EXTRA_CATEGORY_ID));
        }


        OrderDialogScreen orderDialogScreen = new OrderDialogScreen();
        orderDialogScreen.setOrderButtonListener(this);


    }

    public void setCustomActionBar(ActionBar actionBar) {
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_action_bar, null);

        actionBar.setCustomView(mCustomView);

        actionBar.setDisplayShowCustomEnabled(true);

        showOrderButton = (Button) actionBar.getCustomView().findViewById(R.id.ab_button);

        showOrderButton.setOnClickListener(new ShowOrderButtonListener());

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        ImageView appLogo=(ImageView)mCustomView.findViewById(R.id.app_logo);

        appLogo.setOnLongClickListener(new AppLogoListener());




    }

    @Override
    public void onBackPressed() {
        //If Grid of Items is visible, hide that

        TODO://Removed Hardcoded fragment
        /**
        CategoryItemScreen itemFragment = (CategoryItemScreen) getSupportFragmentManager().findFragmentById(R.id.fragment_itemScreen);
        if(itemFragment.getView().getVisibility() == View.VISIBLE){
            itemFragment.getView().setVisibility(View.GONE);
        }**/

        //TODO: REMOVE THIS FOR PRODUCTION BUILD
        this.finish();

    }

    @Override
    public void onCategorySelected(long id) {

        setCustomActionBar(actionBar);
        setActionBarValues();
        Bundle args = new Bundle();
        args.putLong(Constants.EXTRA_CATEGORY_ID,id);
        CategoryItemScreen itemScreen = CategoryItemScreen.getInstance();
        itemScreen.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_items, itemScreen)
                .commit();

    }





    @Override
    public void onBackStackChanged() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.action_cart:
                showOrderFragment();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showOrderFragment() {

        fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_items, OrderFragment.getInstance());
        fragmentTransaction.commit();

    }

    private void showOrderActivity(){
        Intent intent = new Intent(this,OrderScreen.class);
        startActivity(intent);
    }

    private void showOrderScreen(){
        if(DeviceInfo.getScreen()==DeviceInfo.ScreenSize.SCREEN_LARGE){
            //case of 7inch
            // show order screen as external view

            showOrderActivity();

        }

        if(DeviceInfo.getScreen()==DeviceInfo.ScreenSize.SCREEN_XLARGE){
            //case of 10inch
            //show Order screen as embedded fragment

            showOrderFragment();

        }


    }
    @Override
    public void onOrderButtonClick() {
        //this.onCreate(null);
        showOrderScreen();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onActionBarValuesUpdate() {
        setActionBarValues();

    }


    private class ShowOrderButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            //getActionBar().getCustomView().findViewById(R.id.ab_parent).setVisibility(View.GONE);
            LinearLayout amountView = (LinearLayout)getActionBar().getCustomView().findViewById(R.id.ab_amount_view_parent);
            Button viewOrderButton = (Button)getActionBar().getCustomView().findViewById(R.id.ab_button);

            amountView.setVisibility(View.GONE);
            viewOrderButton.setVisibility(View.GONE);
            showOrderScreen();
            AnimationUtils.setAnimation(MainActivity.this, showOrderButton, false);

            AnimationUtils.setAnimation(MainActivity.this,showOrderButton,false);

        }
    }


    private class AppLogoListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View view) {
            NavigationController.showSectionNameDialog(MainActivity.this.getFragmentManager());
            return true;
        }
    }

    public void setActionBarValues(){
        TextView quantityView= (TextView)getActionBar().getCustomView().findViewById(R.id.ab_quantity);
        quantityView.setText(Integer.toString(OrderContainer.getInstance().getTotalItemQuantity()) + " " + "item(s)");

        TextView totalView = (TextView)getActionBar().getCustomView().findViewById(R.id.ab_total);
        String totalAmount = rupeeFormat(Integer.toString((int) (OrderContainer.getInstance().getOrderTotal())));
        totalView.setText(CurrencyHelper.getCurrencyPrefix()+" "+totalAmount);
       // totalView.setText(Integer.toString((int) (OrderContainer.getInstance().getOrderTotal())) + " " + getString(R.string.currency));

        if(!OrderContainer.getInstance().getOrderableItems().isEmpty()){
            AnimationUtils.setAnimation(this,showOrderButton,true);
        }

        else{
            AnimationUtils.setAnimation(this,showOrderButton,false);
        }
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

}
