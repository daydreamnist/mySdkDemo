package com.broadlink.mysdkdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.broadlink.mysdkdemo.R;

public class ProductListActivity extends AppCompatActivity {

    private TextView mTv_title;
    private RecyclerView mRv_products;

    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        findViews();

        getDataFromIntent();

        initView();

    }

    private void initView() {
        mTv_title.setText(title);

    }

    private void getDataFromIntent() {
        title = getIntent().getStringExtra(ProductManageActivity.INTENT_CATEGORYNAME)
                +"\n"
                +getIntent().getStringExtra(ProductManageActivity.INTENT_CATEGORYID);
    }

    private void findViews() {
        mTv_title = findViewById(R.id.mTv_productsTitle);
        mRv_products = findViewById(R.id.mRv_products);
    }
}
