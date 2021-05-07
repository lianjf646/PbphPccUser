package com.pbph.pcc.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pbph.pcc.R;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.response.WXPayBean;
import com.pbph.pcc.tools.ConstantData;
import com.pbph.pcc.view.GlideCircleTransform;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.text.DecimalFormat;

/**
 * 调用微信支付界面
 * Created by 孟庆奎 on 2017/9/25 0025.
 */
public class PayOrderActivity extends BaseActivity implements View.OnClickListener {
    private IWXAPI api;
    private TextView title, totTv, nameOrderTv, totlePriceTv,reductionTv,reduction;
    private ImageView shopHeadImg;
    private ImageView leftBtn;
    private LinearLayout pay;
    private String shopName, shopImg, orderNum;
    private double shippingAmount, totlePrice;
    private WXPayBean wxPayBean;
    private Receiver receiver;
    private int orderType = 1;
    private ConstraintLayout reduction_lay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_order);
        wxPayBean = (WXPayBean) getIntent().getExtras().getSerializable("wxPayBean");
        totlePrice = getIntent().getExtras().getDouble("totlePrice");
        shippingAmount = getIntent().getExtras().getDouble("shippingAmount");
        shopName = getIntent().getExtras().getString("shopName");
        shopImg = getIntent().getExtras().getString("shopImg");
        orderType = getIntent().getIntExtra("orderType", 0);
        orderNum = getIntent().getExtras().getString("orderNum");
        title = (TextView) findViewById(R.id.tv_title);
        leftBtn = (ImageView) findViewById(R.id.btn_left);
        shopHeadImg = (ImageView) findViewById(R.id.shop_img);
        totTv = (TextView) findViewById(R.id.tot);
        nameOrderTv = (TextView) findViewById(R.id.name_order);
        totlePriceTv = (TextView) findViewById(R.id.totle_price);
        reduction =(TextView) findViewById(R.id.reduction);
        reductionTv =(TextView) findViewById(R.id.reductionTv);
        pay = (LinearLayout) findViewById(R.id.pay);
        reduction_lay =(ConstraintLayout) findViewById(R.id.reduction_lay);
        reductionTv.setText(wxPayBean.getData().getRedcutionAmountText());
        Double redcutionAmount = Double.parseDouble(wxPayBean.getData().getRedcutionAmount());
        if (redcutionAmount > 0){
            reduction.setText("-"+ redcutionAmount + "元");
            reduction_lay.setVisibility(View.VISIBLE);
        }else{
            reduction_lay.setVisibility(View.GONE);
        }
        DecimalFormat df = new DecimalFormat("#0.00");
        totTv.setText("￥" + df.format(totlePrice + shippingAmount));
        shopName = "订单编号:";
        nameOrderTv.setText(shopName + "" + orderNum);
        //nameOrderTv.setText(shopName + "," + orderNum);
        totlePriceTv.setText("￥" + df.format(totlePrice + shippingAmount - redcutionAmount));
        totlePriceTv.setVisibility(View.GONE);
        title.setText("支付订单");
        Glide.with(PayOrderActivity.this)
                .load(shopImg).transform(new GlideCircleTransform(PayOrderActivity.this)).placeholder(R.mipmap.banner_zw).into(shopHeadImg);
        leftBtn.setVisibility(View.VISIBLE);
        WXTextObject textObject = new WXTextObject();
        leftBtn.setOnClickListener(this);
        pay.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                onBackPressed();
                break;
            case R.id.pay:
                //提交微信支付
                /*api = WXAPIFactory.createWXAPI(this,ConstantData.WX_APP_ID,true);
                api.registerApp(ConstantData.WX_APP_ID);*/
                PccApplication.setDataMapData("orderId", Integer.parseInt(wxPayBean.getData().getOrderId()));
                PccApplication.setDataMapData("orderType", orderType);
                String appId = wxPayBean.getData().getAppid();
                api = WXAPIFactory.createWXAPI(PayOrderActivity.this, appId, false);
                api.registerApp(appId);
                PayReq req = new PayReq();
                req.appId = wxPayBean.getData().getAppid();
                req.partnerId = wxPayBean.getData().getPartnerid();
                req.prepayId = wxPayBean.getData().getPrepayid();
                req.packageValue = wxPayBean.getData().getPackages();
                req.nonceStr = wxPayBean.getData().getNoncestr();
                req.timeStamp = wxPayBean.getData().getTimestamp();
                req.sign = wxPayBean.getData().getSign();
                api.sendReq(req);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (receiver == null) {
            receiver = new Receiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConstantData.WXPAY_RESULT);
            registerReceiver(receiver, intentFilter);
        }
    }

    //广播 支付成功关闭界面
    public class Receiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String action = intent.getAction();
            int errCode = bundle.getInt("errCode");
            if (ConstantData.WXPAY_RESULT.equals(action) && errCode == 0) {
                /*Intent intent2 = new Intent(PayOrderActivity.this,OrderDetailActivity.class);
                intent2.putExtra("orderId",Integer.parseInt(wxPayBean.getData().getOrderId()));
                intent2.putExtra("orderFrom",1);
                intent2.putExtra("orderType",orderType);
                intent2.putExtra("orderState",1);
                startActivity(intent2);*/
                finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }
}
