package com.pbph.pcc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pbph.pcc.R;
import com.pbph.pcc.bean.response.GetUserStoreGoodsBean;

import java.util.List;


/**
 * Created by caobo on 2016/7/20.
 * 购物车适配器
 */
public class ShopTypeAdapter extends BaseAdapter {


    private List<GetUserStoreGoodsBean.DataBean.GoodsListEntityBean> shopProducts;
    private LayoutInflater mInflater;

    public ShopTypeAdapter(Context context, List<GetUserStoreGoodsBean.DataBean.GoodsListEntityBean> shopProducts) {
        this.shopProducts = shopProducts;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return shopProducts.size();
    }

    @Override
    public Object getItem(int position) {
        return shopProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.listview_type, null);
            viewHolder.textView = convertView.findViewById(R.id.mainitem_txt);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(shopProducts.get(position).getTypeName());
        return convertView;
    }

    class ViewHolder {
        public TextView textView;
    }


}
