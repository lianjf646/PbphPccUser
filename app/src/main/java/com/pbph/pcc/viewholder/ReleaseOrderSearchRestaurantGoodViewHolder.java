package com.pbph.pcc.viewholder;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.adapter.ViewHolder;
import com.bumptech.glide.Glide;
import com.pbph.pcc.R;
import com.pbph.pcc.bean.response.GetUserStoreGoodsSeclectBean;


public class ReleaseOrderSearchRestaurantGoodViewHolder extends ViewHolder {


    public ImageView head;
    public TextView name;
    public TextView prise;
    public TextView sell;

    @Override

    protected int getLayout() {
        return R.layout.listview_releaseordersearchgood;
    }

    @Override
    protected void getView(View view) {

        head = view.findViewById(R.id.head);
        name = view.findViewById(R.id.name);
        prise = view.findViewById(R.id.prise);
        sell = view.findViewById(R.id.sell);
    }

    @Override
    protected void showView() {

        final GetUserStoreGoodsSeclectBean.DataBean.StoreListEntityBean.GoodsListBean product = (GetUserStoreGoodsSeclectBean.DataBean.StoreListEntityBean.GoodsListBean) item;

        Glide.with((Activity) adapter.activity).load(product.getStoreGoodsImg()).placeholder(R.mipmap.banner_zw).error(R.mipmap.banner_zw).into(head);

        name.setText(product.getStoreGoodsName());
        prise.setText(String.valueOf(product.getStoreGoodsPrice()));
        sell.setText(String.valueOf(product.getSales()));

    }
}
