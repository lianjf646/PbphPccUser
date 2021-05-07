package com.pbph.pcc.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ui.OnSingleClickListener;
import com.android.utils.LogUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pbph.pcc.R;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.request.BaseRequestBean;
import com.pbph.pcc.bean.request.GetMyInfoRequestBean;
import com.pbph.pcc.bean.request.GetValidCodeRequestBean;
import com.pbph.pcc.bean.request.LoginRequestBean;
import com.pbph.pcc.bean.response.GetMyInfoResponseBean;
import com.pbph.pcc.bean.response.GetValidCodeResponseBean;
import com.pbph.pcc.bean.response.LoginResponseBean;
import com.pbph.pcc.http.HttpAction;
import com.pbph.pcc.json.JsonMananger;
import com.pbph.pcc.tools.AMUtils;
import com.pbph.pcc.tools.DownTimer;
import com.pbph.pcc.tools.WaitUI;
import com.pbph.pcc.update.UpdateManager;
import com.utils.StringUtils;

public class LoginActivity extends BaseActivity implements DownTimer.DownTimerListener {

    private EditText mUsername, mCode;
    private TextView getCodeTextView = null;
    private String mCodeString = "", mUserNameString = "";


    Button mEmailSignInButton;


    DownTimer downTimer = new DownTimer();

    private int first = 0;
    public boolean edge_out_id = false;
    private UpdateManager manager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = UpdateManager.getInstance(this);
        manager.checkVersion();
        setContentView(R.layout.activity_login);
        downTimer.setListener(this);
        mUsername = (EditText) findViewById(R.id.edt_login_username);
        mCode = (EditText) findViewById(R.id.edt_login_code);
        getCodeTextView = (TextView) findViewById(R.id.tv_login_getcode);
        mEmailSignInButton = (Button) findViewById(R.id.btn_login_submit);
        getCodeTextView.setOnClickListener(onSingleClickListener);
        mEmailSignInButton.setOnClickListener(onSingleClickListener);

        mUsername.setText(application.getUsername());
//        initDialog();
    }

    private void getValidCode(String phone) {
        WaitUI.Show(this);
        GetValidCodeRequestBean bean = new GetValidCodeRequestBean(phone);
        HttpAction.getInstance().sendSmsValCode(bean, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                WaitUI.Cancel();
                try {
                    if (null == response || response.code() != 200) {
                        Toast.makeText(LoginActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    LogUtils.e("res=  " + response.body());
                    GetValidCodeResponseBean bean = JsonMananger.jsonToBean(response.body(), GetValidCodeResponseBean.class);
                    if (StringUtils.isEqual(bean.getCode(), "200")) {
                        downTimer.startDown(60 * 1000);
                    } else {
                        Toast.makeText(LoginActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    private void userLogin(final String phone, String validCode) {

        mEmailSignInButton.setClickable(false);
        WaitUI.Show(this);
        LoginRequestBean bean = new LoginRequestBean(phone, validCode, PccApplication.getImei());
        HttpAction.getInstance().userLogin(bean, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {

                try {
                    if (null == response || response.code() != 200) {
                        Toast.makeText(LoginActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
                        mEmailSignInButton.setClickable(true);
                        WaitUI.Cancel();
                        return;
                    }
                    LogUtils.e("res=  " + response.body());
                    LoginResponseBean loginResponse = JsonMananger.jsonToBean(response.body(), LoginResponseBean.class);

                    if (!StringUtils.isEqual(loginResponse.getCode(), "200")) {
                        Toast.makeText(LoginActivity.this, loginResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        mEmailSignInButton.setClickable(true);
                        WaitUI.Cancel();
                        return;
                    }
                    application.httpRequestData.setToken(loginResponse.getData().getToken());
                    application.setUserID(loginResponse.getData().getUserId());
                    application.setUsername(phone);
                    getMyinfo();

                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    mEmailSignInButton.setClickable(true);
                }

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                WaitUI.Cancel();
                mEmailSignInButton.setClickable(true);
            }
        });
    }


    private void getMyinfo() {

        BaseRequestBean bean = new GetMyInfoRequestBean(PccApplication.getUserid());
        HttpAction.getInstance().queryMyInfo(bean, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                WaitUI.Cancel();
                try {
                    if (null == response || response.code() != 200) {
                        Toast.makeText(LoginActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
                        mEmailSignInButton.setClickable(true);
                        return;
                    }
                    LogUtils.e("getMyinfo" + "res=  " + response.body());
                    GetMyInfoResponseBean bean = JsonMananger.jsonToBean(response.body(), GetMyInfoResponseBean.class);
                    if (!StringUtils.isEqual(bean.getCode(), "200")) {
                        Toast.makeText(LoginActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        mEmailSignInButton.setClickable(true);
                        return;
                    }
                    if (bean.getData().getUserStatus() == 5 && StringUtils.isEmpty(bean.getData().getSchoolId())) {
                        Toast.makeText(LoginActivity.this, "您的身份为学校管理员，请去绑定您的学校吧", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }

                    application.setMyInfoData(bean.getData());
                    startActivity(new Intent(LoginActivity.this, MainTabActivity.class));
                    finish();
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    mEmailSignInButton.setClickable(true);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                WaitUI.Cancel();
                mEmailSignInButton.setClickable(true);
            }

        });
    }

    OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {


        @Override
        public void onCanClick(View view) {

            mUserNameString = mUsername.getText().toString();
            switch (view.getId()) {
                case R.id.tv_login_getcode:
                    if (TextUtils.isEmpty(mUserNameString)) {
                        Toast.makeText(LoginActivity.this, R.string.phone_number_is_null, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!AMUtils.isMobile(mUserNameString)) {
                        Toast.makeText(LoginActivity.this, R.string.Illegal_phone_number, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    getValidCode(mUserNameString);
                    break;
                case R.id.btn_login_submit:
                    mCodeString = mCode.getText().toString();
                    if (TextUtils.isEmpty(mUserNameString)) {
                        Toast.makeText(LoginActivity.this, R.string.phone_number_is_null, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!AMUtils.isMobile(mUserNameString)) {
                        Toast.makeText(LoginActivity.this, R.string.Illegal_phone_number, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(mCodeString)) {
                        Toast.makeText(LoginActivity.this, "请填写验证码", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    if (!AMUtils.isYZm(mCodeString)) {
                        Toast.makeText(LoginActivity.this, "验证码不正确", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    userLogin(mUserNameString, mCodeString);
                    break;
            }
        }
    };


    @Override
    public void onTick(long millisUntilFinished) {
        getCodeTextView.setBackgroundResource(R.color.dark_gray);
        getCodeTextView.setText(String.valueOf(millisUntilFinished / 1000) + "秒后可重发");
        getCodeTextView.setClickable(false);
    }

    @Override
    public void onFinish() {
        getCodeTextView.setBackgroundResource(R.color.main_tab_color_bg);
        getCodeTextView.setText(R.string.btn_login_getcode_text);
        getCodeTextView.setClickable(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        edge_out_id = getIntent().getBooleanExtra("kickedByOtherClient", false);
        if (edge_out_id == true) {
            showDialog();
//            mHintDialog.show();
        }
    }

    private void showDialog() {
        AlertDialog.Builder mHintDialog = null;
        if (mHintDialog == null) {
            mHintDialog = new AlertDialog.Builder(LoginActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            mHintDialog.setTitle("提示:");
            mHintDialog.setMessage("您的账号在别的设备上登入，被迫下线");
            mHintDialog.setPositiveButton("知道了", null);
        }
        mHintDialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        downTimer.stopDown();
        downTimer = null;
    }
}

