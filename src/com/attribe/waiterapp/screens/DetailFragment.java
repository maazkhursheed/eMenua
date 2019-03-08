package com.attribe.waiterapp.screens;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.attribe.waiterapp.R;
import com.attribe.waiterapp.models.Item;
import com.attribe.waiterapp.utils.Constants;

/**
 * Created by Sabih Ahmed on 5/13/2015.
 */
public class DetailFragment extends Fragment implements android.support.v4.app.FragmentManager.OnBackStackChangedListener,View.OnClickListener{

    TextView itemName,itemPrice,totalPrice,itemDescription;
    Item item;
    Button orderButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getFragmentManager().addOnBackStackChangedListener(this);

    }

    private void initContents(View view) {
        itemName =(TextView)view.findViewById(R.id.detail_fragment_itemName);
        itemDescription = (TextView) view.findViewById(R.id.detail_fragment_itemDesc);
        itemPrice= (TextView) view.findViewById(R.id.detail_fragment_itemPrice);
        orderButton = (Button) view.findViewById(R.id.detail_fragment_itemOrderButton);
        orderButton.setOnClickListener(this);
        initValues();
    }

    private void initValues() {
        itemName.setText(item.getName());
        itemDescription.setText(item.getDescription());
        itemPrice.setText(String.valueOf(item.getPrice()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        item = (Item) getArguments().getSerializable(Constants.KEY_SERIALIZEABLE_ITEM_OBJECT);
        initContents(view);

        return view;
    }

    public static DetailFragment getInstance(){
        DetailFragment fragment = new DetailFragment();

        return fragment;
    }


    @Override
    public void onBackStackChanged() {


    }



    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), OrderDialogScreen.class);
        intent.putExtra(Constants.KEY_SERIALIZEABLE_ITEM_OBJECT, item);
        startActivity(intent);
    }
}
