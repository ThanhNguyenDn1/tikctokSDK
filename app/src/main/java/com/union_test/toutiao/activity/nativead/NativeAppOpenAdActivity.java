package com.union_test.toutiao.activity.nativead;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppOpenAd;
import com.union_test.internationad.R;
import com.union_test.toutiao.PangleAppOpenAdManager;
import com.union_test.toutiao.RitConstants;
import com.union_test.toutiao.config.TTAdManagerHolder;
import com.union_test.toutiao.utils.TToast;

public class NativeAppOpenAdActivity extends AppCompatActivity {

    private View mLoadVerticalAd;
    private View mLoadAd;
    private View showAd;
    private TTAdNative mAdNative;
    private TTAdNative.AppOpenAdListener mAppOpenAdListener;
    private TTAppOpenAd ttAppOpenAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_open_ad);
        findViewById(R.id.btn_fsv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLoadVerticalAd = findViewById(R.id.btn_app_open_ad_vertical_load);
        mLoadAd = findViewById(R.id.btn_app_open_ad_load);
        showAd = findViewById(R.id.btn_app_open_ad_show);
        mAdNative = TTAdManagerHolder.get().createAdNative(this);
        mAppOpenAdListener = new TTAdNative.AppOpenAdListener(){
            @Override
            public void onError(int code, String message) {
                TToast.show(NativeAppOpenAdActivity.this.getApplicationContext(), "code: " + code + " message: " + message);
            }

            @Override
            public void onAppOpenAdLoaded(TTAppOpenAd ad) {
                ttAppOpenAd = ad;
                TToast.show(NativeAppOpenAdActivity.this.getApplicationContext(), "load success");
            }
        };
        initClickEvent();
    }

    private void initClickEvent() {
        final int timeout = 3500;
        mLoadAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdSlot adSlot = new AdSlot.Builder()
                        .setCodeId(RitConstants.RIT_OPEN_HORIZONTAL)
                        .build();
                mAdNative.loadAppOpenAd(adSlot, mAppOpenAdListener, timeout);
            }
        });

        mLoadVerticalAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdSlot adSlot = new AdSlot.Builder()
                        .setCodeId(RitConstants.RIT_OPEN_VERTICAL)
                        .build();
                mAdNative.loadAppOpenAd(adSlot, mAppOpenAdListener, timeout);
            }
        });


        showAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ttAppOpenAd != null){
                    ttAppOpenAd.setOpenAdInteractionListener(new TTAppOpenAd.AppOpenAdInteractionListener() {
                        @Override
                        public void onAdShow() {
                            TToast.show(NativeAppOpenAdActivity.this.getApplicationContext().getApplicationContext(), "onAdShow");
                            PangleAppOpenAdManager.setIsShowingAd(true);
                        }

                        @Override
                        public void onAdClicked() {
                            TToast.show(NativeAppOpenAdActivity.this.getApplicationContext(), "onAdClicked");
                        }

                        @Override
                        public void onAdSkip() {
                            TToast.show(NativeAppOpenAdActivity.this.getApplicationContext(), "onAdSkip");
                            PangleAppOpenAdManager.setIsShowingAd(false);
                        }

                        @Override
                        public void onAdCountdownToZero() {
                            TToast.show(NativeAppOpenAdActivity.this.getApplicationContext(), "onAdCountdownToZero");
                            PangleAppOpenAdManager.setIsShowingAd(false);
                        }
                    });
                    ttAppOpenAd.showAppOpenAd(NativeAppOpenAdActivity.this);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAppOpenAdListener = null;
    }
}