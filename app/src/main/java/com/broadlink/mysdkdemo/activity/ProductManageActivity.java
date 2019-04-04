package com.broadlink.mysdkdemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.broadlink.mysdkdemo.R;
import com.broadlink.mysdkdemo.model.DNAKitDirInfo;
import com.broadlink.mysdkdemo.commonUtils.GetDNAKitDirParam;
import com.broadlink.mysdkdemo.commonUtils.GetDNAKitDirResult;
import com.broadlink.mysdkdemo.commonUtils.HttpUtils;
import com.broadlink.mysdkdemo.commonUtils.ImageLoadTask;
import com.broadlink.mysdkdemo.commonUtils.loadCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.com.broadlink.sdk.BLLet;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ProductManageActivity extends AppCompatActivity implements View.OnClickListener {


    public static String BASE_APP_MANAGE = "https://%sappservice.ibroadlink.com";
    public static String ADD_PRODUCT_DIRECTORY = "/ec4/v1/system/resource/categorylist";
    public static final String PRODUCT_ICON() { return BASE_APP_MANAGE + "/ec4/v1/system/configfile";}


    public static final String INTENT_CATEGORYID = "INTENT_CATEGORYID";
    public static final String INTENT_CATEGORYNAME = "INTENT_CATEGORYNAME";

    String responseStr;
    static Handler handler;

    ImageView mIv_categoryIcon;
    TextView mTv_categoryName;

    GetDNAKitDirResult getDNAKitDirResult;
    DNAKitDirInfo dnaKitDirInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_manage);

        findViews();

        try {
            initCategoryData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.obj instanceof Bitmap){
                    mIv_categoryIcon.setImageBitmap((Bitmap) msg.obj);
                }
                if (msg.obj instanceof DNAKitDirInfo){
                    dnaKitDirInfo = (DNAKitDirInfo) msg.obj;
                    mTv_categoryName.setText(dnaKitDirInfo.getName());
                    mTv_categoryName.setOnClickListener(ProductManageActivity.this);
                    mIv_categoryIcon.setOnClickListener(ProductManageActivity.this);
                }
            }
        };


    }

    private void findViews() {
        mIv_categoryIcon = findViewById(R.id.mIv_categoryIcon);
        mTv_categoryName = findViewById(R.id.mTv_categoryName);
    }

    private void initCategoryData() throws IOException {
        String lid = BLLet.getLicenseId();
        BASE_APP_MANAGE = String.format(BASE_APP_MANAGE, lid);
        ADD_PRODUCT_DIRECTORY = BASE_APP_MANAGE+ADD_PRODUCT_DIRECTORY;
        GetDNAKitDirParam getDNAKitDirParam = new GetDNAKitDirParam();
        getDNAKitDirParam.setProtocols(null);
        getDNAKitDirParam.setBrandid("");

        String reqBody = JSON.toJSONString(getDNAKitDirParam);
        Map<String, String> headers = initHeaders();
        HttpUtils.postJSON(ProductManageActivity.this, ADD_PRODUCT_DIRECTORY,headers, reqBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.d("onResponse:  ",response.toString()+"  "+response.body().string());
                responseStr = response.body().string();
                getDNAKitDirResult = JSON.parseObject(responseStr,GetDNAKitDirResult.class);
                initView(getDNAKitDirResult);
            }
        });
    }

    private void initView(GetDNAKitDirResult getDNAKitDirResult) {

        DNAKitDirInfo dnaKitDirInfo = getDNAKitDirResult.getCategorylist().get(0);
//        mTv_categoryName.setText(dnaKitDirInfo.getName());


        Message message = handler.obtainMessage();
        message.obj = dnaKitDirInfo;
        handler.sendMessage(message);

        String iconUri = PRODUCT_ICON()+dnaKitDirInfo.getLink();
        new ImageLoadTask(iconUri, new loadCallback() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                Message message = handler.obtainMessage();
                message.obj = bitmap;
                handler.sendMessage(message);
//                mIv_categoryIcon.setImageBitmap(bitmap);
            }
        }).execute();
    }

    public static Map<String, String> initHeaders() {
        Map<String, String> res = new HashMap<>();
        res.put("appPlatform","android");
        res.put("appVersion","1.3");
        res.put("countryCode","1");
        res.put("language","zh-cn");
        res.put("licenseid","91dd309227daa029a69b404ad6e0c747");
        res.put("loginsession","d1fe21111d4611cfc7dbab9ba151a90e");
        res.put("system","android");
        res.put("userid","0447056e57028436f1a2f501a70ec7f6");


        return res;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(ProductManageActivity.this,ProductListActivity.class);
        intent.putExtra(INTENT_CATEGORYID,dnaKitDirInfo.getCategoryid());
        intent.putExtra(INTENT_CATEGORYNAME,dnaKitDirInfo.getName());
        startActivity(intent);
    }


}
