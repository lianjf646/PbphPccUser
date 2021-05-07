package com.pbph.pcc.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.adapter.DataAdapter;
import com.android.utils.LogUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pbph.pcc.R;
import com.pbph.pcc.activity.OrderDetailActivity;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.response.GetReceivedOrderListBean;
import com.pbph.pcc.bean.response.GetSendedOrderListBean;
import com.pbph.pcc.bean.response.GetSendedOrderListBean.DataBean.OrderListBean;
import com.pbph.pcc.http.HttpAction;
import com.pbph.pcc.json.JsonMananger;
import com.pbph.pcc.viewholder.CommissionRecieveRecordOrderViewHolder;
import com.pbph.pcc.viewholder.CommissionSendRecordOrderViewHolder;
import com.pbph.pcc.viewholder.ExpressRecieveRecordOrderViewHolder;
import com.pbph.pcc.viewholder.ExpressSendRecordOrderViewHolder;
import com.pbph.pcc.viewholder.MarketRecieveRecordOrderViewHolder;
import com.pbph.pcc.viewholder.MarketSendRecordOrderViewHolder;
import com.pbph.pcc.viewholder.RestaurantRecieveRecordOrderViewHolder;
import com.pbph.pcc.viewholder.RestaurantSendRecordOrderViewHolder;
import com.utils.StringUtils;

import java.util.List;

public class MyOrderListFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    int userId;

    private View view = null;

    private View rl_order_card1, rl_order_card2;
    private ListView listView1, listView2;
    private DataAdapter adapter1, adapter2;


    private RadioButton[] radioButtons = new RadioButton[2];
    private int checkType = 0;

    public MyOrderListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    //    防止在onrseume中二次刷新
    boolean isFlush = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userId = Integer.parseInt(PccApplication.getUserid());

        isFlush = false;

        if (null == view) {

            isFlush = true;

            view = inflater.inflate(R.layout.fragment_my_order_list, container, false);

            radioButtons[0] = view.findViewById(R.id.tv_myorderlist_take);
            radioButtons[1] = view.findViewById(R.id.tv_myorderlist_release);
            radioButtons[0].setOnCheckedChangeListener(this);
            radioButtons[1].setOnCheckedChangeListener(this);

            rl_order_card1 = view.findViewById(R.id.rl_order_card1);
            View empty = view.findViewById(R.id.ll_listview_empty1);
            listView1 = view.findViewById(R.id.lv_myorder_list1);
            listView1.setEmptyView(empty);
            adapter1 = new DataAdapter(this, listView1, 4);
            listView1.setOnItemClickListener(listener1);

            rl_order_card2 = view.findViewById(R.id.rl_order_card2);
            empty = view.findViewById(R.id.ll_listview_empty2);
            listView2 = view.findViewById(R.id.lv_myorder_list2);
            listView2.setEmptyView(empty);
            adapter2 = new DataAdapter(this, listView2, 4);
            listView2.setOnItemClickListener(listener2);

            radioButtons[0].setChecked(true);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isFlush) {
            isFlush = false;
            return;
        }

        if (radioButtons[0].isChecked()) {
            checkType = 0;
            rl_order_card2.setVisibility(View.GONE);
            rl_order_card1.setVisibility(View.VISIBLE);
            getSendedOrderList(checkType);
        } else {
            checkType = 1;
            rl_order_card1.setVisibility(View.GONE);
            rl_order_card2.setVisibility(View.VISIBLE);
            getReceivedOrderList(checkType);
        }
    }

    AdapterView.OnItemClickListener listener1 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            OrderListBean ovo = (OrderListBean) adapter1.getItem(i);
            Intent intent = new Intent(getContext(), OrderDetailActivity.class);
            intent.putExtra("orderId", Integer.parseInt(ovo.getOrderId()));
            intent.putExtra("orderFrom", 1);
            intent.putExtra("orderType", Integer.parseInt(ovo.getOrderType()));

            intent.putExtra("orderState", Integer.parseInt(ovo.getOrderStatus()));

            startActivity(intent);

        }
    };
    AdapterView.OnItemClickListener listener2 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            GetReceivedOrderListBean.DataBean.OrderListBean ovo = (GetReceivedOrderListBean.DataBean.OrderListBean) adapter2.getItem(i);
            Intent intent = new Intent(getContext(), OrderDetailActivity.class);
            intent.putExtra("orderId", Integer.parseInt(ovo.getOrderId()));
            intent.putExtra("orderFrom", 2);
            intent.putExtra("orderType", Integer.parseInt(ovo.getOrderType()));

            intent.putExtra("orderState", Integer.parseInt(ovo.getOrderStatus()));

            startActivity(intent);
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        if (!b) return;

        switch (compoundButton.getId()) {
            case R.id.tv_myorderlist_take: {
                checkType = 0;
//                adapter1.clearDatas();
                rl_order_card2.setVisibility(View.GONE);
                rl_order_card1.setVisibility(View.VISIBLE);
                getSendedOrderList(checkType);
            }
            break;
            case R.id.tv_myorderlist_release: {
                checkType = 1;
//                adapter2.clearDatas();
                rl_order_card1.setVisibility(View.GONE);
                rl_order_card2.setVisibility(View.VISIBLE);
                getReceivedOrderList(checkType);
            }
            break;
        }
    }

    @Override

    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void getSendedOrderList(final int now_checkType) {
//        adapter1.clearDatas();

        if (checkType != now_checkType) {
            return;
        }

//        这里加上看似安全影响效率。不加又不释然。。。。。。
//        HttpAction.getInstance().getSendedOrderListCancel();

        HttpAction.getInstance().getSendedOrderList(userId, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("res=  " + response.body());
                try {


                    if (checkType != now_checkType) {
                        return;
                    }

                    adapter1.clearDatas();

                    GetSendedOrderListBean vo = JsonMananger.jsonToBean(response.body(), GetSendedOrderListBean.class);

                    if (!StringUtils.isEqual(vo.code, "200")) {
                        Toast.makeText(getContext(), StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<OrderListBean> datas = vo.getData().getOrderList();
                    if (datas == null || datas.size() <= 0) {
//                        tvLoading.setText("暂无数据");
                        return;
                    }


                    for (int i = 0, count = datas.size(); i < count; i++) {
                        OrderListBean ovo = datas.get(i);
                        int type = Integer.parseInt(ovo.getOrderType());
                        switch (type) {
                            case 1:
                                adapter1.addData(ovo, RestaurantSendRecordOrderViewHolder.class);
                                break;
                            case 2:
                                adapter1.addData(ovo, MarketSendRecordOrderViewHolder.class);
                                break;
                            case 3:
                                adapter1.addData(ovo, ExpressSendRecordOrderViewHolder.class);
                                break;
                            case 4:
                                adapter1.addData(ovo, CommissionSendRecordOrderViewHolder.class);
                                break;

                        }
                    }
                    adapter1.notifyDataSetChanged();


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MyOrderListFragment.this.getContext(), "系统错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getReceivedOrderList(final int now_checkType) {
//        adapter2.clearDatas();

        if (checkType != now_checkType) {
            return;
        }
//        HttpAction.getInstance().getReceivedOrderListCancel();

        HttpAction.getInstance().getReceivedOrderList(userId, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("res=  " + response.body());
                try {

                    if (checkType != now_checkType) {
                        return;
                    }
                    adapter2.clearDatas();

                    GetReceivedOrderListBean vo = JsonMananger.jsonToBean(response.body(), GetReceivedOrderListBean.class);

                    if (!StringUtils.isEqual(vo.code, "200")) {
                        Toast.makeText(getContext(), StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<GetReceivedOrderListBean.DataBean.OrderListBean> datas = vo.getData().getOrderList();
                    if (datas == null || datas.size() <= 0) {
//                        tvLoading.setText("暂无数据");
                        return;
                    }


                    for (int i = 0, count = datas.size(); i < count; i++) {
                        GetReceivedOrderListBean.DataBean.OrderListBean ovo = datas.get(i);
                        int type = Integer.parseInt(ovo.getOrderType());
                        switch (type) {
                            case 1:
                                adapter2.addData(ovo, RestaurantRecieveRecordOrderViewHolder.class);
                                break;
                            case 2:
                                adapter2.addData(ovo, MarketRecieveRecordOrderViewHolder.class);
                                break;
                            case 3:
                                adapter2.addData(ovo, ExpressRecieveRecordOrderViewHolder.class);
                                break;
                            case 4:
                                adapter2.addData(ovo, CommissionRecieveRecordOrderViewHolder.class);
                                break;
                        }
                    }
                    adapter2.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MyOrderListFragment.this.getContext(), "系统错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
