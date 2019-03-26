package com.broadlink.mysdkdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.broadlink.mysdkdemo.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mBtn_login;
    private Button mBtn_device_config;
    private TextView mTv_loginInfo;
    private Context mContext;
    public static final String SHARE_PREFERENCE = "mSharedPreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtn_login = findViewById(R.id.mBtn_login);
        mBtn_login.setOnClickListener(this);
        mBtn_device_config = findViewById(R.id.mBtn_deviceConfig);
        mBtn_device_config.setOnClickListener(this);

        mTv_loginInfo = findViewById(R.id.mTv_LoginInfo);

        mContext = MainActivity.this;

        initLoginInfo();
    }

    private void initLoginInfo() {
        SharedPreferences mSharePreferences = MainActivity.this.getSharedPreferences(SHARE_PREFERENCE,Context.MODE_PRIVATE);
        String loginInfo = mSharePreferences.getString(LoginActivity.USER_INFO_SP,"NO LOGIN");
        mTv_loginInfo.setText(loginInfo);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mBtn_login:
                startActivity(LoginActivity.class);
                break;
            case R.id.mBtn_deviceConfig:
                startActivity(DeviceConfigActivity.class);
                break;
            case R.id.mBtn_deviceProbe:
                startActivity(DeviceProbeActivity.class);
                break;
        }
    }

    private void startActivity(Class cls) {
        Intent intent = new Intent();
        intent.setClass(mContext,cls);
        startActivity(intent);
    }


}
