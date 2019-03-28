package com.broadlink.mysdkdemo.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.ams.common.util.StringUtil;
import com.broadlink.mysdkdemo.R;
import com.broadlink.mysdkdemo.commonUtils.OnItemClickListener;
import com.broadlink.mysdkdemo.commonUtils.SimpleRecyclerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cn.com.broadlink.base.BLConstants;
import cn.com.broadlink.base.BLFileStorageUtils;
import cn.com.broadlink.sdk.BLLet;
import cn.com.broadlink.sdk.data.controller.BLDNADevice;
import cn.com.broadlink.sdk.interfaces.controller.BLDeviceScanListener;
import cn.com.broadlink.sdk.result.controller.BLDownloadScriptResult;
import cn.com.broadlink.sdk.result.controller.BLDownloadUIResult;
import cn.com.broadlink.sdk.result.controller.BLQueryResoureVersionResult;

public class DeviceProbeActivity extends AppCompatActivity implements View.OnClickListener, OnItemClickListener {

    private List<Object> datas;
    private HashSet<BLDNADevice> devices;
    private int probeInterval = 3000;
    private RecyclerView recyclerView;
    private ProgressBar mPb_deviceProbe;
    private EditText mEt_probeInterval;
    private Button mBtn_scan;

    private SimpleRecyclerAdapter simpleRecyclerAdapter;

    public static final String SCRIPT_DOWNLOAD_RESULT = "SCRIPT_DOWNLOAD_RESULT";
    public static final String UI_DOWNLOAD_RESULT = "UI_DOWNLOAD_RESULT";
    private BLDownloadUIResult blDownloadUIResult;
    private BLDownloadScriptResult blDownloadScriptResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_probe);

        mPb_deviceProbe = findViewById(R.id.mPb_deviceProbe);
        mEt_probeInterval = findViewById(R.id.mEt_probeInterval);
        mBtn_scan = findViewById(R.id.mBtn_scan);
        mBtn_scan.setTag("start");
        recyclerView = findViewById(R.id.mRlv_probedDevicesList);


        datas = new ArrayList<>();
        devices = new HashSet<>();

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        simpleRecyclerAdapter = new SimpleRecyclerAdapter(datas,R.layout.view_common_itme,R.id.mTv_commonItem);
        simpleRecyclerAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(simpleRecyclerAdapter);


        mBtn_scan.setOnClickListener(this);


        
    }

    private BLDNADevice buildDevice(int i) {
        BLDNADevice device = new BLDNADevice();
        device.setId(i);
        return device;

    }

    @Override
    public void onClick(View v) {

        switch (v.getTag().toString()){
            case "start":
                mPb_deviceProbe.setVisibility(View.VISIBLE);
                devices.clear();
                for (int i = 0 ; i < 5; i++){
                    devices.add(buildDevice(i));
                }

                simpleRecyclerAdapter.resetList();
                String probeIntervalOfEt = mEt_probeInterval.getText().toString();
                mBtn_scan.setText("stop scan");
                mBtn_scan.setTag("stop");
                if (!StringUtil.isBlank(probeIntervalOfEt)){
                    probeInterval = Integer.valueOf(probeIntervalOfEt);
                }
                setScanListener();
                BLLet.Controller.startProbe(probeInterval);
                break;
            case "stop":
                mPb_deviceProbe.setVisibility(View.GONE);
                mBtn_scan.setText("start scan");
                mBtn_scan.setTag("start");
                BLLet.Controller.stopProbe();
                if (!devices.isEmpty()){
                    addDevices2View();
                }
                break;
        }



    }

    private void addDevices2View() {
        for (BLDNADevice device: devices){
            simpleRecyclerAdapter.addItem(device);
        }
    }


    private void setScanListener() {
        BLLet.Controller.setOnDeviceScanListener(new BLDeviceScanListener() {
            @Override
            public void onDeviceUpdate(BLDNADevice device, boolean isNewDevice) {
                Looper.prepare();
                Toast.makeText(DeviceProbeActivity.this,"find",Toast.LENGTH_SHORT).show();
                devices.add(device);
                Looper.loop();
            }
        });
    }

    @Override
    public void onItemClick(View view,int po) {

    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onItemLongClick(final View view, final int position) {
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.setGravity(Gravity.END);
        popupMenu.getMenuInflater().inflate(R.menu.probed_device_action,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                BLDNADevice device = (BLDNADevice) simpleRecyclerAdapter.getDatas().get(position);

                switch (item.getItemId()){
                    case R.id.add_to_sdk:
                        BLLet.Controller.addDevice(device);
                        Toast.makeText(DeviceProbeActivity.this,"already add to Sdk",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.query_script_version:
                        new QueryScriptVersionTask("query script version").execute(device.getPid());
                        break;
                    case R.id.download_script:
                        new DownloadScriptTask("download script").execute(device.getPid());
                        break;
                    case R.id.query_ui_version:
                        new QueryScriptVersionTask("query UI version").execute(device.getPid());
                        break;
                    case R.id.download_ui:
                        new DownloadUITask("download UI").execute(device.getPid());
                        break;
                    case R.id.web_control:
                        if (! new File(BLLet.Controller.queryUIPath(device.getPid())).exists()
                                || !new File(BLLet.Controller.queryScriptPath(device.getPid())).exists()){
                            AlertDialog alertDialog = new AlertDialog.Builder(DeviceProbeActivity.this)
                                    .setMessage("please download script and UI first")
                                    .create();
                            alertDialog.show();
                            break;
                        }

                        Intent intent = new Intent();
                        intent.setClass(DeviceProbeActivity.this,WebControlActivity.class);
                        intent.putExtra(SCRIPT_DOWNLOAD_RESULT,blDownloadScriptResult);
                        intent.putExtra(UI_DOWNLOAD_RESULT,blDownloadUIResult);
                        intent.putExtra("device",device);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        popupMenu.show();
    }

    AlertDialog showLoadingDialog(String title){
        AlertDialog alertDialog = new AlertDialog.Builder(DeviceProbeActivity.this)
                .setTitle(title)
                .setView(new ProgressBar(DeviceProbeActivity.this))
                .create();
        alertDialog.show();
        return alertDialog;
    }

    void showResultDialog(String result,String title){
        AlertDialog alertDialog = new AlertDialog.Builder(DeviceProbeActivity.this)
                .setTitle(title)
                .setMessage(result)
                .create();
        alertDialog.show();
    }

    class QueryUIVersionTask extends AsyncTask<String,Void, BLQueryResoureVersionResult>{

        AlertDialog loadingDialog;
        String title;

        public QueryUIVersionTask(String title) {
            this.title = title;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = showLoadingDialog(title);
        }

        @Override
        protected BLQueryResoureVersionResult doInBackground(String... strings) {
            return BLLet.Controller.queryUIVersion(strings[0]);
        }

        @Override
        protected void onPostExecute(BLQueryResoureVersionResult o) {
            super.onPostExecute(o);
            loadingDialog.dismiss();
            showResultDialog(JSONObject.toJSONString(o,true),title);
        }
    }

    class DownloadUITask extends AsyncTask<String,Void, BLDownloadUIResult>{

        AlertDialog loadingDialog;
        String title;

        public DownloadUITask(String title) {
            this.title = title;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = showLoadingDialog(title);
        }

        @Override
        protected BLDownloadUIResult doInBackground(String... strings) {
            return BLLet.Controller.downloadUI(strings[0]);
        }

        @Override
        protected void onPostExecute(BLDownloadUIResult o) {
            super.onPostExecute(o);
            loadingDialog.dismiss();
            showResultDialog(JSONObject.toJSONString(o,true),title);
            blDownloadUIResult = o;
        }
    }

    class DownloadScriptTask extends AsyncTask<String,Void, BLDownloadScriptResult>{

        AlertDialog loadingDialog;
        String title;

        public DownloadScriptTask(String title) {
            this.title = title;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = showLoadingDialog(title);
        }

        @Override
        protected BLDownloadScriptResult doInBackground(String... strings) {
            return BLLet.Controller.downloadScript(strings[0]);
        }

        @Override
        protected void onPostExecute(BLDownloadScriptResult o) {
            super.onPostExecute(o);
            loadingDialog.dismiss();
            showResultDialog(JSONObject.toJSONString(o,true),title);
            blDownloadScriptResult = o;
        }
    }



    class QueryScriptVersionTask extends AsyncTask<String,Void, BLQueryResoureVersionResult>{

        AlertDialog loadingDialog;
        String title;

        public QueryScriptVersionTask(String title) {
            this.title = title;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = showLoadingDialog(title);
        }

        @Override
        protected BLQueryResoureVersionResult doInBackground(String... strings) {
            return BLLet.Controller.queryScriptVersion(strings[0]);
        }

        @Override
        protected void onPostExecute(BLQueryResoureVersionResult o) {
            super.onPostExecute(o);
            loadingDialog.dismiss();
            showResultDialog(JSONObject.toJSONString(o,true),title);
        }
    }


    public class MyDialog extends AlertDialog {
        LayoutInflater inflater;
        View view;
        ProgressBar progressBar;
        TextView mTv_title;
        TextView mTv_content;
        AlertDialog.Builder builder;
        MyDialog myDialog;
        Context context;

        protected MyDialog(Context context) {
            super(context);
            context = this.context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.view_common_popup_window,null);
            progressBar = view.findViewById(R.id.mPb_popup);
            mTv_title = view.findViewById(R.id.mTv_popup_title);
            mTv_content = view.findViewById(R.id.mTv_popup_content);
            builder = new AlertDialog.Builder(context);
            builder.setView(view);
        }


        AlertDialog.Builder getBuilder(){
            return builder;
        }

        MyDialog setTitle (String title){
            progressBar.setVisibility(View.GONE);
            mTv_title.setVisibility(View.VISIBLE);
            mTv_title.setText(title);
            return this;
        }

        MyDialog setOnLoading (){
            progressBar.setVisibility(View.VISIBLE);
            return this;
        }
    }


}
