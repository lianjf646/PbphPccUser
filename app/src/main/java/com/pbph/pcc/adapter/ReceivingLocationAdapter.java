package com.pbph.pcc.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pbph.pcc.R;
import com.pbph.pcc.activity.AddLocationActivity;
import com.pbph.pcc.bean.response.ReceivingLocationBean;

/**
 * Created by Administrator on 2017/9/28.
 */

public class ReceivingLocationAdapter extends BaseAdapter {

    private Context context;
    private ReceivingLocationBean mReceivingLocation;
    private DeleteReceivingLocationData deleteReceivingLocationData;
    private Resources resOn, resOff;
    private Drawable drawableOn, drawableOff;
    private String mType = "";

    public ReceivingLocationAdapter(Context context) {
        this.context = context;
        resOn = context.getResources();
        resOff = context.getResources();
        drawableOn = resOn.getDrawable(R.drawable.location_true);
        drawableOff = resOff.getDrawable(R.drawable.location_false);
        drawableOn.setBounds(0, 0, drawableOn.getMinimumWidth(), drawableOn.getMinimumHeight());
        drawableOff.setBounds(0, 0, drawableOff.getMinimumWidth(), drawableOff.getMinimumHeight());
    }

    public void setDeleteReceivingLocationData(DeleteReceivingLocationData deleteReceivingLocationData) {
        this.deleteReceivingLocationData = deleteReceivingLocationData;
    }

    public void setActivityType(String type) {
        this.mType = type;
    }

    public void setmReceivingLocation(ReceivingLocationBean mReceivingLocation) {
        this.mReceivingLocation = mReceivingLocation;
        notifyDataSetInvalidated();
    }

    @Override
    public int getCount() {
        return mReceivingLocation.getData().size();
    }

    @Override
    public Object getItem(int i) {
        return mReceivingLocation.getData().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ReceivingViewHolder receivingViewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_receiving_item, null);
            receivingViewHolder = new ReceivingViewHolder(view);
            view.setTag(receivingViewHolder);
        } else {
            receivingViewHolder = (ReceivingViewHolder) view.getTag();
        }

        if (mType.equals("1")) {
            receivingViewHolder.tv_receiving_delete.setVisibility(View.GONE);
        }
        receivingViewHolder.tv_receiving_name.setText(mReceivingLocation.getData().get(i).getReceiveName());
        receivingViewHolder.tv_receiving_phone.setText(mReceivingLocation.getData().get(i).getReceivePhone());
        receivingViewHolder.tv_receiving_address.setText(mReceivingLocation.getData().get(i).getAddrName() + mReceivingLocation.getData().get(i).getRaddressName());
        receivingViewHolder.tv_receiving_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteReceivingLocationData.deleteReceivingLocationData(i, Integer.valueOf(mReceivingLocation.getData().get(i).getReceiveId()));
            }
        });

        receivingViewHolder.tv_receiving_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, AddLocationActivity.class).putExtra("mReceivingLocation", mReceivingLocation.getData().get(i)));
            }
        });
        if (mReceivingLocation.getData().get(i).getDefaultAddress().equals("0")) {
            receivingViewHolder.tv_adapter_my_address_def.setTextColor(Color.parseColor("#999999"));
            receivingViewHolder.tv_adapter_my_address_def.setClickable(true);
            receivingViewHolder.tv_adapter_my_address_def.setCompoundDrawables(drawableOff, null, null, null);
            receivingViewHolder.tv_adapter_my_address_def.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteReceivingLocationData.defaultAddress(i, Integer.valueOf(mReceivingLocation.getData().get(i).getReceiveId()));
                }
            });
        } else {
            receivingViewHolder.tv_adapter_my_address_def.setTextColor(Color.parseColor("#7DDC98"));
            receivingViewHolder.tv_adapter_my_address_def.setClickable(false);
            receivingViewHolder.tv_adapter_my_address_def.setCompoundDrawables(drawableOn, null, null, null);
        }


        return view;
    }


    public interface DeleteReceivingLocationData {
        void deleteReceivingLocationData(int pos, int receiveId);

        void defaultAddress(int pos, int receiveId);
    }

    class ReceivingViewHolder {
        private TextView tv_receiving_name;
        private TextView tv_receiving_phone;
        private TextView tv_receiving_address;
        private TextView tv_receiving_change;
        private TextView tv_receiving_delete;
        private TextView tv_adapter_my_address_def;

        public ReceivingViewHolder(View view) {
            tv_receiving_name = view.findViewById(R.id.tv_adapter_my_address_customer);
            tv_receiving_phone = view.findViewById(R.id.tv_adapter_my_address_tel);
            tv_receiving_address = view.findViewById(R.id.tv_adapter_my_address);
            tv_receiving_change = view.findViewById(R.id.tv_adapter_my_address_edit);
            tv_receiving_delete = view.findViewById(R.id.tv_adapter_my_address_del);
            tv_adapter_my_address_def = view.findViewById(R.id.tv_adapter_my_address_def);
        }
    }
}
