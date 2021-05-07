package com.pbph.pcc.activity;

import android.Manifest;
import android.Manifest.permission;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.utils.LogUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pbph.pcc.R;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.request.BaseRequestBean;
import com.pbph.pcc.bean.request.GetJpushIdRequestBean;
import com.pbph.pcc.bean.request.QueryRongYunRequestBean;
import com.pbph.pcc.bean.response.QueryRongYunResponseBean;
import com.pbph.pcc.fragment.MainFragment;
import com.pbph.pcc.fragment.MyInfoFragment;
import com.pbph.pcc.fragment.MyOrderListFragment;
import com.pbph.pcc.fragment.ShoppingCarFragment;
import com.pbph.pcc.http.HttpAction;
import com.pbph.pcc.jpush.MyReceiver;
import com.pbph.pcc.json.JsonMananger;
import com.pbph.pcc.service.YingYanServise;
import com.pbph.pcc.tools.WaitUI;
import com.pbph.pcc.update.UpdateManager;
import com.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;


public class MainTabActivity extends AppCompatActivity {
    private FragmentTabHost mTabHost = null;
    private Class fragmentArray[] = {MainFragment.class, ShoppingCarFragment.class, MyOrderListFragment.class, MyInfoFragment.class};
    private int mImageViewArray[] = {R.drawable.tab_1_bg_selecter, R.drawable.tab_2_bg_selecter, R.drawable.tab_3_bg_selecter, R.drawable.tab_4_bg_selecter,};
    private String mTextviewArray[] = {"首页", "购物车", "订单", "我的"};

    private LayoutInflater layoutInflater = null;
    private UpdateManager manager = null;
    MyReceiver receiver;
    private PccApplication application = null;
    private ServiceConnection mServiceConstants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout);
        application = (PccApplication) getApplication();
        manager = UpdateManager.getInstance(this);
        manager.checkVersion();
        initView();
        String rid = JPushInterface.getRegistrationID(getApplicationContext());
        Log.e("rid", "rid = " + rid);
        if (!TextUtils.isEmpty(rid)) {
            getJpushId(rid);
        }
        getRongToken();
    }

    @Override
    protected void onStart() {
        super.onStart();
        List<String> permissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isNeedRequestPermissions(permissions)) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), 0);
        } else {
            startYingYan();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(application.getMyInfoData().getSchoolId())) {
            startActivity(new Intent(this, ChooseSchoolActivity.class));
        }
    }

    private boolean isNeedRequestPermissions(List<String> permissions) {
        // 定位精确位置
        addPermission(permissions, permission.ACCESS_FINE_LOCATION);
        // 存储权限
        addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        // 读取手机状态
        addPermission(permissions, Manifest.permission.READ_PHONE_STATE);

        return permissions.size() > 0;
    }

    private void addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startYingYan();
        }


    }

    public void startYingYan() {
        Intent intentYing = new Intent();
        intentYing.setClass(MainTabActivity.this, YingYanServise.class);
        bindService(intentYing, mServiceConstants, Context.BIND_AUTO_CREATE);
    }


    private void rongConnect(String token) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                LogUtils.e("MainRONG", "-------->");
            }

            @Override
            public void onSuccess(String s) {
                LogUtils.e("MainRONG", s);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                LogUtils.e("MainRONG", errorCode.getMessage());
            }
        });
    }


    private void getRongToken() {
        HttpAction.getInstance().queryRongYun(new QueryRongYunRequestBean(PccApplication.getUserid()), new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtils.e("getRongToken" + response.body());
                try {
                    QueryRongYunResponseBean responseBean = JsonMananger.jsonToBean(response.body(), QueryRongYunResponseBean.class);
                    if (null != responseBean && StringUtils.isEqual(responseBean.getCode(), "200")) {
                        application.queryRongYunResponseBean = responseBean;
                        rongConnect(responseBean.getData().getTokenId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });
    }


    public void setTab(int position) {
        mTabHost.setCurrentTab(position);
    }


    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unbindService(mServiceConstants);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RongIM.getInstance().logout();
//        System.exit(0);
    }

    private void initView() {
        //实例化布局对象
        layoutInflater = LayoutInflater.from(this);
        //实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        //getSupportFragmentManager().beginTransaction().
        //得到fragment的个数
        int count = fragmentArray.length;
        for (int i = 0; i < count; i++) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            Bundle bundle = new Bundle();
            bundle.putString("url", "http://www.baidu.com");
            mTabHost.addTab(tabSpec, fragmentArray[i], bundle);
            //设置Tab按钮的背景
            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.color.white);
        }
        mTabHost.getTabWidget().setDividerDrawable(null);
        mTabHost.getTabWidget().setBackgroundResource(R.color.white);

        mTabHost.setCurrentTab(0);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

            }
        });
        mServiceConstants = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
    }

    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);
        ImageView imageView = view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);
        TextView textView = view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);
        return view;
    }


    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();

            } else {

                finish();

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void getJpushId(String rid) {

//        JPushInterface.setAlias(this,userId);
        BaseRequestBean bean = new GetJpushIdRequestBean(application.getUsername(), rid);
        HttpAction.getInstance().getJpushId(bean, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                WaitUI.Cancel();
                try {
                    if (null == response || response.code() != 200) {
                        Toast.makeText(MainTabActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    LogUtils.e("getJpushId" + "res=  " + response.body());
//                    GetMyInfoResponseBean bean = JsonMananger.jsonToBean(response.body(), GetMyInfoResponseBean.class);
//                    if (StringUtils.isEqual(bean.getCode(),"200")) {
//                        application.setMyInfoData(bean.getData());
//                    } else {
//                        Toast.makeText(MainTabActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
//                    }
                } catch (Exception e) {
                    Toast.makeText(MainTabActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

}
