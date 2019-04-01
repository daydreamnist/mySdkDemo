package com.broadlink.mysdkdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.broadlink.mysdkdemo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import cn.com.broadlink.account.BLAccount;
import cn.com.broadlink.base.BLLoginResult;
import cn.com.broadlink.sdk.BLLet;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mBtn_login;
    private Button mBtn_device_config;
    private Button mBtn_device_probe;
    private Button mBtn_family_manage;
    private TextView mTv_loginInfo;
    private Context mContext;
    public static final String SHARE_PREFERENCE = "mSharedPreference";

    private String accountId;
    private String loginSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtn_login = findViewById(R.id.mBtn_login);
        mBtn_login.setOnClickListener(this);
        mBtn_device_config = findViewById(R.id.mBtn_deviceConfig);
        mBtn_device_config.setOnClickListener(this);
        mBtn_device_probe = findViewById(R.id.mBtn_deviceProbe);
        mBtn_device_probe.setOnClickListener(this);
        mBtn_family_manage = findViewById(R.id.mBtn_familyManage);
        mBtn_family_manage.setOnClickListener(this);
        mTv_loginInfo = findViewById(R.id.mTv_LoginInfo);
        mContext = MainActivity.this;

        initLoginInfo();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                copyAssertJSToAPP();
                toActivity();
            }


        },1000);
    }

    private void copyAssertJSToAPP() {
        String trgPath = BLLet.Controller.queryUIPath() + File.separator + "cordova.js";
        String srcPath = "js/cordova.js";
        copyAssertToSDCard(MainActivity.this, srcPath , trgPath);

    }

    private void copyAssertToSDCard(Context context, String srcPath, String trgPath) {
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;

        try{
            File file = new File(trgPath);

            if (!file.exists()){
                inputStream = context.getResources().getAssets().open(srcPath);
                fileOutputStream = new FileOutputStream(trgPath);
                byte[] bytes = new byte[1024];
                int count = 0;
                while ((count = inputStream.read(bytes))>0){
                    fileOutputStream.write(bytes,0,count);
                }

                return ;


            }

        }catch (Exception e){

        }finally {
            if (inputStream != null){
                try{
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                inputStream = null;
            }

            if (fileOutputStream != null){
                try{
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fileOutputStream = null;
            }
        }
    }

    private void toActivity() {
        if (TextUtils.isEmpty(accountId) || TextUtils.isEmpty(loginSession)){
            return;
        }

        BLLoginResult loginResult = new BLLoginResult();
        loginResult.setUserid(accountId);
        loginResult.setLoginsession(loginSession);

        BLLoginResult result = BLAccount.localLogin(loginResult);
        if (result != null && result.succeed()){
            Looper.prepare();
            Toast.makeText(MainActivity.this,"local login succeed",Toast.LENGTH_SHORT).show();
            Looper.loop();
        }


    }
    private void initLoginInfo() {

        SharedPreferences mSharePreferences = MainActivity.this.getSharedPreferences(SHARE_PREFERENCE,Context.MODE_PRIVATE);
        String loginInfo = mSharePreferences.getString(LoginActivity.USER_INFO_SP,"NO LOGIN");
        accountId = mSharePreferences.getString(LoginActivity.USER_ID,"");
        loginSession = mSharePreferences.getString(LoginActivity.LOGIN_SESSION,"");

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
            case R.id.mBtn_familyManage:
                startActivity(FamilyManageActivity.class);

        }
    }

    private void startActivity(Class cls) {
        Intent intent = new Intent();
        intent.setClass(mContext,cls);
        startActivity(intent);
    }


}
