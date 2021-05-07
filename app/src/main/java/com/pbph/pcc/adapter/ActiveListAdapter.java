package com.pbph.pcc.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pbph.pcc.R;
import com.pbph.pcc.bean.response.BaseActiveBean;

import java.util.List;


public class ActiveListAdapter extends BaseAdapter {
    private Context context = null;
    private List<? extends BaseActiveBean> list = null;
    private LayoutInflater inflater = null;
    private int width = 0;

    public ActiveListAdapter(Context context, List<BaseActiveBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels / 2;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (null == view) {
            view = inflater.inflate(R.layout.adapter_active_list_item,
                    null);
        }
        BaseActiveBean bean = list.get(i);
        TextView title = (TextView) ViewHolder.get(view, R.id.tv_adapter_active_list_title);
        final TextView content = (TextView) ViewHolder.get(view, R.id.tv_adapter_active_list_content);
        TextView time = (TextView) ViewHolder.get(view, R.id.tv_adapter_active_list_time);
        ImageView image = (ImageView) ViewHolder.get(view, R.id.iv_adapter_active_list_image);
//        image.getLayoutParams().width=width;
        image.getLayoutParams().height = width / 2;
        title.setText(bean.getTitle());
        Spanned sp = Html.fromHtml(bean.getContent());
        content.setText(sp);
        time.setText(bean.getTime());
        Glide.with(context).load(bean.getImage()).into(image);
        return view;
    }

    private static class ViewHolder {

        public static View get(View view, int id) {

            SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
            if (viewHolder == null) {
                viewHolder = new SparseArray<View>();
                view.setTag(viewHolder);
            }
            View childView = viewHolder.get(id);
            if (childView == null) {
                childView = view.findViewById(id);
                viewHolder.put(id, childView);
            }
            return childView;
        }
    }
}
