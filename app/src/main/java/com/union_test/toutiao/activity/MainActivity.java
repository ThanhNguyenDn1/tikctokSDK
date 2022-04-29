package com.union_test.toutiao.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.union_test.internationad.R;
import com.union_test.toutiao.activity.expressad.BannerActivity;
import com.union_test.toutiao.activity.nativead.FeedListActivity;
import com.union_test.toutiao.activity.nativead.FeedRecyclerActivity;
import com.union_test.toutiao.activity.nativead.NativeAppOpenAdActivity;
import com.union_test.toutiao.activity.testtools.AllTestToolActivity;
import com.union_test.toutiao.config.TTAdManagerHolder;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.activity_main);

        bindButton(R.id.btn_main_feed_lv, FeedListActivity.class);
        bindButton(R.id.btn_main_feed_rv, FeedRecyclerActivity.class);

        bindButton(R.id.btn_banner_ad, BannerActivity.class);

        bindButton(R.id.btn_mian_reward, RewardVideoActivity.class);
        bindButton(R.id.btn_main_full, FullScreenVideoActivity.class);
        bindButton(R.id.btn_main_app_open_ad, NativeAppOpenAdActivity.class);



        bindButton(R.id.btn_api_example, ApiExampleActivity.class);
        bindButton(R.id.btn_all_test_tool, AllTestToolActivity.class);


        TextView tvVersion = findViewById(R.id.tv_version);
        String ver = getString(R.string.main_sdk_version_tip, TTAdManagerHolder.get().getSDKVersion());
        tvVersion.setText(ver);

    }

    private void bindButton(@IdRes int id, final Class clz) {
        findViewById(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, clz);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
