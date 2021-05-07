package com.pbph.pcc.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pbph.pcc.R;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.response.GetUserStoreInfoBean;


public class RestaurantInfoActivity extends AppCompatActivity {

    public Context context = this;

    private GetUserStoreInfoBean.DataBean.StoreInfoEntityBean storeInfoEntityBean;

    TextView tv_title;

    private ImageView ivShopPic;
    private TextView tvShopName;
    private TextView tvShopDesc;
    //    private TextView[] tvLine1 = new TextView[3];
    private TextView[] tvLine2 = new TextView[3];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storeInfoEntityBean = (GetUserStoreInfoBean.DataBean.StoreInfoEntityBean) PccApplication.getDataMapData("shopInfo");

        if (null == storeInfoEntityBean) {
            Toast.makeText(this, "数据错误", Toast.LENGTH_SHORT).show();
            return;
        }

        setContentView(R.layout.activity_restaurant_info);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("店铺信息");
        findViewById(R.id.btn_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ivShopPic = (ImageView) findViewById(R.id.iv_shop_pic);
        tvShopName = (TextView) findViewById(R.id.tv_shop_name);
        tvShopDesc = (TextView) findViewById(R.id.tv_shop_desc);

//        tvLine1[0] = (TextView) findViewById(R.id.tv_line11);
        tvLine2[0] = (TextView) findViewById(R.id.tv_line12);

//        tvLine1[1] = (TextView) findViewById(R.id.tv_line21);
        tvLine2[1] = (TextView) findViewById(R.id.tv_line22);

//        tvLine1[2] = (TextView) findViewById(R.id.tv_line31);
        tvLine2[2] = (TextView) findViewById(R.id.tv_line32);


        Glide.with(context).load(storeInfoEntityBean.getStoreImg()).placeholder(R.mipmap.banner_zw).error(R.mipmap.banner_zw).into(ivShopPic);
        tvShopName.setText(storeInfoEntityBean.getStoreName());

        tvShopDesc.setText(storeInfoEntityBean.getStoreDescribe());

        tvLine2[0].setText(storeInfoEntityBean.getAddrName());
        tvLine2[1].setText(storeInfoEntityBean.getStoreAddress());
        tvLine2[2].setText(storeInfoEntityBean.getStoreStarttime() + "-" + storeInfoEntityBean.getStoreEndtime());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
