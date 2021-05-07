package com.pbph.pcc.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.pbph.pcc.bean.vo.KeyValueBean;

/**
 * Created by Administrator on 2017/9/26.
 */


public class MyArraySpinnerAdapter extends MySpinnerAdapter {


    public MyArraySpinnerAdapter(@NonNull Context context, int key, int value) {
        this(context, null, key, value);
    }

    public MyArraySpinnerAdapter(@NonNull Context context, OnSetDataListener onSetDataListener, int key, int value) {
        super(context, onSetDataListener);

        int[] keys = context.getResources().getIntArray(key);
        String[] values = context.getResources().getStringArray(value);

        for (int i = 0; i < values.length; i++) {
            KeyValueBean vo = new KeyValueBean();
            vo.key = keys[i];
            vo.value = values[i];

            list.add(vo);
        }
        notifyDataSetChanged();
    }

}
