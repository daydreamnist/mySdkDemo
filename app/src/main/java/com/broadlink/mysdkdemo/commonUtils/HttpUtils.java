package com.broadlink.mysdkdemo.commonUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.broadlink.mysdkdemo.activity.LoginActivity;
import com.broadlink.mysdkdemo.activity.MainActivity;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {

    public static final String MEDIA_TYPE_JSON = "application/json; charset=utf-8";


    Callback callback;

    public static void postJSON(final Context context, final String url, final Map<String,String> headers, final String bodyStr, final Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                MediaType JSON = MediaType.parse(MEDIA_TYPE_JSON);
                RequestBody body = RequestBody.create(JSON, bodyStr);
                SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.SHARE_PREFERENCE, Context.MODE_PRIVATE);
                String loginSession = sharedPreferences.getString(LoginActivity.LOGIN_SESSION, "");
                Request.Builder requestBuilder = new Request.Builder()
                        .url(url)
                        .post(body);

                addHeaders(requestBuilder,headers);
                Request request = requestBuilder.build();
                client.newCall(request).enqueue(callback);
            }
            
        }).start();
    }

    public static Response postJSON(final Context context, final String url, final Map<String,String> headers, final String bodyStr) throws IOException {

        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse(MEDIA_TYPE_JSON);
        RequestBody body = RequestBody.create(JSON, bodyStr);
        SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.SHARE_PREFERENCE, Context.MODE_PRIVATE);
        String loginSession = sharedPreferences.getString(LoginActivity.LOGIN_SESSION, "");
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(body);
        addHeaders(requestBuilder,headers);
        Request request = requestBuilder.build();
        return  client.newCall(request).execute();
    }

    private static void addHeaders(Request.Builder requestBuilder, Map<String, String> headers) {
        if (headers.isEmpty()){
            return;
        }
        for (String s:headers.keySet()) {
            requestBuilder.addHeader(s,headers.get(s));
        }
    }


}
