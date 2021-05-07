package com.pbph.pcc.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pbph.pcc.R;
import com.pbph.pcc.adapter.ReceivingLocationAdapter;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.request.BaseRequestBean;
import com.pbph.pcc.bean.request.GetMyInfoQueryTakeAddress;
import com.pbph.pcc.bean.response.ReceivingLocationBean;
import com.pbph.pcc.http.HttpAction;
import com.pbph.pcc.json.JsonMananger;

/**
 * 收货地址
 * Created by Administrator on 2017/9/28.
 */

public class ReceivingLocationActivity extends BaseActivity implements View.OnClickListener, ReceivingLocationAdapter.DeleteReceivingLocationData, AdapterView.OnItemClickListener {

    //private SwipeRefreshLayout swipeLayout;
    private ListView lv_my_address_list;
    private Button btn_add_location;
    private ReceivingLocationAdapter mReceivingLocationAdapter;
    private PccApplication mApp;
    private BaseRequestBean bean;
    private ReceivingLocationBean mReceivingLocationBean;
    private View mHeardView;
    private ImageView btn_left;
    private TextView tv_title;
    private AlertDialog.Builder builder = null;
    private ImageView iv_null_receiving;
    ReceivingLocationBean.DataBean vo;
    public String ReceivingLocationActivityType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiving_location);
        initView();
        initBuilder();
        initData();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        getNetworkData();
    }

    private void initView() {
        //swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        lv_my_address_list = (ListView) findViewById(R.id.lv_my_address_list);
        btn_add_location = (Button) findViewById(R.id.btn_add_location);
        mHeardView = findViewById(R.id.include_receiving);
        btn_left = mHeardView.findViewById(R.id.btn_left);
        tv_title = mHeardView.findViewById(R.id.tv_title);
        iv_null_receiving = (ImageView) findViewById(R.id.iv_null_receiving);
        btn_left.setOnClickListener(this);
        btn_add_location.setOnClickListener(this);
        lv_my_address_list.setOnItemClickListener(this);
        //swipeLayout.setEnabled(false);

    }

    private void initBuilder() {

        builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setInverseBackgroundForced(true);
        builder.setTitle("是否删除地址");


    }

    private void initData() {
        mApp = (PccApplication) getApplication();
        mReceivingLocationAdapter = new ReceivingLocationAdapter(this);
        mReceivingLocationAdapter.setDeleteReceivingLocationData(this);
        bean = new GetMyInfoQueryTakeAddress(PccApplication.getUserid(), mApp.getMyInfoData().getSchoolId());
        tv_title.setText("管理收货地址");
        ReceivingLocationActivityType = getIntent().getStringExtra("ReceivingLocationActivity");
        mReceivingLocationAdapter.setActivityType(ReceivingLocationActivityType);
        getNetworkData();
    }

    /**
     * 获取地址列表
     */
    private void getNetworkData() {
        HttpAction.getInstance().queryTakeAddress(bean, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                try {
                    if (!response.body().equals("")) {


                        mReceivingLocationBean = JsonMananger.jsonToBean(response.body(), ReceivingLocationBean.class);

                        if (mReceivingLocationBean.getCode().equals("200")
                                && mReceivingLocationBean.getData().size() != 0) {

                            mReceivingLocationAdapter.setmReceivingLocation(mReceivingLocationBean);
                            lv_my_address_list.setAdapter(mReceivingLocationAdapter);
                            iv_null_receiving.setVisibility(View.GONE);

                            if (mReceivingLocationBean.getData().get(0).getDefaultAddress().equals("0")) {
                                mApp.getMyInfoData().setReceiveSex("");
                                mApp.getMyInfoData().setReceivePhone("");
                                mApp.getMyInfoData().setReceiveName("");
                                mApp.getMyInfoData().setReceiveId("");
                                mApp.getMyInfoData().setRaddressName("");
                                mApp.getMyInfoData().setReceiveId("");
                            } else if (mReceivingLocationBean.getData().get(0).getDefaultAddress().equals("1")) {
                                mApp.getMyInfoData().setReceiveSex(mReceivingLocationBean.getData().get(0).getReceiveSex());
                                mApp.getMyInfoData().setReceivePhone(mReceivingLocationBean.getData().get(0).getReceivePhone());
                                mApp.getMyInfoData().setReceiveName(mReceivingLocationBean.getData().get(0).getReceiveName());
                                mApp.getMyInfoData().setReceiveId(mReceivingLocationBean.getData().get(0).getReceiveId());
                                mApp.getMyInfoData().setRaddressName(mReceivingLocationBean.getData().get(0).getRaddressName());
                                mApp.getMyInfoData().setReceiveId(mReceivingLocationBean.getData().get(0).getReceiveId());
                            }

                        } else {
                            iv_null_receiving.setVisibility(View.VISIBLE);
                            mApp.getMyInfoData().setReceiveSex("");
                            mApp.getMyInfoData().setReceivePhone("");
                            mApp.getMyInfoData().setReceiveName("");
                            mApp.getMyInfoData().setReceiveId("");
                            mApp.getMyInfoData().setRaddressName("");
                            mApp.getMyInfoData().setReceiveId("");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Toast.makeText(mApp, "网络异常请检测网络状态", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 删除地址
     */
    private void deleteReceivingData(final int pos, final int receiveId) {
        HttpAction.getInstance().delTakeAddressByAddId(receiveId, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
//                getNetworkData();
                if (mReceivingLocationBean != null && mReceivingLocationBean.getData().size() != 0) {
                    mReceivingLocationBean.getData().remove(pos);
                    mReceivingLocationAdapter.setmReceivingLocation(mReceivingLocationBean);
                    iv_null_receiving.setVisibility(View.GONE);
                    if (mReceivingLocationBean.getData().size() == 0) {
                        mApp.getMyInfoData().setReceiveSex("");
                        mApp.getMyInfoData().setReceivePhone("");
                        mApp.getMyInfoData().setReceiveName("");
                        mApp.getMyInfoData().setReceiveId("");
                        mApp.getMyInfoData().setRaddressName("");
                        mApp.getMyInfoData().setReceiveId("");
                        iv_null_receiving.setVisibility(View.VISIBLE);

                    }
                } else {
                    mApp.getMyInfoData().setReceiveSex("");
                    mApp.getMyInfoData().setReceivePhone("");
                    mApp.getMyInfoData().setReceiveName("");
                    mApp.getMyInfoData().setReceiveId("");
                    mApp.getMyInfoData().setRaddressName("");
                    mApp.getMyInfoData().setReceiveId("");
                }

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Toast.makeText(mApp, "网络异常请检测网络状态", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 修改默认地址
     *
     * @param receiveId
     */
    private void upDataDefaultAddress(final int receiveId, final ReceivingLocationBean.DataBean vo) {
        String userID = PccApplication.getUserid();
        HttpAction.getInstance().defaultAddress(String.valueOf(receiveId), userID,
                mApp.getMyInfoData().getSchoolId(), new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        mApp.getMyInfoData().setReceiveSex(vo.getReceiveSex());
                        mApp.getMyInfoData().setReceivePhone(vo.getReceivePhone());
                        mApp.getMyInfoData().setReceiveName(vo.getReceiveName());
                        mApp.getMyInfoData().setReceiveId(vo.getReceiveId());
                        mApp.getMyInfoData().setRaddressName(vo.getRaddressName());
                        mApp.getMyInfoData().setReceiveId(vo.getReceiveId());
                        getNetworkData();

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();

                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_location:
                startActivity(new Intent(this, AddLocationActivity.class));
                break;
            case R.id.btn_left:
                onBackPressed();
                break;
        }
    }

    @Override
    public void deleteReceivingLocationData(final int pos, final int receiveId) {
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteReceivingData(pos, receiveId);
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }


    @Override
    public void defaultAddress(int pos, int receiveIdd) {
        upDataDefaultAddress(receiveIdd, mReceivingLocationBean.getData().get(pos));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (ReceivingLocationActivityType.equals("0")) {
            return;
        } else {
            vo = mReceivingLocationBean.getData().get(i);
            Intent intent = new Intent();
            intent.putExtra("ReceivingLocationBean", vo);
            setResult(222, intent);
            ReceivingLocationActivity.this.finish();
        }


    }

    @Override
    public void onBackPressed() {
        if (mReceivingLocationBean != null) {
            Intent intent = new Intent();
            intent.putExtra("ReceivingLocationBean", vo);
            setResult(222, intent);
            ReceivingLocationActivity.this.finish();
        } else {
            super.onBackPressed();
        }
    }
}
