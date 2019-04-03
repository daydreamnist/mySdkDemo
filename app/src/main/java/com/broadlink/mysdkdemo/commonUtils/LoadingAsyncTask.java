package com.broadlink.mysdkdemo.commonUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import cn.com.broadlink.base.BLBaseResult;

public abstract class LoadingAsyncTask<T extends BLBaseResult> extends AsyncTask<String,Void,T> {

    public LoadingAsyncTask(Context mContext, String title) {
        this.mContext = mContext;
        this.title = title;
    }

    AlertDialog alertDialog;
    Context mContext;
    String title;
    public abstract void onPre();
    public abstract T run(String... strings);
    public abstract void onPost(T t);
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        alertDialog = new MyProgressDialog(mContext,title)
                .getInstance()
                .create();
        alertDialog.show();
        onPre();

    }

    @Override
    protected T doInBackground(String... strings) {
        return run(strings);
    }

    @Override
    protected void onPostExecute(T t) {
        super.onPostExecute(t);
        onPost(t);
        Toast.makeText(mContext,t.succeed()?"operate success":"operate fail,"+t.getMsg(),Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();

    }
}
