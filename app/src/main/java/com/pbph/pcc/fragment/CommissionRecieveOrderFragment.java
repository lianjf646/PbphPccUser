package com.pbph.pcc.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.adapter.DataAdapter;
import com.android.utils.LogUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pbph.pcc.R;
import com.pbph.pcc.activity.OrderDetailActivity;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.response.GetAcceptOrderListBean;
import com.pbph.pcc.http.HttpAction;
import com.pbph.pcc.interfaces.RecieveOrderActivityFragmentLetter;
import com.pbph.pcc.json.JsonMananger;
import com.pbph.pcc.viewholder.CommissionRecieveOrderViewHolder;
import com.utils.StringUtils;

import java.util.List;

public class CommissionRecieveOrderFragment extends Fragment implements AdapterView.OnItemClickListener, RecieveOrderActivityFragmentLetter {


    String schoolId;


    View root;

    private PullToRefreshListView listView;
    private DataAdapter adapter;

    int page_num = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        PccApplication application = (PccApplication) getActivity().getApplication();
        schoolId = application.getMyInfoData().getSchoolId();
        initData();
        if (root == null) {

            root = inflater.inflate(R.layout.fragment_all_recieveorder, container, false);

            listView = root.findViewById(R.id.lv_order_item_list);
            listView.setMode(PullToRefreshBase.Mode.DISABLED);

            View empty = root.findViewById(R.id.ll_listview_empty1);
            listView.setEmptyView(empty);

            ListView actualListView = listView.getRefreshableView();

            adapter = new DataAdapter(this, actualListView, CommissionRecieveOrderViewHolder.class);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);


            // 设置监听事件
            listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                    getAcceptOrderList(page_num = 0);
                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    if (page_num < 0) {
                        listView.onRefreshComplete();
                        return;
                    }
                    page_num += 1;
                    getAcceptOrderList(page_num);
                }
            });
        }

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAcceptOrderList(page_num = 0);
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        GetAcceptOrderListBean.DataBean.OrderListEntityBean vo = (GetAcceptOrderListBean.DataBean.OrderListEntityBean) adapter.getItem(i);

        Intent intent = new Intent(getContext(), OrderDetailActivity.class);
        intent.putExtra("orderId", Integer.parseInt(vo.getOrderId()));
        intent.putExtra("orderFrom", 2);
        intent.putExtra("orderType", Integer.parseInt(vo.getOrderType()));

        intent.putExtra("from", true);

        intent.putExtra("orderState", 1);

        startActivity(intent);
    }


    String orderType = "4";
    String getAddressId = "";
    String sendAddressId = "";
    String gettingAmountMin = "";
    String gettingAmountMax = "";

    private void initData() {
        orderType = "4";
        getAddressId = "";
        sendAddressId = "";
        gettingAmountMin = "";
        gettingAmountMax = "";
    }

    @Override
    public void flushData(String orderType, String getAddressId, String sendAddressId, String gettingAmountMin, String gettingAmountMax) {

        this.orderType = orderType;
        this.getAddressId = getAddressId;
        this.sendAddressId = sendAddressId;
        this.gettingAmountMin = gettingAmountMin;
        this.gettingAmountMax = gettingAmountMax;

        getAcceptOrderList(page_num = 0);
    }

    @Override
    public void clearData() {
        PccApplication application = (PccApplication) getActivity().getApplication();
        int state = Integer.parseInt(application.getMyInfoData().getIsOrderSwitch());
        if (state == 0) {
            adapter.clearDatas();
        } else {
            getAcceptOrderList(page_num = 0);
        }
    }


    private void getAcceptOrderList(final int pageIndex) {

        PccApplication application = (PccApplication) getActivity().getApplication();
        int state = Integer.parseInt(application.getMyInfoData().getIsOrderSwitch());
        if (state == 0) {
            adapter.clearDatas();
            return;
        }

        if (orderType == null) return;
        if (pageIndex == 0) {
            listView.setMode(PullToRefreshBase.Mode.DISABLED);
//            adapter.clearDatas();
        }

        if (pageIndex < 0) {
            listView.onRefreshComplete();
            listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            return;
        }
        if (pageIndex != page_num) {
            listView.onRefreshComplete();
            return;
        }

        StringBuffer sb = new StringBuffer();
        sb.append(application.locationInfo.getmLongitude());
        sb.append(",");
        sb.append(application.locationInfo.getmLatitude());
        LogUtils.e(sb.toString());

        HttpAction.getInstance().getAcceptOrderList(
                schoolId,
                PccApplication.getUserid(),
                sb.toString(),

                pageIndex,
                orderType,
                getAddressId,
                sendAddressId,
                gettingAmountMin,
                gettingAmountMax,
                new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogUtils.e("res=  " + response.body());
                        try {

                            listView.onRefreshComplete();
                            listView.setMode(PullToRefreshBase.Mode.BOTH);

                            if (pageIndex != page_num) {
                                return;
                            }

                            if (pageIndex == 0) adapter.clearDatas();


                            GetAcceptOrderListBean vo = JsonMananger.jsonToBean(response.body(), GetAcceptOrderListBean.class);

                            if (!StringUtils.isEqual(vo.code, "200")) {
                                Toast.makeText(CommissionRecieveOrderFragment.this.getContext(), StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            List<GetAcceptOrderListBean.DataBean.OrderListEntityBean> datas = vo.getData().getOrderListEntity();
                            if (datas == null || datas.size() <= 0) {
                                page_num = -1;
                                listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
//                        tvLoading.setText("暂无数据");
                                return;
                            }

                            //添加数据
                            adapter.addDatas(datas);
                            adapter.notifyDataSetChanged();

//                            if (datas.size() >= PccApplication.PAGE_COUNT_10) return;
////                    否则就代表服务器没有数据了 设置负数代表 不再能刷新
//                            page_num = -1;
//                            listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "系统错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}

