package com.pbph.pcc.viewholder;

import android.view.View;
import android.widget.TextView;

import com.android.adapter.ViewHolder;
import com.pbph.pcc.R;
import com.pbph.pcc.adapter.MyShopCarAdapter;
import com.pbph.pcc.bean.response.GetUserStoreGoodsBean.DataBean.GoodsListEntityBean.StoreGoodsListBean;


public class MyShopCarRestaurantViewHolder extends ViewHolder {

    public TextView commodityName; //购物车商品名称
    public TextView commodityPrise;  // 购物车商品价格

    public TextView increase;//增加
    public TextView shoppingNum;//商品数目
    public TextView reduce;//减少


    @Override

    protected int getLayout() {
        return R.layout.listview_shopcar;
    }

    @Override
    protected void getView(View view) {
        commodityName = view.findViewById(R.id.commodityName);
        commodityPrise = view.findViewById(R.id.commodityPrise);
        increase = view.findViewById(R.id.increase);
        reduce = view.findViewById(R.id.reduce);
        shoppingNum = view.findViewById(R.id.shoppingNum);
/////////////
        increase.setOnClickListener(onIncreaseListener);
        reduce.setOnClickListener(onReduceListener);
    }

    @Override
    protected void showView() {
        StoreGoodsListBean vo = (StoreGoodsListBean) item;
        MyShopCarAdapter myShopCarAdapter = (MyShopCarAdapter) adapter;

        commodityName.setText(vo.getStoreGoodsName());

        commodityPrise.setText(myShopCarAdapter.scale(vo.getNumber(), vo.getStoreGoodsPrice()));

        shoppingNum.setText(String.valueOf(vo.getNumber()));

    }


    View.OnClickListener onIncreaseListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            StoreGoodsListBean vo = (StoreGoodsListBean) item;
            MyShopCarAdapter myShopCarAdapter = (MyShopCarAdapter) adapter;
            int num = vo.getNumber();
            num++;
            vo.setNumber(num);
            shoppingNum.setText(vo.getNumber() + "");

            commodityPrise.setText(myShopCarAdapter.scale(vo.getNumber(), vo.getStoreGoodsPrice()));

            if (myShopCarAdapter.onFlushDataListener != null) {
                myShopCarAdapter.onFlushDataListener.onFlushData(vo, true);
            }
        }
    };
    View.OnClickListener onReduceListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            StoreGoodsListBean vo = (StoreGoodsListBean) item;
            MyShopCarAdapter myShopCarAdapter = (MyShopCarAdapter) adapter;
            int num = vo.getNumber();
            if (num > 0) {
                num--;
                vo.setNumber(num);
                shoppingNum.setText(vo.getNumber() + "");
                if (myShopCarAdapter.onFlushDataListener != null) {
                    myShopCarAdapter.onFlushDataListener.onFlushData(vo, false);
                }
                commodityPrise.setText(myShopCarAdapter.scale(vo.getNumber(), vo.getStoreGoodsPrice()));
            }
        }
    };


}
