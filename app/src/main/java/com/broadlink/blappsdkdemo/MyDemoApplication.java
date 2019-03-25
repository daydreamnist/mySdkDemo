package com.broadlink.blappsdkdemo;

import android.app.Application;

import cn.com.broadlink.account.BLAccount;
import cn.com.broadlink.base.BLConfigParam;
import cn.com.broadlink.base.BLConstants;
import cn.com.broadlink.family.BLFamily;
import cn.com.broadlink.ircode.BLIRCode;
import cn.com.broadlink.sdk.BLLet;

public class MyDemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        sdkInit();
    }

    private void sdkInit() {

        // 配置参数类
        BLConfigParam blConfigParam = new BLConfigParam();
        // 1. 设置日志级别，默认为 4 全部打印
        blConfigParam.put(BLConfigParam.CONTROLLER_LOG_LEVEL, "4");
        // 2. 设置底层打印日志级别，默认为 4 全部打印
        blConfigParam.put(BLConfigParam.CONTROLLER_JNI_LOG_LEVEL, "4");
        // 3. 设置脚本保存目录, 默认在 ../let/ 目录下
        // blConfigParam.put(BLConfigParam.SDK_FILE_PATH, "");
        // 4. 设置本地控制超时时间，默认 3000ms
        blConfigParam.put(BLConfigParam.CONTROLLER_LOCAL_TIMEOUT, "3000");
        // 5. 设置远程控制超时时间，默认 5000ms
        blConfigParam.put(BLConfigParam.CONTROLLER_REMOTE_TIMEOUT, "5000");
        // 6. 设置控制重试次数，默认 1
        blConfigParam.put(BLConfigParam.CONTROLLER_SEND_COUNT, "1");
        // 7. 设置设备控制支持的网络模式，默认 -1 都支持。  0 - 局域网控制，非0 - 局域网/远程都支持。
        blConfigParam.put(BLConfigParam.CONTROLLER_NETMODE, "-1");
        // 8. 设置脚本和UI文件下载资源平台。 默认 0 老平台。  1 - 新平台
        blConfigParam.put(BLConfigParam.CONTROLLER_SCRIPT_DOWNLOAD_VERSION, "1");
        // 9. 批量查询设备在线状态最小设备数
        blConfigParam.put(BLConfigParam.CONTROLLER_QUERY_COUNT, "8");
        // 10. 使用APPService服务，此功能为新集群，若为老集群，请设置为 0
        blConfigParam.put(BLConfigParam.APP_SERVICE_ENABLE, "1");

        // 初始化核心库
//        BLLet.init(this, blConfigParam);

        String packageName = "com.broadlink.blappsdkdemo";
        String license = "4oQxAHVFYnnY7HPuDlYnm0I6pGcRvFTh/Ct2Vv+/5qZDpJJiIweBn2RUUA6oE8InRDV+XAAAAABz4LOxmXdGndIQ0J762DN4lXimLcoYN1h90T3OlpYrQrNgvm0/7+Kdmrgfawtr+QWBY+UBaf8hxk19tobFrLsFsEkbxXTfoUSQjDzWcfVjcAAAAAA=";
        blConfigParam.put(BLConfigParam.CONTROLLER_AUTH_PACKAGE_NAME,packageName);
        BLLet.init(this,license,"",blConfigParam);

        String lid = BLLet.getLicenseId();
        String companyId = BLLet.getCompanyid();

        // 初始化家庭库
        BLFamily.init(companyId, lid);
        // 初始化账户库
        BLAccount.init(companyId, lid);
        // 初始化红外码库
        BLIRCode.init(lid, blConfigParam);

        // 添加登录成功回调函数
        BLAccount.addLoginListener(BLLet.Controller.getLoginListener());
        BLAccount.addLoginListener(BLFamily.getLoginListener());
        BLAccount.addLoginListener(BLIRCode.getLoginListener());
    }
}
