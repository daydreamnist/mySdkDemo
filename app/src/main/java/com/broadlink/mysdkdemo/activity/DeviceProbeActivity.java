package com.broadlink.mysdkdemo.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.ams.common.util.StringUtil;
import com.broadlink.mysdkdemo.R;
import com.broadlink.mysdkdemo.commonUtils.OnItemClickListener;
import com.broadlink.mysdkdemo.commonUtils.SimpleRecyclerAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cn.com.broadlink.sdk.BLLet;
import cn.com.broadlink.sdk.data.controller.BLDNADevice;
import cn.com.broadlink.sdk.interfaces.controller.BLDeviceScanListener;

public class DeviceProbeActivity extends AppCompatActivity implements View.OnClickListener, OnItemClickListener {

    private List<Object> datas;
    private HashSet<BLDNADevice> devices;
    private int probeInterval = 3000;
    private RecyclerView recyclerView;
    private ProgressBar mPb_deviceProbe;
    private EditText mEt_probeInterval;
    private Button mBtn_scan;

    private SimpleRecyclerAdapter simpleRecyclerAdapter;

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

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null){
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),0);
                }
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

                setSanListener();
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


    private void setSanListener() {
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

    @Override
    public void onItemLongClick(final View view, final int position) {
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.getMenuInflater().inflate(R.menu.probed_device_action,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.add_to_sdk:
                        BLDNADevice device = (BLDNADevice) simpleRecyclerAdapter.getDatas().get(position);
                        Toast.makeText(DeviceProbeActivity.this,device.getDid(),Toast.LENGTH_SHORT).show();

                        BLLet.Controller.addDevice(device);
                }
                return true;
            }
        });

        popupMenu.show();
    }
}
