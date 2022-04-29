package com.union_test.toutiao;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Base64;
import android.util.Log;

//import com.squareup.leakcanary.LeakCanary;
//import com.squareup.leakcanary.RefWatcher;
import com.bytedance.crash.ICommonParams;
import com.bytedance.crash.Npth;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.union_test.toutiao.PangleAppOpenAdManager;
import com.union_test.toutiao.config.TTAdManagerHolder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Create by hanweiwei on 11/07/2018
 */
@SuppressWarnings("unused")
public class DemoNpthApplication extends DemoApplication {

    private static final String TAG = "DemoApplication";


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initNPTH();

    }



    private void initNPTH(){

        Npth.init(this, new ICommonParams() {
            String uuid = UUID.randomUUID().toString();
            @Override
            public Map<String, Object> getCommonParams() {
                Map<String, Object> info = new HashMap<>();
                info.put("aid", 309362); // 必须， 写Slardar平台申请的App id
                info.put("channel", "channel"); // 必须, 自行定义, 只要是字符串都行
                String versionName = TTAdSdk.getAdManager().getSDKVersion();
                int versionCode = Integer.parseInt(versionName.replace(".",""));
                Log.i(TAG, "versionName="+versionName+"  versionCode="+versionCode);
                info.put("app_version", TTAdSdk.getAdManager().getSDKVersion()); // 字符串版本号, 对应大版本
                info.put("version_code", versionCode/10); // 数字版本号
                info.put("update_version_code", versionCode); // 数字小版本号
                return info;
            }

            @Override
            public String getDeviceId() {
                return uuid.hashCode()+""; // 推荐使用Applog提供的did, 字符串内只允许出现数字
            }

            @Override
            public long getUserId() {
                return uuid.hashCode();
            }

            @Override
            public String getSessionId() {
                return uuid;
            }

            @Override
            public Map<String, Integer> getPluginInfo() {
                return null;
            }

            @Override
            public List<String> getPatchInfo() {
                return null;
            }
        }, true, true, true);

        Npth.addTag("monkey_app", "demo_rd");
        Log.i(TAG, "exec initNPTH");

    }



}
