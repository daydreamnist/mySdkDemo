package com.broadlink.mysdkdemo.activity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.broadlink.mysdkdemo.R;
import com.broadlink.mysdkdemo.commonUtils.MyProgressDialog;
import com.broadlink.mysdkdemo.commonUtils.OnItemClickListener;
import com.broadlink.mysdkdemo.commonUtils.SimpleRecyclerAdapter;

import java.util.List;
import java.util.Map;

import cn.com.broadlink.family.BLFamily;
import cn.com.broadlink.family.params.BLFamilyAllInfo;
import cn.com.broadlink.family.params.BLFamilyDeviceInfo;
import cn.com.broadlink.family.params.BLFamilyInfo;
import cn.com.broadlink.family.params.BLFamilyModuleInfo;
import cn.com.broadlink.family.params.BLFamilyRoomInfo;
import cn.com.broadlink.family.result.BLAllFamilyInfoResult;
import cn.com.broadlink.family.result.BLModuleControlResult;
import cn.com.broadlink.sdk.data.controller.BLDNADevice;

public class DevicesListActivity extends AppCompatActivity implements OnItemClickListener {

    private List<BLDNADevice> devices;
    private RecyclerView mRv_deviceList;
    private SimpleRecyclerAdapter simpleRecyclerAdapter;
    private BLFamilyAllInfo blFamilyAllInfo;
    private BLFamilyRoomInfo selectedRoom;
    private BLDNADevice selectedDevice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_list);


        devices = getIntent().getParcelableArrayListExtra(FamilyOperationActivity.PROBED_DEVICES);
        new initRoomsTask().execute(getIntent().getStringExtra(FamilyOperationActivity.FAMILY_ID));
//        if (devices.isEmpty()){
//            AlertDialog alertDialog = new AlertDialog.Builder(DevicesListActivity.this)
//                    .setMessage()
//        }

        mRv_deviceList = findViewById(R.id.mRv_devicesList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DevicesListActivity.this);
        mRv_deviceList.setLayoutManager(linearLayoutManager);
        simpleRecyclerAdapter = new SimpleRecyclerAdapter(devices,R.layout.view_common_itme,R.id.mTv_commonItem);
        simpleRecyclerAdapter.setOnItemClickListener(this);
        mRv_deviceList.setAdapter(simpleRecyclerAdapter);
    }

    @Override
    public void onItemClick(View view, final int position) {
        AlertDialog alertDialog = new AlertDialog.Builder(DevicesListActivity.this)
                .setTitle("add device")
                .setMessage("add device: "+devices.get(position).getName()+" into family: "+blFamilyAllInfo.getFamilyInfo().getFamilyName())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedDevice = (BLDNADevice) simpleRecyclerAdapter.getDatas().get(position);
                        selectRoom2Device();
                    }
                })
                .create();
        alertDialog.show();
    }

    @Override
    public void onItemLongClick(View view, int position) {





    }

    private void selectRoom2Device() {
        final List<BLFamilyRoomInfo> roomInfos = blFamilyAllInfo.getRoomInfos();
        String[] roomItems = new String[roomInfos.size()];
        int i = 0;

        for (BLFamilyRoomInfo r:roomInfos){
            roomItems[i++] = r.getName()+" "+r.getRoomId();
        }
        AlertDialog alertDialog = new AlertDialog.Builder(DevicesListActivity.this)
                .setTitle("select a room for device")
                .setItems(roomItems,new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedRoom = roomInfos.get(which);
                        BLFamilyModuleInfo moduleInfo = new BLFamilyModuleInfo();
                        moduleInfo.setFamilyId(blFamilyAllInfo.getFamilyInfo().getFamilyId());
                        moduleInfo.setRoomId(selectedRoom.getRoomId());
                        BLFamilyDeviceInfo deviceInfo = new BLFamilyDeviceInfo();
                        deviceInfo.setFamilyId(blFamilyAllInfo.getFamilyInfo().getFamilyId());
                        deviceInfo.setRoomId(selectedRoom.getRoomId());
                        deviceInfo.setPid(selectedDevice.getPid());
                        deviceInfo.setDid(selectedDevice.getDid());
                        deviceInfo.setAeskey(selectedDevice.getKey());
                        deviceInfo.setType(selectedDevice.getType());
                        deviceInfo.setMac(selectedDevice.getMac());
                        deviceInfo.setTerminalId(selectedDevice.getId());
                        deviceInfo.setName(selectedDevice.getName());
                        deviceInfo.setExtend(selectedDevice.getExtend());
                        deviceInfo.setLock(selectedDevice.isLock());

                        new AddDevice2FamilyTask().execute(moduleInfo,blFamilyAllInfo.getFamilyInfo(),deviceInfo,null);
                        //TODO:run the task
                    }
                })
                .create();
        alertDialog.show();


    }

    public class AddDevice2FamilyTask extends AsyncTask<Object,Void, BLModuleControlResult>{

        @Override
        protected BLModuleControlResult doInBackground(Object... objects) {

            return BLFamily.addModuleToFamily((BLFamilyModuleInfo)objects[0],(BLFamilyInfo)objects[1],(BLFamilyDeviceInfo)objects[2],(BLFamilyDeviceInfo)objects[3]);
        }

        @Override
        protected void onPostExecute(BLModuleControlResult o) {
            super.onPostExecute(o);
            Log.d("BLModuleControlResult",JSON.toJSONString(o));
            Toast.makeText(DevicesListActivity.this,JSON.toJSONString(o,true),Toast.LENGTH_SHORT).show();
            if (o.succeed()){

            }
        }
    }

    class initRoomsTask extends AsyncTask<String,Void,BLAllFamilyInfoResult> {
        android.app.AlertDialog alertDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = new MyProgressDialog(DevicesListActivity.this,"init familyInfo").getInstance().create();

        }

        @Override
        protected BLAllFamilyInfoResult doInBackground(String... objects) {
            return BLFamily.queryAllFamilyInfos(new String[]{objects[0]});
        }

        @Override
        protected void onPostExecute(BLAllFamilyInfoResult blAllFamilyInfoResult) {
            super.onPostExecute(blAllFamilyInfoResult);

            blFamilyAllInfo = blAllFamilyInfoResult.getAllInfos().get(0);
            Log.d("blFamilyAllInfo", JSON.toJSONString(blFamilyAllInfo,true));
            alertDialog.dismiss();
        }
    }
}
