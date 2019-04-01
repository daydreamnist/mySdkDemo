package com.broadlink.mysdkdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.broadlink.mysdkdemo.R;
import com.broadlink.mysdkdemo.commonUtils.MyProgressDialog;

import java.util.ArrayList;
import java.util.Arrays;

import cn.com.broadlink.account.BLAccount;
import cn.com.broadlink.base.BLLoginResult;
import cn.com.broadlink.family.BLFamily;
import cn.com.broadlink.sdk.BLLet;
import cn.com.broadlink.sdk.data.controller.BLDNADevice;
import cn.com.broadlink.sdk.data.controller.BLStdData;
import cn.com.broadlink.sdk.param.controller.BLStdControlParam;
import cn.com.broadlink.sdk.result.controller.BLProfileStringResult;
import cn.com.broadlink.sdk.result.controller.BLStdControlResult;

public class DNAControlActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private BLDNADevice device;
    private ConstraintLayout constraintLayout;
    private TextView mTv_Profile;
    private PopupWindow popupWindow;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dna_control);

        getDataFromIntent();

        initViews();


    }
    private void getDataFromIntent() {
        device = getIntent().getParcelableExtra("device");
    }

    private void initViews() {
        constraintLayout = findViewById(R.id.layout_dna_control);
        floatingActionButton = findViewById(R.id.mFab_addControlParam);
        floatingActionButton.setOnClickListener(this);
        recyclerView = findViewById(R.id.mRv_controlParams);
        mTv_Profile = findViewById(R.id.mTv_deviceProfile);
        progressBar = findViewById(R.id.mPb_deviceControl);
        BLProfileStringResult blProfileStringResult = BLLet.Controller.queryProfileByPid(getDevice().getPid());

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mFab_addControlParam:
                showPopupWindow();
                break;
        }
    }

    private void showPopupWindow() {

        lightOff();
        popupWindow = new PopupWindow();
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = layoutInflater.inflate(R.layout.view_common_popup_window,null);

        final TextInputEditText mTv_index = contentView.findViewById(R.id.mTv_index);
        final TextInputEditText mTv_key = contentView.findViewById(R.id.mTv_key);
        final TextInputEditText mTv_value = contentView.findViewById(R.id.mTv_value);
        Button mBtn_save = contentView.findViewById(R.id.mBtn_saveControlParam);
        mBtn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BLStdControlParam blStdControlParam = new BLStdControlParam();
                blStdControlParam.setAct("set");
                blStdControlParam.setParams(new ArrayList<String>(Arrays.asList(new String[]{mTv_key.getText().toString()})));
                BLStdData.Value val = new BLStdData.Value();
                if (TextUtils.isEmpty(mTv_index.getText().toString())
                        || TextUtils.isEmpty(mTv_value.getText().toString())){
                    return;
                }
                val.setIdx(Integer.valueOf(mTv_index.getText().toString()));
                val.setVal(Integer.valueOf(mTv_value.getText().toString()));
                ArrayList<BLStdData.Value> arrayList = new ArrayList<>();
                arrayList.add(val);
                ArrayList<ArrayList<BLStdData.Value>> vals = new ArrayList<>();
                vals.add(arrayList);
                blStdControlParam.setVals(vals);
                blStdControlParam.setPassword(String.valueOf(device.getPassword()));
                Log.d("mBtn_saveControlParam",device.getDid());

                Log.d("mBtn_saveControlParam1",JSON.toJSONString(blStdControlParam,true));

                new ControlDeviceTask().execute(new BLStdControlParam[]{blStdControlParam});
            }
        });
        popupWindow.setContentView(contentView);
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x0fffff));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                layoutParams.alpha=1.0f;
                getWindow().setAttributes(layoutParams);
            }
        });
        popupWindow.showAtLocation(constraintLayout,Gravity.CENTER | Gravity.CENTER_HORIZONTAL,0, 0);

    }


    class ControlDeviceTask extends AsyncTask<BLStdControlParam,Void,BLStdControlResult>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            popupWindow.dismiss();
            progressBar.setIndeterminate(true);
            mTv_Profile.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected BLStdControlResult doInBackground(BLStdControlParam... blStdControlParams) {

            Log.d("mBtn_saveControlParam2",JSON.toJSONString(blStdControlParams[0],true));

            return BLLet.Controller.dnaControl(device.getDid(),null,blStdControlParams[0]);

        }

        @Override
        protected void onPostExecute(BLStdControlResult blStdControlResult) {
            super.onPostExecute(blStdControlResult);
            Log.d("mBtn_saveControlParam",JSON.toJSONString(blStdControlResult,true));
            mTv_Profile.setText(JSON.toJSONString(blStdControlResult,true));
            progressBar.setVisibility(View.GONE);
            mTv_Profile.setVisibility(View.VISIBLE);
        }
    }
    public BLDNADevice getDevice() {
        return device;
    }

    private void lightOff() {

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();

        layoutParams.alpha=0.3f;

        getWindow().setAttributes(layoutParams);

    }

}
