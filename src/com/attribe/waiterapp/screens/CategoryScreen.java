package com.attribe.waiterapp.screens;


import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LauncherActivity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.attribe.waiterapp.Database.DatabaseHelper;
import com.attribe.waiterapp.R;
import com.attribe.waiterapp.adapters.CategoryAdapter;
import com.attribe.waiterapp.adapters.PromotionAreaAdapter;
import com.attribe.waiterapp.models.Category;
import com.attribe.waiterapp.network.RestClient;
import com.attribe.waiterapp.utils.DevicePreferences;
import com.attribe.waiterapp.utils.ExceptionHanlder;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.ArrayList;

/**
 * Created by Sabih Ahmed on 5/12/2015.
 */

public class CategoryScreen extends Fragment implements AdapterView.OnItemClickListener{

    private static final float RIGHT_VOLUME = 0;
    private static final float LEFT_VOLUME = 0;
    OnCategorySelectListener callBack;
    private CategoryAdapter listAdapter;
    private ArrayList<Category> categoryArrayList;
    private ListView listView;
    private ViewPager viewPager;
    private int oldPosition = - 1;
    private SwipeTimer timer;
    private int position;
    private Button btnFeedBack;
    private LinearLayout frame_feedBack;

    public CategoryScreen(){

    }

    public void setOnCategorySelecListener(OnCategorySelectListener callBack, int id){

        this.callBack=callBack;
        callBack.onCategorySelected(id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHanlder(getActivity()));

        categoryArrayList = new ArrayList<>();

        DatabaseHelper mDatabaseHelper = new DatabaseHelper(getActivity());
        categoryArrayList= mDatabaseHelper.getAllCategories();

        int size = categoryArrayList.size() -1;
        ArrayList<Category> sortedCategoryList = new ArrayList<>();


        for(int i=size; i>=0 ;i--){

            sortedCategoryList.add(categoryArrayList.get(i));

        }


        categoryArrayList = sortedCategoryList;
        listAdapter = new CategoryAdapter(getActivity(), categoryArrayList);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category_screen, container,false);
        listView=(ListView)view.findViewById(R.id.catrgoryScreen_list);
        listView.setOnItemClickListener(this);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(listAdapter);

//        viewPager = (ViewPager)view.findViewById(R.id.promo_area_pager);
//        viewPager.setAdapter(new PromotionAreaAdapter(getActivity()));
//
//        position = 0;
//        timer = new SwipeTimer(3000,3000);

        initVideo(view);



        return view;
    }

    private void initVideo(View view) {
       final VideoView videoView = (VideoView)view.findViewById(R.id.videoView);

        if(videoView !=null){

            String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.restaurant_vdo;

            videoView.setVideoURI(Uri.parse(path));
            videoView.start();


            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    mp.setVolume(RIGHT_VOLUME, LEFT_VOLUME);
                    mp.setLooping(false);
                }
            });

            videoView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (videoView.isPlaying()) {
                        videoView.pause();
                    } else {
                        videoView.start();
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callBack = (OnCategorySelectListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    +"must implement onCategorySelect Listener");
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        if(oldPosition != position){
            if(oldPosition !=-1){
                categoryArrayList.get(oldPosition).setSelected(false);
            }

            categoryArrayList.get(position).setSelected(true);
            listAdapter.notifyDataSetChanged();
        }

        oldPosition=position;

        callBack.onCategorySelected(id);
    }


    public class SwipeTimer extends CountDownTimer {


        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public SwipeTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {


            viewPager.setCurrentItem(position);
            viewPager.setAnimationCacheEnabled(true);
            position++;
            timer.start();


        }
    }
    public interface OnCategorySelectListener{

        public void onCategorySelected(long id);
    }


}