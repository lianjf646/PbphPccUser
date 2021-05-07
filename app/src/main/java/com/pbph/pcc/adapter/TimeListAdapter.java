package com.pbph.pcc.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pbph.pcc.R;
import com.pbph.pcc.bean.vo.SelectTimeBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/24 0024.
 */

public class TimeListAdapter extends RecyclerView.Adapter<TimeListAdapter.TimeListHolder> implements View.OnClickListener {
    static Context context;
    int lastPosition = 0;
    Double shippingAmount = 0.0;
    private List<SelectTimeBean> list = new ArrayList<SelectTimeBean>();
    SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
    private OnItemClickListener mOnItemClickListener = null;

    public TimeListAdapter(Context context, Double shippingAmount, List<SelectTimeBean> list) {
        this.context = context;
        this.list = list;
        this.shippingAmount = shippingAmount;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public TimeListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_list, parent, false);
        return new TimeListHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(TimeListHolder holder, final int position) {
        if (position == 0) {
            holder.tvTime.setText("尽快送达|预计" + sdf.format(list.get(position).getDate()));
        } else {
            holder.tvTime.setText(sdf.format(list.get(position).getDate()));
        }
        holder.tvAmount.setText("(" + shippingAmount + "元配送费)");
        if (list.get(position).isChecked()) {
            holder.isChecked.setVisibility(View.VISIBLE);
        } else {
            holder.isChecked.setVisibility(View.GONE);
        }
        holder.card.setTag(position);
        holder.card.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void showItemAnimation(TimeListHolder holder, int position) {
        if (position > lastPosition) {
            lastPosition = position;
            ObjectAnimator.ofFloat(holder.card, "translationY", 1f * holder.card.getHeight(), 0f)
                    .setDuration(500)
                    .start();
        }
    }

    static class TimeListHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvAmount;
        View card;
        ImageView isChecked;

        public TimeListHolder(View view) {
            super(view);
            card = view;
            tvTime = view.findViewById(R.id.tv_time);
            tvAmount = view.findViewById(R.id.tv_amount);
            isChecked = view.findViewById(R.id.is_checked);
        }
    }
}
