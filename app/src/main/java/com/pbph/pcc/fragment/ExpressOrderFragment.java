package com.pbph.pcc.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ui.OnSingleClickListener;
import com.android.utils.LogUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pbph.pcc.R;
import com.pbph.pcc.activity.PayOrderActivity;
import com.pbph.pcc.activity.ReceivingLocationActivity;
import com.pbph.pcc.adapter.MyArraySpinnerAdapter;
import com.pbph.pcc.adapter.MySpinnerAdapter;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.response.GetUserExpressInfoBean;
import com.pbph.pcc.bean.response.GetUserSelectExpressBean;
import com.pbph.pcc.bean.response.GetUserSelectExpressBean.DataBean.ExpressListEntityBean;
import com.pbph.pcc.bean.response.ReceivingLocationBean;
import com.pbph.pcc.bean.response.WXPayBean;
import com.pbph.pcc.bean.vo.KeyValueBean;
import com.pbph.pcc.http.HttpAction;
import com.pbph.pcc.http.JsonCallback;
import com.pbph.pcc.json.JsonMananger;
import com.pbph.pcc.tools.AMUtils;
import com.pbph.pcc.tools.ConstantData;
import com.pbph.pcc.tools.TextFilter;
import com.utils.DoubleUtil;
import com.utils.StringUtils;

import java.text.DecimalFormat;
import java.util.List;


public class ExpressOrderFragment extends Fragment {


    String userId;

    String orderType = "3";

    int schoolId;

    String sendAddressId;

    Double shippingAmount = null;
    Double expressUpstairsPrice = null;
    Double urgentPrice = null;
    Double areaPrice = null;

    boolean isUpStairs = false;

    Double tot_prise = null;

    ///
    private TextView tv_express_address_info;

    private Spinner sp_express_type;
    private MySpinnerAdapter sp_express_type_adapter;

    private Spinner sp_express_time_spinner;
    private MyArraySpinnerAdapter sp_express_time_spinner_adapter;

    private Spinner sp_express_is_upstairs;
    private MySpinnerAdapter sp_express_is_upstairs_adapter;

    private Spinner sp_express_urgent;
    private MySpinnerAdapter sp_express_urgent_adapter;

    private Spinner sp_express_areaurgent;
    private MySpinnerAdapter sp_express_areaurgent_adapter;


    private EditText edt_express_sms_info;
    private EditText edt_express_remarks_content;
    private TextView tv_express_total_price;

    private View include_cannot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        PccApplication application = (PccApplication) this.getActivity().getApplication();
        schoolId = Integer.parseInt(application.getMyInfoData().getSchoolId());
        userId = application.getUserid();

        View root = inflater.inflate(R.layout.fragment_express, container, false);


        tv_express_address_info = root.findViewById(R.id.tv_express_address_info);

        tv_express_address_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExpressOrderFragment.this.getContext(), ReceivingLocationActivity.class);

                intent.putExtra("ReceivingLocationActivity", "1");

                startActivityForResult(intent, 1000);
            }
        });
        sendAddressId = application.getMyInfoData().getReceiveId();
        tv_express_address_info.setText(application.getMyInfoData().getRaddressName());
        if (StringUtils.isEmpty(sendAddressId)) {
            sendAddressId = null;
            tv_express_address_info.setText("请选择收货地址");
        }


        sp_express_type = root.findViewById(R.id.sp_express_type);
        sp_express_type_adapter = new MySpinnerAdapter(getContext(), new MySpinnerAdapter.OnSetDataListener() {
            @Override
            public <T> String onSetData(T obj) {
                ExpressListEntityBean vo = (ExpressListEntityBean) obj;
                return vo.getAddrName();
            }
        });
        sp_express_type.setAdapter(sp_express_type_adapter);


        sp_express_time_spinner = root.findViewById(R.id.sp_express_time_spinner);
        sp_express_time_spinner_adapter = new MyArraySpinnerAdapter(getContext(), R.array.time_key, R.array.time_value);
        sp_express_time_spinner.setAdapter(sp_express_time_spinner_adapter);

        sp_express_is_upstairs = root.findViewById(R.id.sp_express_is_upstairs);
        sp_express_is_upstairs_adapter = new MyArraySpinnerAdapter(getContext(), R.array.yesorno_key, R.array.yesorno_value);
        sp_express_is_upstairs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    isUpStairs = false;
                } else {
                    isUpStairs = true;
                }
                setPrise();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_express_is_upstairs.setAdapter(sp_express_is_upstairs_adapter);


        sp_express_urgent = root.findViewById(R.id.sp_express_urgent);
        sp_express_urgent_adapter = new MyArraySpinnerAdapter(getContext(), R.array.money_key, R.array.money_value);
        sp_express_urgent.setAdapter(sp_express_urgent_adapter);
        sp_express_urgent.setOnItemSelectedListener(urgentListener);


        sp_express_areaurgent = root.findViewById(R.id.sp_express_areaurgent);
        sp_express_areaurgent_adapter = new MyArraySpinnerAdapter(getContext(), R.array.money_key, R.array.money_value);
        sp_express_areaurgent.setAdapter(sp_express_areaurgent_adapter);
        sp_express_areaurgent.setOnItemSelectedListener(areaUrgentListener);

        edt_express_sms_info = root.findViewById(R.id.edt_express_sms_info);
        edt_express_remarks_content = root.findViewById(R.id.edt_express_remarks_content);
        tv_express_total_price = root.findViewById(R.id.tv_express_total_price);

        root.findViewById(R.id.button).setOnClickListener(onSingleClickListener);

        include_cannot = root.findViewById(R.id.include_cannot);
        include_cannot.setVisibility(View.VISIBLE);
        include_cannot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        getUserExpressInfo();

        return root;
    }


    @Override
    public void onPause() {

        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onPause();
    }


    private void setPrise() {
        if (shippingAmount == null) {
            return;
        }
        if (expressUpstairsPrice == null) {
            return;
        }

        if (isUpStairs) {
            tot_prise = DoubleUtil.sum(shippingAmount, expressUpstairsPrice);
        } else {
            tot_prise = shippingAmount;
        }

        if (urgentPrice != null) {
            tot_prise = DoubleUtil.sum(tot_prise, urgentPrice);
        }

        if (areaPrice != null) {
            tot_prise = DoubleUtil.sum(tot_prise, areaPrice);
        }

        tv_express_total_price.setText("合计:" + (new DecimalFormat("0.00")).format(tot_prise));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 1000) return;

        if (resultCode != 222) return;

        ReceivingLocationBean.DataBean vo = (ReceivingLocationBean.DataBean) data.getSerializableExtra("ReceivingLocationBean");
        if (vo == null) return;

        sendAddressId = vo.getReceiveId();
        tv_express_address_info.setText(vo.getRaddressName());
    }

    OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {

        @Override
        public void onCanClick(View v) {
            switch (v.getId()) {
                case R.id.button: {
                    addNewOrderInfo(v);
                }
                break;
            }
        }
    };

    AdapterView.OnItemSelectedListener urgentListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            KeyValueBean vo = (KeyValueBean) sp_express_urgent_adapter.getItem(pos);

            urgentPrice = Double.valueOf(vo.key);

            setPrise();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    };

    AdapterView.OnItemSelectedListener areaUrgentListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            KeyValueBean vo = (KeyValueBean) sp_express_areaurgent_adapter.getItem(pos);

            areaPrice = Double.valueOf(vo.key);

            setPrise();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    };

    @Override
    public void onDestroyView() {
        HttpAction.getInstance().getUserSelectExpressCancel();
        HttpAction.getInstance().getUserExpressInfoCancel();

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


    private void getUserSelectExpress() {

        sp_express_type_adapter.clears();

        HttpAction.getInstance().getUserSelectExpress(schoolId, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("res=  " + response.body());
                try {
                    sp_express_type_adapter.clears();

                    GetUserSelectExpressBean vo = JsonMananger.jsonToBean(response.body(), GetUserSelectExpressBean.class);

                    if (!StringUtils.isEqual(vo.code, "200")) {
                        Toast.makeText(ExpressOrderFragment.this.getContext(), StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<ExpressListEntityBean> datas = vo.getData().getExpressListEntity();
                    if (datas == null || datas.size() <= 0) {
                        return;
                    }
                    sp_express_type_adapter.setDatas(datas);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ExpressOrderFragment.this.getContext(), "系统错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void getUserExpressInfo() {
        HttpAction.getInstance().getUserExpressInfo(schoolId, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("res=  " + response.body());
                try {

                    GetUserExpressInfoBean vo = JsonMananger.jsonToBean(response.body(), GetUserExpressInfoBean.class);

                    if (!StringUtils.isEqual(vo.code, "200")) {
                        Toast.makeText(ExpressOrderFragment.this.getContext(), StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                        return;
                    }


                    if (StringUtils.isEqual(vo.getData().getExpressEntity().getExpressStatus(), "0")) {
                        include_cannot.setVisibility(View.VISIBLE);
                        return;
                    }
                    include_cannot.setVisibility(View.GONE);

                    shippingAmount = Double.parseDouble(vo.getData().getExpressEntity().getExpressPrice());
                    expressUpstairsPrice = Double.parseDouble(vo.getData().getExpressEntity().getExpressUpstairsPrice());

                    setPrise();

                    getUserSelectExpress();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ExpressOrderFragment.this.getContext(), "系统错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void clearData() {
        sp_express_type.setSelection(0);
        sp_express_time_spinner.setSelection(0);
        sp_express_is_upstairs.setSelection(0);
        sp_express_urgent.setSelection(0);
        sp_express_areaurgent.setSelection(0);
        edt_express_sms_info.setText("");
        edt_express_sms_info.setText("");
        edt_express_remarks_content.setText("");
        setPrise();
    }


    public void addNewOrderInfo(final View v) {

        if (StringUtils.isEmpty(sendAddressId)) {
            Toast.makeText(getContext(), "请选择收货地", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sp_express_type_adapter.getCount() <= 0) {
            Toast.makeText(getContext(), "正在获取数据,请稍等", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sp_express_time_spinner_adapter.getCount() <= 0) {
            Toast.makeText(getContext(), "正在获取数据,请稍等", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sp_express_is_upstairs_adapter.getCount() <= 0) {
            Toast.makeText(getContext(), "正在获取数据,请稍等", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sp_express_urgent_adapter.getCount() <= 0) {
            Toast.makeText(getContext(), "正在获取数据,请稍等", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sp_express_areaurgent_adapter.getCount() <= 0) {
            Toast.makeText(getContext(), "正在获取数据,请稍等", Toast.LENGTH_SHORT).show();
            return;
        }
        String regEx = "[/]";
        if (AMUtils.hasEmoji(edt_express_sms_info.getText() == null ? "" : edt_express_sms_info.getText().toString().trim())) {
            Toast.makeText(getActivity(), "不支持表情", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextFilter.filter(regEx, edt_express_sms_info.getText() == null ? "" : edt_express_sms_info.getText().toString().trim())) {
            Toast.makeText(getActivity(), "请不要输入'/'呦！！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (AMUtils.hasEmoji(edt_express_remarks_content.getText() == null ? "" : edt_express_remarks_content.getText().toString().trim())) {
            Toast.makeText(getActivity(), "不支持表情", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextFilter.filter(regEx, edt_express_remarks_content.getText() == null ? "" : edt_express_remarks_content.getText().toString().trim())) {
            Toast.makeText(getActivity(), "请不要输入'/'呦！！", Toast.LENGTH_SHORT).show();
            return;
        }

        ExpressListEntityBean expressListEntityBean = (ExpressListEntityBean) sp_express_type.getSelectedItem();
        String expressNameId = String.valueOf(expressListEntityBean.getAddrId());
        if (StringUtils.isEmpty(expressNameId)) {
            Toast.makeText(getContext(), "请选择快递", Toast.LENGTH_SHORT).show();
            return;
        }


        String expressInfo = edt_express_sms_info.getText().toString().trim();
        if (StringUtils.isEmpty(expressInfo)) {
            Toast.makeText(getContext(), "请输入快递信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (expressInfo.length() > 200) {
            Toast.makeText(getContext(), "快递内容文字长度最长200个字", Toast.LENGTH_SHORT).show();
            return;
        }

        KeyValueBean keyValueBean = (KeyValueBean) sp_express_time_spinner.getSelectedItem();
        String receiveTimeRemark = String.valueOf(keyValueBean.value);
        if (StringUtils.isEmpty(receiveTimeRemark)) {
            Toast.makeText(getContext(), "请选择送达时间", Toast.LENGTH_SHORT).show();
            return;
        }


        keyValueBean = (KeyValueBean) sp_express_is_upstairs.getSelectedItem();
        String isUpstairs = String.valueOf(keyValueBean.key);
        if (StringUtils.isEmpty(isUpstairs)) {
            Toast.makeText(getContext(), "请选择是否上楼", Toast.LENGTH_SHORT).show();
            return;
        }

        keyValueBean = (KeyValueBean) sp_express_urgent.getSelectedItem();
        String surplusAmount = String.valueOf(keyValueBean.key);
        if (StringUtils.isEmpty(surplusAmount)) {
            Toast.makeText(getContext(), "请选择加急打赏", Toast.LENGTH_SHORT).show();
            return;
        }

        keyValueBean = (KeyValueBean) sp_express_areaurgent.getSelectedItem();
        String areaAmount = String.valueOf(keyValueBean.key);
        if (StringUtils.isEmpty(areaAmount)) {
            Toast.makeText(getContext(), "请选择跨区悬赏", Toast.LENGTH_SHORT).show();
            return;
        }

        String remarkInfo = edt_express_remarks_content.getText().toString().trim();

        if (remarkInfo.length() > 100) {
            Toast.makeText(getContext(), "备注内容文字长度最长100个字", Toast.LENGTH_SHORT).show();
            return;
        }

        if (tot_prise == null || tot_prise <= 0) {
            Toast.makeText(getContext(), "数据错误", Toast.LENGTH_SHORT).show();
            return;
        }


        v.setClickable(false);
        HttpAction.getInstance().addNewOrderInfo_Express(userId,
                String.valueOf(schoolId),
                orderType,
                sendAddressId,
                expressNameId,
                expressInfo,
                receiveTimeRemark,
                isUpstairs,
                surplusAmount,
                remarkInfo,
                String.valueOf(tot_prise), new JsonCallback<WXPayBean>(WXPayBean.class) {
                    @Override
                    public void onSuccess(Response<WXPayBean> response) {

                        try {
                            v.setClickable(true);

                            if (response.code() == 200) {

                                WXPayBean wxPayBean = response.body();

                                if (!StringUtils.isEqual(wxPayBean.getCode(), "200")) {
                                    Toast.makeText(ExpressOrderFragment.this.getContext(), StringUtils.isEmpty(wxPayBean.getMsg()) ? "数据错误" : wxPayBean.getMsg(), Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                register();


                                Intent intent = new Intent(ExpressOrderFragment.this.getContext(), PayOrderActivity.class);
                                intent.putExtra("totlePrice", tot_prise);
                                intent.putExtra("shippingAmount", 0D);
                                intent.putExtra("shopName", "快递任务");
                                intent.putExtra("shopImg", "");
                                intent.putExtra("orderNum", wxPayBean.getData().getOrderCode());
                                intent.putExtra("wxPayBean", wxPayBean);
                                intent.putExtra("orderType", Integer.parseInt(orderType));
                                startActivity(intent);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ExpressOrderFragment.this.getContext(), "系统错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public class PayReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String action = intent.getAction();
            if (ConstantData.WXPAY_RESULT.equals(action)) {
                clearData();
                unregister();
            }
        }
    }

    @Override
    public void onDestroy() {
        unregister();
        super.onDestroy();
    }

    PayReceiver payReceiver;

    private void register() {
        payReceiver = new PayReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantData.WXPAY_RESULT);
        getActivity().registerReceiver(payReceiver, intentFilter);
    }

    private void unregister() {
        if (null != payReceiver) {
            try {
                getActivity().unregisterReceiver(payReceiver);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

}
