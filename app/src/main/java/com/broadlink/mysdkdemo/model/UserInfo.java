package com.broadlink.mysdkdemo.model;

import cn.com.broadlink.base.BLLoginResult;

public class UserInfo {

    private String userId ;
    private String nickname ;
    private String email ;
    private String phone ;
    private String loginip ;
    private String logintime ;

    public UserInfo(BLLoginResult blLoginResult) {
        this.userId = blLoginResult.getUserid();
        this.nickname = blLoginResult.getNickname();
        this.email = blLoginResult.getEmail();
        this.phone = blLoginResult.getPhone();
        this.loginip = blLoginResult.getLoginip();
        this.logintime = blLoginResult.getLogintime();
    }



    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("userId: "+userId+"\n");
        stringBuilder.append("nickname: "+nickname+"\n");
        stringBuilder.append("email: "+email+"\n");
        stringBuilder.append("phone: "+phone+"\n");
        stringBuilder.append("loginip: "+loginip+"\n");
        stringBuilder.append("logintime: "+logintime);
        return stringBuilder.toString();
    }
}
