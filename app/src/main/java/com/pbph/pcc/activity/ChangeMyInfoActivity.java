package com.pbph.pcc.activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.utils.LogUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pbph.pcc.R;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.response.BaseResponseBean;
import com.pbph.pcc.http.HttpAction;
import com.pbph.pcc.json.JsonMananger;
import com.pbph.pcc.tools.AMUtils;
import com.utils.StringUtils;

/**
 * Created by Administrator on 2017/10/12.
 */

public class ChangeMyInfoActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mFinish;
    private TextView mTitle;
    private TextView mSave;
    private TextView tv_change;
    private String mType;
    private String userImg, userName, mSexType, userAge;
    private EditText et_person_info_nickname;
    private PccApplication mApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changemyinfo);
        initView();
        initData();
    }


    private void initView() {
        mFinish = (ImageView) findViewById(R.id.btn_left);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mSave = (TextView) findViewById(R.id.btn_save);
        tv_change = (TextView) findViewById(R.id.tv_change);
        et_person_info_nickname = (EditText) findViewById(R.id.et_person_info_nickname);
        mFinish.setOnClickListener(this);
        mTitle.setOnClickListener(this);
        mSave.setOnClickListener(this);
    }

    private void initData() {
        mApp = (PccApplication) getApplication();
        mTitle.setText("修改信息");
        mType = getIntent().getStringExtra("Type");
        userImg = getIntent().getStringExtra("userImg");
        userName = getIntent().getStringExtra("userName");
        mSexType = getIntent().getStringExtra("mSexType");
        userAge = getIntent().getStringExtra("userAge");
        if (mType.equals("nickName")) {
            tv_change.setText("昵称:");
            et_person_info_nickname.setText(userName);
        } else if (mType.equals("nickAge")) {
            tv_change.setText("年龄:");
            et_person_info_nickname.setText(userAge);
            et_person_info_nickname.setInputType(InputType.TYPE_CLASS_NUMBER);
            InputFilter[] filters = {new InputFilter.LengthFilter(3)};
            et_person_info_nickname.setFilters(filters);

        }
    }

    private void updateUser(final String userImg, final String userName, final String userSex, final String userAge) {
        HttpAction.getInstance().updateUser(Integer.parseInt(PccApplication.getUserid()), userImg, userName, userSex, userAge, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("res=  " + response.body());
                try {
                    BaseResponseBean vo = JsonMananger.jsonToBean(response.body(), BaseResponseBean.class);

                    if (!StringUtils.isEqual(vo.code, "200")) {
                        Toast.makeText(ChangeMyInfoActivity.this, StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(ChangeMyInfoActivity.this, "更改成功", Toast.LENGTH_SHORT).show();
                    mApp.getMyInfoData().setUserImg(userImg);
                    mApp.getMyInfoData().setUserName(userName);
                    mApp.getMyInfoData().setUserSex(userSex);
                    mApp.getMyInfoData().setUserAge(userAge);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ChangeMyInfoActivity.this, "系统错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_save:
                if (AMUtils.hasEmoji(et_person_info_nickname.getText().toString())) {
                    Toast.makeText(mApp, "不支持表情", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mType.equals("nickName")) {
                    if (et_person_info_nickname.getText().toString().equals("")) {
                        Toast.makeText(mApp, "请输入姓名", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    updateUser(userImg, et_person_info_nickname.getText().toString(), mSexType, userAge);
                } else if (mType.equals("nickAge")) {
                    if (et_person_info_nickname.getText().toString().equals("")) {
                        Toast.makeText(mApp, "您输入的年龄不在范围内", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (10 < Integer.valueOf(et_person_info_nickname.getText().toString()) &&
                            Integer.valueOf(et_person_info_nickname.getText().toString()) < 100) {
                        updateUser(userImg, userName, mSexType, et_person_info_nickname.getText().toString());
                    } else {
                        Toast.makeText(mApp, "您输入的年龄不在范围内", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
