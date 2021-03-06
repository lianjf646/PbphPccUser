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
import com.pbph.pcc.viewholder.ExpressRecieveOrderViewHolder;
import com.pbph.pcc.viewholder.MarketRecieveOrderViewHolder;
import com.pbph.pcc.viewholder.RestaurantRecieveOrderViewHolder;
import com.utils.StringUtils;

import java.util.List;


public class AllRecieveOrderFragment extends Fragment implements AdapterView.OnItemClickListener, RecieveOrderActivityFragmentLetter {


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

            adapter = new DataAdapter(this, actualListView, 4);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);


            // ??????????????????
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

    String orderType = "";
    String getAddressId = "";
    String sendAddressId = "";
    String gettingAmountMin = "";
    String gettingAmountMax = "";

    private void initData() {
        orderType = "";
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

        if (StringUtils.isEqual("0", this.orderType)) this.orderType = "";

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
                                Toast.makeText(AllRecieveOrderFragment.this.getContext(), StringUtils.isEmpty(vo.msg) ? "????????????" : vo.msg, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            List<GetAcceptOrderListBean.DataBean.OrderListEntityBean> datas = vo.getData().getOrderListEntity();
                            if (datas == null || datas.size() <= 0) {
                                page_num = -1;
                                listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
//                        tvLoading.setText("????????????");
                                return;
                            }

                            //????????????
//                            adapter.addDatas(datas);

                            for (int i = 0, count = datas.size(); i < count; i++) {

                                GetAcceptOrderListBean.DataBean.OrderListEntityBean ovo = datas.get(i);

                                if (StringUtils.isEqual(ovo.getOrderType(), "1")) {
                                    adapter.addData(ovo, RestaurantRecieveOrderViewHolder.class);
                                    continue;
                                }
                                if (StringUtils.isEqual(ovo.getOrderType(), "2")) {
                                    adapter.addData(ovo, MarketRecieveOrderViewHolder.class);
                                    continue;
                                }
                                if (StringUtils.isEqual(ovo.getOrderType(), "3")) {
                                    adapter.addData(ovo, ExpressRecieveOrderViewHolder.class);
                                    continue;
                                }
                                if (StringUtils.isEqual(ovo.getOrderType(), "4")) {
                                    adapter.addData(ovo, CommissionRecieveOrderViewHolder.class);
                                    continue;
                                }
                            }
                            adapter.notifyDataSetChanged();


//                            if (datas.size() >= PccApplication.PAGE_COUNT_10) return;
////                    ??????????????????????????????????????? ?????????????????? ???????????????
//                            page_num = -1;
//                            listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "????????????", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}
