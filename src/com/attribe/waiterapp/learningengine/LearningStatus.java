package com.attribe.waiterapp.learningengine;

/**
 * Created by Sabih Ahmed on 17-Feb-16.
 */
public class LearningStatus {

    public static int STATUS_DEFAULT = 0;
    public static int STATUS_LEARNED = 1;


    public static boolean isGridIemClicked;
    public static boolean isAddOrderButtonClicked;
    public static boolean isViewOrderButtonClicked;


    public static boolean isGridIemClicked() {
        return isGridIemClicked;
    }

    public static void setIsGridIemClicked(boolean isGridIemClicked) {
        LearningStatus.isGridIemClicked = isGridIemClicked;
    }

    public static boolean isAddOrderButtonClicked() {
        return isAddOrderButtonClicked;
    }

    public static void setIsAddOrderButtonClicked(boolean isAddOrderButtonClicked) {
        LearningStatus.isAddOrderButtonClicked = isAddOrderButtonClicked;
    }

    public static boolean isViewOrderButtonClicked() {
        return isViewOrderButtonClicked;
    }

    public static void setIsViewOrderButtonClicked(boolean isViewOrderButtonClicked) {
        LearningStatus.isViewOrderButtonClicked = isViewOrderButtonClicked;
    }

    public static void resetLearning() {
        LearningStatus.isGridIemClicked = false;
        LearningStatus.isAddOrderButtonClicked = false;
    }
}

