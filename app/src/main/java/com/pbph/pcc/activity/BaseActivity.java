package com.pbph.pcc.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.pbph.pcc.R;
import com.pbph.pcc.application.PccApplication;

public class BaseActivity extends AppCompatActivity {
    PccApplication application = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application = (PccApplication) getApplication();
    }

    /**
     * @param titleView          标题布局
     * @param title              标题内容
     * @param leftButtonVisible  左按钮是否显示
     * @param rightButtonVisible 右按钮是否显示
     * @param leftResID          左按钮图片
     * @param rightResID         右按钮图片
     */

    protected void initTitle(@NonNull View titleView, @NonNull String title, @NonNull boolean leftButtonVisible, @NonNull boolean rightButtonVisible, @DrawableRes int leftResID, @DrawableRes int rightResID) {

        TextView titleTextView = titleView.findViewById(R.id.tv_title);
        titleTextView.setText(title);
        ImageView leftButton = titleView.findViewById(R.id.btn_left);
        ImageView rightButton = titleView.findViewById(R.id.btn_right);
        leftButton.setVisibility(leftButtonVisible ? View.VISIBLE : View.INVISIBLE);
        rightButton.setVisibility(rightButtonVisible ? View.VISIBLE : View.INVISIBLE);
        leftButton.setImageResource(leftResID);
        rightButton.setImageResource(rightResID);

    }

    protected void setTitle(View view, String title) {

    }

    @Override
    protected void onPause() {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm.isActive()) imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();

    }
}
