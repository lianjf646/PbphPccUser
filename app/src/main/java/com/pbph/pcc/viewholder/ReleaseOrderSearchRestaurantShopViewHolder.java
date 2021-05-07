package com.pbph.pcc.viewholder;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.adapter.ViewHolder;
import com.bumptech.glide.Glide;
import com.pbph.pcc.R;
import com.pbph.pcc.bean.response.GetUserStoreGoodsSeclectBean;


public class ReleaseOrderSearchRestaurantShopViewHolder extends ViewHolder {


    ImageView imageView1;
    TextView textView1;

    TextView textView21;
    TextView textView22;
    TextView textView3;

    @Override

    protected int getLayout() {
        return R.layout.listview_restaurant_order;
    }

    @Override
    protected void getView(View view) {

        imageView1 = view.findViewById(R.id.ImageView1);

        textView1 = view.findViewById(R.id.textView1);

        textView21 = view.findViewById(R.id.textView21);
        textView22 = view.findViewById(R.id.textView22);

        textView3 = view.findViewById(R.id.textView3);
    }

    @Override
    protected void showView() {
        GetUserStoreGoodsSeclectBean.DataBean.StoreListEntityBean vo = (GetUserStoreGoodsSeclectBean.DataBean.StoreListEntityBean) item;

        textView1.setText(vo.getStoreName());


        textView21.setText("月售");
        textView21.append("");
        textView21.append("单");


        textView22.setText("营业时间 ");
        textView22.append(vo.getStoreStarttime());
        textView22.append("-");
        textView22.append(vo.getStoreEndtime());

        Glide.with((Activity) adapter.activity).load(vo.getStoreImg()).placeholder(R.mipmap.banner_zw).error(R.mipmap.banner_zw).into(imageView1);


    }
}
