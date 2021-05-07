package com.pbph.pcc.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import com.pbph.pcc.activity.RestaurantGoodsActivity;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.response.GetUserStoreBean;
import com.pbph.pcc.bean.response.GetUserTakeoutInfoBean;
import com.pbph.pcc.http.HttpAction;
import com.pbph.pcc.json.JsonMananger;
import com.pbph.pcc.viewholder.RestaurantOrderViewHolder;
import com.utils.StringUtils;

import java.util.List;


public class RestaurantOrderFragment extends Fragment implements AdapterView.OnItemClickListener {

    int schoolId;

    private PullToRefreshListView listView;
    private DataAdapter adapter;


    int page_num = 0;

    private View include_cannot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        PccApplication application = (PccApplication) getActivity().getApplication();
        schoolId = Integer.parseInt(application.getMyInfoData().getSchoolId());

        View root = inflater.inflate(R.layout.fragment_restaurant, container, false);


        include_cannot = root.findViewById(R.id.include_cannot);
        include_cannot.setVisibility(View.VISIBLE);
        include_cannot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        listView = root.findViewById(R.id.lv_order_item_list);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        ListView actualListView = listView.getRefreshableView();
        actualListView.setDivider(new ColorDrawable(getResources().getColor(R.color.main_bg)));
        actualListView.setDividerHeight(1);

        adapter = new DataAdapter(this, actualListView, RestaurantOrderViewHolder.class);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);


        // 设置监听事件
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (page_num < 0) {
                    listView.onRefreshComplete();
                    return;
                }
                page_num += PccApplication.PAGE_COUNT;
                getUserStore(page_num);
            }
        });

        getUserTakeoutInfo();
        return root;
    }

    @Override
    public void onDestroyView() {
        HttpAction.getInstance().getUserStoreCancel();
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
        GetUserStoreBean.DataBean.TakeoutListEntityBean vo = (GetUserStoreBean.DataBean.TakeoutListEntityBean) adapter.getItem(i);
        Intent intent = new Intent(RestaurantOrderFragment.this.getContext(), RestaurantGoodsActivity.class);
        intent.putExtra("storeId", vo.getStoreId());
        intent.putExtra("storeName", vo.getStoreName());
        intent.putExtra("storeImg", vo.getStoreImg());

        startActivity(intent);
    }


    private void getUserTakeoutInfo() {
        HttpAction.getInstance().getUserTakeoutInfo(schoolId, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("res=  " + response.body());
                try {

                    GetUserTakeoutInfoBean vo = JsonMananger.jsonToBean(response.body(), GetUserTakeoutInfoBean.class);

                    if (!StringUtils.isEqual(vo.code, "200")) {
                        Toast.makeText(RestaurantOrderFragment.this.getContext(), StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (StringUtils.isEqual(vo.getData().getTakeoutEntity().getTakeStatus(), "0")) {
                        include_cannot.setVisibility(View.VISIBLE);
                        return;
                    }
                    include_cannot.setVisibility(View.GONE);

                    getUserStore(page_num = 0);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(RestaurantOrderFragment.this.getContext(), "系统错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getUserStore(final int page) {

        if (page == 0) {
            listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
            adapter.clearDatas();
        }

        if (page < 0) {
            listView.onRefreshComplete();
            listView.setMode(PullToRefreshBase.Mode.DISABLED);
            return;
        }
        if (page != page_num) {
            listView.onRefreshComplete();
            return;
        }

        HttpAction.getInstance().getUserStore(schoolId, page, page + PccApplication.PAGE_COUNT, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("res=  " + response.body());
                try {
                    listView.onRefreshComplete();

                    if (page != page_num) {
                        return;
                    }

                    if (page == 0) adapter.clearDatas();

                    GetUserStoreBean vo = JsonMananger.jsonToBean(response.body(), GetUserStoreBean.class);

                    if (!StringUtils.isEqual(vo.code, "200")) {
                        Toast.makeText(RestaurantOrderFragment.this.getContext(), StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<GetUserStoreBean.DataBean.TakeoutListEntityBean> datas = vo.getData().getTakeoutListEntity();
                    if (datas == null || datas.size() <= 0) {
                        if (adapter.getCount() <= 0) {
                            include_cannot.setVisibility(View.VISIBLE);
                        }
                        page_num = -1;
                        listView.setMode(PullToRefreshBase.Mode.DISABLED);
//                        tvLoading.setText("暂无数据");
                        return;
                    }

                    //添加数据
                    adapter.addDatas(datas);
                    adapter.notifyDataSetChanged();

//                    两个判断条件 其实 加一个 就可以，判断服务器是否还有数据
                    if (datas.size() >= PccApplication.PAGE_COUNT) return;
//                    否则就代表服务器没有数据了 设置负数代表 不再能刷新
                    page_num = -1;
                    listView.setMode(PullToRefreshBase.Mode.DISABLED);
                    //
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(RestaurantOrderFragment.this.getContext(), "系统错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
