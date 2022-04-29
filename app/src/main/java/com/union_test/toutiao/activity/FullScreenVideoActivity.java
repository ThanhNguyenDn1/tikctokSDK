package com.union_test.toutiao.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.union_test.internationad.R;
import com.union_test.toutiao.PangleAppOpenAdManager;
import com.union_test.toutiao.RitConstants;
import com.union_test.toutiao.config.TTAdManagerHolder;
import com.union_test.toutiao.utils.TToast;

import java.lang.ref.WeakReference;

/**
 * Created by bytedance on 2018/2/1.
 */

public class FullScreenVideoActivity extends Activity {
    public static final String TAG = "FullScreenVideoActivity";
    private Button mLoadAd;
    private Button mLoadAdVertical;
    private Button mShowAd;
    private TTAdNative mTTAdNative;
    private TTFullScreenVideoAd mttFullVideoAd;
    private String mHorizontalCodeId = RitConstants.RIT_INTER_HORIZONTAL;
    private String mVerticalCodeId = RitConstants.RIT_INTER_VERTICAL;
    private boolean mIsExpress;
    @SuppressWarnings("RedundantCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);
        findViewById(R.id.btn_fsv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLoadAd = (Button) findViewById(R.id.btn_reward_load);
        mLoadAdVertical = (Button) findViewById(R.id.btn_reward_load_vertical);
        mShowAd = (Button) findViewById(R.id.btn_reward_show);
        //step1:Initialize to access the TikTok Ad Network sdk, see the access documentation and the TikTok Ad Network for details
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        //step2:create TTAdNative object which use to request ad
        mTTAdNative = ttAdManager.createAdNative(getApplicationContext());
        initClickEvent();
    }




    private void initClickEvent() {

        mLoadAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAd(mHorizontalCodeId, TTAdConstant.HORIZONTAL);
            }
        });
        mLoadAdVertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAd(mVerticalCodeId, TTAdConstant.VERTICAL);
            }
        });
        mShowAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mttFullVideoAd != null) {
                    //step6:display ad
//                    mttFullVideoAd.showFullScreenVideoAd(FullScreenVideoActivity.this);

                    //Display the AD and pass display scene parameter
                    mttFullVideoAd.showFullScreenVideoAd(FullScreenVideoActivity.this,TTAdConstant.RitScenes.GAME_GIFT_BONUS,null);
                    mttFullVideoAd = null;
                } else {
                    TToast.show(FullScreenVideoActivity.this, "Please load the ad first !");
                }
            }
        });
    }

    @SuppressWarnings("SameParameterValue")
    private void loadAd(String codeId, int orientation) {
        //step4:Create a parameter AdSlot for reward ad request type,
        //      refer to the document for meanings of specific parameters
        AdSlot adSlot;
//        AdSlot adSlot = new AdSlot.Builder()
//                .setCodeId(codeId)
//                .setSupportDeepLink(true)
//                .isExpressAd(mIsExpress)
//                .setExpressViewAcceptedSize(1080, 1920)
//                .build();
        if (mIsExpress) {
            adSlot = new AdSlot.Builder()
                    .setCodeId(codeId)
                    //模板广告需要设置期望个性化模板广告的大小,单位dp,全屏视频场景，只要设置的值大于0即可
                    .isExpressAd(mIsExpress)
                    .setExpressViewAcceptedSize(500,500)
                    .build();

        } else {
            adSlot = new AdSlot.Builder()
                    .setCodeId(codeId)
                    .isExpressAd(mIsExpress)
                    .build();
        }
        //step5:request ad
        mTTAdNative.loadFullScreenVideoAd(adSlot,
                new FullScreenVideoAdListener(FullScreenVideoActivity.this.getApplicationContext(),
                new AdLoadCallBack() {
                    @Override
                    public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ad) {
                        Log.i(TAG, "onFullScreenVideoAdLoad: ");
                        mttFullVideoAd = ad;
                    }
                }));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    interface AdLoadCallBack{
        void onFullScreenVideoAdLoad(TTFullScreenVideoAd ad);
    }

    public static class FullScreenVideoAdListener implements TTAdNative.FullScreenVideoAdListener {
        Context mContext;
        WeakReference<AdLoadCallBack> adLoadCallBackWeakReference;
        FullScreenVideoAdListener(Context context, AdLoadCallBack adLoadCallBack) {
            mContext = context;
            adLoadCallBackWeakReference = new WeakReference<>(adLoadCallBack);
        }

        @Override
        public void onError(int code, String message) {
            Log.e(TAG, "Callback --> onError: " + code + ", " + String.valueOf(message));
            TToast.show(mContext, message);
        }

        @Override
        public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ad) {
            TToast.show(mContext, "FullVideoAd loaded");
            if(adLoadCallBackWeakReference.get() != null){
                adLoadCallBackWeakReference.get().onFullScreenVideoAdLoad(ad);
            }
            ad.setFullScreenVideoAdInteractionListener(new MyFullScreenVideoAdListener(mContext));

        }

        @Override
        public void onFullScreenVideoCached() {
            Log.e(TAG, "Callback --> onFullScreenVideoCached");

        }
    }
    public static class MyFullScreenVideoAdListener implements TTFullScreenVideoAd.FullScreenVideoAdInteractionListener {
        Context mContext;

        MyFullScreenVideoAdListener(Context context) {
            mContext = context;
        }

        @Override
        public void onAdShow() {
            Log.d(TAG, "Callback --> FullVideoAd show");
            TToast.show(mContext, "FullVideoAd show");

            PangleAppOpenAdManager.setIsShowingAd(true);
        }

        @Override
        public void onAdVideoBarClick() {
            Log.d(TAG, "Callback --> FullVideoAd bar click");
            TToast.show(mContext, "FullVideoAd bar click");
        }

        @Override
        public void onAdClose() {
            Log.d(TAG, "Callback --> FullVideoAd close");
            TToast.show(mContext, "FullVideoAd close");

            PangleAppOpenAdManager.setIsShowingAd(false);
        }

        @Override
        public void onVideoComplete() {
            Log.d(TAG, "Callback --> FullVideoAd complete");
            TToast.show(mContext, "FullVideoAd complete");
        }

        @Override
        public void onSkippedVideo() {
            Log.d(TAG, "Callback --> FullVideoAd skipped");
            TToast.show(mContext, "FullVideoAd skipped");

        }

    }
}
