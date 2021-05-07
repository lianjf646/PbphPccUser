package com.pbph.pcc.adapter;

import android.view.View;

import com.android.adapter.DataAdapter;
import com.utils.DoubleUtil;

import java.text.DecimalFormat;


public class MyShopCarAdapter extends DataAdapter {

    public OnFlushDataListener onFlushDataListener;


    public MyShopCarAdapter(Object activity, View view, Class viewholder) {
        super(activity, view, viewholder);
    }


    public String scale(double num, double price) {
        try {
            double sum = 0;
            sum = DoubleUtil.sum(sum, DoubleUtil.mul(num, price));
            return new DecimalFormat("0.00").format(sum);
        } catch (Exception e) {
            e.printStackTrace();
            return "未知";
        }
    }

    public void setOnFlushDataListener(OnFlushDataListener onFlushDataListener) {
        this.onFlushDataListener = onFlushDataListener;
    }

    public interface OnFlushDataListener {
        void onFlushData(Object product, boolean type);
    }
}
