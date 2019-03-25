package com.broadlink.blappsdkdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.broadlink.blappsdkdemo.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mBtn_login;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtn_login = findViewById(R.id.mBtn_login);
        mBtn_login.setOnClickListener(this);

        mContext = MainActivity.this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mBtn_login:
                startLoginActivity();
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent();
        intent.setClass(mContext,LoginActivity.class);
        startActivity(intent);
    }


}
