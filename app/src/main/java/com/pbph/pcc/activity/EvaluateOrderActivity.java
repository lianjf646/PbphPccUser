package com.pbph.pcc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
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

/**
 * Created by Administrator on 2017/10/12.
 */

public class EvaluateOrderActivity extends BaseActivity {

    private int orderId;

    private ImageView mEvaluateLeft;
    private TextView mTvCommit;
    private RatingBar mRatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_evaluate_order);
        initView();
    }

    private void initData() {
        orderId = getIntent().getIntExtra("orderId", 0);
    }

    private void initView() {
        mEvaluateLeft = (ImageView) findViewById(R.id.evaluate_left);
        mTvCommit = (TextView) findViewById(R.id.btn_commit);
        mRatingBar = (RatingBar) findViewById(R.id.evaluate_ratingBar);

        mEvaluateLeft.setOnClickListener(onSingleClickListener);
        mTvCommit.setOnClickListener(onSingleClickListener);
    }


    OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {

        @Override
        public void onCanClick(View view) {
            switch (view.getId()) {
                case R.id.evaluate_left:
                    finish();
                    break;
                case R.id.btn_commit:
                    appraiseOrder(String.valueOf(mRatingBar.getRating()), "", view);
                    break;
            }
        }
    };

    private void appraiseOrder(String appraiseStar, String appraiseContent, final View view) {
        view.setClickable(false);
        HttpAction.getInstance().appraiseOrder(orderId, appraiseStar, appraiseContent,
                new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogUtils.e("res=  " + response.body());
                        try {
                            OrderStateBean vo = JsonMananger.jsonToBean(response.body(), OrderStateBean.class);

                            if (!StringUtils.isEqual(vo.code, "200")) {
                                Toast.makeText(EvaluateOrderActivity.this, StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                                view.setClickable(true);
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
                            Toast.makeText(EvaluateOrderActivity.this, "系统错误", Toast.LENGTH_SHORT).show();
                            view.setClickable(true);
                        }
                    }
                });
    }

}
