package com.broadlink.mysdkdemo.commonUtils;

import java.util.List;

public class GetDNAKitDirParam {

    private String brandid;
    private List<String> protocols;

    public String getBrandid() {
        return brandid;
    }

    public void setBrandid(String brandid) {
        this.brandid = brandid;
    }

    public List<String> getProtocols() {
        return protocols;
    }

    public void setProtocols(List<String> protocols) {
        this.protocols = protocols;
    }
}
