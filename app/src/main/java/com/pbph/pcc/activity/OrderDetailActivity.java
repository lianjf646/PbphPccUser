package com.pbph.pcc.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ui.OnSingleClickListener;
import com.android.utils.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pbph.pcc.R;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.response.GetOrderDetailByIdBean;
import com.pbph.pcc.bean.response.GetOrderDetailByIdBean.DataBean.OrderTakeoutSubBean;
import com.pbph.pcc.bean.response.OrderStateBean;
import com.pbph.pcc.http.HttpAction;
import com.pbph.pcc.json.JsonMananger;
import com.pbph.pcc.tools.MoneyUtils;
import com.utils.DoubleUtil;
import com.utils.StringUtils;

import java.util.List;

import io.rong.imkit.RongIM;


public class OrderDetailActivity extends AppCompatActivity {

    int orderId = 0;
    int orderFrom = 0;//1//普通用户 下单  2 抢单用户 接单
    int orderType = 0;//1外卖2超市3快递4任务

    int orderState = 0;

    boolean from = false;//true 从我要赚钱进入此界面。otherwise 其他界面


    GetOrderDetailByIdBean.DataBean orderDetailVo;

    public Context context = this;
    LayoutInflater inflater;

    TextView tv_title;


    View includeprise;

    View ll_orderdetail_prise1;
    TextView tv_orderdetail_prise1;
    View view_orderdetail_prise;
    TextView tv_orderdetail_prise2;

    TextView tv_order_state;


    LinearLayout ll_cancel_evaluate;


    View ll_order_contact_address;
    TextView textViewtype;
    LinearLayout ll_order_simple_info_container;


    LinearLayout ll_orderinfo;

    TextView tv_order_title;
    LinearLayout ll_order_content_container;


    View view_order_content_line;
    View ll_order_content_moneyall;
    TextView tv_money_all;


    LinearLayout ll_orderdetail_card_psxx;
    TextView[] tvs_psxx;


    LinearLayout ll_orderdetail_card_ddxx;
    TextView[] tvs_ddxx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        orderId = getIntent().getIntExtra("orderId", 0);
        orderFrom = getIntent().getIntExtra("orderFrom", 0);
        orderType = getIntent().getIntExtra("orderType", 0);

        orderState = getIntent().getIntExtra("orderState", 0);

        from = getIntent().getBooleanExtra("from", false);


        inflater = LayoutInflater.from(this);


        setContentView(R.layout.activity_orderdetail);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("订单详情");
        findViewById(R.id.btn_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ImageView right = (ImageView) findViewById(R.id.btn_right);
        right.setImageResource(R.drawable.kefu_button);
        right.setVisibility(View.VISIBLE);
        right.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onCanClick(View v) {
                RongIM.getInstance().startCustomerServiceChat(context, PccApplication.RONGYUN_CUSTOMERSERVICEID, "在线客服", null);
            }
        });

        includeprise = findViewById(R.id.includeprise);

        ll_orderdetail_prise1 = includeprise.findViewById(R.id.ll_orderdetail_prise1);
        tv_orderdetail_prise1 = includeprise.findViewById(R.id.tv_orderdetail_prise1);

        view_orderdetail_prise = includeprise.findViewById(R.id.view_orderdetail_prise);

        tv_orderdetail_prise2 = includeprise.findViewById(R.id.tv_orderdetail_prise2);

        tv_order_state = (TextView) findViewById(R.id.tv_order_state);

        ll_cancel_evaluate = (LinearLayout) findViewById(R.id.ll_cancel_evaluate);

        ll_order_contact_address = findViewById(R.id.ll_order_contact_address);
        ll_order_contact_address.setVisibility(View.GONE);
        textViewtype = (TextView) findViewById(R.id.textViewtype);
        ll_order_simple_info_container = (LinearLayout) findViewById(R.id.ll_order_simple_info_container);


        ll_orderinfo = (LinearLayout) findViewById(R.id.ll_orderinfo);

        tv_order_title = (TextView) findViewById(R.id.tv_order_title);
        ll_order_content_container = (LinearLayout) findViewById(R.id.ll_order_content_container);


        view_order_content_line = findViewById(R.id.view_order_content_line);
        ll_order_content_moneyall = findViewById(R.id.ll_order_content_moneyall);
        tv_money_all = (TextView) findViewById(R.id.tv_money_all);


        View includepsxx = findViewById(R.id.includepsxx);
        TextView tv_orderdetail_card_psxx = includepsxx.findViewById(R.id.tv_orderdetail_card);
        tv_orderdetail_card_psxx.setText("派送信息");
        ll_orderdetail_card_psxx = includepsxx.findViewById(R.id.ll_orderdetail_card);

        View includeddxx = findViewById(R.id.includeddxx);
        TextView tv_orderdetail_card_ddxx = includeddxx.findViewById(R.id.tv_orderdetail_card);
        tv_orderdetail_card_ddxx.setText("订单信息");
        ll_orderdetail_card_ddxx = includeddxx.findViewById(R.id.ll_orderdetail_card);

        initView();

        getOrderDetailById();
    }

    @Override
    protected void onDestroy() {
        HttpAction.getInstance().getOrderDetailByIdCancel();
        super.onDestroy();
    }

    OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {
        @Override
        public void onCanClick(View v) {
            switch (v.getId()) {
                case R.id.tv_order_cancel: {
                    doClick(v);
                }
                break;
            }
        }
    };

    private void initView() {
        if (orderFrom == 1) {//普通用户
            initNormalUser();
        } else if (orderFrom == 2) {//抢单用户
            initWorkUser();
        }

        initOrderContent();
        initpsxx();
        initddxx();
    }

    private void setViewData() {

        updateViewData();

        setOrderContent();

        setpsxxData();
        setddxxData();
    }

    private void updateViewData() {
        if (orderFrom == 1) {//普通用户
            setNormalUser();
        } else if (orderFrom == 2) {//抢单用户
            setWorkUser();
        }
    }


    TextView tv_order_cancel;

    View ratingBarView;
    RatingBar ratingBar;


    private void initNormalUser() {

        includeprise.setVisibility(View.GONE);
        tv_order_state.setVisibility(View.VISIBLE);

        initOrderContact();

        addButton();

        tv_order_cancel.setVisibility(View.GONE);
    }

    private void setNormalUser() {

        setOrderContactNormal(!(orderState == 1 || orderState == 0));

        switch (orderState) {//根据不同状态添加按钮模块还是评价模块
            case 0: {//已取消
                tv_order_cancel.setVisibility(View.GONE);

                tv_order_state.setText("已取消");
            }
            break;
            case 1: {//待接单
                tv_order_cancel.setVisibility(View.VISIBLE);

                tv_order_cancel.setText("取消订单");

                tv_order_state.setText("待接单");

                if (orderType == 4) {
                    if (StringUtils.isEqual(orderDetailVo.getTaskStatus(), "7")) {//待审核
                        tv_order_cancel.setVisibility(View.VISIBLE);

                        tv_order_cancel.setText("取消订单");

                        tv_order_state.setText("待审核");
                    } else if (StringUtils.isEqual(orderDetailVo.getTaskStatus(), "8")) {//审核通过

                    } else if (StringUtils.isEqual(orderDetailVo.getTaskStatus(), "9")) {//审核不通过
                        tv_order_cancel.setVisibility(View.GONE);

                        tv_order_state.setText("审核未通过");
                    }

                }
            }
            break;
            case 2: {//已接单
                tv_order_cancel.setVisibility(View.GONE);

                tv_order_state.setText("已接单");
            }
            break;
            case 3: {//已送达
                tv_order_cancel.setVisibility(View.VISIBLE);

                tv_order_cancel.setText("确认送达");

                tv_order_state.setText("已送达");
            }
            break;
            case 4: {//已完成

                tv_order_cancel.setVisibility(View.VISIBLE);

                tv_order_cancel.setText("去评价");

                tv_order_state.setText("已完成");

            }
            break;
            case 5: {//已评价

                tv_order_cancel.setVisibility(View.VISIBLE);

                tv_order_cancel.setText("再来一单");

                tv_order_state.setText("已评价");
            }
            break;
            case 6: {//已撤销

                tv_order_cancel.setVisibility(View.GONE);

                tv_order_state.setText("已撤销");
            }
            break;
        }

    }

    private void initWorkUser() {//添加我要抢单按钮

        includeprise.setVisibility(View.VISIBLE);
        tv_order_state.setVisibility(View.GONE);


        switch (orderType) {
            case 1:
                ll_orderdetail_prise1.setVisibility(View.VISIBLE);
                view_orderdetail_prise.setVisibility(View.VISIBLE);

                view_order_content_line.setVisibility(View.VISIBLE);
                ll_order_content_moneyall.setVisibility(View.VISIBLE);
                break;
            case 2:
                ll_orderdetail_prise1.setVisibility(View.VISIBLE);
                view_orderdetail_prise.setVisibility(View.VISIBLE);

                view_order_content_line.setVisibility(View.VISIBLE);
                ll_order_content_moneyall.setVisibility(View.VISIBLE);
                break;
            case 3:
                ll_orderdetail_prise1.setVisibility(View.GONE);
                view_orderdetail_prise.setVisibility(View.GONE);

                view_order_content_line.setVisibility(View.GONE);
                ll_order_content_moneyall.setVisibility(View.GONE);
                break;
            case 4:
                ll_orderdetail_prise1.setVisibility(View.GONE);
                view_orderdetail_prise.setVisibility(View.GONE);

                view_order_content_line.setVisibility(View.GONE);
                ll_order_content_moneyall.setVisibility(View.GONE);
                break;
        }

//        initOrderAddress();
        initOrderContact();

        addRatingBar();
        addButton();

        tv_order_cancel.setVisibility(View.GONE);
        ratingBarView.setVisibility(View.GONE);
    }

    private void setWorkUser() {//添加我要抢单按钮
        double money = 0;
        switch (orderType) {
            case 1: {
//                double a = Double.parseDouble(orderDetailVo.getShippingAmount());
                double b = Double.parseDouble(orderDetailVo.getDinnerAmount());
                money = DoubleUtil.sum(0, b);
            }
            break;
            case 2: {
//                double a = Double.parseDouble(orderDetailVo.getShippingAmount());
                double b = Double.parseDouble(orderDetailVo.getDinnerAmount());
                money = DoubleUtil.sum(0, b);
            }
            break;
            case 3:
                money = Double.parseDouble(orderDetailVo.getShippingAmount());
                break;
            case 4:
                money = Double.parseDouble(orderDetailVo.getTaskAmount());
                break;
        }

        tv_orderdetail_prise1.setText(MoneyUtils.getMoneyString(money));

        tv_orderdetail_prise2.setText(orderDetailVo.getGettingAmount());


//        setOrderAddress();

        setOrderContactWorker();

        switch (orderState) {//根据不同状态添加按钮模块还是评价模块
            case 0: {//已取消
                tv_order_cancel.setVisibility(View.GONE);
                ratingBarView.setVisibility(View.GONE);
                tv_order_state.setVisibility(View.VISIBLE);
                tv_order_state.setText("unknow");

                ll_order_simple_info_container.setVisibility(View.VISIBLE);
            }
            break;
            case 1: {//待接单
                tv_order_cancel.setVisibility(View.VISIBLE);
                ratingBarView.setVisibility(View.GONE);
                tv_order_state.setVisibility(View.GONE);

                tv_order_cancel.setText("接  单");

                tv_order_state.setText("待接单");

                ll_order_simple_info_container.setVisibility(View.GONE);
            }
            break;
            case 2: {//已接单
                if (clickLazy) {
                    tv_order_cancel.setVisibility(View.INVISIBLE);
                    handler.sendEmptyMessageDelayed(0, 1000);
                } else {
                    tv_order_cancel.setVisibility(View.VISIBLE);
                }

                ratingBarView.setVisibility(View.GONE);
                tv_order_state.setVisibility(View.GONE);

                tv_order_cancel.setText("确认送达");

                tv_order_state.setText("已接单");

                ll_order_simple_info_container.setVisibility(View.VISIBLE);
            }
            break;
            case 3: {//已送达
                tv_order_cancel.setVisibility(View.GONE);
                ratingBarView.setVisibility(View.GONE);
                tv_order_state.setVisibility(View.VISIBLE);

                tv_order_state.setText("等待用户确认");

                ll_order_simple_info_container.setVisibility(View.VISIBLE);
            }
            break;
            case 4: {//已完成
                tv_order_cancel.setVisibility(View.GONE);
                ratingBarView.setVisibility(View.GONE);
                tv_order_state.setVisibility(View.VISIBLE);

                tv_order_state.setText("等待用户评价");

                ll_order_simple_info_container.setVisibility(View.VISIBLE);

            }
            break;
            case 5: {//已评价
                tv_order_cancel.setVisibility(View.GONE);
                ratingBarView.setVisibility(View.VISIBLE);
                tv_order_state.setVisibility(View.GONE);

                setRatingBar();

                tv_order_state.setText("用户评价");

                ll_order_simple_info_container.setVisibility(View.VISIBLE);
            }
            break;
            case 6: {//已撤销
                tv_order_cancel.setVisibility(View.GONE);
                ratingBarView.setVisibility(View.GONE);
                tv_order_state.setVisibility(View.VISIBLE);
                tv_order_state.setText("已撤销");

                ll_order_simple_info_container.setVisibility(View.VISIBLE);
            }
            break;
        }
    }

    private void addButton() {
        tv_order_cancel = (TextView) inflater.inflate(R.layout.layout_orderdetail_cancel_btn, null);
        tv_order_cancel.setOnClickListener(onSingleClickListener);
        ll_cancel_evaluate.addView(tv_order_cancel);

    }

    private void addRatingBar() {
        ratingBarView = inflater.inflate(R.layout.layout_orderdetail_evaluate, null);
        ratingBar = ratingBarView.findViewById(R.id.ratingBar);
        ll_cancel_evaluate.addView(ratingBarView);
    }

    private void setRatingBar() {
        ratingBar.setRating(Float.parseFloat(orderDetailVo.getAppraiseStar()));
    }

    View ll_orderdetail_address1;
    TextView tv_orderdetail_address1;
    TextView tv_orderdetail_address2, tv_orderdetail_address21;

    private void initOrderAddress() {

        View view = inflater.inflate(R.layout.layout_orderdetail_address, null);

        ll_orderdetail_address1 = view.findViewById(R.id.ll_orderdetail_address1);

        tv_orderdetail_address1 = view.findViewById(R.id.tv_orderdetail_address1);
        tv_orderdetail_address2 = view.findViewById(R.id.tv_orderdetail_address2);
        tv_orderdetail_address21 = view.findViewById(R.id.tv_orderdetail_address21);


        ll_order_simple_info_container.addView(view);

    }

    private void setOrderAddress() {

        switch (orderType) {
            case 1:
                tv_orderdetail_address1.setText(orderDetailVo.getGetAddressName());
                if (orderFrom == 2 && orderState <= 1) {//抢单用户
                    tvs_psxx[0].setText(orderDetailVo.getBriefaddressName());
                } else {
                    tvs_psxx[0].setText(orderDetailVo.getReceiveAddressName());
                }
                break;
            case 2:
                ll_orderdetail_address1.setVisibility(View.GONE);
                if (orderFrom == 2 && orderState <= 1) {//抢单用户
                    tvs_psxx[0].setText(orderDetailVo.getBriefaddressName());
                } else {
                    tvs_psxx[0].setText(orderDetailVo.getReceiveAddressName());
                }
                break;
            case 3:
                tv_orderdetail_address1.setText(orderDetailVo.getGetAddressName());
                if (orderFrom == 2 && orderState <= 1) {//抢单用户
                    tvs_psxx[0].setText(orderDetailVo.getBriefaddressName());
                } else {
                    tvs_psxx[0].setText(orderDetailVo.getReceiveAddressName());
                }
                break;
            case 4:
                ll_orderdetail_address1.setVisibility(View.GONE);
                tv_orderdetail_address2.setText(orderDetailVo.getReceiveAddressName());

                tv_orderdetail_address21.setText("任务地点");

                break;
        }

    }

    ImageView iv_orderdetail_contact;
    TextView tv_orderdetail_contact;
    //    ImageView iv_orderdetail_contact_call;
    TextView tv_orderdetail_contact_call;

    private void initOrderContact() {
        View ll_contact = inflater.inflate(R.layout.layout_orderdetail_contact, null);

        iv_orderdetail_contact = ll_contact.findViewById(R.id.iv_orderdetail_contact);
        tv_orderdetail_contact = ll_contact.findViewById(R.id.tv_orderdetail_contact);
//        iv_orderdetail_contact_call = view.findViewById(R.id.iv_orderdetail_contact_call);
        tv_orderdetail_contact_call = ll_contact.findViewById(R.id.tv_orderdetail_contact_call);


        ll_order_simple_info_container.addView(ll_contact);
    }

    private void setOrderContactNormal(boolean b) {
        if (b) {
            ll_order_contact_address.setVisibility(View.VISIBLE);

            Glide.with(this).load(orderDetailVo.getReceiveUserImg()).asBitmap().centerCrop().error(R.drawable.ico_def_photo).placeholder(R.drawable.ico_def_photo).into(new BitmapImageViewTarget(iv_orderdetail_contact) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    iv_orderdetail_contact.setImageDrawable(circularBitmapDrawable);
                }
            });


            tv_orderdetail_contact.setText(orderDetailVo.getReceiveUserName());
            tv_orderdetail_contact_call.setText(orderDetailVo.getReceiveUserPhone());
            tv_orderdetail_contact_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (StringUtils.isEmpty(orderDetailVo.getReceiveUserPhone())) {
                        Toast.makeText(context, "派送人员无电话", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent1 = new Intent(Intent.ACTION_DIAL);
                    intent1.setData(Uri.parse("tel:" + orderDetailVo.getReceiveUserPhone()));
                    startActivity(intent1);
                }
            });
        } else {
            ll_order_contact_address.setVisibility(View.GONE);

//            Glide.with(this).load(orderDetailVo.getReceiveUserImg()).into(iv_orderdetail_contact);
            tv_orderdetail_contact.setText("暂无用户接单");
        }

    }

    private void setOrderContactWorker() {

        ll_order_contact_address.setVisibility(View.VISIBLE);

        Glide.with(this).load(orderDetailVo.getSendUserImg()).asBitmap().centerCrop().error(R.drawable.ico_def_photo).placeholder(R.drawable.ico_def_photo).into(new BitmapImageViewTarget(iv_orderdetail_contact) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                iv_orderdetail_contact.setImageDrawable(circularBitmapDrawable);
            }
        });
        tv_orderdetail_contact.setText(orderDetailVo.getSendUserName());
        tv_orderdetail_contact_call.setText(orderDetailVo.getSendUserPhone());
        tv_orderdetail_contact_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtils.isEmpty(orderDetailVo.getSendUserPhone())) {
                    Toast.makeText(context, "发单人员无电话", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent1 = new Intent(Intent.ACTION_DIAL);
                intent1.setData(Uri.parse("tel:" + orderDetailVo.getSendUserPhone()));
                startActivity(intent1);
            }
        });
    }


    private void initOrderContent() {
        ll_orderinfo.setVisibility(View.GONE);
        switch (orderType) {
            case 1: {
                textViewtype.setText("外卖订单");
                tv_order_title.setText("外卖信息");
                initOrderContent1();
            }
            break;
            case 2: {
                textViewtype.setText("超市订单");
                tv_order_title.setText("商品信息");
                initOrderContent1();
            }
            break;
            case 3: {
                textViewtype.setText("快递订单");
                tv_order_title.setText("快递信息");
                initOrderContent2();
            }
            break;
            case 4: {
                textViewtype.setText("任务订单");
                tv_order_title.setText("任务信息");
                initOrderContent2();
            }
            break;
            default: {
                textViewtype.setText("订单");
                tv_order_title.setText("订单信息");
            }
            break;
        }
    }

    private void setOrderContent() {

        ll_orderinfo.setVisibility(View.VISIBLE);

        if (orderFrom == 2 && orderState == 1 && orderType == 3) {
            ll_orderinfo.setVisibility(View.GONE);
        }

        switch (orderType) {
            case 1: {

                tv_order_title.setText(orderDetailVo.getStoreName());

                if (orderFrom == 1) {
                    tv_peisongfei.setText("￥" + orderDetailVo.getShippingAmount());

                    double a = Double.parseDouble(orderDetailVo.getShippingAmount());
                    double b = Double.parseDouble(orderDetailVo.getDinnerAmount());
                    double money = DoubleUtil.sum(a, b);
                    tv_money_all.setText("￥" + MoneyUtils.getMoneyString(money));
                } else {
                    tv_peisongfei.setText("￥" + orderDetailVo.getGettingAmount());

                    double a = Double.parseDouble(orderDetailVo.getGettingAmount());
                    double b = Double.parseDouble(orderDetailVo.getDinnerAmount());
                    double money = DoubleUtil.sum(a, b);
                    tv_money_all.setText("￥" + MoneyUtils.getMoneyString(money));
                }


                setOrderContent1();
            }
            break;
            case 2: {

                if (orderFrom == 1) {
                    tv_peisongfei.setText("￥" + orderDetailVo.getShippingAmount());

                    double a = Double.parseDouble(orderDetailVo.getShippingAmount());
                    double b = Double.parseDouble(orderDetailVo.getDinnerAmount());
                    double money = DoubleUtil.sum(a, b);
                    tv_money_all.setText("￥" + MoneyUtils.getMoneyString(money));
                } else {
                    tv_peisongfei.setText("￥" + orderDetailVo.getGettingAmount());

                    double a = Double.parseDouble(orderDetailVo.getGettingAmount());
                    double b = Double.parseDouble(orderDetailVo.getDinnerAmount());
                    double money = DoubleUtil.sum(a, b);
                    tv_money_all.setText("￥" + MoneyUtils.getMoneyString(money));
                }

                setOrderContent1();
            }
            break;
            case 3: {
                double a = Double.parseDouble(orderDetailVo.getShippingAmount());
                double b = Double.parseDouble(orderDetailVo.getSurplusAmount());
                double money = DoubleUtil.sum(a, b);
                tv_money_all.setText("￥" + MoneyUtils.getMoneyString(money));

//                tv_money_all.setText("￥" + orderDetailVo.getShippingAmount());
                setOrderContent2();
            }
            break;
            case 4: {
                tv_money_all.setText("￥" + orderDetailVo.getTaskAmount());

                setOrderContent2();
            }
            break;
        }
    }

    TableLayout tableLayout;

    TextView tv_peisongfei_title;
    TextView tv_peisongfei;

    TextView[] good_names;
    TextView[] good_nums;
    TextView[] good_prises;

    private void initOrderContent1() {

        tableLayout = (TableLayout) inflater.inflate(R.layout.layout_orderdetail_type_restaurantorder, null);
        tv_peisongfei_title = tableLayout.findViewById(R.id.tv_peisongfei_title);
        if (orderFrom == 1) {
            tv_peisongfei_title.setText("配送费");
        } else {
            tv_peisongfei_title.setText("佣金");
        }

        tv_peisongfei = tableLayout.findViewById(R.id.tv_peisongfei);

        ll_order_content_container.addView(tableLayout);

    }


    private void setOrderContent1() {

        List<OrderTakeoutSubBean> tempList = null;
        switch (orderType) {
            case 1: {
                tempList = orderDetailVo.getOrderTakeoutSub();
                for (int i = 0, c = tableLayout.getChildCount() - 1; i < c; i++) {
                    tableLayout.removeViewAt(i);
                }
            }
            break;
            case 2: {
                tempList = orderDetailVo.getOrderMarketSub();
                for (int i = 0, c = tableLayout.getChildCount() - 1; i < c; i++) {
                    tableLayout.removeViewAt(i);
                }
            }
            break;
            case 3: {
                tableLayout.removeAllViews();
            }
            break;
            case 4: {
                tableLayout.removeAllViews();
            }
            break;
        }

        if (tempList == null) return;

        int num = tempList.size();

        if (num <= 0) return;

        good_names = new TextView[num];
        good_nums = new TextView[num];
        good_prises = new TextView[num];

        for (int i = 0; i < num; i++) {
            OrderTakeoutSubBean vo = tempList.get(i);

            View view = inflater.inflate(R.layout.tablerow_goodinfo, null);

            good_names[i] = view.findViewById(R.id.name_c1);
            good_nums[i] = view.findViewById(R.id.name_c2);
            good_prises[i] = view.findViewById(R.id.name_c3);


            good_names[i].setText(vo.getGoodsName());
            good_nums[i].setText("×" + vo.getGoodsNum());
            good_prises[i].setText("￥" + MoneyUtils.getMoneyString(vo.getTotalAmount()));

            tableLayout.addView(view, i);
        }
    }

    TextView orderContent;

    private void initOrderContent2() {

        orderContent = (TextView) inflater.inflate(R.layout.layout_orderdetail_type_commissionorder, null);
        ll_order_content_container.addView(orderContent);

    }

    private void setOrderContent2() {
        switch (orderType) {
            case 1: {

            }
            break;
            case 2: {

            }
            break;
            case 3: {
                orderContent.setText(orderDetailVo.getExpressInfo());
            }
            break;
            case 4: {
                orderContent.setText(orderDetailVo.getTaskDescribe());
            }
            break;
        }

    }


    private void initpsxx() {
        String[] strs = getResources().getStringArray(R.array.psxx);

        // orderType = 0;//1外卖2超市3快递4任务
        switch (orderType) {
            case 1: {
            }
            break;
            case 2: {
            }
            break;
            case 3: {
                strs[0] = "";
            }
            break;
            case 4: {
                strs[0] = "";
            }
            break;
        }

        if (orderFrom == 1) {//发单
            strs[1] = "收货地址";
        } else if (orderFrom == 2) {
            strs[1] = "送货地址";
        }


        tvs_psxx = new TextView[strs.length];

        for (int i = 0; i < strs.length; i++) {
            View view = inflater.inflate(R.layout.layout_two_textview_line, null);

            TextView tv_ll_2tv_1 = view.findViewById(R.id.tv_ll_2tv_1);
            tv_ll_2tv_1.setText(strs[i]);

            tvs_psxx[i] = view.findViewById(R.id.tv_ll_2tv_2);

            ll_orderdetail_card_psxx.addView(view);

            if (StringUtils.isEmpty(strs[i])) {
                view.setVisibility(View.GONE);
            }
        }
    }

    //  <item>预计到达</item>
//        <item>送货地址</item>
    private void setpsxxData() {
        tvs_psxx[0].setText(orderDetailVo.getExpectedDeliveryTime());

        switch (orderType) {
            case 1: {

                tvs_psxx[0].setText(orderDetailVo.getExpectedDeliveryTime());

                if (orderFrom == 2 && orderState <= 1) {//抢单用户
                    tvs_psxx[1].setText(orderDetailVo.getBriefaddressName());
                } else {
                    tvs_psxx[1].setText(orderDetailVo.getReceiveAddressName());
                }
            }
            break;
            case 2: {

                tvs_psxx[0].setText(orderDetailVo.getExpectedDeliveryTime());

                if (orderFrom == 2 && orderState <= 1) {//抢单用户
                    tvs_psxx[1].setText(orderDetailVo.getBriefaddressName());
                } else {
                    tvs_psxx[1].setText(orderDetailVo.getReceiveAddressName());
                }
            }
            break;
            case 3: {

//                tvs_psxx[0].setText(orderDetailVo.getExpectedDeliveryTime());

                if (orderFrom == 2 && orderState <= 1) {//抢单用户
                    tvs_psxx[1].setText(orderDetailVo.getBriefaddressName());
                } else {
                    tvs_psxx[1].setText(orderDetailVo.getReceiveAddressName());
                }
            }
            break;
            case 4: {

//                tvs_psxx[0].setText(orderDetailVo.getExpectedDeliveryTime());

                tvs_psxx[1].setText(orderDetailVo.getTaskAddressDetail());
            }
            break;
        }

    }

    private void initddxx() {
        String[] strs = getResources().getStringArray(R.array.ddxx);
        tvs_ddxx = new TextView[strs.length];

        for (int i = 0; i < strs.length; i++) {
            LinearLayout view = (LinearLayout) inflater.inflate(R.layout.layout_two_textview_line, null);

            TextView tv_ll_2tv_1 = view.findViewById(R.id.tv_ll_2tv_1);
            tv_ll_2tv_1.setText(strs[i]);

            tvs_ddxx[i] = view.findViewById(R.id.tv_ll_2tv_2);

            if (i == 0) addCopyButton(view);

            ll_orderdetail_card_ddxx.addView(view);
        }
    }

    View copyBtn;

    private void addCopyButton(LinearLayout far) {
        copyBtn = inflater.inflate(R.layout.layout_textview_copy, null);
        copyBtn.setTag("");
        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str = copyBtn.getTag().toString();

                if (StringUtils.isEmpty(str)) return;

                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(str);
                Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
            }
        });
        far.addView(copyBtn);
    }

    //   <item>订单编号</item>
//        <item>支付方式</item>
//        <item>支付时间</item>
//        <item>订单备注</item>
    private void setddxxData() {
        tvs_ddxx[0].setText(orderDetailVo.getOrderCode());
        tvs_ddxx[1].setText(orderDetailVo.getOrderPayWay());
        tvs_ddxx[2].setText(orderDetailVo.getOrderPayTime());
        tvs_ddxx[3].setText(orderDetailVo.getRemarkInfo());


        copyBtn.setTag(orderDetailVo.getOrderCode());
    }

    private void getOrderDetailById() {

        HttpAction.getInstance().getOrderDetailById(orderId, orderType, orderFrom, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("res=  " + response.body());
                try {
                    GetOrderDetailByIdBean vo = JsonMananger.jsonToBean(response.body(), GetOrderDetailByIdBean.class);

                    if (!StringUtils.isEqual(vo.code, "200")) {
                        Toast.makeText(context, StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //如果是我要赚钱，进入此界面，将订单状态锁死为1.防止出现其他状态用户还能操作。
                    if (from) {
                        vo.getData().setOrderStatus("1");
                    }

                    orderDetailVo = vo.getData();

                    orderState = Integer.parseInt(orderDetailVo.getOrderStatus());

                    setViewData();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "系统错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == 1000) {
            int temp = data.getIntExtra("orderState", -1);
            if (temp == -1) return;

            orderState = temp;
            updateViewData();
            return;
        }
        if (requestCode == 1001) {
            int temp = data.getIntExtra("orderState", -1);
            if (temp == -1) return;

            orderState = temp;
            updateViewData();
            return;
        }

    }

    private void doClick(View v) {

        switch (orderState) {//根据不同状态添加按钮模块还是评价模块
            case 0: {//已取消

            }
            break;
            case 1: {//待接单
                if (orderFrom == 1) {//普通用户
                    startActivityForResult(new Intent(this, CancelOrderActivity.class).putExtra("orderId", orderId), 1000);
                } else if (orderFrom == 2) {//抢单用户
                    toRobOrder();
                }
            }
            break;
            case 2: {//已接单
                deliveredOrder(v);
            }
            break;
            case 3: {//已送达
                toOrderFinish(v);
            }
            break;
            case 4: {//已完成
                startActivityForResult(new Intent(this, EvaluateOrderActivity.class).putExtra("orderId", orderId), 1001);
            }
            break;
            case 5: {//已评价
                switch (orderType) {
                    case 1: {
                        Intent intent = new Intent(this, RestaurantGoodsActivity.class);
                        intent.putExtra("storeId", Integer.parseInt(orderDetailVo.getStoreId()));

                        startActivity(intent);
                    }
                    break;
                    case 2: {
                        Intent intent = new Intent(this, ReleaseOrderActivity.class);
                        intent.putExtra("position", 3);
                        startActivity(intent);
                    }
                    break;
                    case 3: {
                        Intent intent = new Intent(this, ReleaseOrderActivity.class);
                        intent.putExtra("position", 0);
                        startActivity(intent);
                    }
                    break;
                    case 4: {
                        Intent intent = new Intent(this, ReleaseOrderActivity.class);
                        intent.putExtra("position", 2);
                        startActivity(intent);
                    }
                    break;
                }

            }
            break;
            case 6: {//已撤销

            }
            break;
        }
    }

    //已完成
    private void toOrderFinish(final View v) {
        v.setClickable(false);

        HttpAction.getInstance().toOrderFinish(orderId, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("res=  " + response.body());
                v.setClickable(true);
                try {
                    OrderStateBean vo = JsonMananger.jsonToBean(response.body(), OrderStateBean.class);

                    if (!StringUtils.isEqual(vo.code, "200")) {
                        Toast.makeText(context, StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!StringUtils.isEmpty(vo.getData().getOrderStatus())) {
                        try {
                            orderState = Integer.parseInt(vo.getData().getOrderStatus());
                            updateViewData();
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "系统错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //  已送达
    private void deliveredOrder(final View v) {
        v.setClickable(false);

        HttpAction.getInstance().deliveredOrder(orderId, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("res=  " + response.body());
                v.setClickable(true);
                try {
                    OrderStateBean vo = JsonMananger.jsonToBean(response.body(), OrderStateBean.class);

                    if (!StringUtils.isEqual(vo.code, "200")) {
                        Toast.makeText(context, StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!StringUtils.isEmpty(vo.getData().getOrderStatus())) {
                        try {
                            orderState = Integer.parseInt(vo.getData().getOrderStatus());
                            updateViewData();
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "系统错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void toRobOrder() {

        HttpAction.getInstance().toRobOrder(orderId, Integer.parseInt(PccApplication.getUserid()), new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("res=  " + response.body());
                try {
                    OrderStateBean vo = JsonMananger.jsonToBean(response.body(), OrderStateBean.class);

                    if (!StringUtils.isEqual(vo.code, "200")) {
                        Toast.makeText(context, StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }

                    if (!StringUtils.isEmpty(vo.getData().getOrderStatus())) {
                        try {
                            orderState = Integer.parseInt(vo.getData().getOrderStatus());
                            clickLazy = true;
                            updateViewData();
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "系统错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private volatile boolean clickLazy = false;
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv_order_cancel.setVisibility(View.VISIBLE);
        }
    };


}
