package com.broadlink.mysdkdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.broadlink.mysdkdemo.R;
import com.broadlink.mysdkdemo.commonUtils.GetDNAKitDirResult;
import com.broadlink.mysdkdemo.commonUtils.HttpUtils;
import com.broadlink.mysdkdemo.commonUtils.LoadingAsyncTask;
import com.broadlink.mysdkdemo.commonUtils.ProductRecyclerAdapter;
import com.broadlink.mysdkdemo.model.GetDNAKitProductListParam;
import com.broadlink.mysdkdemo.model.GetDNAKitProductListResult;
import com.broadlink.mysdkdemo.model.ProductInfo;

import java.io.IOException;
import java.util.List;

import cn.com.broadlink.base.BLBaseResult;
import okhttp3.Response;

public class ProductListActivity extends AppCompatActivity {

    private ProductRecyclerAdapter productRecyclerAdapter;

    private TextView mTv_title;
    private RecyclerView mRv_products;

    private String title;
    private List<ProductInfo> productInfos;
    private String categoryId;
    private String categoryName;

    public static final String ADD_PRODUCT_LIST = "/ec4/v1/system/resource/productlist";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        findViews();

        getDataFromIntent();

        initDatas();

//        initView();

    }

    private void initDatas() {

        String reqPath = ProductManageActivity.getReqPath(ADD_PRODUCT_LIST);

        Log.d("PathUrl",reqPath);
        GetDNAKitProductListParam productParam = new GetDNAKitProductListParam();
        productParam.setCategoryid(categoryId);
        productParam.setBrandid("");
        productParam.setProtocols(null);
        productParam.setBrandname(null);

        new LoadingAsyncTask<GetDNAKitProductListResult>(ProductListActivity.this,"init products data") {
            @Override
            public void onPre() {

            }

            @Override
            public GetDNAKitProductListResult run(String... strings) {
                Response response = null;
                GetDNAKitProductListResult result = null;
                try {
                     response = HttpUtils.postJSON(ProductListActivity.this,reqPath,ProductManageActivity.initHeaders(), JSON.toJSONString(productParam));
                     String responseStr = response.body().string();
                     Log.d("response",responseStr);
                     result = JSON.parseObject(responseStr,GetDNAKitProductListResult.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            public void onPost(GetDNAKitProductListResult getDNAKitProductListResult) {
                productInfos = getDNAKitProductListResult.getProductlist();
                if (getDNAKitProductListResult!=null){
                    initView();
                }
            }


        }.execute();
    }

    private void initView() {
        if (productInfos != null){
            LinearLayoutManager layoutManager = new LinearLayoutManager(ProductListActivity.this);
//            layoutManager.setAutoMeasureEnabled();
            mRv_products.setLayoutManager(layoutManager);
            productRecyclerAdapter = new ProductRecyclerAdapter(productInfos);
            mRv_products.setAdapter(productRecyclerAdapter);
        }
    }

    private void getDataFromIntent() {
        categoryId = getIntent().getStringExtra(ProductManageActivity.INTENT_CATEGORYID);
        categoryName = getIntent().getStringExtra(ProductManageActivity.INTENT_CATEGORYNAME);
        title = categoryId
                +"\n"
                +categoryName;
        mTv_title.setText(title);

    }

    private void findViews() {
        mTv_title = findViewById(R.id.mTv_productsTitle);
        mRv_products = findViewById(R.id.mRv_products);
    }
}
