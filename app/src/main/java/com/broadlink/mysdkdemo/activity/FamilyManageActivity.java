package com.broadlink.mysdkdemo.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ProgressBar;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.broadlink.mysdkdemo.R;
import com.broadlink.mysdkdemo.commonUtils.LoadingAsyncTask;
import com.broadlink.mysdkdemo.commonUtils.MyProgressDialog;
import com.broadlink.mysdkdemo.commonUtils.OnItemClickListener;
import com.broadlink.mysdkdemo.commonUtils.SimpleRecyclerAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.com.broadlink.base.BLBaseResult;
import cn.com.broadlink.family.BLFamily;
import cn.com.broadlink.family.params.BLFamilyAllInfo;
import cn.com.broadlink.family.params.BLFamilyBaseInfo;
import cn.com.broadlink.family.params.BLFamilyInfo;
import cn.com.broadlink.family.result.BLFamilyBaseInfoListResult;
import cn.com.broadlink.family.result.BLFamilyInfoResult;
import cn.com.broadlink.sdk.BLLet;

public class FamilyManageActivity extends AppCompatActivity implements View.OnClickListener, OnItemClickListener {

    private RecyclerView mRv_familys;
    private FloatingActionButton mFab_addFamily;
    private SimpleRecyclerAdapter simpleRecyclerAdapter;
    private List<String> families;
    private AlertDialog alertDialog;

    private TextInputEditText mEt_familyAddName;
    private TextInputEditText mEt_familyAddCountry;
    private TextInputEditText mEt_familyAddProvince;
    private TextInputEditText mEt_familyAddCity;
    private ProgressBar mPb_familyAdd;
    private Button mBtn_addFamily;


    public static final String FAMILY_ID = "family_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_manage);
        simpleRecyclerAdapter = new SimpleRecyclerAdapter();
        initData();

        findViewById();
        
        setClickListener();
    }

    private void setClickListener() {
        mFab_addFamily.setOnClickListener(this);
        simpleRecyclerAdapter.setOnItemClickListener(this);
    }

    private void initData() {
        families = new ArrayList<>();
        new getFamiliesInfo().execute();
    }

    private void findViewById() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRv_familys = findViewById(R.id.mRv_familyList);
        mRv_familys.setLayoutManager(linearLayoutManager);
        mFab_addFamily = findViewById(R.id.mFab_addFamily);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mFab_addFamily:
                showDialog();
                break;
            case R.id.mBtn_familyAdd:
                String[] strings = getParams(v);
                new addFamilyTask().execute(strings);
        }
    }

    private String[] getParams(View v) {
        String[] results = new String[4];
        results[0] = mEt_familyAddName.getText().toString();
        results[1] = mEt_familyAddCountry.getText().toString();
        results[2] = mEt_familyAddProvince.getText().toString();
        results[3] = mEt_familyAddCity.getText().toString();
        return results;
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FamilyManageActivity.this);
        builder.setTitle("add a family");
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.view_family_add,null,false);
        mBtn_addFamily = contentView.findViewById(R.id.mBtn_familyAdd);
        mEt_familyAddName = contentView.findViewById(R.id.meEt_family_name);
        mEt_familyAddCountry = contentView.findViewById(R.id.meEt_family_country);
        mEt_familyAddProvince = contentView.findViewById(R.id.meEt_family_province);
        mEt_familyAddCity = contentView.findViewById(R.id.meEt_family_city);
        mPb_familyAdd = contentView.findViewById(R.id.mPb_familyAdd);
        mBtn_addFamily.setOnClickListener(FamilyManageActivity.this);
        builder.setView(contentView);
        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onItemClick(View view, int position) {

        String s = (String) simpleRecyclerAdapter.getDatas().get(position);
        Log.d("onItemClick",s);
        BLFamilyInfo blFamilyInfo = JSON.parseObject(s,BLFamilyInfo.class);

        Intent intent = new Intent();
        intent.putExtra(FAMILY_ID,blFamilyInfo.getFamilyId());
        intent.setClass(FamilyManageActivity.this,FamilyOperationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, final int position) {

        AlertDialog alertDialog = new AlertDialog.Builder(FamilyManageActivity.this)
                .setTitle("operations")
                .setItems(new String[]{"delete familyInfo"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ( which == 0 ){
                            showConfirmDialog((String) simpleRecyclerAdapter.getDatas().get(position),position);
                        }
                    }
                })
                .create();
        alertDialog.show();

    }

    private void showConfirmDialog(final String s, final int position) {
        final BLFamilyInfo blFamilyInfo = JSON.parseObject(s,BLFamilyInfo.class);
        AlertDialog alertDialog = new AlertDialog.Builder(FamilyManageActivity.this)
                .setTitle("Confirm")
                .setMessage("confirm to delete family: "+blFamilyInfo.getFamilyName())
                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        new DeleteFamilyInfoTask(FamilyManageActivity.this,"delete ing").execute(blFamilyInfo.getFamilyId(),blFamilyInfo.getFamilyVersion(),String.valueOf(position));
                    }
                })
                .create();
        alertDialog.show();
    }


    class DeleteFamilyInfoTask extends LoadingAsyncTask<BLBaseResult> {

        int position;
        public DeleteFamilyInfoTask(Context mContext, String title) {
            super(mContext, title);
        }

        @Override
        public void onPre() {

        }

        @Override
        public BLBaseResult run(String... strings) {
            position = Integer.valueOf(strings[2]);
            return BLFamily.delFamily(strings[0],strings[1]);
        }

        @Override
        public void onPost(BLBaseResult blBaseResult) {
            if (blBaseResult.succeed()){
                simpleRecyclerAdapter.removeItem(position);
            }
        }


    }

    class addFamilyTask extends AsyncTask<String,Void, BLFamilyInfoResult>{



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mBtn_addFamily.setVisibility(View.GONE);
            mPb_familyAdd.setVisibility(View.VISIBLE);

        }

        @Override
        protected BLFamilyInfoResult doInBackground(String... strings) {
            return BLFamily.createDefaultFamily(strings[0],strings[1],strings[2],strings[3]);
        }

        @Override
        protected void onPostExecute(BLFamilyInfoResult blFamilyInfoResult) {
            super.onPostExecute(blFamilyInfoResult);

            alertDialog.dismiss();
            simpleRecyclerAdapter.addItem(blFamilyInfoResult.getFamilyInfo());
        }
    }

    class getFamiliesInfo extends AsyncTask<Object,Void,List<String>>{

        AlertDialog myProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myProgressDialog = new MyProgressDialog(FamilyManageActivity.this,"init families data").getInstance().create();
            myProgressDialog.show();


        }

        @Override
        protected List<String> doInBackground(Object[] objects) {
            List<String> result = new ArrayList<>();

            BLFamilyBaseInfoListResult blFamilyBaseInfoListResult = BLFamily.queryLoginUserFamilyBaseInfoList();
            if (blFamilyBaseInfoListResult != null){
                List<BLFamilyBaseInfo> blFamilyBaseInfos = new ArrayList<>();
                if (!(blFamilyBaseInfos=blFamilyBaseInfoListResult.getInfoList()).isEmpty()){
                    for (BLFamilyBaseInfo baseInfo : blFamilyBaseInfos){
                        result.add(JSON.toJSONString(baseInfo.getFamilyInfo(),true));
                    }
                }

            }else {
                result.add(blFamilyBaseInfoListResult.succeed()?"no familyInfo":"query fail"+blFamilyBaseInfoListResult.getMsg());
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
            myProgressDialog.dismiss();
            families.addAll(strings);
            simpleRecyclerAdapter.setDatas(families);
            simpleRecyclerAdapter.setItemViewId(R.layout.view_common_itme);
            simpleRecyclerAdapter.setContentViewId(R.id.mTv_commonItem);
            mRv_familys.setAdapter(simpleRecyclerAdapter);
        }
    }

}
