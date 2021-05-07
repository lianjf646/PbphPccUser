package com.pbph.pcc.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.pbph.pcc.bean.vo.KeyValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/26.
 */


public class MySpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    Context context;
    LayoutInflater inflater;
    protected List<Object> list = new ArrayList<>();

    OnSetDataListener onSetDataListener;

    public MySpinnerAdapter(@NonNull Context context, OnSetDataListener onSetDataListener) {

        this.context = context;
        inflater = LayoutInflater.from(context);
        this.onSetDataListener = onSetDataListener;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView holder;
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_spinner_item, null);

            holder = convertView.findViewById(android.R.id.text1);

            convertView.setTag(holder);// important必须有

        } else {
            holder = (TextView) convertView.getTag();
        }

        Object obj = getItem(position);
        String str;
        if (obj instanceof KeyValueBean) {
            str = ((KeyValueBean) obj).value;
        } else if (onSetDataListener != null) {
            str = onSetDataListener.onSetData(obj);
        } else {
            str = obj.toString();
        }
        holder.setText(str);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView holder;
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_dropdown_item_1line, null);

            holder = convertView.findViewById(android.R.id.text1);

            convertView.setTag(holder);// important必须有

        } else {
            holder = (TextView) convertView.getTag();
        }


        Object obj = getItem(position);
        String str;
        if (obj instanceof KeyValueBean) {
            str = ((KeyValueBean) obj).value;
        } else if (onSetDataListener != null) {
            str = onSetDataListener.onSetData(obj);
        } else {
            str = obj.toString();
        }
        holder.setText(str);

        return convertView;
    }


    public interface OnSetDataListener {
        public <T extends Object> String onSetData(T obj);
    }

    public <T extends Object> void setDatas(List<T> datas) {
        list.clear();
        if (datas != null && datas.size() > 0) {
            list.addAll(datas);
        }
        notifyDataSetChanged();
    }

    public void clears() {
        list.clear();
        notifyDataSetChanged();
    }
}
