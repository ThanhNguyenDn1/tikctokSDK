package com.union_test.toutiao.config;

import android.content.Context;
import android.util.Log;

import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.union_test.internationad.R;
import com.union_test.toutiao.RitConstants;
import com.union_test.toutiao.sp.PangleSpUtils;

/**
 * You can use a singleton to save the TTFAdManager instance, and call it when you need to initialize sdk
 */
public class TTAdManagerHolder {

    private static boolean sInit;

    private static final String TAG = "TTAdManagerHolder";

    private static PangleSdkHasInitSuccess pangleSdkHasInitSuccess;

    public static TTAdManager get() {
        if (!sInit) {
            throw new RuntimeException("TTAdSdk is not init, please check.");
        }
        return TTAdSdk.getAdManager();
    }

    public static void init(Context context) {
        doInit(context);
    }

    //step1: Initialize sdk
    private static void doInit(Context context) {
        if (!sInit) {
            TTAdSdk.init(context, buildConfig(context), new TTAdSdk.InitCallback() {
                @Override
                public void success() {
                    Log.i(TAG, "success: ");

                    if (pangleSdkHasInitSuccess != null){
                        pangleSdkHasInitSuccess.initSuccess();
                        pangleSdkHasInitSuccess = null;
                    }
                }

                @Override
                public void fail(int code, String msg) {
                    Log.i(TAG, "fail: "+code);
                }
            });
            sInit = true;
        }
    }

    private static TTAdConfig buildConfig(Context context) {
        return new TTAdConfig.Builder()
                .appId(RitConstants.AD_APP_ID)
                .useTextureView(true)// Use TextureView to play the video. The default setting is SurfaceView, when the context is in conflict with SurfaceView, you can use TextureView.You will need to save the changed Gdpr locally and retrieve it from the local store the next time the process starts
                .setGDPR(PangleSpUtils.getInstance().getGdpr())
                .setCCPA(PangleSpUtils.getInstance().getCcpa())
                .debug(true)// Turn it on during the testing phase, you can troubleshoot with the log, remove it after launching the app
                .supportMultiProcess(false) // true for support multi-process environment,false for single-process
                .coppa(PangleSpUtils.getInstance().getCoppa()) // Fields to indicate whether you are a child or an adult ，0:adult ，1:child. You will need to save the changed Coppa locally and retrieve it from the local store the next time the process starts.
                .appIcon(R.mipmap.app_icon)
                .build();
    }


    public interface PangleSdkHasInitSuccess {
        void initSuccess();
    }

    public static void setPangleSdkHasInitSuccess(PangleSdkHasInitSuccess sdkHasInitSuccess) {
        pangleSdkHasInitSuccess = sdkHasInitSuccess;
    }
}
