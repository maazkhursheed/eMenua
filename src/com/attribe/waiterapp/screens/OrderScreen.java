package com.attribe.waiterapp.screens;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.attribe.waiterapp.R;
import com.attribe.waiterapp.adapters.OrderListAdapter;
import com.attribe.waiterapp.interfaces.OnItemRemoveListener;
import com.attribe.waiterapp.interfaces.OnQuantityChangeListener;
import com.attribe.waiterapp.interfaces.PrintOrder;
import com.attribe.waiterapp.utils.CurrencyHelper;
import com.attribe.waiterapp.utils.NavigationController;
import com.attribe.waiterapp.utils.OrderContainer;

import static com.attribe.waiterapp.screens.MainActivity.rupeeFormat;


public class OrderScreen extends FragmentActivity implements OnQuantityChangeListener, OnItemRemoveListener,
        PrintOrder{

    @Bind(R.id.fragment_order_textTotal)TextView totalText;
    @Bind(R.id.fragment_order_textTotalPrice) TextView totalPrice;
    @Bind(R.id.grid_TotalItemsQuantity) TextView totalItemsQuantity;
    @Bind(R.id.fragment_order_confirmButton) Button confirmButton;
    @Bind(R.id.itemOrderList) RecyclerView orderlist;
    @Bind(R.id.fragment_order_amountLayout) LinearLayout amountLayout;
    private LinearLayoutManager mLayoutmanager;
    private OrderListAdapter orderListAdapter;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order);
        ButterKnife.bind(this);

        initContents();
    }

    private void initContents() {

        setCustomActionBar();
        mLayoutmanager = new LinearLayoutManager(this);
        orderlist.setLayoutManager(mLayoutmanager);

        orderListAdapter = new OrderListAdapter(this, OrderContainer.getInstance().getOrderableItems());
        orderlist.setAdapter(orderListAdapter);
        orderListAdapter.setQuantityChangeListener(this);
        orderListAdapter.setOnItemRemoveListener(this);

        setTotalsInUI();
        setConfirmButtonListener();
//        OrderTakingScreen.setOrderPrintInterface(this);
        TableReferenceFragment.setOrderPrintInterface(this);

    }

    private void setConfirmButtonListener() {
        confirmButton.setOnClickListener(new ConfirmOrderListener());
    }

    private void setCustomActionBar() {
        actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bumzees_primary_red)));

        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.action_bar_order, null);

        actionBar.setCustomView(mCustomView);

        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        Button backToMenu = (Button)actionBar.getCustomView().findViewById(R.id.ab_back);

        backToMenu.setOnClickListener(new BackToMenuListener());

        ImageView appLogo=(ImageView)mCustomView.findViewById(R.id.app_logo);
        appLogo.setOnLongClickListener(new AppLogoListener());

    }

    /**This method sets Price and Quantity in UI
     *
     */
    private void setTotalsInUI() {
        String totalAmount = rupeeFormat(Integer.toString((int)(OrderContainer.getInstance().getOrderTotal())));
        totalPrice.setText(CurrencyHelper.getCurrencyPrefix() + " " + totalAmount);

        totalItemsQuantity.setText(Integer.toString((OrderContainer.getInstance().getTotalItemQuantity())) + " " + "item(s)");
    }

    private void showOrderPage() {
        Intent intent = new Intent(OrderScreen.this,OrderTakingScreen.class);
        startActivity(intent);

    }

    private void hideOrderFragment() {
        findViewById(R.id.fragment_order_thankyouFrame).setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.fragment_order_parent).setVisibility(View.GONE);

            }
        }, 6000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_order_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onQuantityChanged() {
        setTotalsInUI();
    }

    @Override
    public void onItemRemoved() {
        setTotalsInUI();
    }

    @Override
    public void onOrderSentToPrint() {
        hideOrderFragment();
        OrderContainer.getInstance().setTableNumber("");
        OrderContainer.getInstance().getOrderableItems().clear();
    }



    private class BackToMenuListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(OrderScreen.this, MainActivity.class);
            startActivity(intent);
            OrderScreen.this.finish();
        }
    }

    private class ConfirmOrderListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if(OrderContainer.getInstance().getOrderableItems().isEmpty()){
                Toast.makeText(OrderScreen.this, getString(R.string.select_items_prompt), Toast.LENGTH_SHORT).show();
            }
            else{

                NavigationController.showOrderScreen(OrderScreen.this);

            }
        }
    }


    private class AppLogoListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View view) {
            NavigationController.showSectionNameDialog(OrderScreen.this.getFragmentManager());
            return true;
        }
    }
}
