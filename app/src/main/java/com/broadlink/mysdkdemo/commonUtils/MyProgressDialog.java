package com.broadlink.mysdkdemo.commonUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

public class MyProgressDialog extends AlertDialog.Builder {

    ProgressBar progressBar;
    Context context;
    String title;

    public MyProgressDialog(Context context,String title) {
        super(context);
        this.title = title;
        this.context = context;
    }

    public AlertDialog.Builder getInstance(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        progressBar = new ProgressBar(context);
        progressBar.setIndeterminate(true);
        builder.setView(progressBar);
        builder.setTitle(title);
        return builder;
    }


}
