package com.attribe.waiterapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.attribe.waiterapp.screens.ExceptionView;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Sabih Ahmed on 5/21/2015.
 */
public class ExceptionHanlder implements java.lang.Thread.UncaughtExceptionHandler{

    private final Context context;
    private String LINE_SEPARATOR = "\n";

    public ExceptionHanlder(Context context){

        this.context = context;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace=new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));

        StringBuilder errorReport=new StringBuilder();
        errorReport.append("**********************CAUSE***********************");
        errorReport.append(stackTrace.toString());

        Intent intent=new Intent(context,ExceptionView.class);
        intent.putExtra("errorReport", errorReport.toString());
        context.startActivity(intent);

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }
}
