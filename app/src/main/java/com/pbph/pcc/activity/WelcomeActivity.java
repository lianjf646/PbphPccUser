package com.pbph.pcc.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.utils.LogUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pbph.pcc.R;
import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.request.BaseRequestBean;
import com.pbph.pcc.bean.request.GetMyInfoRequestBean;
import com.pbph.pcc.bean.response.GetMyInfoResponseBean;
import com.pbph.pcc.http.HttpAction;
import com.pbph.pcc.json.JsonMananger;
import com.pbph.pcc.tools.WaitUI;
import com.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class WelcomeActivity extends Activity {

    Context context = this;

    private long lastTime = System.currentTimeMillis();

    private static final long SPLASH_DELAY_MILLIS = 1000;
    //    public static boolean isForeground = false;
    PccApplication application = null;
    private AlertDialog.Builder builder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (PccApplication) getApplication();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        initDialog();
        requestPermissions();


    }

    private void initDialog() {
        builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setInverseBackgroundForced(true);
        builder.setTitle("是否去设置权限？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

                Uri uri = Uri.fromParts("package", getPackageName(), null);

                intent.setData(uri);

                WelcomeActivity.this.startActivityForResult(intent, 600);


            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 600) {
            toMain();
        }
    }

    private void toMain() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED
                ) {
            //进入到这里代表没有权限.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                //已经禁止提示了
                Toast.makeText(WelcomeActivity.this, "您已禁止该权限，需要重新开启。", Toast.LENGTH_SHORT).show();
                builder.show();
            } else {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
//                Toast.makeText(WelcomeActivity.this, "您已禁止该权限，需要重新开启。", Toast.LENGTH_SHORT).show();

            }

        } else {

            TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            String imei = manager.getDeviceId();
            PccApplication.setImei(imei);
            Log.e("imei", imei);
            if (!TextUtils.isEmpty(application.getUserid())) {
                getMyinfo();
                return;
            }

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    goHome();
                }
            }, SPLASH_DELAY_MILLIS);
        }
    }

    private void requestPermissions() {
        List<String> permissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isNeedRequestPermissions(permissions)) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), 0);
        } else {
            toMain();
        }
    }

    private boolean isNeedRequestPermissions(List<String> permissions) {
        addPermission(permissions, Manifest.permission.READ_PHONE_STATE);
        addPermission(permissions, Manifest.permission.ACCESS_COARSE_LOCATION);
        addPermission(permissions, Manifest.permission.ACCESS_FINE_LOCATION);
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

        switch (requestCode) {
            case 0:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                        boolean b = shouldShowRequestPermissionRationale(permissions[0]);

                        Log.e("bbbbbbbb", b + "");
                        if (!b) {

//                            requestPermissions();
                            Toast.makeText(application, "您已禁止该权限，需要重新开启。", Toast.LENGTH_SHORT).show();
                            builder.show();
                            return;
                        }
                        Toast.makeText(application, "您已禁止该权限，需要重新开启。", Toast.LENGTH_SHORT).show();
                        builder.show();
                    } else {
                        toMain();
                        return;
                    }
//                    toMain();
//                    finish();
                }
                break;
        }
    }

    private void goHome() {
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

//    private void init() {
//        JPushInterface.init(getApplicationContext());
//    }

    @Override
    protected void onResume() {
//        isForeground = true;
        JPushInterface.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
//        isForeground = false;
        JPushInterface.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void getMyinfo() {
//        WaitUI.Show(getActivity());
        BaseRequestBean bean = new GetMyInfoRequestBean(PccApplication.getUserid());
        HttpAction.getInstance().queryMyInfo(bean, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                WaitUI.Cancel();
                try {
                    if (null == response || response.code() != 200) {
                        Toast.makeText(WelcomeActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    LogUtils.e("res=  " + response.body());
                    GetMyInfoResponseBean bean = JsonMananger.jsonToBean(response.body(), GetMyInfoResponseBean.class);


                    long nowTime = SPLASH_DELAY_MILLIS - System.currentTimeMillis() + lastTime;
                    nowTime = nowTime < 0 ? 0 : nowTime;

                    if (!StringUtils.isEqual(bean.getCode(), "200")) {

                        application.setUserID("");

                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                goHome();
                            }
                        }, nowTime);
//                        Toast.makeText(WelcomeActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (bean.getData().getUserStatus() == 5 && StringUtils.isEmpty(bean.getData().getSchoolId())) {
                        Toast.makeText(WelcomeActivity.this, "您的身份为学校管理员，请去绑定您的学校吧", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
//                    JPushInterface.getRegistrationID()
//                    JPushInterface.addTags();
//                    JPushInterface.setAlias(context,System.currentTimeMillis(),bean.getData().ge);

                    application.setMyInfoData(bean.getData());


                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            startActivity(new Intent(WelcomeActivity.this, MainTabActivity.class));
                            finish();
                        }
                    }, nowTime);

                } catch (Exception e) {
                    Toast.makeText(WelcomeActivity.this, R.string.connect_error, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                application.setUserID("");


                long nowTime = SPLASH_DELAY_MILLIS - System.currentTimeMillis() - lastTime;
                nowTime = nowTime < 0 ? 0 : nowTime;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        goHome();
                    }
                }, nowTime);
            }
        });
    }
}
