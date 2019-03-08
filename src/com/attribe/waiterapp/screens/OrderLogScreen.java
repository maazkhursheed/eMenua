package com.attribe.waiterapp.screens;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import com.attribe.waiterapp.Database.DatabaseHelper;
import com.attribe.waiterapp.Database.OrderService;
import com.attribe.waiterapp.R;
import com.attribe.waiterapp.adapters.ExpandableOrderListAdapter;
import com.attribe.waiterapp.learningengine.LearningStatus;
import com.attribe.waiterapp.services.OrderUploadService;
import com.attribe.waiterapp.utils.OrderContainer;

/**
 * Created by Maaz on 13-April-16.
 */
public class OrderLogScreen extends Activity {

    private Button backToListButton;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_log_screen);

        view = View.inflate(this,com.attribe.waiterapp.R.layout.fragment_order,null);

        ExpandableListView listView = (ExpandableListView) findViewById(R.id.expOrderLogList);
        ExpandableOrderListAdapter adapter = new ExpandableOrderListAdapter(this,
                                             new OrderService(getApplicationContext()).getAllDbOrders());
        listView.setAdapter(adapter);

        backToListButton = (Button) findViewById(R.id.backButton);
        backToListButton.setOnClickListener(new BacktoListListener());

        startUploadService();
    }

    private void startUploadService() {
        Intent mServiceIntent = new Intent(getApplicationContext(), OrderUploadService.class);
        mServiceIntent.setData(Uri.parse("uploadService"));
        startService(mServiceIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order_log_screen, menu);
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

    private class BacktoListListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            OrderLogScreen.this.finish();
            hideOrderFragment();
            OrderContainer.getInstance().getOrderableItems().clear();
            LearningStatus.resetLearning();
        }
    }

    private void hideOrderFragment() {
        view.findViewById(R.id.fragment_order_thankyouFrame).setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.findViewById(R.id.fragment_order_parent).setVisibility(View.GONE);

            }
        }, 6000);
    }
}