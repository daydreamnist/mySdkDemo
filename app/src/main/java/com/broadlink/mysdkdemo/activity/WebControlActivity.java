package com.broadlink.mysdkdemo.activity;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.alibaba.fastjson.JSON;
import com.broadlink.mysdkdemo.R;

import org.apache.cordova.ConfigXmlParser;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaInterfaceImpl;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewImpl;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewEngine;

import java.io.File;

import cn.com.broadlink.base.BLCommonTools;
import cn.com.broadlink.base.BLFileStorageUtils;
import cn.com.broadlink.sdk.data.controller.BLDNADevice;
import cn.com.broadlink.sdk.result.controller.BLDownloadScriptResult;
import cn.com.broadlink.sdk.result.controller.BLDownloadUIResult;

public class WebControlActivity extends AppCompatActivity {


    private SystemWebView systemWebView;
    private CordovaWebView cordovaWebView;

    public BLDNADevice device;
    private BLDownloadUIResult blDownloadUIResult;
    private BLDownloadScriptResult blDownloadScriptResult;
    private ConstraintLayout mContentWebLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_control);

        systemWebView = findViewById(R.id.mWv_device_control);

        initData();

        initView();
    }

    private void initView() {

        mContentWebLayout = findViewById(R.id.content_web_layout);
        CordovaInterface cordovaInterface = new CordovaInterfaceImpl(WebControlActivity.this){

            @Override
            public Object onMessage(String id, Object data) {
                Log.d("WebControlActivity", "id-"+id);
                Log.d("WebControlActivity", "data-"+data);

                return super.onMessage(id, data);
            }
        };

        ConfigXmlParser parser = new ConfigXmlParser();
        parser.parse(this);
        systemWebView.getSettings().setJavaScriptEnabled(true);
        cordovaWebView = new CordovaWebViewImpl(new SystemWebViewEngine(systemWebView));
        cordovaWebView.init(cordovaInterface,parser.getPluginEntries(),parser.getPreferences());

        String loadUrl ="file:///"+ BLFileStorageUtils.getDefaultUIPath(device.getPid()
                + File.separator + BLCommonTools.getLanguage())
                +File.separator + "app.html";

        cordovaWebView.loadUrl(loadUrl);

    }

    private void initData() {
        blDownloadUIResult = getIntent().getParcelableExtra(DeviceProbeActivity.UI_DOWNLOAD_RESULT);
        blDownloadScriptResult =  getIntent().getParcelableExtra(DeviceProbeActivity.SCRIPT_DOWNLOAD_RESULT);
        device = getIntent().getParcelableExtra("device");
        Log.d("WebControlActivity", JSON.toJSONString(blDownloadScriptResult));
        Log.d("WebControlActivity", JSON.toJSONString(blDownloadUIResult));
        Log.d("WebControlActivity", JSON.toJSONString(device));

    }
    @Override
    protected void onPause() {
        super.onPause();
        systemWebView.onPause();
        if (cordovaWebView != null) {
            cordovaWebView.handlePause(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        systemWebView.onResume();
        if (cordovaWebView != null) {
            cordovaWebView.handleResume(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (cordovaWebView != null) {
            cordovaWebView.handleDestroy();
        }

        mContentWebLayout.removeView(systemWebView);
        systemWebView.removeAllViews();
        systemWebView.destroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (cordovaWebView != null) {
            cordovaWebView.handleStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (cordovaWebView != null) {
            cordovaWebView.handleStop();
        }
    }


}
