package com.pbph.pcc.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.utils.LogUtils;
import com.pbph.pcc.R;
import com.pbph.pcc.activity.ConfirmOrderActivity;
import com.pbph.pcc.adapter.ShoppingCarAdapter;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.dao.ShoppingCarBean;
import com.pbph.pcc.db.DaoSession;
import com.pbph.pcc.db.ShoppingCarC;
import com.pbph.pcc.db.ShoppingCarCDao;
import com.pbph.pcc.db.ShoppingCarP;
import com.pbph.pcc.db.ShoppingCarPDao;
import com.pbph.pcc.tools.ConstantData;
import com.pbph.pcc.view.RxDialogSureCancel;
import com.pbph.pcc.view.ShoppingCarDialog;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCarFragment extends Fragment implements View.OnClickListener,ShoppingCarAdapter.OnDeleteRefreshListener{
    View view;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView title;
    private TextView btnRight;
    private String mParam1;
    private String mParam2;
    private ShoppingCarAdapter listadapter ;
    private ExpandableListView listview;
    private LinearLayout shoppingCarEmpeyImg;
    List<ShoppingCarBean> shoppingCarList = new ArrayList<ShoppingCarBean>();
    ShoppingCarPDao shoppingCarPDao ;
    ShoppingCarCDao shoppingCarCDao ;
    PayReceiver payReceiver;
    String userId = "";
    String schoolId = "";
    PccApplication application = null;
    public ShoppingCarFragment() {

    }

    public static ShoppingCarFragment newInstance(String param1, String param2) {
        ShoppingCarFragment fragment = new ShoppingCarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        LogUtils.e("==>>>","gwc create");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*if (null != view) {
            return view;
        }*/
        DaoSession daoSession = PccApplication.getDaoSession();
        application = (PccApplication) getActivity().getApplication();
        schoolId = application.getMyInfoData().getSchoolId();
        userId = PccApplication.getUserid();
        shoppingCarPDao = daoSession.getShoppingCarPDao();
        shoppingCarCDao = daoSession.getShoppingCarCDao();
        //initData();
        view = inflater.inflate(R.layout.fragment_shopping_car, container, false);
        title = view.findViewById(R.id.tv_title);
        btnRight = view.findViewById(R.id.btn_right);
        ImageView leftBtn = view.findViewById(R.id.btn_left);
        leftBtn.setVisibility(View.GONE);
        shoppingCarEmpeyImg = view.findViewById(R.id.shopping_car_empty_img);
        btnRight.setVisibility(View.VISIBLE);
        title.setText("购物车");
        btnRight.setOnClickListener(this);
        listadapter = new ShoppingCarAdapter(shoppingCarList,getActivity());
        listadapter.setDeleteRefreshListener(this);
        listview = view.findViewById(R.id.expandablelistview);
        listview.setAdapter(listadapter);
        /*listview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return true;   //默认为false，设为true时，点击事件不会展开Group
            }
        });*/
        initialData();
        if (shoppingCarList.isEmpty()){
            listview.setVisibility(View.GONE);
            btnRight.setVisibility(View.GONE);
            shoppingCarEmpeyImg.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private void initialData() {
        shoppingCarList.clear();
        List<ShoppingCarP> shoppingCarPs = shoppingCarPDao.queryBuilder().where(ShoppingCarPDao.Properties.UserId.eq(userId)
                //,ShoppingCarPDao.Properties.ShopId.notEq("-1")
                ,ShoppingCarPDao.Properties.SchoolId.eq(schoolId)).list();
        for (int i = 0;i<shoppingCarPs.size();i++){
            List<ShoppingCarC> shoppingCarCs =shoppingCarCDao.queryBuilder().where(ShoppingCarCDao.Properties.ShopId.eq(shoppingCarPs.get(i).getShopId())
                    ,ShoppingCarCDao.Properties.UserId.eq(userId)
                    ,ShoppingCarCDao.Properties.SchoolId.eq(schoolId)).list();
            ShoppingCarBean bean = new ShoppingCarBean();
            Double totlePrice = 0.0;
            for (int j = 0;j < shoppingCarCs.size();j++){
                totlePrice += shoppingCarCs.get(j).getGoodPrice() * shoppingCarCs.get(j).getGoodNum();
            }
            shoppingCarPs.get(i).setTotlePrice(totlePrice);
            if (/*shoppingCarPs.get(i).getShopIsChecked() && */!shoppingCarCs.isEmpty()){
                bean.setShoppingCarP(shoppingCarPs.get(i));
                bean.setShoppingCarCs(shoppingCarCs);
                shoppingCarList.add(bean);
            }
            if (shoppingCarCs.isEmpty()){
                shoppingCarPDao.delete(shoppingCarPs.get(i));
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        payReceiver = new PayReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantData.REFRESH_SHOPPING_CAR);
        getActivity().registerReceiver(payReceiver,intentFilter);
    }

    @Override
    public void onResume() {
        super.onResume();
        //设置自动展开
        //自动展开必须写在onResume方法中，否则会发生错误
        if (listadapter != null){
            for (int i = 0; i < listadapter.getGroupCount(); i++) {
                listview.expandGroup(i);
            }
        }
        if (listadapter != null){
            initialData();
            listadapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.settlement:
                for (int i = 0;i< shoppingCarList.size();i++){
                    ShoppingCarP shoppingCarP = shoppingCarList.get(i).getShoppingCarP();
                    shoppingCarPDao.update(shoppingCarP);
                    for (int j = 0;j<shoppingCarList.get(i).getShoppingCarCs().size();j++){
                        ShoppingCarC shoppingCarC = shoppingCarList.get(i).getShoppingCarCs().get(j);
                        shoppingCarCDao.update(shoppingCarC);
                    }
                }
                Intent intent = new Intent(getActivity(), ConfirmOrderActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.btn_right:
                final RxDialogSureCancel rxDialogSureCancel = ShoppingCarDialog.getDialog(getActivity(),"是否清空购物车？");
                rxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxDialogSureCancel.cancel();
                    }
                });
                rxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shoppingCarPDao.deleteAll();
                        shoppingCarCDao.deleteAll();
                        shoppingCarList.clear();
                        listadapter.notifyDataSetChanged();
                        rxDialogSureCancel.cancel();
                        listview.setVisibility(View.GONE);
                        btnRight.setVisibility(View.GONE);
                        shoppingCarEmpeyImg.setVisibility(View.VISIBLE);
                    }
                });
                rxDialogSureCancel.show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != payReceiver){
            getActivity().unregisterReceiver(payReceiver);
        }
    }

    @Override
    public void onDeleteRefresh() {
        listview.setVisibility(View.GONE);
        shoppingCarEmpeyImg.setVisibility(View.VISIBLE);
        btnRight.setVisibility(View.GONE);
    }

    public class PayReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String action = intent.getAction();
            if (ConstantData.REFRESH_SHOPPING_CAR.equals(action)){
                initialData();
                if (shoppingCarList.isEmpty()){
                    listview.setVisibility(View.GONE);
                    btnRight.setVisibility(View.GONE);
                    shoppingCarEmpeyImg.setVisibility(View.VISIBLE);
                }
                listadapter.notifyDataSetChanged();
            }
        }
    }
}
