package com.android.ui;

import android.view.View;

/**
 * Created by Administrator on 2017/9/18.
 */


public abstract class OnSingleClickListener implements View.OnClickListener {

    private static final int MIN_CLICK_DELAY_TIME = 300;

    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime < MIN_CLICK_DELAY_TIME) return;
        lastClickTime = currentTime;
        onCanClick(v);
    }

    public abstract void onCanClick(View v);

}
