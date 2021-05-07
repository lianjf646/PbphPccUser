package com.pbph.pcc.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.android.utils.LogUtils;
import com.pbph.pcc.R;
import com.pbph.pcc.broadcast.BroadcastManager;
import com.pbph.pcc.fragment.WebViewOtherFragment;
import com.pbph.pcc.tools.ConstantData;


public class MyBrowsersActivity extends FragmentActivity {
    WebViewOtherFragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_browsers);
        String current_url = getIntent().getStringExtra("url");
        LogUtils.e("current_url=" + current_url);
        String title = getIntent().getStringExtra("title");
        fragment = WebViewOtherFragment.newInstance(current_url, title);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content_frame, fragment).commit();
//        BroadcastManager.getInstance(this).addAction(ConstantData.CLOSE_PAGE_ACTION, new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                finish();
//            }
//        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
        fragment.onKeyDown(keyCode, event);
//        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        try {
//            BroadcastManager.getInstance(this).destroy(ConstantData.CLOSE_PAGE_ACTION);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
