package com.broadlink.mysdkdemo.activity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.broadlink.mysdkdemo.R;
import com.broadlink.mysdkdemo.commonUtils.OnItemClickListener;
import com.broadlink.mysdkdemo.commonUtils.SimpleRecyclerAdapter;

import java.util.List;
import java.util.Map;

import cn.com.broadlink.family.BLFamily;
import cn.com.broadlink.family.params.BLFamilyAllInfo;
import cn.com.broadlink.family.params.BLFamilyRoomInfo;
import cn.com.broadlink.family.result.BLAllFamilyInfoResult;
import cn.com.broadlink.sdk.data.controller.BLDNADevice;

public class DevicesListActivity extends AppCompatActivity implements OnItemClickListener {

    private List<BLDNADevice> devices;
    private RecyclerView mRv_deviceList;
    private SimpleRecyclerAdapter simpleRecyclerAdapter;
    private BLFamilyAllInfo blFamilyAllInfo;
    private BLFamilyRoomInfo selectedRoom;


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
        simpleRecyclerAdapter = new SimpleRecyclerAdapter(devices,R.layout.view_common_itme,R.id.mTv_commonItem);
        simpleRecyclerAdapter.setOnItemClickListener(this);
        mRv_deviceList.setAdapter(simpleRecyclerAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onItemLongClick(View view, int position) {
        selectRoom2Device();
        if (null == selectedRoom){
            return;
        }

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
                        //TODO:run the task
                    }
                })
                .create();
        alertDialog.show();


    }

    class addDevice2Family extends AsyncTask{
        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
        }
    };

    class initRoomsTask extends AsyncTask<String,Void,BLAllFamilyInfoResult> {

        @Override
        protected BLAllFamilyInfoResult doInBackground(String... objects) {
            return BLFamily.queryAllFamilyInfos(new String[]{objects[0]});
        }

        @Override
        protected void onPostExecute(BLAllFamilyInfoResult blAllFamilyInfoResult) {
            super.onPostExecute(blAllFamilyInfoResult);
            blFamilyAllInfo = blAllFamilyInfoResult.getAllInfos().get(0);
            Log.d("blFamilyAllInfo", JSON.toJSONString(blFamilyAllInfo,true));
        }
    }
}
