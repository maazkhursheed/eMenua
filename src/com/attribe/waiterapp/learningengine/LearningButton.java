package com.attribe.waiterapp.learningengine;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Sabih Ahmed on 17-Feb-16.
 */
public class LearningButton extends Button{
    private int learningStatus = LearningStatus.STATUS_DEFAULT;

    public LearningButton(Context context) {
        super(context);
    }

    public LearningButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LearningButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LearningButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setLearningStatus(int learningStatus) {
        this.learningStatus = learningStatus;
    }

    public int getLearningStatus() {
        return learningStatus;
    }
}
