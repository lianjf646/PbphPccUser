package com.pbph.pcc.viewholder;

import android.view.View;
import android.widget.TextView;

import com.android.adapter.ViewHolder;
import com.pbph.pcc.R;
import com.pbph.pcc.bean.response.GetSendedOrderListBean;


public class MarketSendRecordOrderViewHolder extends ViewHolder {

    TextView textView1;
    TextView textView11;


    TextView textView33;

    TextView textView66;


    @Override

    protected int getLayout() {
        return R.layout.listview_market_send_record_order;
    }

    @Override
    protected void getView(View view) {

        textView1 = view.findViewById(R.id.textView1);
        textView11 = view.findViewById(R.id.textView11);

        textView33 = view.findViewById(R.id.textView33);

        textView66 = view.findViewById(R.id.textView66);


    }

    @Override
    protected void showView() {

        GetSendedOrderListBean.DataBean.OrderListBean vo = (GetSendedOrderListBean.DataBean.OrderListBean) item;

        textView1.setText("预计送达:" + vo.getExpectedDeliveryTime());
        textView11.setText(vo.getOrderStatusText());

        textView33.setText(vo.getSendAddress());
    }
}
