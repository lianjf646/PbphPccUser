package com.pbph.pcc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pbph.pcc.R;

/**
 * Created by Administrator on 2017/10/19.
 */

public class ImmediatelyApplyActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mImmediatelyApply;
    private View view;
    private ImageView mApplyFinish;
    private TextView mApplyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immediately_apply);
        initView();
        initData();
    }


    private void initView() {
        mImmediatelyApply = (ImageView) findViewById(R.id.iv_immediately_apply);
        view = findViewById(R.id.apply_heard);
        mApplyFinish = view.findViewById(R.id.btn_left);
        mApplyTextView = view.findViewById(R.id.tv_title);

    }

    private void initData() {
        mApplyTextView.setText("立即申请");
        mImmediatelyApply.setOnClickListener(this);
        mApplyFinish.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_immediately_apply:
                startActivity(new Intent(ImmediatelyApplyActivity.this, CertificationActivity.class));
                finish();
                break;
            case R.id.btn_left:
                finish();
                break;
        }
    }
}
