package com.broadlink.mysdkdemo.commonUtils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.broadlink.mysdkdemo.activity.WebControlActivity;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import cn.com.broadlink.sdk.BLLet;
import cn.com.broadlink.sdk.data.controller.BLDNADevice;

public class BLNativeBridge extends CordovaPlugin  {

    private WebControlActivity webControlActivity;
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        webControlActivity = (WebControlActivity) cordova.getActivity();

        if (!TextUtils.isEmpty(action)){
            if (action == "deviceinfo"){
                return deviceInfo(callbackContext);
            }
        }
        return super.execute(action, args, callbackContext);
    }

    /**
     * 获取设备信息
     *
     * @param callbackContext 回调
     * @return
     */
    private boolean deviceInfo(CallbackContext callbackContext) {

        WebControlActivity activity = (WebControlActivity) cordova.getActivity();
        if (activity != null && activity.device != null) {
            BLDNADevice deviceInfo = activity.device;
            String did = TextUtils.isEmpty(deviceInfo.getpDid()) ? deviceInfo.getDid() : deviceInfo.getpDid();
            String sdid = TextUtils.isEmpty(deviceInfo.getpDid()) ? null : deviceInfo.getDid();

            BLJSDeviceInfo startUpInfo = new BLJSDeviceInfo();
            startUpInfo.setDeviceStatus(BLLet.Controller.queryDeviceState(did));
            startUpInfo.setDeviceID(did);
            startUpInfo.setSubDeviceID(sdid);
            startUpInfo.setProductID(deviceInfo.getPid());
            startUpInfo.setDeviceName(deviceInfo.getName());
            startUpInfo.setDeviceMac(deviceInfo.getMac());
//            startUpInfo.getNetworkStatus().setStatus(BLCommonUtils.checkNetwork(activity) ? BLPluginInterfacer.NETWORK_AVAILAVLE : BLPluginInterfacer.NETWORK_UNAVAILAVLE);


//            BLUserInfoUnits blUserInfoUnits = BLApplication.mBLUserInfoUnits;
//            String username = "";
//            if (blUserInfoUnits.getPhone() != null) {
//                username = blUserInfoUnits.getPhone();
//            } else if (blUserInfoUnits.getEmail() != null) {
//                username = blUserInfoUnits.getEmail();
//            }

//            startUpInfo.getUser().setName(username);
//            BLLog.d(TAG, "deviceInfo: " + JSON.toJSONString(startUpInfo));
            callbackContext.success(JSON.toJSONString(startUpInfo));
        }
        return true;
    }
}
