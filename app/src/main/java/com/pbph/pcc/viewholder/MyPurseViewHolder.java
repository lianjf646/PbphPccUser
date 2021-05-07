package com.pbph.pcc.viewholder;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.android.adapter.ViewHolder;
import com.pbph.pcc.R;
import com.pbph.pcc.bean.response.MyPurseBean;


public class MyPurseViewHolder extends ViewHolder {

    private TextView tv_adapter_wallet_income_expenses_list_item_name;
    private TextView tv_adapter_wallet_income_expenses_list_item_time;
    private TextView tv_adapter_wallet_income_expenses_list_item_money;


    @Override

    protected int getLayout() {
        return R.layout.adapter_wallet_income_expenses_list_item;
    }

    @Override
    protected void getView(View view) {
        tv_adapter_wallet_income_expenses_list_item_name = view.findViewById(R.id.tv_adapter_wallet_income_expenses_list_item_name);
        tv_adapter_wallet_income_expenses_list_item_time = view.findViewById(R.id.tv_adapter_wallet_income_expenses_list_item_time);
        tv_adapter_wallet_income_expenses_list_item_money = view.findViewById(R.id.tv_adapter_wallet_income_expenses_list_item_money);

    }

    @Override
    protected void showView() {

        final MyPurseBean.DataBean.DealsBean vo = (MyPurseBean.DataBean.DealsBean) item;

        int type = Integer.parseInt(vo.getDealStatus());


//vo.getDealStatus()        0收入 +，1支出 -，2退款 +
//        0 收入，1 支出，2 退款，3 提现
        String state = "";
        switch (type) {
            case 0:
                state = "收入";
                tv_adapter_wallet_income_expenses_list_item_money.setTextColor(adapter.context.getResources().getColor(R.color.style_color));
                tv_adapter_wallet_income_expenses_list_item_money.setText("+");
                break;
            case 1:
                state = "支出";
                tv_adapter_wallet_income_expenses_list_item_money.setTextColor(Color.GRAY);
                tv_adapter_wallet_income_expenses_list_item_money.setText("-");
                break;
            case 2:
                state = "退款";
                tv_adapter_wallet_income_expenses_list_item_money.setTextColor(adapter.context.getResources().getColor(R.color.style_color));
                tv_adapter_wallet_income_expenses_list_item_money.setText("+");
                break;
            case 3:
                state = "提现";
                tv_adapter_wallet_income_expenses_list_item_money.setTextColor(Color.GRAY);
                tv_adapter_wallet_income_expenses_list_item_money.setText("-");
                break;
        }
        tv_adapter_wallet_income_expenses_list_item_money.append(String.valueOf(vo.getDealPrice()));

        if (type != 3) {
            String text;
            switch (vo.getOrderType()) {
                case 1:
                    text = "外卖-";
                    break;
                case 2:
                    text = "超市-";
                    break;
                case 3:
                    text = "快递-";
                    break;
                case 4:
                    text = "任务-";
                    break;
                default:
                    text = "";
                    break;
            }
            tv_adapter_wallet_income_expenses_list_item_name.setText(text);

            tv_adapter_wallet_income_expenses_list_item_name.append(state);
        } else {
            tv_adapter_wallet_income_expenses_list_item_name.setText(state);
        }

        tv_adapter_wallet_income_expenses_list_item_time.setText(vo.getDealCreatetime());


    }
}
