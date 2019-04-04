package com.broadlink.mysdkdemo.commonUtils;

import com.broadlink.mysdkdemo.model.DNAKitDirInfo;

import java.util.ArrayList;
import java.util.List;

public class GetDNAKitDirResult {

    private int status = - 1;

    private List<DNAKitDirInfo> categorylist = new ArrayList<>();

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DNAKitDirInfo> getCategorylist() {
        return categorylist;
    }

    public void setCategorylist(List<DNAKitDirInfo> categorylist) {
        this.categorylist = categorylist;
    }
    public boolean succeed() {
        return status == 0;
    }
}
