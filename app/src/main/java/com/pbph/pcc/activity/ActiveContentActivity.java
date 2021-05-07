package com.pbph.pcc.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pbph.pcc.R;
import com.pbph.pcc.bean.response.GetActiveWebResponseBean;

public class ActiveContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_content);
        GetActiveWebResponseBean.DataBean.ResultBean bean = (GetActiveWebResponseBean.DataBean.ResultBean) getIntent().getSerializableExtra("content");
        TextView title = (TextView) findViewById(R.id.tv_title);
        TextView desc = (TextView) findViewById(R.id.tv_active_content_desc);
        TextView startTime = (TextView) findViewById(R.id.tv_active_content_start_time);
        TextView endTime = (TextView) findViewById(R.id.tv_active_content_end_time);
        TextView phone = (TextView) findViewById(R.id.tv_active_content_phone);    desc.setText(Html.fromHtml(bean.getThirdActiveContent()));
        title.setText(bean.getThirdActiveName());

        startTime.setText("开始时间:" + bean.getThirdActiveCreatetime());
        endTime.setText("结束时间:" + bean.getThirdActiveEndtime());
        phone.setText("联系电话:" + bean.getThirdPhone());

        ImageView back = (ImageView) findViewById(R.id.btn_left);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
