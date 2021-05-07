package com.pbph.pcc.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pbph.pcc.R;
import com.pbph.pcc.adapter.MessageListAdapter;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.request.GetMessageWebRequestBean;
import com.pbph.pcc.bean.response.GetMessageWebResponseBean;
import com.pbph.pcc.bean.request.MessageReadRequestBean;
import com.pbph.pcc.bean.request.UpdateStatusWebRequestBean;
import com.pbph.pcc.bean.response.UpdateStatusWebResponseBean;
import com.pbph.pcc.broadcast.BroadcastManager;
import com.pbph.pcc.http.HttpAction;
import com.pbph.pcc.json.JsonMananger;
import com.pbph.pcc.tools.ConstantData;
import com.pbph.pcc.tools.WaitUI;
import com.utils.DateUtils;
import com.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MessageListActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout mActiveLayout, mNoticeWebLayout;
    private TextView mActiveTimeTextView, mNoticeTimeTextView;
    private DateUtils dateUtils = null;
    private ListView mMessageListView;
    private MessageListAdapter mMessageListAdapter;
    private List<GetMessageWebResponseBean.DataBean> list = new ArrayList<>();
    GetMessageWebResponseBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        TextView title = (TextView) findViewById(R.id.tv_title);
        ImageView back = (ImageView) findViewById(R.id.btn_left);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        dateUtils = new DateUtils();
        mActiveLayout = (LinearLayout) findViewById(R.id.ll_message_list_avtive_layout);
        mNoticeWebLayout = (LinearLayout) findViewById(R.id.ll_message_list_notice_layout);
        mActiveTimeTextView = (TextView) findViewById(R.id.tv_message_list_avtive_time);
        mNoticeTimeTextView = (TextView) findViewById(R.id.tv_message_list_notice_time);
        mMessageListView = (ListView) findViewById(R.id.lv_message_list);
        mMessageListAdapter = new MessageListAdapter(this, list);
        mMessageListView.setAdapter(mMessageListAdapter);
        mActiveTimeTextView.setText(dateUtils.getString(DateUtils.PATTERN_8));
        mNoticeTimeTextView.setText(dateUtils.getString(DateUtils.PATTERN_8));
        mActiveLayout.setOnClickListener(this);
        mNoticeWebLayout.setOnClickListener(this);
        title.setText("消息");
        messageWeb();
        BroadcastManager.getInstance(this).addAction(ConstantData.REFRESH_MESSAGE_LIST_ACITON, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                messageWeb();
            }
        });
    }

    public void openOrderDetail(String orderid, String orderType, String orderState) {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra("orderId", Integer.parseInt(orderid));
        intent.putExtra("orderFrom", 1);
        intent.putExtra("orderType", Integer.parseInt(orderType));
        intent.putExtra("orderState", Integer.parseInt(orderState));
        startActivity(intent);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json = (String) msg.obj;
            try {
                GetMessageWebResponseBean.DataBean bean = JsonMananger.jsonToBean(json, GetMessageWebResponseBean.DataBean.class);
                list.add(bean);
                mMessageListAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
        messageWeb();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BroadcastManager.getInstance(this).destroy(ConstantData.REFRESH_MESSAGE_LIST_ACITON);
    }

    private void messageRead(GetMessageWebResponseBean bean) {
        MessageReadRequestBean readRequestBean = new MessageReadRequestBean(bean);
        if (readRequestBean.arrayString.length() < 3) {
            return;
        }
        HttpAction.getInstance().messageRead(readRequestBean, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                try {
                    if (null == response || response.code() != 200) {
                        Toast.makeText(MessageListActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.e("messageRead", "res=  " + response.body());
//                    GetMessageWebResponseBean bean = JsonMananger.jsonToBean(response.body(), GetMessageWebResponseBean.class);
//                    if (bean.getCode() == 200 && null != bean.getData() && !bean.getData().isEmpty()) {
//                        list.clear();
//                        list.addAll(bean.getData());
//                        mMessageListAdapter.notifyDataSetChanged();
//                    }
                } catch (Exception e) {
                    Toast.makeText(MessageListActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateStatusWeb(final String status, String recordId, String messageId) {
        HttpAction.getInstance().updateStatusWeb(new UpdateStatusWebRequestBean(status, recordId, messageId), new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                try {
                    if (null == response || response.code() != 200) {
                        Toast.makeText(MessageListActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.e("updateStatusWeb", "res=  " + response.body());
                    UpdateStatusWebResponseBean bean = JsonMananger.jsonToBean(response.body(), UpdateStatusWebResponseBean.class);
                    if (StringUtils.isEqual(bean.getCode(), "200")) {
                        if ("1".equals(status)) {
                            startActivity(new Intent(MessageListActivity.this, RecieveOrderActivity.class));
                        }
                        messageWeb();
                    }
                } catch (Exception e) {
                    Toast.makeText(MessageListActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    private void messageWeb() {
        WaitUI.Show(this);
        HttpAction.getInstance().messageWeb(new GetMessageWebRequestBean(PccApplication.getUserid()), new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                WaitUI.Cancel();
                try {
                    if (null == response || response.code() != 200) {
                        Toast.makeText(MessageListActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.e("messageWeb", "res=  " + response.body());
                    GetMessageWebResponseBean bean = JsonMananger.jsonToBean(response.body(), GetMessageWebResponseBean.class);
                    if (StringUtils.isEqual(bean.getCode(), "200") && null != bean.getData() && !bean.getData().isEmpty()) {
                        list.clear();
                        list.addAll(bean.getData());
                        mMessageListAdapter.notifyDataSetChanged();
                        messageRead(bean);
                    }

                } catch (Exception e) {
                    Toast.makeText(MessageListActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_message_list_avtive_layout:
                startActivity(new Intent(this, ActiveListActivity.class).putExtra("name", "active"));
                break;
            case R.id.ll_message_list_notice_layout:
                startActivity(new Intent(this, ActiveListActivity.class).putExtra("name", "notice"));
                break;
        }
    }
}
