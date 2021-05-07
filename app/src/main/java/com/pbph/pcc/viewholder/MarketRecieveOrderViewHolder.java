package com.pbph.pcc.viewholder;

import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.adapter.ViewHolder;
import com.pbph.pcc.R;
import com.pbph.pcc.bean.response.GetAcceptOrderListBean;
import com.utils.StringUtils;


public class MarketRecieveOrderViewHolder extends ViewHolder {


    RelativeLayout rl_recieveorderitem;

    TextView textView1;

    TextView textView22;

    TextView textView44;

    TextView textView55;

    TextView textView66;


    @Override

    protected int getLayout() {
        return R.layout.listview_market_recieve_order;
    }

    @Override
    protected void getView(View view) {

        rl_recieveorderitem = view.findViewById(R.id.rl_recieveorderitem);

        textView1 = view.findViewById(R.id.textView1);

        textView22 = view.findViewById(R.id.textView22);

        textView44 = view.findViewById(R.id.textView44);

        textView55 = view.findViewById(R.id.textView55);

        textView66 = view.findViewById(R.id.textView66);


    }

    @Override
    protected void showView() {
        GetAcceptOrderListBean.DataBean.OrderListEntityBean vo = (GetAcceptOrderListBean.DataBean.OrderListEntityBean) item;

        textView1.setText(vo.getOrderTypeText() + "(" + vo.getExpectedDeliveryTime() + "送达)");

        textView22.setText(vo.getSendAddressName());
        textView44.setText(vo.getGettingAmount() + "元");

        textView55.setText(vo.getDinnerAmount() + "元");

        if (StringUtils.isEqual(vo.getResortStatus(), "0")) {
            textView66.setText("抢单 >>");
            rl_recieveorderitem.setBackgroundColor(Color.WHITE);
        } else {
            textView66.setText("滞留单 >>");
            rl_recieveorderitem.setBackgroundResource(R.color.zld_bg);
        }
    }
}
