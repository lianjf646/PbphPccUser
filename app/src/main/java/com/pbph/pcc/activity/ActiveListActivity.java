package com.pbph.pcc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.utils.LogUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pbph.pcc.R;
import com.pbph.pcc.adapter.ActiveListAdapter;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.response.BaseActiveBean;
import com.pbph.pcc.bean.request.GetActiveWebRequestBean;
import com.pbph.pcc.bean.response.GetActiveWebResponseBean;
import com.pbph.pcc.bean.request.GetNoticeWebRequestBean;
import com.pbph.pcc.bean.response.GetNoticeWebResponseBean;
import com.pbph.pcc.http.HttpAction;
import com.pbph.pcc.json.JsonMananger;
import com.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ActiveListActivity extends AppCompatActivity {
    PccApplication application;
    private ListView listView = null;
    private ActiveListAdapter adapter = null;
    private List<BaseActiveBean> dataList = new ArrayList<>();
    private String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (PccApplication) getApplication();
        name = getIntent().getStringExtra("name");
        setContentView(R.layout.activity_active_list);
        TextView title = (TextView) findViewById(R.id.tv_title);
        ImageView back = (ImageView) findViewById(R.id.btn_left);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        listView = (ListView) findViewById(R.id.lv_active_list);

        adapter = new ActiveListAdapter(this, dataList);
        listView.setAdapter(adapter);
//        TextView emptyView = (TextView) findViewById(R.id.tv_active_list_empty);
        LinearLayout emptyView = (LinearLayout) findViewById(R.id.ll_active_list_empty_layout);
        listView.setEmptyView(emptyView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if ("active".equals(name)) {
                    startActivity(new Intent(ActiveListActivity.this, ActiveContentActivity.class).putExtra("content", dataList.get(i)));
                }
            }
        });
        if ("active".equals(name)) {
            title.setText("活动");
            activeWeb();
        } else {
            title.setText("通知");
            noticeWeb();
        }
    }

    private void noticeWeb() {
        HttpAction.getInstance().noticeWeb(new GetNoticeWebRequestBean(PccApplication.getUserid()), new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                try {
                    if (null == response || response.code() != 200) {
                        Toast.makeText(ActiveListActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    LogUtils.e(response.body());
                    GetNoticeWebResponseBean bean = JsonMananger.jsonToBean(response.body(), GetNoticeWebResponseBean.class);
                    if (StringUtils.isEqual(bean.getCode(), "200")) {
                        dataList.addAll(bean.getData().getResult());
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ActiveListActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(ActiveListActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    private void activeWeb() {
        if (null == application || null == application.getMyInfoData()) return;
        HttpAction.getInstance().activeWeb(new GetActiveWebRequestBean(application.getMyInfoData().getSchoolId()), new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {

                try {
                    if (null == response || response.code() != 200) {
                        Toast.makeText(ActiveListActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    LogUtils.e(response.body());
                    GetActiveWebResponseBean bean = JsonMananger.jsonToBean(response.body(), GetActiveWebResponseBean.class);
                    if (StringUtils.isEqual(bean.getCode(), "200")) {
                        dataList.addAll(bean.getData().getResult());
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ActiveListActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(ActiveListActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }
}
