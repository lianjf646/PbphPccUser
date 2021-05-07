package com.pbph.pcc.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ui.OnSingleClickListener;
import com.android.utils.LogUtils;
import com.lzy.okgo.model.Response;
import com.pbph.pcc.R;
import com.pbph.pcc.adapter.ConfirmOrderListViewAdapter;
import com.pbph.pcc.adapter.TimeListAdapter;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.vo.SelectTimeBean;
import com.pbph.pcc.bean.vo.OrderBean;
import com.pbph.pcc.bean.response.ReceivingLocationBean;
import com.pbph.pcc.bean.response.WXPayBean;
import com.pbph.pcc.db.DaoSession;
import com.pbph.pcc.db.ShoppingCarC;
import com.pbph.pcc.db.ShoppingCarCDao;
import com.pbph.pcc.db.ShoppingCarP;
import com.pbph.pcc.db.ShoppingCarPDao;
import com.pbph.pcc.http.HttpAction;
import com.pbph.pcc.http.JsonCallback;
import com.pbph.pcc.tools.AMUtils;
import com.pbph.pcc.tools.ConstantData;
import com.pbph.pcc.tools.TextFilter;
import com.pbph.pcc.tools.WaitUI;
import com.pbph.pcc.view.SelectTimePopWindow;
import com.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 确认订单界面
 * Created by 孟庆奎 on 2017/9/25 0025.
 */

public class ConfirmOrderActivity extends BaseActivity implements AdapterView.OnItemSelectedListener,TimeListAdapter.OnItemClickListener{
    private TextView title, pay, totle, totleAll, address, name, telephone, addressTv,arriveDate;
    private ImageView leftBtn;
    private EditText remarkInfoEt;
    private ConfirmOrderListViewAdapter listadapter;
    private ExpandableListView listview;
    private ConstraintLayout addressLay,selectTimeLay;
    private LinearLayout address_ly;
    private Spinner rewardSp;
    List<OrderBean> orderBeanList = new ArrayList<OrderBean>();
    ShoppingCarPDao shoppingCarPDao;
    ShoppingCarCDao shoppingCarCDao;
    double tot = 0.0;
    String shopId = "";
    List<ShoppingCarP> shoppingCarPs;
    List<ShoppingCarC> shoppingCarCs;
    String schoolId = "";
    String sendAddressId = "";
    String receivePhone = "";
    String receiveName = "";
    String receiveSex = "";
    String raddressName = "";
    String storeId = "";
    String remarkInfo = "";
    String reward = "0";
    double dinnerAmount = 0.0;
    double shippingAmount = 0.0;
    String shopName = "";
    String shopImg = "";
    String userId = "";
    String orderId = "";
    int orderType;
    String expectedDeliveryTime = "";
    JSONArray takeoutSubList;
    private Receiver receiver;
    DecimalFormat df ;
    TimeListAdapter adapter;
    List<SelectTimeBean> selectTimeList ;
    SelectTimePopWindow selectTimePopWindow;
    SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        selectTimeList = new ArrayList<SelectTimeBean>();
        Date date = new Date();
        int hours = date.getHours();
        int minutes = date.getMinutes();
        int timeH = hours;
        int timeM = 0;
        if (minutes > 30){
            timeH = hours + 1;
            timeM = 0;
        }else{
            timeH = hours;
            timeM = 30;
        }
        date.setHours(timeH);
        date.setMinutes(timeM);
        expectedDeliveryTime = sdf.format(date);

        shopId = getIntent().getExtras().getString("shopId");
        orderType = getIntent().getExtras().getInt("orderType", 1);

        schoolId = application.getMyInfoData().getSchoolId();
        userId = PccApplication.getUserid();
        storeId = shopId;

        sendAddressId = application.getMyInfoData().getReceiveId();
        receivePhone = application.getMyInfoData().getReceivePhone();
        receiveName = application.getMyInfoData().getReceiveName();
        String sex = application.getMyInfoData().getReceiveSex();
        if ("0".equals(sex)) {
            receiveSex = "先生";
        } else if ("1".equals(sex)) {
            receiveSex = "女士";
        }
        raddressName = application.getMyInfoData().getRaddressName();
        shoppingCarCs = new ArrayList<ShoppingCarC>();
        shoppingCarPs = new ArrayList<ShoppingCarP>();
        DaoSession daoSession = PccApplication.getDaoSession();
        shoppingCarPDao = daoSession.getShoppingCarPDao();
        shoppingCarCDao = daoSession.getShoppingCarCDao();
        title = (TextView) findViewById(R.id.tv_title);
        leftBtn = (ImageView) findViewById(R.id.btn_left);
        remarkInfoEt = (EditText) findViewById(R.id.remark_info);
        pay = (TextView) findViewById(R.id.pay);
        totle = (TextView) findViewById(R.id.totle);
        totleAll = (TextView) findViewById(R.id.totle_all);
        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.address);
        telephone = (TextView) findViewById(R.id.telephone);
        addressTv = (TextView) findViewById(R.id.address_tv);
        arriveDate =(TextView) findViewById(R.id.arrive_date);
        address_ly = (LinearLayout) findViewById(R.id.address_ly);
        rewardSp =(Spinner) findViewById(R.id.reward);
        rewardSp.setOnItemSelectedListener(this);
        if (!(sendAddressId == null || "".equals(sendAddressId))) {
            name.setText(receiveName + " " + receiveSex);
            address.setText(raddressName);
            telephone.setText(receivePhone);
            addressTv.setVisibility(View.GONE);
            address_ly.setVisibility(View.VISIBLE);

        } else {
            addressTv.setVisibility(View.VISIBLE);
            address_ly.setVisibility(View.INVISIBLE);
        }
        addressLay = (ConstraintLayout) findViewById(R.id.address_lay);
        selectTimeLay = (ConstraintLayout) findViewById(R.id.select_time_lay);
        title.setText("确认订单");
        leftBtn.setVisibility(View.VISIBLE);
        leftBtn.setOnClickListener(onSingleClickListener);
        pay.setOnClickListener(onSingleClickListener);
        addressLay.setOnClickListener(onSingleClickListener);
        selectTimeLay.setOnClickListener(onSingleClickListener);
        listadapter = new ConfirmOrderListViewAdapter(orderBeanList, ConfirmOrderActivity.this);
        listview = (ExpandableListView) findViewById(R.id.expandablelistview);
        listview.setAdapter(listadapter);
        initialData();
        df = new DecimalFormat("#0.00");
        totle.setText("￥" + df.format(tot));
        totleAll.setText("￥" + df.format(tot + shippingAmount));
    }

    private void initialData() {
        shoppingCarPs = shoppingCarPDao.queryBuilder().where(ShoppingCarPDao.Properties.ShopId.eq(shopId)
                , ShoppingCarPDao.Properties.UserId.eq(userId)
                , ShoppingCarPDao.Properties.SchoolId.eq(schoolId)).list();
        List<ShoppingCarC> shoppingCarCs1 = shoppingCarCDao.queryBuilder().where(ShoppingCarCDao.Properties.ShopId.eq(shopId)
                , ShoppingCarCDao.Properties.UserId.eq(userId)
                , ShoppingCarCDao.Properties.SchoolId.eq(schoolId)).list();
        OrderBean bean = new OrderBean();
        bean.setTypeP("校内专送");
        bean.setShopId(shopId);
        if (shoppingCarPs.size() > 0) {
            shippingAmount = shoppingCarPs.get(0).getShippingAmount();
            shopName = shoppingCarPs.get(0).getShopName();
            shopImg = shoppingCarPs.get(0).getShopImgUrl();
            bean.setNameP(shopName);
            bean.setImgUrlP(shopImg);
            bean.setShippingAmount(shoppingCarPs.get(0).getShippingAmount());
        }
        /*bean.setNameP(shoppingCarPs.get(parentPos).getShopName());
        bean.setImgUrlP(shoppingCarPs.get(parentPos).getShopImgUrl());*/
        List<OrderBean.ChildBean> list = new ArrayList<OrderBean.ChildBean>();
        shoppingCarCs.clear();
        for (int j = 0; j < shoppingCarCs1.size(); j++) {
            if (shoppingCarCs1.get(j).getGoodNum() != 0) {
                OrderBean.ChildBean childBean = new OrderBean.ChildBean();
                childBean.setNameC(shoppingCarCs1.get(j).getGoodName());
                childBean.setImgUrlC(shoppingCarCs1.get(j).getGoodImgUrl());
                childBean.setPriceC(shoppingCarCs1.get(j).getGoodPrice());
                childBean.setNumC(shoppingCarCs1.get(j).getGoodNum());
                list.add(childBean);
                tot += shoppingCarCs1.get(j).getGoodPrice() * shoppingCarCs1.get(j).getGoodNum();
                shoppingCarCs.add(shoppingCarCs1.get(j));
            }
        }
        if (list.size() != 0) {
            bean.setChildBeens(list);
            orderBeanList.add(bean);
        }
    }

    //插入订单
    private void addNewOrderInfo() {
        if (sendAddressId == null || "".equals(sendAddressId)) {
            Toast.makeText(ConfirmOrderActivity.this, "请选择收货地址", Toast.LENGTH_LONG).show();
            return;
        }
        dinnerAmount = tot;
        remarkInfo = remarkInfoEt.getText().toString();
        DecimalFormat df = new DecimalFormat("#0.00");
        takeoutSubList = new JSONArray();
        for (int i = 0; i < shoppingCarCs.size(); i++) {
            JSONObject json = new JSONObject();
            try {
                if ("".equals(shoppingCarCs.get(i).getGoodId())) {
                    Toast.makeText(ConfirmOrderActivity.this, getResources().getString(R.string.connect_error), Toast.LENGTH_LONG).show();
                    return;
                } else {
                    json.put("goodsId", Integer.valueOf(shoppingCarCs.get(i).getGoodId()));
                    json.put("goodsName", shoppingCarCs.get(i).getGoodName());
                    json.put("goodsNum", shoppingCarCs.get(i).getGoodNum());
                    json.put("totalAmount", df.format(shoppingCarCs.get(i).getGoodPrice() * shoppingCarCs.get(i).getGoodNum()));
                    json.put("goodsPrice", shoppingCarCs.get(i).getGoodPrice());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            takeoutSubList.put(json);
        }
        WaitUI.Show(this);
        HttpAction.getInstance().addNewOrderInfo(schoolId, sendAddressId, storeId, remarkInfo
                , dinnerAmount, shippingAmount, takeoutSubList, userId, orderType,expectedDeliveryTime, new JsonCallback<WXPayBean>(WXPayBean.class) {
                    @Override
                    public void onSuccess(Response<WXPayBean> response) {
                        WaitUI.Cancel();
                        LogUtils.e("res=  " + response.body());
                        try {
                            if (response.code() == 200) {
                                WXPayBean wxPayBean = response.body();

                                if (!StringUtils.isEqual(wxPayBean.getCode(), "200")) {
                                    Toast.makeText(ConfirmOrderActivity.this, StringUtils.isEmpty(wxPayBean.getMsg()) ? "数据错误" :wxPayBean.getMsg(), Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if ("200".equals(wxPayBean.getCode())) {
                                    Intent intent = new Intent(ConfirmOrderActivity.this, PayOrderActivity.class);
                                    intent.putExtra("totlePrice", tot);
                                    intent.putExtra("shippingAmount", shippingAmount);
                                    intent.putExtra("shopName", shopName);
                                    intent.putExtra("shopImg", shopImg);
                                    intent.putExtra("orderType", orderType);
                                    intent.putExtra("orderNum", wxPayBean.getData().getOrderCode());
                                    orderId = wxPayBean.getData().getOrderId();
                                    intent.putExtra("wxPayBean", wxPayBean);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(ConfirmOrderActivity.this, StringUtils.isEmpty(wxPayBean.getMsg()) ? "数据错误" : wxPayBean.getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ConfirmOrderActivity.this, "系统错误", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Response<WXPayBean> response) {
                        super.onError(response);
                        WaitUI.Cancel();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (receiver == null) {
            receiver = new Receiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConstantData.WXPAY_RESULT);
            registerReceiver(receiver, intentFilter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //设置自动展开
        //自动展开必须写在onResume方法中，否则会发生错误
        if (listadapter != null) {
            for (int i = 0; i < listadapter.getGroupCount(); i++) {
                listview.expandGroup(i);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {
        @Override
        public void onCanClick(View view) {
            switch (view.getId()) {
                case R.id.btn_left:
                    onBackPressed();
                    break;
                case R.id.address_lay:
                    Intent intent = new Intent(ConfirmOrderActivity.this,ReceivingLocationActivity.class);
                    intent.putExtra("ReceivingLocationActivity","1");
                    startActivityForResult(intent,222);
                    break;
                case R.id.select_time_lay:
                    //选择预计送达时间
                    if (selectTimeList.size() == 0){
                        Date date = new Date();
                        int hours = date.getHours();
                        int minutes = date.getMinutes();
                        int timeH = hours;
                        int timeM = 0;
                        if (minutes > 30){
                            timeH = hours + 1;
                            timeM = 0;
                        }else{
                            timeH = hours;
                            timeM = 30;
                        }
                        date.setHours(timeH);
                        date.setMinutes(timeM);
                        expectedDeliveryTime = sdf.format(date);
                        SelectTimeBean selectTimeBean = new SelectTimeBean();
                        selectTimeBean.setDate(date);
                        selectTimeBean.setChecked(true);
                        selectTimeList.add(selectTimeBean);
                        for (int i = 0;i< 12;i++){
                            selectTimeBean = new SelectTimeBean();
                            if (timeM == 0){
                                timeM = 30;
                            }else if (timeM == 30){
                                timeM = 0;
                                timeH ++;
                            }
                            date = new Date();
                            date.setHours(timeH);
                            date.setMinutes(timeM);
                            selectTimeBean.setDate(date);
                            selectTimeBean.setChecked(false);
                            selectTimeList.add(selectTimeBean);
                            if (timeH == 24){
                                break;
                            }
                        }
//                      Toast.makeText(ConfirmOrderActivity.this,""+hours + ":"+minutes,Toast.LENGTH_LONG).show();
                        adapter = new TimeListAdapter(ConfirmOrderActivity.this,shippingAmount,selectTimeList);
                        adapter.setOnItemClickListener(ConfirmOrderActivity.this);
                        selectTimePopWindow = new SelectTimePopWindow(ConfirmOrderActivity.this,adapter);
                        selectTimePopWindow.showPopupWindow(findViewById(R.id.title_lay),ConfirmOrderActivity.this);
                    }else{
                        selectTimePopWindow.showPopupWindow(findViewById(R.id.title_lay),ConfirmOrderActivity.this);
                    }
                    break;
                case R.id.pay:
                    String regEx = "[/]";
                    if (AMUtils.hasEmoji(remarkInfoEt.getText() == null ? "" : remarkInfoEt.getText().toString().trim())) {
                        Toast.makeText(ConfirmOrderActivity.this, "不支持表情", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextFilter.filter(regEx, remarkInfoEt.getText() == null ? "" : remarkInfoEt.getText().toString().trim())) {
                        Toast.makeText(ConfirmOrderActivity.this, "请不要输入'/'呦！！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    addNewOrderInfo();
            }
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        /*String[] rewards = getResources().getStringArray(R.array.rewards);
        reward = rewards[i];*/
        shippingAmount -= Double.parseDouble(reward);
        switch (i){
            case 0:
                reward = "0";
                break;
            case 1:
                reward = "1";
                break;
            case 2:
                reward = "2";
                break;
            case 3:
                reward = "3";
                break;
            case 4:
                reward = "4";
                break;
            case 5:
                reward = "5";
                break;
            case 6:
                reward = "6";
                break;
            default:
                break;
        }
        shippingAmount += Double.parseDouble(reward);
        totleAll.setText("¥" + df.format(tot + shippingAmount));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClick(View view, int position) {
        TextView tv = view.findViewById(R.id.tv_time);
        for (int i = 0;i<selectTimeList.size();i++){
            if (i == position){
                selectTimeList.get(i).setChecked(true);
                arriveDate.setText(tv.getText().toString());
            }else{
                selectTimeList.get(i).setChecked(false);
            }
        }
        expectedDeliveryTime = sdf.format(selectTimeList.get(position).getDate());
        adapter.notifyDataSetChanged();
        if (selectTimePopWindow != null){
            selectTimePopWindow.dismiss();
        }
    }


    //广播通知关闭界面 并更新本地数据库
    public class Receiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String action = intent.getAction();
            int errCode = bundle.getInt("errCode");
            if (ConstantData.WXPAY_RESULT.equals(action) && errCode == 0) {
                for (int i = 0; i < shoppingCarCs.size(); i++) {
                    ShoppingCarC shoppingCarC = shoppingCarCs.get(i);
                    shoppingCarCDao.delete(shoppingCarC);
                }
                List<ShoppingCarC> list = shoppingCarCDao.queryBuilder().where(ShoppingCarCDao.Properties.ShopId.eq(shopId)
                        , ShoppingCarCDao.Properties.UserId.eq(userId)
                        , ShoppingCarCDao.Properties.SchoolId.eq(schoolId)).list();
                if (list.size() == 0) {
                    for (int i = 0; i < shoppingCarPs.size(); i++) {
                        ShoppingCarP shoppingCarP = shoppingCarPs.get(i);
                        shoppingCarPDao.delete(shoppingCarP);
                    }
                }
                Intent intent1 = new Intent();
                intent1.setAction(ConstantData.REFRESH_SHOPPING_CAR);
                sendBroadcast(intent1);
                /*Intent intent2 = new Intent(ConfirmOrderActivity.this,OrderDetailActivity.class);
                intent2.putExtra("orderId",Integer.parseInt(orderId));
                intent2.putExtra("orderFrom",1);
                intent2.putExtra("orderType",orderType);
                intent2.putExtra("orderState",1);
                startActivity(intent2);*/
                finish();
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }
    //选择地址界面回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (resultCode == 222) {
                ReceivingLocationBean.DataBean dataBean = (ReceivingLocationBean.DataBean) data.getExtras().get("ReceivingLocationBean");
                if (dataBean != null) {
                    String sex = dataBean.getReceiveSex();
                    String rSex = "";
                    if ("0".equals(sex)) {
                        rSex = "先生";
                    } else if ("1".equals(sex)) {
                        rSex = "女士";
                    }
                    sendAddressId = dataBean.getReceiveId();
                    address.setText(dataBean.getRaddressName());
                    name.setText(dataBean.getReceiveName() + " " + rSex);
                    telephone.setText(dataBean.getReceivePhone());
                    addressTv.setVisibility(View.GONE);
                    address_ly.setVisibility(View.VISIBLE);
                } else {
                    if (!(sendAddressId == null || "".equals(sendAddressId))) {
                        String id = application.getMyInfoData().getReceiveId();
                        if (!(id == null || "".equals(id))) {
                            sendAddressId = id;
                            receivePhone = application.getMyInfoData().getReceivePhone();
                            receiveName = application.getMyInfoData().getReceiveName();
                            raddressName = application.getMyInfoData().getRaddressName();
                            String sex = application.getMyInfoData().getReceiveSex();
                            if ("0".equals(sex)) {
                                receiveSex = "先生";
                            } else if ("1".equals(sex)) {
                                receiveSex = "女士";
                            }
                            name.setText(receiveName + " " + receiveSex);
                            address.setText(raddressName);
                            telephone.setText(receivePhone);
                            addressTv.setVisibility(View.GONE);
                            address_ly.setVisibility(View.VISIBLE);
                        }
                    } else {
                        addressTv.setVisibility(View.VISIBLE);
                        address_ly.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
    }
}
