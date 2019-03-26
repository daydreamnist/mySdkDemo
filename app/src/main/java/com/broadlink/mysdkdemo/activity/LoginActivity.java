package com.broadlink.mysdkdemo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.broadlink.mysdkdemo.R;
import com.broadlink.mysdkdemo.commonUtils.UserInfo;

import cn.com.broadlink.account.BLAccount;
import cn.com.broadlink.base.BLLoginResult;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    // UI references.
    private EditText mPhoneView;
    private EditText mPasswordView;
    private Button mBtn_login;
    private ProgressBar mPbar_login;
    private SharedPreferences mSharedPreferences;

    public static final String USER_INFO_SP = "userInfo";
    public static final String USER_PHONE_SP = "userPhone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSharedPreferences = LoginActivity.this.getSharedPreferences(MainActivity.SHARE_PREFERENCE, Context.MODE_PRIVATE);

        mPhoneView = findViewById(R.id.phoneNum);
        initPhone(mSharedPreferences);
        mPasswordView =  findViewById(R.id.password);


        mBtn_login = findViewById(R.id.mBtn_sign_in);
        mBtn_login.setOnClickListener(this);

        mPbar_login = findViewById(R.id.mPbar_lgin);

    }

    private void initPhone(SharedPreferences mSharedPreferences) {
        mPhoneView.setText(mSharedPreferences.getString(USER_PHONE_SP,""));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mBtn_sign_in :
                doLogin();
        }
    }

    private void doLogin() {
        String phone = String.valueOf(mPhoneView.getText());
        String passwd = String.valueOf(mPasswordView.getText());
        Toast.makeText(this,"phone:"+phone+",passwd:"+passwd,Toast.LENGTH_SHORT).show();
        new LoginTask().execute(phone,passwd);


    }

    public class LoginTask extends AsyncTask<String, Void, BLLoginResult>{
        private String phone;
        private String passwd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mBtn_login.setClickable(false);
            mPbar_login.setVisibility(View.VISIBLE);
            mPbar_login.setIndeterminate(true);
        }

        @Override
        protected BLLoginResult doInBackground(String[] params) {
            phone = params[0];
            passwd = params[1];
            return BLAccount.login(phone,passwd);
        }

        @Override
        protected void onPostExecute(BLLoginResult blLoginResult) {
            super.onPostExecute(blLoginResult);
            mPbar_login.setVisibility(View.GONE);
            if (blLoginResult != null && blLoginResult.succeed()){
                Toast.makeText(LoginActivity.this,"login success",Toast.LENGTH_SHORT).show();
                doAfterLoginSuccess(blLoginResult);
            }else {
                Toast.makeText(LoginActivity.this,"login fail:"+blLoginResult.getMsg(),Toast.LENGTH_SHORT).show();
            }

            mBtn_login.setClickable(true);
        }

        private void doAfterLoginSuccess(BLLoginResult blLoginResult) {

            String loginResultStr = new UserInfo(blLoginResult).toString();

            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(USER_INFO_SP,loginResultStr);
            editor.putString(USER_PHONE_SP,blLoginResult.getPhone());
            editor.commit();

            Intent intent = new Intent();
            intent.setClass(LoginActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            LoginActivity.this.startActivity(intent);


        }
    }


}

