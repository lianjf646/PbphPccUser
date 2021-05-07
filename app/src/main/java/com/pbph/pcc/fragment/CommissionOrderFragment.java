package com.pbph.pcc.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ui.MoneyInputFilter;
import com.android.ui.OnSingleClickListener;
import com.android.utils.LogUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pbph.pcc.R;
import com.pbph.pcc.activity.PayOrderActivity;
import com.pbph.pcc.adapter.MyArraySpinnerAdapter;
import com.pbph.pcc.adapter.MySpinnerAdapter;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.response.GetUserSelectTaskAddressBean;
import com.pbph.pcc.bean.response.GetUserSelectTaskAddressBean.DataBean.TaskAddressEntityBean;
import com.pbph.pcc.bean.response.GetUserSelectTaskTypeBean;
import com.pbph.pcc.bean.response.GetUserSelectTaskTypeBean.DataBean.TaskTypeEntityBean;
import com.pbph.pcc.bean.response.GetUserTaskInfoBean;
import com.pbph.pcc.bean.response.WXPayBean;
import com.pbph.pcc.bean.vo.KeyValueBean;
import com.pbph.pcc.http.HttpAction;
import com.pbph.pcc.http.JsonCallback;
import com.pbph.pcc.json.JsonMananger;
import com.pbph.pcc.tools.AMUtils;
import com.pbph.pcc.tools.ConstantData;
import com.pbph.pcc.tools.TextFilter;
import com.utils.StringUtils;

import java.util.List;

public class CommissionOrderFragment extends Fragment {


    String userId;

    String orderType = "4";

    int schoolId;

    int addrTypeId = 1;

    String taskAmount;

    String taskStatus = "7";

    private Spinner sp_commission_mission_type;
    private MySpinnerAdapter sp_commission_mission_type_adapter;
    private EditText edt_commission_mission_desc_content;
    private Spinner sp_commission_address1;
    private MyArraySpinnerAdapter sp_commission_address1_adapter;
    private Spinner sp_commission_address2;
    private MySpinnerAdapter sp_commission_address2_adapter;
    private EditText edt_commission_address_content;
    private EditText et_commission_phone;


    private TextView tv_express_total_price;
    private EditText edt_express_total_price;

    private View include_cannot;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        PccApplication application = (PccApplication) this.getActivity().getApplication();
        schoolId = Integer.parseInt(application.getMyInfoData().getSchoolId());
        userId = application.getUserid();

        View root = inflater.inflate(R.layout.fragment_commission, container, false);
        sp_commission_mission_type = root.findViewById(R.id.sp_commission_mission_type);
        sp_commission_mission_type_adapter = new MySpinnerAdapter(getContext(), new MySpinnerAdapter.OnSetDataListener() {
            @Override
            public <T> String onSetData(T obj) {
                TaskTypeEntityBean vo = (TaskTypeEntityBean) obj;
                return vo.getReferName();
            }
        });
        sp_commission_mission_type.setAdapter(sp_commission_mission_type_adapter);


        edt_commission_mission_desc_content = root.findViewById(R.id.edt_commission_mission_desc_content);

        sp_commission_address1 = root.findViewById(R.id.sp_commission_address1);
        sp_commission_address1_adapter = new MyArraySpinnerAdapter(getContext(), R.array.place_key, R.array.place_value);
        sp_commission_address1.setAdapter(sp_commission_address1_adapter);
        sp_commission_address1.setOnItemSelectedListener(onItemSelectedListener);

        sp_commission_address2 = root.findViewById(R.id.sp_commission_address2);
        sp_commission_address2_adapter = new MySpinnerAdapter(getContext(), new MySpinnerAdapter.OnSetDataListener() {
            @Override
            public <T> String onSetData(T obj) {
                TaskAddressEntityBean vo = (TaskAddressEntityBean) obj;
                return vo.getAddName();
            }
        });
        sp_commission_address2.setAdapter(sp_commission_address2_adapter);

        edt_commission_address_content = root.findViewById(R.id.edt_commission_address_content);
        et_commission_phone = root.findViewById(R.id.et_commission_phone);
        tv_express_total_price = root.findViewById(R.id.tv_express_total_price);
        edt_express_total_price = root.findViewById(R.id.edt_express_total_price);
        setInputTypePrise();

        root.findViewById(R.id.button).setOnClickListener(onSingleClickListener);

        include_cannot = root.findViewById(R.id.include_cannot);
        include_cannot.setVisibility(View.VISIBLE);
        include_cannot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        getUserTaskInfo();

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

    AdapterView.OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            KeyValueBean vo = (KeyValueBean) sp_commission_address1_adapter.getItem(pos);
            addrTypeId = vo.key;
            getUserSelectTaskAddress(addrTypeId);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    };


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

    @Override
    public void onDestroyView() {
        HttpAction.getInstance().getUserSelectTaskTypeCancel();
        HttpAction.getInstance().getUserSelectTaskAddressCancel();
        HttpAction.getInstance().getUserTaskInfoCancel();
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


    private void getUserSelectTaskType() {

        HttpAction.getInstance().getUserSelectTaskType(schoolId, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("res=  " + response.body());
                try {

                    GetUserSelectTaskTypeBean vo = JsonMananger.jsonToBean(response.body(), GetUserSelectTaskTypeBean.class);

                    if (!StringUtils.isEqual(vo.code, "200")) {
                        Toast.makeText(CommissionOrderFragment.this.getContext(), StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<TaskTypeEntityBean> datas = vo.getData().getTaskTypeEntity();
                    if (datas == null || datas.size() <= 0) {
                        return;
                    }
                    sp_commission_mission_type_adapter.setDatas(datas);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CommissionOrderFragment.this.getContext(), "系统错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getUserSelectTaskAddress(final int addrTypeId_temp) {

        sp_commission_address2_adapter.clears();

        if (addrTypeId_temp != addrTypeId) return;

        HttpAction.getInstance().getUserSelectTaskAddress(schoolId, addrTypeId_temp, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("res=  " + response.body());
                try {
                    sp_commission_address2_adapter.clears();

                    if (addrTypeId_temp != addrTypeId) return;

                    GetUserSelectTaskAddressBean vo = JsonMananger.jsonToBean(response.body(), GetUserSelectTaskAddressBean.class);

                    if (!StringUtils.isEqual(vo.code, "200")) {
                        Toast.makeText(CommissionOrderFragment.this.getContext(), StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<TaskAddressEntityBean> datas = vo.getData().getTaskAddressEntity();
                    if (datas == null || datas.size() <= 0) {
                        return;
                    }

                    sp_commission_address2_adapter.setDatas(datas);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CommissionOrderFragment.this.getContext(), "系统错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void getUserTaskInfo() {
        HttpAction.getInstance().getUserTaskInfo(schoolId, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("res=  " + response.body());
                try {

                    GetUserTaskInfoBean vo = JsonMananger.jsonToBean(response.body(), GetUserTaskInfoBean.class);

                    if (!StringUtils.isEqual(vo.code, "200")) {
                        Toast.makeText(CommissionOrderFragment.this.getContext(), StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (StringUtils.isEqual(vo.getData().getTaskInfoEntity().getTaskStatus(), "0")) {
                        include_cannot.setVisibility(View.VISIBLE);
                        return;
                    }
                    include_cannot.setVisibility(View.GONE);

                    taskAmount = vo.getData().getTaskInfoEntity().getTaskPrice();
//                    tv_express_total_price.setText("合计:" + (new DecimalFormat("0.00")).format(taskAmount));
//                    tv_express_total_price.setText("合计:" + taskAmount);
//                    edt_express_total_price.setHint("起始价格不能低于" + taskAmount + "元");
                    edt_express_total_price.setText(taskAmount);


                    getUserSelectTaskType();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CommissionOrderFragment.this.getContext(), "系统错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void clearData() {
        sp_commission_mission_type.setSelection(0);
        sp_commission_address1.setSelection(0);
        edt_commission_mission_desc_content.setText("");
        edt_commission_address_content.setText("");
        et_commission_phone.setText("");
    }

    public void addNewOrderInfo(final View v) {

        if (sp_commission_mission_type_adapter.getCount() <= 0) {
            Toast.makeText(CommissionOrderFragment.this.getContext(), "正在获取数据,请稍等", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sp_commission_address1_adapter.getCount() <= 0) {
            Toast.makeText(CommissionOrderFragment.this.getContext(), "正在获取数据,请稍等", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sp_commission_address2_adapter.getCount() <= 0) {
            Toast.makeText(CommissionOrderFragment.this.getContext(), "正在获取数据,请稍等", Toast.LENGTH_SHORT).show();
            return;
        }
        String regEx = "[/]";
        if (AMUtils.hasEmoji(edt_commission_mission_desc_content.getText() == null ? "" : edt_commission_mission_desc_content.getText().toString().trim())) {
            Toast.makeText(getActivity(), "不支持表情", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextFilter.filter(regEx, edt_commission_mission_desc_content.getText() == null ? "" : edt_commission_mission_desc_content.getText().toString().trim())) {
            Toast.makeText(getActivity(), "请不要输入'/'呦！！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (AMUtils.hasEmoji(edt_commission_address_content.getText() == null ? "" : edt_commission_address_content.getText().toString().trim())) {
            Toast.makeText(getActivity(), "不支持表情", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextFilter.filter(regEx, edt_commission_address_content.getText() == null ? "" : edt_commission_address_content.getText().toString().trim())) {
            Toast.makeText(getActivity(), "请不要输入'/'呦！！", Toast.LENGTH_SHORT).show();
            return;
        }

        TaskTypeEntityBean taskTypeEntityBean = (TaskTypeEntityBean) sp_commission_mission_type.getSelectedItem();
        String taskType = String.valueOf(taskTypeEntityBean.getReferId());
        final String taskTypeName = taskTypeEntityBean.getReferName();
        if (StringUtils.isEmpty(taskType)) {
            Toast.makeText(CommissionOrderFragment.this.getContext(), "请选择任务类型", Toast.LENGTH_SHORT).show();
            return;
        }


        String taskDescribe = edt_commission_mission_desc_content.getText().toString().trim();
        if (StringUtils.isEmpty(taskDescribe)) {
            Toast.makeText(CommissionOrderFragment.this.getContext(), "任务描述不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (taskDescribe.length() > 200) {
            Toast.makeText(getContext(), "任务描述文字长度最长200个字", Toast.LENGTH_SHORT).show();
            return;
        }


        TaskAddressEntityBean taskAddressEntityBean = (TaskAddressEntityBean) sp_commission_address2.getSelectedItem();
        String taskAddressId = String.valueOf(taskAddressEntityBean.getAddId());
        if (StringUtils.isEmpty(taskAddressId)) {
            Toast.makeText(CommissionOrderFragment.this.getContext(), "请选地址", Toast.LENGTH_SHORT).show();
            return;
        }


        String taskAddressDetail = edt_commission_address_content.getText().toString().trim();
        if (StringUtils.isEmpty(taskAddressDetail)) {
            Toast.makeText(CommissionOrderFragment.this.getContext(), "地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (taskAddressDetail.length() > 100) {
            Toast.makeText(getContext(), "地址信息文字长度最长100个字", Toast.LENGTH_SHORT).show();
            return;
        }

        String taskTelephone = et_commission_phone.getText().toString().trim();
        if (StringUtils.isEmpty(taskTelephone)) {
            Toast.makeText(CommissionOrderFragment.this.getContext(), "电话不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!AMUtils.isMobile(taskTelephone)) {
            Toast.makeText(CommissionOrderFragment.this.getContext(), "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringUtils.isEmpty(taskAmount)) {
            Toast.makeText(CommissionOrderFragment.this.getContext(), "数据错误", Toast.LENGTH_SHORT).show();
            return;
        }

        final String newTaskAmount = edt_express_total_price.getText().toString().trim();
        if (StringUtils.isEmpty(newTaskAmount)) {
            Toast.makeText(CommissionOrderFragment.this.getContext(), "请输入价格", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double d1 = Double.parseDouble(taskAmount);
            double d2 = Double.parseDouble(newTaskAmount);
            if (d1 > d2) {
                Toast.makeText(CommissionOrderFragment.this.getContext(), "最低价格不能少于" + taskAmount + "元", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(CommissionOrderFragment.this.getContext(), "数据错误", Toast.LENGTH_SHORT).show();
        }


        v.setClickable(false);
        HttpAction.getInstance().addNewOrderInfo_Task(userId,
                String.valueOf(schoolId),
                orderType,
                taskType,
                taskDescribe,
                taskAddressId,
                taskAddressEntityBean.getAddName() + taskAddressDetail,
                taskTelephone,
                newTaskAmount,
                taskStatus
                , new JsonCallback<WXPayBean>(WXPayBean.class) {
                    @Override
                    public void onSuccess(Response<WXPayBean> response) {
                        try {
                            v.setClickable(true);
                            if (response.code() == 200) {

                                WXPayBean wxPayBean = response.body();

                                if (!StringUtils.isEqual(wxPayBean.getCode(), "200")) {
                                    Toast.makeText(CommissionOrderFragment.this.getContext(), StringUtils.isEmpty(wxPayBean.getMsg()) ? "数据错误" : wxPayBean.getMsg(), Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                register();


                                Intent intent = new Intent(CommissionOrderFragment.this.getContext(), PayOrderActivity.class);
                                intent.putExtra("totlePrice", Double.parseDouble(newTaskAmount));
                                intent.putExtra("shippingAmount", 0D);
                                intent.putExtra("shopName", taskTypeName);
                                intent.putExtra("shopImg", "");
                                intent.putExtra("orderNum", wxPayBean.getData().getOrderCode());
                                intent.putExtra("wxPayBean", wxPayBean);
                                intent.putExtra("orderType", Integer.parseInt(orderType));
                                startActivity(intent);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(CommissionOrderFragment.this.getContext(), "系统错误", Toast.LENGTH_SHORT).show();
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


    private void setInputTypePrise() {
//设置Input的类型两种都要
        edt_express_total_price.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
//设置字符过滤
        edt_express_total_price.setFilters(new InputFilter[]{new MoneyInputFilter()});
    }

}
