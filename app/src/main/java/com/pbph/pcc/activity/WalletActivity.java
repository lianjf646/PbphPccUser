package com.pbph.pcc.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.adapter.DataAdapter;
import com.android.utils.LogUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pbph.pcc.R;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.request.GetAppAuthorizeRequestBean;
import com.pbph.pcc.bean.response.GetAppAuthorizeResonseBean;
import com.pbph.pcc.bean.response.MyPurseBean;
import com.pbph.pcc.bean.request.SendRedRequestBean;
import com.pbph.pcc.broadcast.BroadcastManager;
import com.pbph.pcc.http.HttpAction;
import com.pbph.pcc.json.JsonMananger;
import com.pbph.pcc.tools.ConstantData;
import com.pbph.pcc.tools.WaitUI;
import com.pbph.pcc.viewholder.MyPurseViewHolder;
import com.pbph.pcc.wxutil.WXUtil;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class WalletActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context = this;
    private TextView tv_wallet_balance;
    private TextView tv_wallet_total_balance;
    private TextView tv_wallet_get_cash;
    private ListView listView = null;
    private DataAdapter adapter;
    private MyPurseBean vo;
    AlertDialog.Builder builder;
    private PccApplication application = null;
    String unionid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application = (PccApplication) getApplication();

        setContentView(R.layout.activity_wallet);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("我的钱包");
        findViewById(R.id.btn_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_wallet_balance = (TextView) findViewById(R.id.tv_wallet_balance);
        tv_wallet_total_balance = (TextView) findViewById(R.id.tv_wallet_total_balance);
        tv_wallet_get_cash = (TextView) findViewById(R.id.tv_wallet_get_cash);
        tv_wallet_get_cash.setOnClickListener(this);

        View view = findViewById(R.id.ll_listview_empty);
        listView = (ListView) findViewById(R.id.lv_wallet_log);
        listView.setEmptyView(view);
        adapter = new DataAdapter(this, listView, MyPurseViewHolder.class);
        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(this);
        myPurse();

        BroadcastManager.getInstance(this).addAction(ConstantData.GET_UNIONID_ACTION, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String unionid = intent.getStringExtra("unionid");
                if (!TextUtils.isEmpty(unionid)) {
                    getAppAuthorize(unionid);
                }
            }
        });
        initBuilder();
    }

    private void initBuilder() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("绑定公众号提示：\n微信提现需要绑定微信公众平台，请分享二维码到微信，关注公众平台呦!");
        builder.setIcon(R.drawable.pcc_code1);
        builder.setNegativeButton("关注", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                WXUtil.getInstance(WalletActivity.this).shareImageBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.pcc_code1), SendMessageToWX.Req.WXSceneSession);
            }
        });
        builder.setPositiveButton("提现", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if ("0".equals(application.getMyInfoData().getIsSign().trim())) {
                    takeMoney();
                } else {
                    WXUtil.getInstance(WalletActivity.this).weiXinLogin();
                }
            }
        });
    }

    private void takeMoney() {
        try {
            if (TextUtils.isEmpty(vo.getData().getMyBalance()) || Double.valueOf(vo.getData().getMyBalance()) < 1) {
                Toast.makeText(WalletActivity.this, "提现金额必须大于1元", Toast.LENGTH_SHORT).show();
                return;
            }
            int money = (int) (Float.valueOf(vo.getData().getMyBalance()) * 100);
            sendRed(String.valueOf(money));
        } catch (NumberFormatException e) {
            Toast.makeText(context, "系统错误", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void sendRed(String capital) {
        WaitUI.Show(this);
        HttpAction.getInstance().sendRed(new SendRedRequestBean(PccApplication.getUserid(), capital), new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                WaitUI.Cancel();
                LogUtils.e("res=  " + response.body());

                //{"msg":"成功","code":"200","data":{}}
                try {
                    if (null == response || response.code() != 200) {
                        Toast.makeText(WalletActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject object = new JSONObject(response.body());
                    int code = object.getInt("code");
                    if (code == 200) {
                        myPurse();
                    }
                    Toast.makeText(WalletActivity.this, code == 200 ? "提现成功" : object.getString("msg"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "系统错误", Toast.LENGTH_SHORT).show();
                }

                WaitUI.Cancel();
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                WaitUI.Cancel();
            }
        });
    }

    private void getAppAuthorize(final String unionId) {
        HttpAction.getInstance().getAppAuthorize(new GetAppAuthorizeRequestBean(unionId, PccApplication.getUserid()), new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {

                try {
                    if (null == response || response.code() != 200) {
                        Toast.makeText(WalletActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    LogUtils.e("res=  " + response.body());
                    GetAppAuthorizeResonseBean bean = JsonMananger.jsonToBean(response.body(), GetAppAuthorizeResonseBean.class);
                    if (!StringUtils.isEqual(bean.getCode(), "200")) {
                        Toast.makeText(WalletActivity.this, "请先关注微信公众号", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    unionid = unionId;
                    application.getMyInfoData().setIsSign("0");
                    takeMoney();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "系统错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        HttpAction.getInstance().myPurseCancel();
        BroadcastManager.getInstance(this).destroy(ConstantData.GET_UNIONID_ACTION);
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_wallet_get_cash:
//                if ("0".equals(application.getMyInfoData().getIsSign())) {
                builder.show();
//                } else {
//                    WXUtil.getInstance(this).weiXinLogin();
//                }

                break;
        }
    }

    private void myPurse() {

        HttpAction.getInstance().myPurse(Integer.parseInt(PccApplication.getUserid()), new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("res=  " + response.body());
                try {
                    vo = JsonMananger.jsonToBean(response.body(), MyPurseBean.class);
                    if (!StringUtils.isEqual(vo.code, "200")) {
                        Toast.makeText(context, StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    tv_wallet_balance.setText(TextUtils.isEmpty(vo.getData().getMyBalance()) ? "0.00" : vo.getData().getMyBalance());
                    tv_wallet_balance.append("元");
                    tv_wallet_total_balance.setText("总提现金额:");
                    tv_wallet_total_balance.append(TextUtils.isEmpty(vo.getData().getGetTotleSum()) ? "0" : vo.getData().getGetTotleSum());
                    tv_wallet_total_balance.append("元");

                    List<MyPurseBean.DataBean.DealsBean> list = vo.getData().getDeals();
                    if (list == null || list.size() <= 0) {
                        return;
                    }

//                    这里过滤掉一部分数据
                    adapter.clearDatas();
                    for (int i = 0, c = list.size(); i < c; i++) {
                        MyPurseBean.DataBean.DealsBean vo = list.get(i);
                        int type = Integer.parseInt(vo.getDealStatus());
//        0 收入，1 支出，2 退款，3 提现
                        switch (type) {
                            case 0:
                                break;
                            case 1:
                                continue;
                            case 2:
                                continue;
                            case 3:
                                break;
                            case 4:
                                continue;
                        }

                        adapter.addData(vo);
                    }

                    adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "系统错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
