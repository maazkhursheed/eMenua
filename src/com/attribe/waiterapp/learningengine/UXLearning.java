package com.attribe.waiterapp.learningengine;

import android.view.View;
import android.widget.Button;

/**
 * Created by Sabih Ahmed on 17-Feb-16.
 */
public class UXLearning {

    public static int learningStatus=LearningStatus.STATUS_DEFAULT;

    public int learningStatusOrderButton;



    public static int getLearningStatus() {
        return learningStatus;
    }

    public static void setLearningStatus(int learningStatus) {


        UXLearning.learningStatus = learningStatus;
    }



}
