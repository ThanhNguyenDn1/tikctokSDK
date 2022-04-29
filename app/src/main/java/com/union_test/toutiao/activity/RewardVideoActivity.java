package com.union_test.toutiao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.union_test.internationad.R;
import com.union_test.toutiao.PangleAppOpenAdManager;
import com.union_test.toutiao.RitConstants;
import com.union_test.toutiao.config.TTAdManagerHolder;
import com.union_test.toutiao.utils.TToast;

/**
 * Created by bytedance on 2018/2/1.
 */

public class RewardVideoActivity extends Activity {
    public static final String TAG = "RewardVideoActivity";
    private Button mLoadAd;
    private Button mLoadAdVertical;
    private Button mShowAd;
    private TTAdNative mTTAdNative;
    private TTRewardVideoAd mttRewardVideoAd;
    private int mGdrp;
    private String mHorizontalCodeId = RitConstants.RIT_REWARDED_HORIZONTAL;
    private String mVerticalCodeId = RitConstants.RIT_REWARDED_VERTICAL;
    private boolean mIsExpress;

    @SuppressWarnings("RedundantCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_video);
        findViewById(R.id.btn_arv_back).setOnClickListener(new View.OnClickListener() {
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
        //GDRP value if need : 0 close GDRP Privacy protection ，1: open GDRP Privacy protection
        mGdrp = TTAdManagerHolder.get().getGdpr();
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
                loadAd( mVerticalCodeId, TTAdConstant.VERTICAL);
            }
        });
        mShowAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mttRewardVideoAd != null) {
                    //step5:display ad
                    //Display the AD and pass display scene parameter
                    mttRewardVideoAd.showRewardVideoAd(RewardVideoActivity.this,TTAdConstant.RitScenes.GAME_GIFT_BONUS,null);
                    mttRewardVideoAd = null;
                } else {
                    TToast.show(RewardVideoActivity.this, "Please load the ad first !");
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void loadAd(String codeId, int orientation) {
        //step3:Create a parameter AdSlot for reward ad request type,
        //      refer to the document for meanings of specific parameters
        AdSlot adSlot;
        if (mIsExpress) {
            adSlot = new AdSlot.Builder()
                    .setCodeId(codeId)
                    .isExpressAd(mIsExpress)
                    .setExpressViewAcceptedSize(1080,1920)
                    .build();
        } else {
            adSlot = new AdSlot.Builder()
                    .setCodeId(codeId)
                    .isExpressAd(mIsExpress)
                    .setImageAcceptedSize(1080, 1920)
                    .build();
        }
        //step4:request ad
        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                Log.e(TAG, "Callback --> onError: " + code + ", " + String.valueOf(message));
                TToast.show(RewardVideoActivity.this, message);
            }

            //After the video ad had loaded and the video resource is cached to the local ,
            //the "onRewardVideoCached" callback will be invoke .
            //The local video can be played smoothly without any delays.
            @Override
            public void onRewardVideoCached() {
                Log.e(TAG, "Callback --> onRewardVideoCached");
                TToast.show(RewardVideoActivity.this, "rewardVideoAd video cached");
            }

            //The video ad's material such as video url had loaded.  After this callback invoke,
            //you can  play the online video by url.
            //Note: Poor network quality may result in buffering of the video since it’s played online.
            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
                TToast.show(RewardVideoActivity.this, "rewardVideoAd loaded");
                mttRewardVideoAd = ad;
//                mttRewardVideoAd.setShowDownLoadBar(false);
                mttRewardVideoAd.setRewardAdInteractionListener(new MyRewardAdInteractionListener(RewardVideoActivity.this.getApplicationContext()));
            }
        });
    }

    public static class MyRewardAdInteractionListener implements TTRewardVideoAd.RewardAdInteractionListener {

        Context mContext;

        MyRewardAdInteractionListener(Context context) {
            mContext = context;
        }
        @Override
        public void onAdShow() {
            Log.d(TAG, "Callback --> rewardVideoAd show");
            TToast.show(mContext, "rewardVideoAd show");

            PangleAppOpenAdManager.setIsShowingAd(true);
        }

        @Override
        public void onAdVideoBarClick() {
            Log.d(TAG, "Callback --> rewardVideoAd bar click");
            TToast.show(mContext, "rewardVideoAd bar click");
        }

        @Override
        public void onAdClose() {
            Log.d(TAG, "Callback --> rewardVideoAd close");
            TToast.show(mContext, "rewardVideoAd close");

            PangleAppOpenAdManager.setIsShowingAd(false);
        }

        @Override
        public void onVideoComplete() {
            Log.d(TAG, "Callback --> rewardVideoAd complete");
            TToast.show(mContext, "rewardVideoAd complete");
        }

        @Override
        public void onVideoError() {
            Log.e(TAG, "Callback --> rewardVideoAd error");
            TToast.show(mContext, "rewardVideoAd error");
        }

        //After the video is played, the reward verification callback,
        // rewardVerify : whether it is valid, rewardAmount : reward combing, rewardName : reward name
        @Override
        public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName, int errorCode, String errorMsg) {
            TToast.show(mContext, "verify:" + rewardVerify + " amount:" + rewardAmount +
                    " name:" + rewardName + " errorCode:" + errorCode + " errorMsg:" + errorMsg);
            String logString = "verify:" + rewardVerify + " amount:" + rewardAmount +
                    " name:" + rewardName;
            Log.e(TAG, "Callback --> " + logString);
            TToast.show(mContext, logString);
        }

        @Override
        public void onSkippedVideo() {
            TToast.show(mContext, "onSkippedVideo");
        }
    }
}
