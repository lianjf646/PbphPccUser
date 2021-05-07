package com.pbph.pcc.viewholder;

import android.view.View;
import android.widget.TextView;

import com.android.adapter.ViewHolder;
import com.pbph.pcc.R;
import com.pbph.pcc.bean.response.GetReceivedOrderListBean;


public class MarketRecieveRecordOrderViewHolder extends ViewHolder {

    TextView textView1;
    TextView textView11;


    TextView textView33;

    TextView textView66;

    TextView textView7;


    @Override

    protected int getLayout() {
        return R.layout.listview_market_recieve_record_order;
    }

    @Override
    protected void getView(View view) {

        textView1 = view.findViewById(R.id.textView1);
        textView11 = view.findViewById(R.id.textView11);

        textView33 = view.findViewById(R.id.textView33);

        textView66 = view.findViewById(R.id.textView66);

        textView7 = view.findViewById(R.id.textView7);


    }

    @Override
    protected void showView() {
        GetReceivedOrderListBean.DataBean.OrderListBean vo = (GetReceivedOrderListBean.DataBean.OrderListBean) item;

        textView1.setText("预计送达:" + vo.getExpectedDeliveryTime());
        textView11.setText(vo.getOrderStatusText());

        textView33.setText(vo.getSendAddress());
        textView7.setText(vo.getGettingAmount());
    }
}
