package com.pbph.pcc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pbph.pcc.R;
import com.pbph.pcc.application.PccApplication;
import com.utils.VibrateUtils;


/**
 * Created by Administrator on 2017/10/19.
 */


public class SetAppActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private CheckBox mSetMessageCB, mSetVoiceCB, mSetShackCB;
    private RelativeLayout mKnowRelative;
    private PccApplication mApp;
    private View mHeardView;
    private ImageView mSetAppAtyFinish;
    private TextView mTitleTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_app);

        initView();
        initData();
    }


    private void initView() {
        mSetMessageCB = (CheckBox) findViewById(R.id.cb_message);
        mSetVoiceCB = (CheckBox) findViewById(R.id.cb_voice);
        mSetShackCB = (CheckBox) findViewById(R.id.cb_shack);
        mKnowRelative = (RelativeLayout) findViewById(R.id.relative_know_me);
        mHeardView = findViewById(R.id.set_app_include);
        mSetAppAtyFinish = mHeardView.findViewById(R.id.btn_left);
        mTitleTv = (TextView) findViewById(R.id.tv_title);


    }

    private void initData() {
        mApp = (PccApplication) getApplication();
        mKnowRelative.setOnClickListener(this);
        mSetMessageCB.setOnCheckedChangeListener(this);
        mSetVoiceCB.setOnCheckedChangeListener(this);
        mSetShackCB.setOnCheckedChangeListener(this);

        mSetMessageCB.setChecked(mApp.settingData.getSetMessage());
        mSetVoiceCB.setChecked(mApp.settingData.getSetVoice());
        mSetShackCB.setChecked(mApp.settingData.getSetShack());
        mSetAppAtyFinish.setOnClickListener(this);
        mTitleTv.setText("设置");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relative_know_me:
                startActivity(new Intent(this, KnowMeActivity.class));
                break;
            case R.id.btn_left:
                finish();
                break;

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.cb_message:
                mApp.settingData.setSetMessage(b);
                break;
            case R.id.cb_voice:
                mApp.settingData.setSetVoice(b);
                break;
            case R.id.cb_shack:
                mApp.settingData.setSetShack(b);
                if (b == true) {
                    VibrateUtils.vibrateOnce(SetAppActivity.this, 500);
                }
                break;
        }
    }
}
