package com.pbph.pcc.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pbph.pcc.R;

/**
 * Created by Administrator on 2017/10/19.
 */

public class KnowMeActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mKnowFinish;
    private TextView mKnowTitle;
    private View mHeardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_me);
        initView();
        initData();
    }


    private void initView() {
        mHeardView = findViewById(R.id.know_heard);
        mKnowFinish = mHeardView. findViewById(R.id.btn_left);
        mKnowTitle =mHeardView. findViewById(R.id.tv_title);


    }

    private void initData() {
        mKnowFinish.setOnClickListener(this);
        mKnowTitle.setText("关于我们");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
        }
    }
}
