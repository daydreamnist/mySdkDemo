package com.broadlink.mysdkdemo.commonUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.broadlink.mysdkdemo.activity.WebControlActivity;
import com.broadlink.mysdkdemo.model.BLJSDeviceInfo;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.broadlink.base.BLConfigParam;
import cn.com.broadlink.sdk.BLLet;
import cn.com.broadlink.sdk.data.controller.BLDNADevice;

public class BLNativeBridge extends CordovaPlugin  {

    private WebControlActivity webControlActivity;
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        webControlActivity = (WebControlActivity) cordova.getActivity();

        if (!TextUtils.isEmpty(action)){
            if (action.equals( "deviceinfo")){
                return deviceInfo(callbackContext);
            }
            if (action.equals("devicecontrol")){
                return deviceControl(args, callbackContext);

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
            startUpInfo.getNetworkStatus().setStatus("available");


//            BLUserInfoUnits blUserInfoUnits = BLApplication.mBLUserInfoUnits;
//            String username = "";
//            if (blUserInfoUnits.getPhone() != null) {
//                username = blUserInfoUnits.getPhone();
//            } else if (blUserInfoUnits.getEmail() != null) {
//                username = blUserInfoUnits.getEmail();
//            }

            startUpInfo.getUser().setName("15161172821");
            Log.d("startUpInfo", "deviceInfo: " + JSON.toJSONString(startUpInfo));
            callbackContext.success(JSON.toJSONString(startUpInfo));
        }
        return true;
    }

    private boolean     deviceControl(JSONArray jsonArray, CallbackContext callbackContext) {
        try {
            String deviceMac = jsonArray.getString(0);
            String subDeviceID = jsonArray.getString(1);
            String cmd = jsonArray.getString(2);
            String method = jsonArray.getString(3);

            String extendStr = null;
            if (jsonArray.length() >= 5) {
                extendStr = jsonArray.getString(4);
            }

            //判断mac地址是否为空，以及method是否为空
            if (TextUtils.isEmpty(deviceMac) || TextUtils.isEmpty(method)) {
                BLJsBaseResult pluginBaseResult = new BLJsBaseResult();
                pluginBaseResult.setCode(2000);//ERRCODE_PARAM = 2000;
                callbackContext.error(JSON.toJSONString(pluginBaseResult));
            } else {
                new ControlTask(cordova.getActivity(), extendStr, callbackContext).execute(deviceMac, subDeviceID, cmd, method);
            }
        } catch (JSONException e) {
            e.printStackTrace();

            BLJsBaseResult pluginBaseResult = new BLJsBaseResult();
            pluginBaseResult.setCode(2000);
            callbackContext.error(JSON.toJSONString(pluginBaseResult));
        }
        return true;
    }

    /**设备控制**/
    class ControlTask extends AsyncTask<String, Void, String> {
        private CallbackContext callbackContext;
        private String extendStr;
        private Activity activity;

        public ControlTask(Activity activity, String extendStr, CallbackContext callbackContext){
            this.activity = activity;
            this.extendStr = extendStr;
            this.callbackContext = callbackContext;
        }

        @Override
        protected String doInBackground(String... params) {
            BLConfigParam configParam = null;
            if(!TextUtils.isEmpty(extendStr)){
                try {
                    JSONObject jsonObject = new JSONObject(extendStr);

                    int localTimeout = jsonObject.optInt("localTimeout", 3000);
                    int remoteTimeout = jsonObject.optInt("remoteTimeout", 5000);
                    int sendcount=jsonObject.optInt("sendCount", -1);
                    configParam = new BLConfigParam();
                    if(localTimeout > 0){
                        configParam.put(BLConfigParam.CONTROLLER_LOCAL_TIMEOUT, String.valueOf(localTimeout));
                    }

                    if(remoteTimeout > 0){
                        configParam.put(BLConfigParam.CONTROLLER_REMOTE_TIMEOUT, String.valueOf(remoteTimeout));
                    }

                    if(sendcount>0){
                        configParam.put(BLConfigParam.CONTROLLER_SEND_COUNT, String.valueOf(sendcount));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return BLLet.Controller.dnaControl(params[0], params[1], params[2], params[3], configParam);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("ControlTask", result);
            if(callbackContext != null && activity != null && !activity.isFinishing()) callbackContext.success(result);
        }
    }
}
