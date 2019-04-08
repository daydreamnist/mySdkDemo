package com.broadlink.mysdkdemo.model;

import java.util.ArrayList;

import cn.com.broadlink.base.BLBaseResult;

public class GetDNAKitProductListResult extends BLBaseResult {

    private int status = -1;

    private ArrayList<ProductInfo> productlist = new ArrayList<>();

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<ProductInfo> getProductlist() {
        return productlist;
    }

    public void setProductlist(ArrayList<ProductInfo> productlist) {
        this.productlist = productlist;
    }
}
