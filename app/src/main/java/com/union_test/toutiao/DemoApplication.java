package com.union_test.toutiao;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Base64;
import android.util.Log;

//import com.squareup.leakcanary.LeakCanary;
//import com.squareup.leakcanary.RefWatcher;
import com.union_test.toutiao.config.TTAdManagerHolder;

import java.util.TimeZone;

/**
 * Create by hanweiwei on 11/07/2018
 */
@SuppressWarnings("unused")
public class DemoApplication extends MultiDexApplication {

//    public static RefWatcher sRefWatcher = null;
    public static String PROCESS_NAME_XXXX = "process_name_xxxx";

    private static final String TAG = "DemoApplication";
    public static Context CONTEXT = null;

    /**
     * app open ad
     */
    private PangleAppOpenAdManager pangleAppOpenAdManager;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = this;

//        if (!LeakCanary.isInAnalyzerProcess(this)) {
//            sRefWatcher = LeakCanary.install(this);
//        }

        // It is strongly recommended to call in Application #onCreate method,
        // to avoid content as null
        TTAdManagerHolder.init(this);

        //app open ad
        pangleAppOpenAdManager = new PangleAppOpenAdManager(this);
    }



    /**
     * fetch an app open ad.
     */
    public void fetchAd(PangleAppOpenAdManager.RealTimeFetchListener realTimeFetchListener) {
        pangleAppOpenAdManager.fetchAd(realTimeFetchListener);
    }

    /**
     * Shows an app open ad.
     */
    public void showAdIfAvailable(PangleAppOpenAdManager.ManagerOpenAdInteractionListener managerOpenAdInteractionListener) {
        pangleAppOpenAdManager.showAdIfAvailable(managerOpenAdInteractionListener);
    }

}
