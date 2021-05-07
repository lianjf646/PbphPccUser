package com.pbph.pcc.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.utils.LogUtils;
import com.pbph.pcc.activity.OrderDetailActivity;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.tools.ConstantData;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;
    PccApplication application = null;

    private void handleIntent(Intent paramIntent) {
        api.handleIntent(paramIntent, this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.pay_result);
        api = WXAPIFactory.createWXAPI(this, ConstantData.WX_APP_ID, false);
        application = (PccApplication) getApplication();
        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //setIntent(intent);
        //  api.handleIntent(intent, this);

        setIntent(intent);
        handleIntent(intent);

    }

    @Override
    public void onReq(BaseReq req) {
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        LogUtils.d("onPayFinish, errCode = " + resp.errCode);


        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            @SuppressWarnings("unused")
            String respString = null;

            if (resp.errCode == 0) {
                Intent intent2 = new Intent(WXPayEntryActivity.this, OrderDetailActivity.class);

                int orderId = Integer.parseInt(PccApplication.getDataMapData("orderId").toString());
                int orderType = Integer.parseInt(PccApplication.getDataMapData("orderType").toString());

                if (orderId != -1) {
                    intent2.putExtra("orderId", orderId);
                    intent2.putExtra("orderFrom", 1);
                    intent2.putExtra("orderType", orderType);
                    intent2.putExtra("orderState", 1);
                    startActivity(intent2);
                }

                respString = "支付成功";
                // 发送广播
                Intent intent1 = new Intent();
                intent1.setAction(ConstantData.WXPAY_RESULT);
                intent1.putExtra("errCode", "" + resp.errCode);
                sendBroadcast(intent1);
            } else if (resp.errCode == -1) {
                respString = "支付错误";

            } else if (resp.errCode == -2) {
                respString = "取消支付";
            }
            Toast.makeText(WXPayEntryActivity.this, respString, Toast.LENGTH_LONG).show();
            finish(); //收到返回之后 关闭页面
        }
    }
}