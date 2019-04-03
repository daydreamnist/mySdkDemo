package com.broadlink.mysdkdemo.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.broadlink.mysdkdemo.R;
import com.broadlink.mysdkdemo.commonUtils.MyProgressDialog;
import com.broadlink.mysdkdemo.commonUtils.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.com.broadlink.family.BLFamily;
import cn.com.broadlink.family.params.BLFamilyAllInfo;
import cn.com.broadlink.family.params.BLFamilyDeviceInfo;
import cn.com.broadlink.family.params.BLFamilyInfo;
import cn.com.broadlink.family.params.BLFamilyModuleInfo;
import cn.com.broadlink.family.params.BLFamilyRoomInfo;
import cn.com.broadlink.family.result.BLAllFamilyInfoResult;
import cn.com.broadlink.family.result.BLManageRoomResult;
import cn.com.broadlink.sdk.BLLet;
import cn.com.broadlink.sdk.data.controller.BLDNADevice;
import cn.com.broadlink.sdk.interfaces.controller.BLDeviceScanListener;
import cn.com.broadlink.sdk.result.controller.BLPairResult;

public class FamilyOperationActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIv_familyIcon;
    private TextView mTv_familyName;
    private TextView mTv_familyInfo;
    private TextView mTv_roomsManage;
    private TextView mTv_devicesManage;
    private TextView mTv_membersManage;
    private FloatingActionButton mFab_shareFamily;
    private FloatingActionButton mFab_familyInfosAdd;

    private BLFamilyAllInfo blFamilyAllInfo;

    private String familyId;
    private String familyVersion;
    private String familyName;

    private LinearLayout ActivityFamilyOperate;
    private TextInputEditText mEt_familyAddName;
    private TextInputEditText mEt_familyAddCountry;
    private TextInputEditText mEt_familyAddProvince;
    private TextInputEditText mEt_familyAddCity;
    private ProgressBar mPb_familyAdd;
    private Button mBtn_addFamily;

    AlertDialog addAlertDialog;
    HashSet<BLDNADevice> devices;

    public static final String PROBED_DEVICES = "probedDevices";
    public static final String FAMILY_ID =  "familyId";
    public static final String FAMILY_ALL_INFO =  "familyAllInfo";

    Map<String,BLDNADevice> deviceMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_operation);

        findViews();

        initData();


    }

    private void initViewsWithData() {
        if (null == blFamilyAllInfo || null == blFamilyAllInfo.getFamilyInfo() ){
            return;
        }
        BLFamilyInfo blFamilyInfo = blFamilyAllInfo.getFamilyInfo();
        familyId = blFamilyInfo.getFamilyId();
        familyVersion = blFamilyInfo.getFamilyVersion();
        familyName = blFamilyInfo.getFamilyName();
        mTv_familyName.setText(familyName);
        mTv_familyInfo.setText(familyId+"\n"+familyVersion);
    }

    private void findViews() {
        ActivityFamilyOperate = findViewById(R.id.ActivityFamilyOperate);
        mIv_familyIcon = findViewById(R.id.mIv_familyIcon);
        mTv_familyName = findViewById(R.id.mTv_familyName);
        mTv_familyName.setOnClickListener(this);
        mTv_familyInfo = findViewById(R.id.mTv_familyInfo);
        mTv_familyInfo.setOnClickListener(this);
        mTv_roomsManage = findViewById(R.id.mTv_roomsManage);
        mTv_roomsManage.setOnClickListener(this);
        mTv_membersManage = findViewById(R.id.mTv_membersManage);
        mTv_membersManage.setOnClickListener(this);
        mTv_devicesManage = findViewById(R.id.mTv_devicesManage);
        mTv_devicesManage.setOnClickListener(this);
        mFab_shareFamily = findViewById(R.id.mFab_shareFamily);
        mFab_shareFamily.setOnClickListener(this);
        mFab_familyInfosAdd = findViewById(R.id.mFab_familyInfosAdd);
        mFab_familyInfosAdd.setOnClickListener(this);
    }

    private void initData() {

        String familyId = getIntent().getStringExtra(FamilyManageActivity.FAMILY_ID);
        new initDataTask().execute(new String[]{familyId});

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mTv_familyName:
            case R.id.mTv_familyInfo:
                modifyFamilyInfo();
                break;
            case R.id.mTv_roomsManage:
                showRooms();
                break;
            case R.id.mTv_devicesManage:
                showDevices();
                break;
            case R.id.mTv_membersManage:
                break;
            case R.id.mFab_shareFamily:
                shareFamily();
                break;
            case R.id.mFab_familyInfosAdd:
                showItemsAdd();
                break;
            case R.id.mBtn_familyAdd:
                new ManageRoomTask().execute(new String[]{"add",mEt_familyAddName.getText().toString()});
                break;

        }
    }

    private void modifyFamilyInfo() {
    }

    private void shareFamily() {

    }


    //strings[0]:action,string[1]:roomName&roomId
    class ManageRoomTask extends AsyncTask<String,Void, BLManageRoomResult>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mBtn_addFamily.setVisibility(View.GONE);
            mPb_familyAdd.setVisibility(View.VISIBLE);
        }

        @Override
        protected BLManageRoomResult doInBackground(String... strings) {

            List<BLFamilyRoomInfo> roomInfos = new ArrayList<>();
            BLFamilyRoomInfo familyRoomInfo = new BLFamilyRoomInfo();
            if (strings[0].equalsIgnoreCase("add")){
                familyRoomInfo.setName(strings[1]);
            }else if (strings[0].equalsIgnoreCase("delete")){
                familyRoomInfo.setFamilyId(strings[1]);
            }

            familyRoomInfo.setAction(strings[0]);

            roomInfos.add(familyRoomInfo);
            return BLFamily.manageFamilyRooms(familyId,familyVersion,roomInfos);
        }

        @Override
        protected void onPostExecute(BLManageRoomResult blManageRoomResult) {
            super.onPostExecute(blManageRoomResult);
            Toast.makeText(FamilyOperationActivity.this,blManageRoomResult.succeed()?"success":"fail",Toast.LENGTH_SHORT).show();

            Log.d("blManageRoomResult",JSON.toJSONString(blManageRoomResult,true));
            addAlertDialog.dismiss();
            initData();

        }
    }

    private void showItemsAdd() {
        String[] infos = new String[]{"room","device","member"};
        showItemsDialog("add a item", infos, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        addRoom();
                        break;
                    case 1:
                        addDevice();
                        break;
                    case 2:
                        addMember();
                        break;
                }
            }
        });
    }

    private void showDevices() {
        Intent intent = new Intent();
        intent.setClass(FamilyOperationActivity.this,FamilyDeviceListActivity.class);
        intent.putExtra(FAMILY_ALL_INFO,JSON.toJSONString(blFamilyAllInfo));
        startActivity(intent);

    }

    private void addMember() {
        Toast.makeText(FamilyOperationActivity.this,"addMember",Toast.LENGTH_SHORT).show();
    }

    private void addDevice() {
        Toast.makeText(FamilyOperationActivity.this,"addDevice",Toast.LENGTH_SHORT).show();
        showProbedDevices();

    }

    private void showProbedDevices() {
        AlertDialog alertDialog = new MyProgressDialog(FamilyOperationActivity.this,"scan ing").getInstance().create();
        alertDialog.show();
        showDevices(alertDialog);

    }

    private void showDevices(final AlertDialog alertDialog) {
        devices = new HashSet<>();
        BLLet.Controller.setOnDeviceScanListener(new BLDeviceScanListener() {
            @Override
            public void onDeviceUpdate(final BLDNADevice device, boolean isNewDevice) {

                final Thread thread =new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BLPairResult pairResult = BLLet.Controller.pair(device);
                        if (pairResult.succeed()){
                            BLLet.Controller.addDevice(device);
                            device.setKey(pairResult.getKey());
                            device.setId(pairResult.getId());
                            devices.add(device);
                            Looper.prepare();
                            Toast.makeText(FamilyOperationActivity.this,"device pair success",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }else {
                            Looper.prepare();
                            Toast.makeText(FamilyOperationActivity.this,"device pair fail",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                            return;
                        }

                    }
                });
                thread.start();

            }
        });
        BLLet.Controller.startProbe(3000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                alertDialog.dismiss();

                BLLet.Controller.stopProbe();

                String[] strings = new String[]{};
                if (devices.isEmpty()){
                    Looper.prepare();
                    Snackbar.make(ActivityFamilyOperate,"not probe any device",Snackbar.LENGTH_LONG)
                        .setAction("scan again", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showProbedDevices();
                            }
                        }).show();

                    Looper.loop();
                    return;
                }else {
                    List<BLFamilyRoomInfo> roomInfoList = blFamilyAllInfo.getRoomInfos();
                    Intent intent = new Intent();
                    intent.setClass(FamilyOperationActivity.this,DevicesListActivity.class);
                    intent.putParcelableArrayListExtra(PROBED_DEVICES,new ArrayList<Parcelable>(devices));
                    intent.putExtra(FAMILY_ID,familyId);

                    startActivity(intent);

                }
            }
        },4000);
    }


    private void addRoom() {
        Toast.makeText(FamilyOperationActivity.this,"addRoom",Toast.LENGTH_SHORT).show();

        View contentView = getView();

        addAlertDialog= new AlertDialog.Builder(FamilyOperationActivity.this)
                .setTitle("input room name")
                .setView(contentView)
                .create();

        addAlertDialog.show();

    }

    private View getView() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.view_family_add,null,false);
        mBtn_addFamily = contentView.findViewById(R.id.mBtn_familyAdd);
        mEt_familyAddName = contentView.findViewById(R.id.meEt_family_name);
        mEt_familyAddName.setHint("room name");
        mEt_familyAddCountry = contentView.findViewById(R.id.meEt_family_country);
        mEt_familyAddCountry.setVisibility(View.GONE);
        mEt_familyAddProvince = contentView.findViewById(R.id.meEt_family_province);
        mEt_familyAddProvince.setVisibility(View.GONE);
        mEt_familyAddCity = contentView.findViewById(R.id.meEt_family_city);
        mEt_familyAddCity.setVisibility(View.GONE);
        mPb_familyAdd = contentView.findViewById(R.id.mPb_familyAdd);
        mBtn_addFamily.setOnClickListener(FamilyOperationActivity.this);
        return contentView;
    }

    private void showRooms() {

        final List<BLFamilyRoomInfo> roomInfos = blFamilyAllInfo.getRoomInfos();
        final String[] roomNames = new String[roomInfos.size()];
        for (int i = 0 ; i < roomInfos.size(); i++){
            roomNames[i] = roomInfos.get(i).getName()+" "+roomInfos.get(i).getRoomId();
        }
        showItemsDialog("rooms list", roomNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final int index = which;
                AlertDialog alertDialog = new AlertDialog.Builder(FamilyOperationActivity.this)
                        .setTitle("delete room")
                        .setMessage("are you sure to delete room:"+roomNames[which]+"from your home:"+familyName)
                        .setPositiveButton("delete!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("ManageRoomTask",roomInfos.get(index).getFamilyId());
                                new ManageRoomTask().execute("delete",roomInfos.get(index).getFamilyId());
                            }
                        }).create();
                alertDialog.show();
            }
        });
    }

    private void showItemsDialog(String title, String[] items,DialogInterface.OnClickListener listener ){
        AlertDialog alertDialog = new AlertDialog.Builder(FamilyOperationActivity.this)
                .setTitle(title)
                .setItems(items,listener).create();
        alertDialog.show();
    }



    class initDataTask extends AsyncTask<String,Void,BLAllFamilyInfoResult>{

        @Override
        protected BLAllFamilyInfoResult doInBackground(String... objects) {
            return BLFamily.queryAllFamilyInfos(new String[]{objects[0]});
        }

        @Override
        protected void onPostExecute(BLAllFamilyInfoResult blAllFamilyInfoResult) {
            super.onPostExecute(blAllFamilyInfoResult);
            blFamilyAllInfo = blAllFamilyInfoResult.getAllInfos().get(0);
            initViewsWithData();
            Log.d("blFamilyAllInfo", JSON.toJSONString(blFamilyAllInfo,true));
        }
    }
}
