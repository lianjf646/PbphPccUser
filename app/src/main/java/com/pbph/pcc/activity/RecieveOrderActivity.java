package com.pbph.pcc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ui.FlowLayout;
import com.android.ui.OnSingleClickListener;
import com.android.utils.LogUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pbph.pcc.R;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.response.OrderFilterBean;
import com.pbph.pcc.bean.response.UUIDResultBean;
import com.pbph.pcc.fragment.AllRecieveOrderFragment;
import com.pbph.pcc.fragment.CommissionRecieveOrderFragment;
import com.pbph.pcc.fragment.ExpressRecieveOrderFragment;
import com.pbph.pcc.fragment.MarketRecieveOrderFragment;
import com.pbph.pcc.fragment.RestaurantRecieveOrderFragment;
import com.pbph.pcc.http.HttpAction;
import com.pbph.pcc.interfaces.RecieveOrderActivityFragmentLetter;
import com.pbph.pcc.json.JsonMananger;
import com.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class RecieveOrderActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    String schoolId;

    private Context context = this;
    PccApplication application;
    private LayoutInflater inflater;
    ///
    private FragmentTabHost mTabHost = null;
    private Class fragmentArray[] = {AllRecieveOrderFragment.class, RestaurantRecieveOrderFragment.class, MarketRecieveOrderFragment.class, ExpressRecieveOrderFragment.class, CommissionRecieveOrderFragment.class};

    private RadioButton[] radioButtons = new RadioButton[5];


    CheckBox right;
    View close_toast;


    TextView btn_shaixuan;

    private View cardLayout;
    private View bg_layout;
    private LinearLayout cardShopLayout;

    View include_type;
    View include_get;
    View include_send;


    FlowLayout flow_type;
    FlowLayout flow_get;
    List<OrderFilterBean.DataBean> datas_get_all;
    List<OrderFilterBean.DataBean> datas_get_shop;
    List<OrderFilterBean.DataBean> datas_get_express;
    FlowLayout flow_send;


    EditText edt_start_prise;
    EditText edt_end_prise;

    TextView tv_reset;
    TextView tv_ok;

    PopStatusRecord lastStatusRecord = new PopStatusRecord();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inflater = LayoutInflater.from(context);
        application = (PccApplication) getApplication();


        schoolId = application.getMyInfoData().getSchoolId();

        setContentView(R.layout.activity_recieve_order);

        initPop();
        initView();
        initTitlebar();


        radioButtons[0] = (RadioButton) findViewById(R.id.stradioButton1);
        radioButtons[1] = (RadioButton) findViewById(R.id.stradioButton2);
        radioButtons[2] = (RadioButton) findViewById(R.id.stradioButton3);
        radioButtons[3] = (RadioButton) findViewById(R.id.stradioButton4);
        radioButtons[4] = (RadioButton) findViewById(R.id.stradioButton5);


        for (RadioButton rb : radioButtons) {
            rb.setOnCheckedChangeListener(this);
        }
        radioButtons[0].setChecked(true);

        close_toast = findViewById(R.id.include_closetoast);

        btn_shaixuan = (TextView) findViewById(R.id.btn_shaixuan);
        btn_shaixuan.setOnClickListener(this);


        int state = Integer.parseInt(application.getMyInfoData().getIsOrderSwitch());
        if (state == 0) {
            right.setChecked(false);
        } else {
            right.setChecked(true);
        }
        close_toast.setVisibility(right.isChecked() ? View.GONE : View.VISIBLE);
/////////////
        setTypes();

        toGetOrderFilter("0");
        toGetOrderFilter("1");
        toGetOrderFilter("3");

        toOrderFilter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void initTitlebar() {
        View titlebar2 = findViewById(R.id.includetitlebar2);
        View left = titlebar2.findViewById(R.id.titlebar_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView tv_title = titlebar2.findViewById(R.id.titlebar_center);
        tv_title.setText("接单");

        right = titlebar2.findViewById(R.id.titlebar_right);

//        right.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
//                int state = b ? 1 : 0;
//                application.getMyInfoData().setIsOrderSwitch(String.valueOf(state));
//                close_toast.setVisibility(b ? View.GONE : View.VISIBLE);
//                isOrderSwitch(state, uuId = String.valueOf(System.currentTimeMillis()));
//
//            }
//        });


        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (application.getMyInfoData().getUserStatus() == 0) {
                    right.setChecked(false);
                    startActivity(new Intent(context, ImmediatelyApplyActivity.class));
                    return;
                }
                if (application.getMyInfoData().getUserStatus() == 6) {
                    right.setChecked(false);
                    startActivity(new Intent(context, CertificationActivity.class));
                    return;
                }
                if (application.getMyInfoData().getUserStatus() == 1) {
                    right.setChecked(false);
                    Toast.makeText(context, "认证中，请等候", Toast.LENGTH_SHORT).show();
                    return;
                }

                int state = right.isChecked() ? 1 : 0;
                application.getMyInfoData().setIsOrderSwitch(String.valueOf(state));
                isOrderSwitch(state, uuId = String.valueOf(System.currentTimeMillis()));
            }
        });
    }

    private void initPop() {

        cardLayout = findViewById(R.id.includeshaixuan);
        bg_layout = findViewById(R.id.bg_layout);
        bg_layout.setOnClickListener(this);
        cardShopLayout = (LinearLayout) findViewById(R.id.cardShopLayout);

        include_type = cardShopLayout.findViewById(R.id.include_type);
        TextView tv_shaixuan_item_title = include_type.findViewById(R.id.tv_shaixuan_item_title);
        tv_shaixuan_item_title.setText("类型");
        flow_type = include_type.findViewById(R.id.fl_shaixuan_item_choice);

        include_get = cardShopLayout.findViewById(R.id.include_get);
        tv_shaixuan_item_title = include_get.findViewById(R.id.tv_shaixuan_item_title);
        tv_shaixuan_item_title.setText("取货地点");
        flow_get = include_get.findViewById(R.id.fl_shaixuan_item_choice);

        include_send = cardShopLayout.findViewById(R.id.include_send);
        tv_shaixuan_item_title = include_send.findViewById(R.id.tv_shaixuan_item_title);
        tv_shaixuan_item_title.setText("送货地点");
        flow_send = include_send.findViewById(R.id.fl_shaixuan_item_choice);

        edt_start_prise = cardShopLayout.findViewById(R.id.edt_start_prise);
        edt_end_prise = cardShopLayout.findViewById(R.id.edt_end_prise);

        tv_reset = cardShopLayout.findViewById(R.id.tv_reset);
        tv_reset.setOnClickListener(popClickListener);
        tv_ok = cardShopLayout.findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(popClickListener);
    }

    OnSingleClickListener popClickListener = new OnSingleClickListener() {
        @Override
        public void onCanClick(View v) {
            if (v.getId() == R.id.tv_reset) {
                checkAll20();
                return;
            }
            if (v.getId() == R.id.tv_ok) {


                String gettingAmountMin = edt_start_prise.getText().toString().trim();
                String gettingAmountMax = edt_end_prise.getText().toString().trim();

                int gettingAmountMinInt = 0;
                int gettingAmountMaxInt = 0;

                if (!StringUtils.isEmpty(gettingAmountMin)) {
                    gettingAmountMinInt = Integer.parseInt(gettingAmountMin);
                }
                if (!StringUtils.isEmpty(gettingAmountMax)) {
                    gettingAmountMaxInt = Integer.parseInt(gettingAmountMax);
                }
                if (gettingAmountMaxInt + gettingAmountMinInt != 0 && gettingAmountMinInt >= gettingAmountMaxInt) {
                    Toast.makeText(context, "请输入一个适当的价格区间", Toast.LENGTH_SHORT).show();
                    return;
                }


                lastStatusRecord.saveRecord();

                hide();

                sendMsg2Fragment();
                return;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (cardLayout.getVisibility() == View.VISIBLE) {
                hide();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {
        //实例化布局对象
        //实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        //getSupportFragmentManager().beginTransaction().
        //得到fragment的个数
        int count = fragmentArray.length;
        for (int i = 0; i < count; i++) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(String.valueOf(i)).setIndicator(getTabItemView(i));

            //将Tab按钮添加进Tab选项卡中
//            Bundle bundle = new Bundle();
//            bundle.putString("url", url[i]);
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            //设置Tab按钮的背景
            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.color.white);
        }
        mTabHost.getTabWidget().setDividerDrawable(null);
        mTabHost.getTabWidget().setBackgroundResource(R.color.de_draft_color);
        mTabHost.getTabWidget().setVisibility(View.GONE);

        mTabHost.setCurrentTab(0);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                int pos = Integer.valueOf(tabId);
                radioButtons[pos].setChecked(true);

//                switch (pos) {
//                    case 0: {
//
//                    }
//                    break;
//                    case 1: {
//
//                    }
//                    break;
//                    case 2: {
//
//
//                    }
//                    break;
//                    case 3: {
//
//                    }
//                    break;
//                }

            }
        });
    }

    private View getTabItemView(int index) {
        TextView view = new TextView(this);
        return view;
    }

    int tab_pos = 0;

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (!isChecked) return;

        switch (buttonView.getId()) {
            case R.id.stradioButton1:
                tab_pos = 0;
                mTabHost.setCurrentTab(0);
                include_type.setVisibility(View.VISIBLE);
                include_get.setVisibility(View.VISIBLE);
                include_send.setVisibility(View.VISIBLE);

                setDatas(datas_get_all, flow_get, onFlowClickListener2);


                break;
            case R.id.stradioButton2:
                tab_pos = 1;
                mTabHost.setCurrentTab(1);
                include_type.setVisibility(View.GONE);
                include_get.setVisibility(View.VISIBLE);
                include_send.setVisibility(View.VISIBLE);

                setDatas(datas_get_shop, flow_get, onFlowClickListener2);

                break;
            case R.id.stradioButton3:
                tab_pos = 2;
                mTabHost.setCurrentTab(2);
                include_type.setVisibility(View.GONE);
                include_get.setVisibility(View.GONE);
                include_send.setVisibility(View.VISIBLE);

                break;
            case R.id.stradioButton4:
                tab_pos = 3;
                mTabHost.setCurrentTab(3);
                include_type.setVisibility(View.GONE);
                include_get.setVisibility(View.VISIBLE);
                include_send.setVisibility(View.VISIBLE);

                setDatas(datas_get_express, flow_get, onFlowClickListener2);

                break;
            case R.id.stradioButton5:
                tab_pos = 4;
                mTabHost.setCurrentTab(4);
                include_type.setVisibility(View.GONE);
                include_get.setVisibility(View.GONE);
                include_send.setVisibility(View.VISIBLE);
                break;
        }
        checkAll20();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_shaixuan:

                lastStatusRecord.loadRecord();

                show();
                break;
            case R.id.bg_layout:

                hide();
                break;
        }
    }

    private void show() {

        if (cardLayout.getVisibility() != View.GONE) return;

        cardLayout.setVisibility(View.VISIBLE);

        // 加载动画
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.push_right_in);
        // 动画开始
        cardShopLayout.setVisibility(View.VISIBLE);
        cardShopLayout.startAnimation(animation);
        bg_layout.setVisibility(View.VISIBLE);

    }

    private void hide() {
        cardLayout.setVisibility(View.GONE);
        bg_layout.setVisibility(View.GONE);
        cardShopLayout.setVisibility(View.GONE);

    }

    String uuId;

    //0关闭接单，1开启接单
    private void isOrderSwitch(final int orderSwitch, final String now_uuId) {
        HttpAction.getInstance().isOrderSwitchCancel();

        if (!StringUtils.isEqual(uuId, now_uuId)) {
            return;
        }
        HttpAction.getInstance().isOrderSwitch(Integer.parseInt(PccApplication.getUserid()), orderSwitch, now_uuId, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("res=  " + response.body());
                try {
                    if (!StringUtils.isEqual(uuId, now_uuId)) {
                        return;
                    }
                    UUIDResultBean vo = JsonMananger.jsonToBean(response.body(), UUIDResultBean.class);

                    if (!StringUtils.isEqual(vo.code, "200")) {
                        int temp = orderSwitch == 1 ? 0 : 1;
                        application.getMyInfoData().setIsOrderSwitch(String.valueOf(temp));
                        close_toast.setVisibility(temp == 1 ? View.GONE : View.VISIBLE);

                        Toast.makeText(context, StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!StringUtils.isEqual(uuId, vo.getData().getUuId())) {
                        return;
                    }

                    application.getMyInfoData().setIsOrderSwitch(String.valueOf(orderSwitch));
                    close_toast.setVisibility(orderSwitch == 1 ? View.GONE : View.VISIBLE);
                    clearData();
                } catch (Exception e) {
                    e.printStackTrace();
                    int temp = orderSwitch == 1 ? 0 : 1;
                    application.getMyInfoData().setIsOrderSwitch(String.valueOf(temp));
                    close_toast.setVisibility(temp == 1 ? View.GONE : View.VISIBLE);
                    Toast.makeText(context, "系统错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void toOrderFilter() {
        HttpAction.getInstance().toOrderFilter(schoolId, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("res=  " + response.body());
                try {

                    OrderFilterBean vo = JsonMananger.jsonToBean(response.body(), OrderFilterBean.class);

                    if (!StringUtils.isEqual(vo.code, "200")) {
                        Toast.makeText(context, StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<OrderFilterBean.DataBean> datas = vo.getData();

                    if (datas == null || datas.size() <= 0) {
                        return;
                    }
                    setDatas(datas, flow_send, onFlowClickListener3);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "系统错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void toGetOrderFilter(final String type) {
        HttpAction.getInstance().toGetOrderFilter(schoolId, type, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("res=  " + response.body());
                try {

                    OrderFilterBean vo = JsonMananger.jsonToBean(response.body(), OrderFilterBean.class);

                    if (!StringUtils.isEqual(vo.code, "200")) {
                        Toast.makeText(context, StringUtils.isEmpty(vo.msg) ? "数据错误" : vo.msg, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<OrderFilterBean.DataBean> datas = vo.getData();

                    if (datas == null || datas.size() <= 0) {
                        return;
                    }

                    if (StringUtils.isEqual(type, "0")) {
                        datas_get_all = datas;
                    } else if (StringUtils.isEqual(type, "1")) {
                        datas_get_shop = datas;
                    } else if (StringUtils.isEqual(type, "3")) {
                        datas_get_express = datas;
                    }
                    switch (tab_pos) {
                        case 0: {//全部
                            setDatas(datas_get_all, flow_get, onFlowClickListener2);
                        }
                        break;
                        case 1: {//外卖
                            setDatas(datas_get_shop, flow_get, onFlowClickListener2);
                        }
                        break;
                        case 2: {
                        }
                        break;
                        case 3: {//快递
                            setDatas(datas_get_express, flow_get, onFlowClickListener2);
                        }
                        break;
                        case 4: {
                        }
                        break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "系统错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setDatas(List<OrderFilterBean.DataBean> list, FlowLayout flowLayout, CompoundButton.OnCheckedChangeListener onFlowClickListener) {

        for (int i = 0; i < flowLayout.getChildCount(); i++) {
            flowLayout.getChildAt(i).setVisibility(View.GONE);
        }
        if (list == null || list.size() <= 0) return;

        for (int i = 0, count = list.size(); i < count; i++) {

            OrderFilterBean.DataBean vo = list.get(i);

            CheckBox cb;
            if (i < flowLayout.getChildCount()) {
                cb = (CheckBox) flowLayout.getChildAt(i);
                setRecordTextView(cb, i, vo, onFlowClickListener);
                cb.setVisibility(View.VISIBLE);
            } else {
                cb = (CheckBox) inflater.inflate(R.layout.layout_checkbox, null);
                setRecordTextView(cb, i, vo, onFlowClickListener);
                flowLayout.addViewByLayoutParams(cb);
                cb.setVisibility(View.VISIBLE);
            }
            cb.setChecked(false);
        }

        CheckBox cb = (CheckBox) flowLayout.getChildAt(0);
        cb.setChecked(true);

        flowLayout.invalidate();
    }

    private void setRecordTextView(CheckBox cb, int i, OrderFilterBean.DataBean vo, CompoundButton.OnCheckedChangeListener onFlowClickListener) {
        cb.setId(i);
        cb.setTag(vo);
        cb.setMaxLines(1);
        cb.setText(vo.getAddrName());
        cb.setOnCheckedChangeListener(onFlowClickListener);
    }

    private void setTypes() {

        String[] keys = getResources().getStringArray(R.array.smet_key);
        String[] values = getResources().getStringArray(R.array.smet_value);

        List<OrderFilterBean.DataBean> list = new ArrayList<>();
        for (int i = 0; i < keys.length; i++) {
            OrderFilterBean.DataBean vo = new OrderFilterBean.DataBean();
            vo.setType(keys[i]);
            vo.setAddrName(values[i]);
            list.add(vo);
        }

        setDatas(list, flow_type, onFlowClickListener1);
    }


    CheckBox fl1_cb = null;
    CompoundButton.OnCheckedChangeListener onFlowClickListener1 = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            CheckBox temp = (CheckBox) compoundButton;
            if (b) {
                if (fl1_cb == temp) {
                    fl1_cb.setChecked(true);
                    return;
                }
                CheckBox last = fl1_cb;
                fl1_cb = temp;

                typeCheck(fl1_cb);
                check20();

                if (last != null) last.setChecked(false);
            } else {
                if (fl1_cb == null) return;
                if (fl1_cb != temp) return;
                fl1_cb.setChecked(true);
            }

        }
    };

    CheckBox fl2_cb;
    CompoundButton.OnCheckedChangeListener onFlowClickListener2 = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            CheckBox temp = (CheckBox) compoundButton;
            if (b) {
                if (fl2_cb == temp) {
                    fl2_cb.setChecked(true);
                    return;
                }
                CheckBox last = fl2_cb;
                fl2_cb = temp;

                if (last != null) last.setChecked(false);
            } else {
                if (fl2_cb == null) return;
                if (fl2_cb != temp) return;
                fl2_cb.setChecked(true);
            }

            OrderFilterBean.DataBean vo = (OrderFilterBean.DataBean) fl2_cb.getTag();
        }
    };

    CheckBox fl3_cb;
    CompoundButton.OnCheckedChangeListener onFlowClickListener3 = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            CheckBox temp = (CheckBox) compoundButton;
            if (b) {
                if (fl3_cb == temp) {
                    fl3_cb.setChecked(true);
                    return;
                }
                CheckBox last = fl3_cb;
                fl3_cb = temp;

                if (last != null) last.setChecked(false);
            } else {
                if (fl3_cb == null) return;
                if (fl3_cb != temp) return;
                fl3_cb.setChecked(true);
            }

            OrderFilterBean.DataBean vo = (OrderFilterBean.DataBean) fl3_cb.getTag();
        }
    };


    private void checkAll20() {

        CheckBox temp = (CheckBox) flow_type.getChildAt(0);
        if (temp != null) temp.setChecked(true);

//        typeCheck(flow_type.getChildAt(0));

        check20();

        edt_start_prise.setText(null);
        edt_end_prise.setText(null);
    }

    private void check20() {

        CheckBox temp = (CheckBox) flow_get.getChildAt(0);
        if (temp != null) temp.setChecked(true);

        temp = (CheckBox) flow_send.getChildAt(0);
        if (temp != null) temp.setChecked(true);

    }


    private void typeCheck(View view) {
        if (view == null) return;

        if (include_type.getVisibility() != View.VISIBLE) return;

        CheckBox tv = (CheckBox) view;

        OrderFilterBean.DataBean vo = (OrderFilterBean.DataBean) tv.getTag();
        int type = Integer.parseInt(vo.getType());

        include_type.setVisibility(View.VISIBLE);
        switch (type) {
            case 0:
                include_get.setVisibility(View.VISIBLE);
                include_send.setVisibility(View.VISIBLE);

                setDatas(datas_get_all, flow_get, onFlowClickListener2);

                break;
            case 1:
                include_get.setVisibility(View.VISIBLE);
                include_send.setVisibility(View.VISIBLE);

                setDatas(datas_get_shop, flow_get, onFlowClickListener2);

                break;
            case 2:
                include_get.setVisibility(View.GONE);
                include_send.setVisibility(View.VISIBLE);

                break;
            case 3:
                include_get.setVisibility(View.VISIBLE);
                include_send.setVisibility(View.VISIBLE);

                setDatas(datas_get_express, flow_get, onFlowClickListener2);

                break;
            case 4:
                include_get.setVisibility(View.GONE);
                include_send.setVisibility(View.VISIBLE);
                break;
        }
    }

    class PopStatusRecord {

        public int card = 0;

        public int fl1_check_pos = 0;
        public int fl2_check_pos = 0;
        public int fl3_check_pos = 0;

        public String startPrise;
        public String endPrise;

        private void saveRecord() {

            card = tab_pos;

            this.fl1_check_pos = fl1_cb != null ? fl1_cb.getId() : 0;
            this.fl2_check_pos = fl2_cb != null ? fl2_cb.getId() : 0;
            this.fl3_check_pos = fl3_cb != null ? fl3_cb.getId() : 0;

            this.startPrise = edt_start_prise.getText().toString().trim();
            this.endPrise = edt_end_prise.getText().toString().trim();

        }

        private void loadRecord() {

            if (card != tab_pos) {
                checkAll20();
                return;
            }
            CheckBox cb;
            try {
                cb = (CheckBox) flow_type.getChildAt(fl1_check_pos);
//                cb.setOnCheckedChangeListener(null);
//                fl1_cb = cb;
//                typeCheck(cb);
                cb.setChecked(true);
//                cb.setOnCheckedChangeListener(onFlowClickListener1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                cb = (CheckBox) flow_get.getChildAt(fl2_check_pos);
                cb.setChecked(true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                cb = (CheckBox) flow_send.getChildAt(fl3_check_pos);
                cb.setChecked(true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            edt_start_prise.setText(startPrise);
            edt_end_prise.setText(endPrise);

        }

    }

    private void sendMsg2Fragment() {
        try {
            RecieveOrderActivityFragmentLetter letter = (RecieveOrderActivityFragmentLetter) getSupportFragmentManager().findFragmentByTag(String.valueOf(mTabHost.getCurrentTab()));

            OrderFilterBean.DataBean vo;

            String orderType = null;

            if (tab_pos == 0) {
                if (fl1_cb != null) {
                    vo = (OrderFilterBean.DataBean) fl1_cb.getTag();
                    orderType = vo.getType();
                }
            } else {
                orderType = String.valueOf(tab_pos);
            }

            if (StringUtils.isEmpty(orderType)) return;

            String getAddressId = "";
            if (fl2_cb != null) {
                vo = (OrderFilterBean.DataBean) fl2_cb.getTag();
                getAddressId = vo.getAddrId();
            }

            String sendAddressId = "";
            if (fl3_cb != null) {
                vo = (OrderFilterBean.DataBean) fl3_cb.getTag();
                sendAddressId = vo.getAddrId();
            }

            String gettingAmountMin = edt_start_prise.getText().toString().trim();
            String gettingAmountMax = edt_end_prise.getText().toString().trim();

            letter.flushData(orderType, getAddressId, sendAddressId, gettingAmountMin, gettingAmountMax);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearData() {
        try {
            RecieveOrderActivityFragmentLetter letter = (RecieveOrderActivityFragmentLetter) getSupportFragmentManager().findFragmentByTag(String.valueOf(mTabHost.getCurrentTab()));
            letter.clearData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
