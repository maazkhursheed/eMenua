package com.attribe.waiterapp.screens;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.attribe.waiterapp.Database.Constants;
import com.attribe.waiterapp.Database.DatabaseHelper;
import com.attribe.waiterapp.Print.PrintUtils;
import com.attribe.waiterapp.R;
import com.attribe.waiterapp.adapters.SwipeImageAdapter;
import com.attribe.waiterapp.interfaces.OnActionBarValuesUpdated;
import com.attribe.waiterapp.learningengine.LearningStatus;
import com.attribe.waiterapp.models.Item;
import com.attribe.waiterapp.utils.AnimationUtils;
import com.attribe.waiterapp.utils.CurrencyHelper;
import com.attribe.waiterapp.utils.NavigationController;
import com.attribe.waiterapp.utils.OrderContainer;

import static com.attribe.waiterapp.screens.MainActivity.rupeeFormat;


/** This is external Detail Screen
 *  which opens up as a activity
 *  for 7 inch Tab
 */
public class DetailScreen extends FragmentActivity implements OnActionBarValuesUpdated{

    private static final int MAX_LENGTH_7_INCH = 20;
    TextView itemName,itemPrice,totalPrice,itemDescription;
    Item item;
    Button orderButton;
    ViewPager itemViewSlider;
    @Bind(R.id.buttonIncrement) Button buttonIncrement;
    @Bind(R.id.buttonDecrement) Button buttonDecrement;
    @Bind(R.id.textViewQuantity) TextView labelItemQuantity;
    @Bind(R.id.back_to_category) Button  btnBackToCategory;
    private SwipeImageAdapter swipeImageAdapter;
    private int initialQuantity = 0;
    public DatabaseHelper databaseHelper;
    private DetailScreen actionBarListener;
    private ActionBar actionBar;
    private Button showOrderButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_screen);

        setCustomActionBar();
        initContents();
    }



    //===================================================Methods========================================================================
    private void initContents() {

        ButterKnife.bind(this);
        setActionBarValues();
        item = (Item)getIntent().getSerializableExtra("Item");
        itemName =(TextView) findViewById(R.id.dialog_order_itemName);
        itemDescription = (TextView) findViewById(R.id.item_detail_desc);
        itemPrice= (TextView) findViewById(R.id.item_detail_price);
        itemViewSlider = (ViewPager)findViewById(R.id.pager);
        actionBarListener = this;
        setQuantityInUI(initialQuantity);
        initValues();
    }

    private void setQuantityInUI(int desiredQuantity) {
        if(OrderContainer.getInstance().containsItem(item.getId())){

            initialQuantity = OrderContainer.getInstance().getItemQuantity(item.getId());
            labelItemQuantity.setText(String.valueOf(initialQuantity));

        }

        else{
            labelItemQuantity.setText(String.valueOf(desiredQuantity));
        }
    }

    private void initValues() {

        databaseHelper = new DatabaseHelper(this);
        itemName.setText(PrintUtils.addLinebreaks(item.getName(),MAX_LENGTH_7_INCH));
        itemDescription.setText(item.getDescription());
        itemPrice.setText(String.valueOf(item.getPrice()));

        swipeImageAdapter = new SwipeImageAdapter(getApplicationContext(),item.getImages(),item.getName(),item.getCreated_at());
        itemViewSlider.setAdapter(swipeImageAdapter);

        String categoryName = databaseHelper.getCategoryName(item.getCategory_id());
        btnBackToCategory.setText("Back to "+categoryName);
        btnBackToCategory.setOnClickListener(new BackNavigationListener());

        initQuantityModifiers();

    }

    private void initQuantityModifiers() {

        buttonIncrement.setOnClickListener(new IncrementButtonListener());
        buttonDecrement.setOnClickListener(new DecrementButtonListener());
    }


    private void setCustomActionBar() {

        //Setting Action bar's color programmatically
        actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bumzees_primary_red)));

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

    private void setActionBarValues() {
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

//**********************************************End Methods*******************************************************************************


//=============================================Overridden methods==================================================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_detail_screen, menu);
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
    public void onActionBarValuesUpdate(){
        setActionBarValues();
    }



//**********************************************End Overridden Methods**************************************************************


//==============================================Inner classes=======================================================================
    private class IncrementButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            if(initialQuantity >=0){
                //TODO:violating DRY from Item detail screen, have to refactor this
                initialQuantity++;

                //set incremented price in price view
                labelItemQuantity.setText(String.valueOf(initialQuantity));

                OrderContainer.getInstance().addOrUpdateOrder(item,initialQuantity);

                actionBarListener.onActionBarValuesUpdate();
                LearningStatus.setIsAddOrderButtonClicked(true);

                AnimationUtils.setAnimation(DetailScreen.this, buttonIncrement, false);
            }
        }

    }


    private class DecrementButtonListener implements View.OnClickListener {

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

    private class BackNavigationListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(DetailScreen.this, MainActivity.class);
            intent.putExtra(Constants.EXTRA_CATEGORY_ID, item.getCategory_id());
            startActivity(intent);
            DetailScreen.this.finish();
        }
    }

    private class ShowOrderButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //show Order Screen
            Intent intent = new Intent(DetailScreen.this, OrderScreen.class);
            startActivity(intent);
            DetailScreen.this.finish();
        }
    }

    private class AppLogoListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View view) {
            NavigationController.showSectionNameDialog(DetailScreen.this.getFragmentManager());
            return true;
        }
    }
}
