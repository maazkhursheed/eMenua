package com.attribe.waiterapp.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import com.attribe.waiterapp.R;
import com.attribe.waiterapp.learningengine.LearningButton;
import com.attribe.waiterapp.learningengine.LearningStatus;
import com.attribe.waiterapp.learningengine.UXLearning;

/**
 * Created by Sabih Ahmed on 17-Feb-16.
 */
public class AnimationUtils {

    private static final int SPECIFIED_POSITION = 0;

    public static void setAnimation(Context context, View viewToAnimate, boolean learningRequired){
             // If the bound view wasn't previously displayed on screen, it's animated
            Animation animation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.shake);

            if(learningRequired){
                viewToAnimate.startAnimation(animation);
            }

            else{
                viewToAnimate.clearAnimation();
            }

        }



    public static void setAnimation(Context context, View viewToAnimate, int position, boolean isGridItemClicked){
        // If the bound view wasn't previously displayed on screen, it's animated
        int lastPosition = -1;

        if (position > lastPosition) {
            Animation animation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.shake);
            animation.setRepeatCount(Animation.INFINITE);

            if(!isGridItemClicked && position==SPECIFIED_POSITION){
                viewToAnimate.startAnimation(animation);
            }

            else{
                viewToAnimate.clearAnimation();
            }


            lastPosition = position;
        }

    }

}
