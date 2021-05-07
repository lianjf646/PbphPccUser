package com.pbph.pcc.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.pbph.pcc.bean.request.GetValidCodeRequestBean;
import com.pbph.pcc.bean.response.BaseResponseBean;
import com.pbph.pcc.bean.response.GetValidCodeResponseBean;
import com.pbph.pcc.http.HttpAction;
import com.pbph.pcc.json.JsonMananger;
import com.pbph.pcc.tools.AMUtils;
import com.pbph.pcc.tools.DownTimer;
import com.utils.StringUtils;


public class UpdatePhoneActivity extends AppCompatActivity {

    PccApplication application;
    public Context context = this;

    TextView tv_title;

    ViewCard card1, card2;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application = (PccApplication) getApplication();
        setContentView(R.layout.activity_update_phone);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("更换手机号");
        findViewById(R.id.btn_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        View view = findViewById(R.id.includephonecode1);
        card1 = new ViewCard(view);

        String str = application.getUsername();
        if (StringUtils.isEmpty(str)) {
            card1.edt_update_phone_username.setHint("请输入原手机号");
        } else {
            card1.edt_update_phone_username.setText(application.getUsername());
            card1.edt_update_phone_username.setEnabled(false);
            card1.edt_update_phone_username.setFocusable(false);
            card1.edt_update_phone_username.setFocusableInTouchMode(false);
        }

        card1.btn_login_submit.setText("验    证");
        card1.btn_login_submit.setOnClickListener(oneClickListener);

        view = findViewById(R.id.includephonecode2);
        card2 = new ViewCard(view);
        card2.edt_update_phone_username.setHint("请输入新手机号");
        card2.btn_login_submit.setOnClickListener(twoClickListener);
        card2.gone();

    }

    String callOne;
    OnSingleClickListener oneClickListener = new OnSingleClickListener() {
        @Override
        public void onCanClick(View view) {

            callOne = card1.edt_update_phone_username.getText().toString().trim();

            if (StringUtils.isEmpty(callOne)) {
                Toast.makeText(context, "请输入手机号!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!AMUtils.isMobile(callOne)) {
                Toast.makeText(context, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            }


            String str2 = card1.edt_update_phone_code.getText().toString().trim();

            if (StringUtils.isEmpty(str2)) {
                Toast.makeText(context, "请输入验证码", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!AMUtils.isYZm(str2)) {
                Toast.makeText(context, "验证码不正确", Toast.LENGTH_SHORT).show();
                return;
            }
            changePhoneOne(callOne, str2, view);
        }
    };


    OnSingleClickListener twoClickListener = new OnSingleClickListener() {
        @Override
        public void onCanClick(View view) {

            String str1 = card2.edt_update_phone_username.getText().toString().trim();

            if (StringUtils.isEmpty(str1)) {
                Toast.makeText(context, "请输入手机号", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!AMUtils.isMobile(str1)) {
                Toast.makeText(context, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            }

            if (StringUtils.isEqual(callOne, str1)) {
                Toast.makeText(context, "请输入新手机号", Toast.LENGTH_SHORT).show();
                return;
            }


            String str2 = card2.edt_update_phone_code.getText().toString().trim();

            if (StringUtils.isEmpty(str2)) {
                Toast.makeText(context, "请输入验证码", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!AMUtils.isYZm(str2)) {
                Toast.makeText(context, "验证码不正确", Toast.LENGTH_SHORT).show();
                return;
            }
            changePhoneTwo(str1, str2, view);
        }
    };


    private final class ViewCard implements DownTimer.DownTimerListener {

        public View includephonecode;
        public EditText edt_update_phone_username;
        public EditText edt_update_phone_code;
        public TextView tv_update_phone_getcode;

        public Button btn_login_submit;

        DownTimer downTimer;

        public ViewCard(View view) {

            includephonecode = view;

            edt_update_phone_username = includephonecode.findViewById(R.id.edt_update_phone_username);
            edt_update_phone_code = includephonecode.findViewById(R.id.edt_update_phone_code);
            tv_update_phone_getcode = includephonecode.findViewById(R.id.tv_update_phone_getcode);
            tv_update_phone_getcode.setOnClickListener(getValidCodeClick);

            btn_login_submit = includephonecode.findViewById(R.id.btn_login_submit);
        }

        OnSingleClickListener getValidCodeClick = new OnSingleClickListener() {
            @Override
            public void onCanClick(View v) {


                String str = edt_update_phone_username.getText().toString().trim();
                if (StringUtils.isEmpty(str)) {
                    Toast.makeText(context, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }

                getValidCode(str);
            }
        };


        @Override
        public void onTick(long millisUntilFinished) {
            tv_update_phone_getcode.setBackgroundResource(R.color.dark_gray);
            tv_update_phone_getcode.setText(String.valueOf(millisUntilFinished / 1000) + "秒后可重发");
            tv_update_phone_getcode.setClickable(false);
        }

        @Override
        public void onFinish() {
            tv_update_phone_getcode.setBackgroundResource(R.color.main_tab_color_bg);
            tv_update_phone_getcode.setText(R.string.btn_login_getcode_text);
            tv_update_phone_getcode.setClickable(true);
        }

        private void getValidCode(String phone) {

            GetValidCodeRequestBean bean = new GetValidCodeRequestBean(phone);
            HttpAction.getInstance().sendSmsValCode(bean, new StringCallback() {
                @Override
                public void onSuccess(Response<String> response) {
                    LogUtils.e("res=  " + response.body());
                    try {

                        GetValidCodeResponseBean bean = JsonMananger.jsonToBean(response.body(), GetValidCodeResponseBean.class);
                        if (StringUtils.isEqual(bean.getCode(), "200")) {
                            downTimer = new DownTimer();
                            downTimer.setListener(ViewCard.this);
                            downTimer.startDown(60 * 1000);
                        } else {
                            Toast.makeText(context, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, R.string.connect_error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        public void gone() {

            includephonecode.setVisibility(View.GONE);

            if (downTimer != null)
                try {
                    downTimer.stopDown();
                } catch (Exception ignored) {

                }
            downTimer = null;
        }

    }


    private void changePhoneOne(String userPhoneOne, String verifyCodeOne, final View view) {
        view.setClickable(false);

        HttpAction.getInstance().changePhoneOne(PccApplication.getUserid(), userPhoneOne, verifyCodeOne, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("res=  " + response.body());
                view.setClickable(true);
                try {
                    BaseResponseBean vo = JsonMananger.jsonToBean(response.body(), BaseResponseBean.class);

                    if (!StringUtils.isEqual(vo.code, "200")) {
                        Toast.makeText(context, StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    tv_title.setText("新账号");
                    card1.gone();
                    card2.includephonecode.setVisibility(View.VISIBLE);
                    card2.edt_update_phone_username.requestFocus();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "系统错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void changePhoneTwo(final String userPhoneOne, String verifyCodeOne, final View view) {
        view.setClickable(false);

        HttpAction.getInstance().changePhoneTwo(PccApplication.getUserid(), userPhoneOne, verifyCodeOne, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("res=  " + response.body());
                view.setClickable(true);
                try {
                    BaseResponseBean vo = JsonMananger.jsonToBean(response.body(), BaseResponseBean.class);

                    if (!StringUtils.isEqual(vo.code, "200")) {
                        Toast.makeText(context, StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    
                    application.setUsername(userPhoneOne);

                    Toast.makeText(context, "更改成功", Toast.LENGTH_SHORT).show();
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "系统错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {

        card1.gone();
        card2.gone();

        HttpAction.getInstance().changePhoneOneCancel();
        HttpAction.getInstance().changePhoneTwoCancel();

        super.onDestroy();
    }
}
