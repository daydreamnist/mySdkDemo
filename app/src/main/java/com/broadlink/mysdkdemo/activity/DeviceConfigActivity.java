package com.broadlink.mysdkdemo.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.broadlink.mysdkdemo.R;


import cn.com.broadlink.sdk.BLLet;
import cn.com.broadlink.sdk.param.controller.BLDeviceConfigParam;
import cn.com.broadlink.sdk.result.controller.BLDeviceConfigResult;

public class DeviceConfigActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private int ConfigVersion = 2;

    private ProgressBar mPb_deviceConfig;
    private EditText mEt_wifi_ssid;
    private EditText mEt_wifi_passwd;
    private RadioGroup mRg_versions;
    private RadioButton mRb_v2;
    private RadioButton mRb_v3;
    private Button mBtn_deviceConfigAction;
    private TextView mTv_configResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_config);

        findViews();

        setLinseners();
    }

    private void setLinseners() {
        mRg_versions.setOnCheckedChangeListener(this);
        mBtn_deviceConfigAction.setOnClickListener(this);
    }

    private void findViews() {
        mPb_deviceConfig = findViewById(R.id.mPb_deviceConfig);
        mEt_wifi_ssid = findViewById(R.id.mEt_ssId);
        mEt_wifi_passwd = findViewById(R.id.mEt_WF_passwd);
        mRg_versions = findViewById(R.id.mRg_versions);
        mRb_v2 = findViewById(R.id.mRbtn_version2);
        mRb_v3 = findViewById(R.id.mRbtn_version3);
        mBtn_deviceConfigAction = findViewById(R.id.mBtn_deviceConfigAction);
        mTv_configResult = findViewById(R.id.mTv_DeviceConfigResult);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (group.getCheckedRadioButtonId()){
            case R.id.mRbtn_version2:
                ConfigVersion = 2;
                break;
            case R.id.mRbtn_version3:
                ConfigVersion = 3;
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mBtn_deviceConfigAction:
                deviceConfig();
        }
    }

    private void deviceConfig() {

        String ssid = mEt_wifi_ssid.getText().toString();
        String passwd = mEt_wifi_passwd.getText().toString();
        BLDeviceConfigParam blDeviceConfigParam = new BLDeviceConfigParam();
        blDeviceConfigParam.setSsid(ssid);
        blDeviceConfigParam.setPassword(passwd);
        blDeviceConfigParam.setVersion(ConfigVersion);

        new DeviceConfigTask().execute(new BLDeviceConfigParam[]{blDeviceConfigParam});
        Toast.makeText(this,"device config begin ",Toast.LENGTH_SHORT).show();
    }


    private class DeviceConfigTask extends AsyncTask<BLDeviceConfigParam,Void,BLDeviceConfigResult>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mBtn_deviceConfigAction.setClickable(false);
            mPb_deviceConfig.setVisibility(View.VISIBLE);
            mTv_configResult.setText("");

        }

        @Override
        protected BLDeviceConfigResult doInBackground(BLDeviceConfigParam... blDeviceConfigParams) {
            return BLLet.Controller.deviceConfig(blDeviceConfigParams[0]);
        }

        @Override
        protected void onPostExecute(BLDeviceConfigResult blDeviceConfigResult) {
            super.onPostExecute(blDeviceConfigResult);
            mBtn_deviceConfigAction.setClickable(true);
            mPb_deviceConfig.setVisibility(View.GONE);
            mTv_configResult.setVisibility(View.VISIBLE);
            mTv_configResult.setText(JSON.toJSONString(blDeviceConfigResult,true));
        }
    }

}
