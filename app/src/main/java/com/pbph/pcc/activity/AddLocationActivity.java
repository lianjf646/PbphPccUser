package com.pbph.pcc.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pbph.pcc.R;
import com.pbph.pcc.adapter.MyArraySpinnerAdapter;
import com.pbph.pcc.adapter.MySpinnerAdapter;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.response.AddLocationStreetBean;
import com.pbph.pcc.bean.vo.KeyValueBean;
import com.pbph.pcc.bean.request.MyTakeAddressRequestBean;
import com.pbph.pcc.bean.response.ReceivingLocationBean;
import com.pbph.pcc.http.HttpAction;
import com.pbph.pcc.json.JsonMananger;
import com.pbph.pcc.tools.AMUtils;
import com.pbph.pcc.tools.TextFilter;
import com.pbph.pcc.tools.WaitUI;

/**
 * 添加地址
 * Created by Administrator on 2017/9/29.
 */

public class AddLocationActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private TextView add_location_tv_save;
    private Spinner sp_add_location;
    private Spinner sp_add_street;
    private EditText add_location_et_name;
    private EditText add_location_et_phone;
    private EditText settlement_et_explain;//详细地址
    private CheckBox add_location_check;
    private TextView tv_add_sex;
    private ImageButton add_location_iv_finish;
    private AlertDialog.Builder builder = null;
    private String taskName;
    private String taskSex = "";
    private String taskPhone;
    private int userId;
    private int addrId = -1;//地址id
    private String addrName;//地址详情
    private MyArraySpinnerAdapter mMyArraySpinnerAdapter;
    private MyTakeAddressRequestBean mMyTakeAddressRequestBean;
    private MySpinnerAdapter mSpAddStreetAdapter;
    private AddLocationStreetBean mAddLocationStreetBean;
    private int locationId;
    private String streetId = "";
    private PccApplication mApp;
    private ReceivingLocationBean.DataBean mReceivingLocationBean = null;
    private int defaultAdd = 1;
    private String streetName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        initView();
        initBuilder();
        initData();
    }

    private void initView() {
        sp_add_location = (Spinner) findViewById(R.id.sp_add_location);
        sp_add_street = (Spinner) findViewById(R.id.sp_add_street);
        add_location_tv_save = (TextView) findViewById(R.id.add_location_tv_save);
        add_location_et_name = (EditText) findViewById(R.id.add_location_et_name);
        add_location_et_phone = (EditText) findViewById(R.id.add_location_et_phone);
        settlement_et_explain = (EditText) findViewById(R.id.settlement_et_explain);
        add_location_check = (CheckBox) findViewById(R.id.add_location_check);
        tv_add_sex = (TextView) findViewById(R.id.tv_add_sex);
        add_location_iv_finish = (ImageButton) findViewById(R.id.add_location_iv_finish);
        add_location_tv_save.setOnClickListener(this);
        add_location_iv_finish.setOnClickListener(this);
        tv_add_sex.setOnClickListener(this);
        add_location_check.setOnCheckedChangeListener(this);
    }

    private void initBuilder() {
        builder = new AlertDialog.Builder(this);
        builder.setItems(getResources().getStringArray(R.array.sex), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String sex[] = getResources().getStringArray(R.array.sex);
                switch (which) {
                    case 0:
                        taskSex = "0";
                        break;
                    case 1:
                        taskSex = "1";
                        break;
                }
                tv_add_sex.setText(sex[which]);
            }
        });
    }

    private void initData() {


        mMyTakeAddressRequestBean = new MyTakeAddressRequestBean();
        mMyArraySpinnerAdapter = new MyArraySpinnerAdapter(this, R.array.place_key, R.array.place_value);
        mApp = (PccApplication) getApplication();
        userId = Integer.valueOf(PccApplication.getUserid());
        mReceivingLocationBean = (ReceivingLocationBean.DataBean) getIntent().getSerializableExtra("mReceivingLocation");

        mSpAddStreetAdapter = new MySpinnerAdapter(AddLocationActivity.this, new MySpinnerAdapter.OnSetDataListener() {
            @Override
            public <T> String onSetData(T obj) {
                AddLocationStreetBean.DataBean vo = (AddLocationStreetBean.DataBean) obj;
                return vo.getAddName();
            }
        });

        sp_add_location.setAdapter(mMyArraySpinnerAdapter);
        sp_add_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                WaitUI.Show(AddLocationActivity.this);
                KeyValueBean vo = (KeyValueBean) mMyArraySpinnerAdapter.getItem(i);
                locationId = vo.key;
                mMyTakeAddressRequestBean.setSchoolId(Integer.parseInt(mApp.getMyInfoData().getSchoolId()));
                mMyTakeAddressRequestBean.setAddrTypeId(vo.key);
                getNetworkDataLocation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_add_street.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                streetId = mAddLocationStreetBean.getData().get(i).getAddId();
                addrId = Integer.valueOf(streetId);
                streetName = mAddLocationStreetBean.getData().get(i).getAddName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (mReceivingLocationBean != null) {

            add_location_et_name.setText(mReceivingLocationBean.getReceiveName());
            add_location_et_phone.setText(mReceivingLocationBean.getReceivePhone());

            String sex = mReceivingLocationBean.getReceiveSex().equals("0") ? "男" : "女";
            tv_add_sex.setText(sex);
            settlement_et_explain.setText(mReceivingLocationBean.getRaddressName());
            taskSex = mReceivingLocationBean.getReceiveSex();
            sp_add_location.setSelection(Integer.valueOf(mReceivingLocationBean.getAddrTypeId()) - 1, true);
            addrId = Integer.valueOf(mReceivingLocationBean.getAddrId());
            if (mReceivingLocationBean.getDefaultAddress().equals("0")) {
                add_location_check.setChecked(false);

            } else {
                add_location_check.setChecked(true);

            }
        }
        if (add_location_check.isChecked()) {
            defaultAdd = 1;

        } else {
            defaultAdd = 0;
        }
    }

    /**
     * 获取网络数据
     */
    private void getNetworkDataLocation() {
        WaitUI.Show(this);
        HttpAction.getInstance().myTakeAddress(mMyTakeAddressRequestBean, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                try {
                    try {
                        mAddLocationStreetBean = JsonMananger.jsonToBean(response.body(), AddLocationStreetBean.class);
                        mSpAddStreetAdapter.setDatas(mAddLocationStreetBean.getData());
                        sp_add_street.setAdapter(mSpAddStreetAdapter);
                        WaitUI.Cancel();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                WaitUI.Cancel();
            }
        });

    }

    /**
     * 上传地址信息
     */
    private void upLoadSaveLocation() {
        WaitUI.Show(this);
        HttpAction.getInstance().addTakeAddress(taskName, taskSex, taskPhone, userId, addrId,
                addrName, defaultAdd, mApp.getMyInfoData().getSchoolId(), new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        WaitUI.Cancel();
                        if (response.code() == 200) {
                            if (response.body().contains("200") && response.body().contains("成功")) {
                                finish();
                            }
                        } else {
                            Toast.makeText(mApp, "不支持输入特殊字符呦！！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        WaitUI.Cancel();
                    }
                });
    }

    /**
     * 修改地址
     */
    private void updateTakeAddress() {
        WaitUI.Show(this);
        HttpAction.getInstance().updateTakeAddress(Integer.valueOf(mReceivingLocationBean.getReceiveId()),
                taskName, taskSex, taskPhone, userId, addrId, addrName, defaultAdd, mApp.getMyInfoData().getSchoolId(),
                new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        WaitUI.Cancel();
                        if (response.code() == 200) {
                            if (response.body().contains("200") && response.body().contains("成功")) {
                                finish();
                            }
                        } else {
                            Toast.makeText(mApp, "不支持输入特殊字符呦！！", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        WaitUI.Cancel();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_location_tv_save:
                taskName = add_location_et_name.getText().toString();
                taskPhone = add_location_et_phone.getText().toString();
                addrName = settlement_et_explain.getText().toString();

                if (mAddLocationStreetBean.getData().size() == 0) {
                    Toast.makeText(mApp, "请选择所在地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (AMUtils.hasEmoji(taskName) || AMUtils.hasEmoji(addrName)) {
                    Toast.makeText(mApp, "不支持输入表情", Toast.LENGTH_SHORT).show();
                    return;
                } else if (taskName.equals("")) {
                    Toast.makeText(mApp, "请填写姓名", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!AMUtils.isMobile(taskPhone)) {
                    Toast.makeText(mApp, "请填写正确手机号码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (addrName.equals("")) {
                    Toast.makeText(mApp, "请填写地址详情", Toast.LENGTH_SHORT).show();
                    return;
                } else if (taskSex.equals("")) {
                    Toast.makeText(mApp, "请选择性别", Toast.LENGTH_SHORT).show();
                    return;
                } else if (addrId == -1) {
                    Toast.makeText(mApp, "请选择地址类型", Toast.LENGTH_SHORT).show();
                    return;
                } else if (streetName.equals("")) {
                    Toast.makeText(mApp, "请选择所在地址", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextFilter.filter("[./]", addrName)) {
                    Toast.makeText(mApp, "不支持输入特殊字符呦！！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (null == mReceivingLocationBean) {
                    upLoadSaveLocation();
                } else {
                    updateTakeAddress();
                }

                break;
            case R.id.tv_add_sex:
                builder.show();
                break;
            case R.id.add_location_iv_finish:
                finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            defaultAdd = 1;
        } else {
            defaultAdd = 0;
        }
    }
}
