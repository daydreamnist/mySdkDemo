package com.broadlink.mysdkdemo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.broadlink.mysdkdemo.R;
import com.broadlink.mysdkdemo.commonUtils.OnItemClickListener;
import com.broadlink.mysdkdemo.commonUtils.SimpleRecyclerAdapter;

import java.util.List;

import cn.com.broadlink.family.params.BLFamilyAllInfo;
import cn.com.broadlink.family.params.BLFamilyDeviceInfo;
import cn.com.broadlink.sdk.data.controller.BLDNADevice;

public class FamilyDeviceListActivity extends AppCompatActivity implements OnItemClickListener {

    private BLFamilyAllInfo blFamilyAllInfo;
    private RecyclerView mRv_familyDeviceList;
    private List<BLFamilyDeviceInfo> blFamilyDeviceInfos;
    private SimpleRecyclerAdapter simpleRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_device_list);

        initFamilyDeviceData();
        mRv_familyDeviceList = findViewById(R.id.mRv_familyDeviceList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FamilyDeviceListActivity.this);
        mRv_familyDeviceList.setLayoutManager(linearLayoutManager);
        simpleRecyclerAdapter = new SimpleRecyclerAdapter(blFamilyDeviceInfos,R.layout.view_common_itme,R.id.mTv_commonItem);
        simpleRecyclerAdapter.setOnItemClickListener(this);
        mRv_familyDeviceList.setAdapter(simpleRecyclerAdapter);




    }

    private void initFamilyDeviceData() {
        blFamilyAllInfo = JSON.parseObject(getIntent().getStringExtra(FamilyOperationActivity.FAMILY_ALL_INFO),BLFamilyAllInfo.class);
        if (blFamilyAllInfo==null){
            Toast.makeText(FamilyDeviceListActivity.this,"familyAllInfo init fail",Toast.LENGTH_LONG).show();
            return;
        }
        blFamilyDeviceInfos = blFamilyAllInfo.getDeviceInfos();
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onItemLongClick(View view, final int position) {
        AlertDialog alertDialog = new AlertDialog.Builder(FamilyDeviceListActivity.this)
                .setItems(new String[]{"delete device from home","control device"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BLFamilyDeviceInfo deviceInfo = (BLFamilyDeviceInfo) simpleRecyclerAdapter.getDatas().get(position);
                        if (which == 0){
                            deleteDevice(deviceInfo);
                        }else if (which == 1){
                            BLDNADevice device = new BLDNADevice();
                            device.setPid(deviceInfo.getPid());
                            device.setDid(deviceInfo.getDid());
                            device.setKey(deviceInfo.getAeskey());
                            device.setMac(deviceInfo.getMac());
                            device.setId(deviceInfo.getTerminalId());
                            device.setName(deviceInfo.getName());
                            device.setType(deviceInfo.getType());
                            device.setExtend(deviceInfo.getExtend());
                            device.setState(1);
                            Intent intent = new Intent();
                            intent.setClass(FamilyDeviceListActivity.this,WebControlActivity.class);
                            intent.putExtra("device",device);
                            startActivity(intent);
                        }
                    }
                })
                .create();
        alertDialog.show();
    }

    private void deleteDevice(BLFamilyDeviceInfo o) {
    }
}
