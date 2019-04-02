package com.broadlink.mysdkdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.broadlink.mysdkdemo.R;

import java.util.List;

import cn.com.broadlink.family.BLFamily;
import cn.com.broadlink.family.params.BLFamilyAllInfo;
import cn.com.broadlink.family.result.BLAllFamilyInfoResult;

public class FamilyOperationActivity extends AppCompatActivity {

    private BLFamilyAllInfo blFamilyAllInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_operation);

        initData();


    }

    private void initData() {

        String familyId = getIntent().getStringExtra(FamilyManageActivity.FAMILY_ID);
        BLAllFamilyInfoResult blAllFamilyInfoResult = BLFamily.queryAllFamilyInfos(new String[]{familyId});
        blFamilyAllInfo = blAllFamilyInfoResult.getAllInfos().get(0);
    }
}
