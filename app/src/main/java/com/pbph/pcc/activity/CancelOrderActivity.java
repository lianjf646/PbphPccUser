package com.pbph.pcc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ui.OnSingleClickListener;
import com.android.utils.LogUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pbph.pcc.R;
import com.pbph.pcc.bean.response.OrderStateBean;
import com.pbph.pcc.http.HttpAction;
import com.pbph.pcc.json.JsonMananger;
import com.utils.StringUtils;

public class CancelOrderActivity extends AppCompatActivity {

    int orderId = 0;


    public Context context = this;
    LayoutInflater inflater;

    TextView tv_title;

    RadioButton[] radioButtons = new RadioButton[5];

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        orderId = getIntent().getIntExtra("orderId", 0);

        inflater = LayoutInflater.from(this);

        setContentView(R.layout.activity_cancelorder);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("取消订单");
        findViewById(R.id.btn_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        radioButtons[0] = (RadioButton) findViewById(R.id.radioButton1);
        radioButtons[1] = (RadioButton) findViewById(R.id.radioButton2);
        radioButtons[2] = (RadioButton) findViewById(R.id.radioButton3);
        radioButtons[3] = (RadioButton) findViewById(R.id.radioButton4);
        radioButtons[4] = (RadioButton) findViewById(R.id.radioButton5);

        for (int i = 0; i < radioButtons.length; i++) {
            radioButtons[i].setTag(String.valueOf(i + 1));
            radioButtons[i].setOnCheckedChangeListener(onCheckedChangeListener);
        }
        editText = (EditText) findViewById(R.id.editText);

        findViewById(R.id.button1).setOnClickListener(onSingleClickListener);

        radioButtons[0].setChecked(true);
    }

    String cancelReason = "1";
    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (!b) return;
//            cancelReason = compoundButton.getText().toString().trim();
            cancelReason = compoundButton.getTag().toString();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {
        @Override
        public void onCanClick(View v) {
            switch (v.getId()) {
                case R.id.button1: {
                    String cancelDetail = editText.getText().toString().trim();
                    cancelOrder(cancelReason, cancelDetail, v);
                }
                break;
            }
        }
    };


    private void cancelOrder(String cancelReason, String cancelDetail, final View v) {
        v.setClickable(false);
        HttpAction.getInstance().cancelOrder(orderId, cancelReason, cancelDetail,
                new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogUtils.e("res=  " + response.body());
                        try {
                            OrderStateBean vo = JsonMananger.jsonToBean(response.body(), OrderStateBean.class);

                            if (!StringUtils.isEqual(vo.code, "200")) {
                                Toast.makeText(context, StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                                v.setClickable(true);
//                                return;
                            }

                            int orderState = -1;
                            if (!StringUtils.isEmpty(vo.getData().getOrderStatus())) {
                                try {
                                    orderState = Integer.parseInt(vo.getData().getOrderStatus());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                    orderState = -1;
                                }
                            }

                            setResult(RESULT_OK, new Intent().putExtra("orderState", orderState));
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "系统错误", Toast.LENGTH_SHORT).show();
                            v.setClickable(true);
                        }
                    }
                });
    }
}
