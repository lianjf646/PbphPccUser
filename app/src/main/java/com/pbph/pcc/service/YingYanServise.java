package com.pbph.pcc.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.android.utils.LogUtils;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.api.entity.LocRequest;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.pbph.pcc.application.PccApplication;


/**
 * Created by 连嘉凡 on 2017/3/29.
 */

public class YingYanServise extends Service implements BDLocationListener {

    long serviceId = 151856;
    // 设备标识
    String entityName;
    // 是否需要对象存储服务，默认为：false，关闭对象存储服务。注：鹰眼 Android SDK v3.0以上版本支持随轨迹上传图像等对象数据，若需使用此功能，该参数需设为 true，且需导入bos-android-sdk-1.0.2.jar。
    boolean isNeedObjectStorage = false;
    // 初始化轨迹服务
    Trace mTrace;
    // 初始化轨迹服务客户端
    LBSTraceClient mTraceClient;
    // 打包回传周期(单位:秒)
    int packInterval = 120;
    int gatherInterval = 120;
    //    int interval = 5;
    //    private RealTimeHandler realTimeHandler = new RealTimeHandler();
//    private RealTimeLocRunnable realTimeLocRunnable = null;
    private LocRequest locRequest;
    private PowerManager.WakeLock mWakeLock = null;
    private PccApplication mApp;
    // 设置定位和打包周期
//    EntityListRequest request;
    // 请求标识
//    int tag = 5;
    //设置活跃时间
//    long activeTime = System.currentTimeMillis() / 1000 - 5 * 60;
    //    long activeTime = 300;
    // 过滤条件
//    FilterCondition filterCondition;
    // 查找当前时间5分钟之内有定位信息上传的entity
    // 返回结果坐标类型
//    CoordType coordTypeOutput = CoordType.bd09ll;
    // 分页索引
//    int pageIndex = 1;
    // 分页大小
//    int pageSize = 100;
    private LocationClient mLocClient;

    @Override
    public IBinder onBind(Intent arg0) {
        LogUtils.e("onBind: ");
        acquireWakeLock();
        initLocation();
        init();
        return null;
    }

    private void initLocation() {
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // ��gps
        option.setCoorType("bd09ll"); // ������������
        option.setScanSpan(20 * 1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        Log.e("initLocation", "开启定位");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mTraceClient.stopTrace(mTrace, mTraceListener);
        mTraceClient.stopGather(mTraceListener);
        releaseWakeLock();
        if (null != mLocClient) {
            mLocClient.stop();
        }
//        stopRealTimeLoc();
        LogUtils.e("onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, START_STICKY, startId);
    }

    private void acquireWakeLock() {
        if (null == mWakeLock) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK
                    | PowerManager.ON_AFTER_RELEASE, this.getClass()
                    .getSimpleName());
            if (null != mWakeLock) {
                mWakeLock.acquire();
            }
        }
    }

    private void releaseWakeLock() {
        if (null != mWakeLock) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        LogUtils.e("onDestroy: ");

    }

    /**
     * 初始化
     */
    private void init() {
        mApp = (PccApplication) getApplication();
        locRequest = new LocRequest(serviceId);
        entityName = PccApplication.getUserid();
        mTrace = new Trace(serviceId, entityName, isNeedObjectStorage);
        mTraceClient = new LBSTraceClient(getApplicationContext());
        mTraceClient.setInterval(gatherInterval, packInterval);
        // 开启服务
        mTraceClient.startTrace(mTrace, mTraceListener);
        // 开启采集
        mTraceClient.startGather(mTraceListener);
//        filterCondition = new FilterCondition();
//        filterCondition.setActiveTime(activeTime);
//        LogUtils.e("init: " + activeTime);
//        request = new EntityListRequest(tag, serviceId, filterCondition, coordTypeOutput, pageIndex, pageSize);
//        startRealTimeLoc(interval);

    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        if (null == location) {
            return;
        }
//        Log.e("currentLatLng", location.getLatitude() + "   " + location
//                .getLongitude() + location.getCity());
        mApp.locationInfo.setmLatitude(String.valueOf(location.getLatitude()));
        mApp.locationInfo.setmLongitude(String.valueOf(location.getLongitude()));
    }

    //    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }

//    /**
//     * 实时定位任务
//     *
//     * @author baidu
//     */
//    class RealTimeLocRunnable implements Runnable {
//
//        private int interval = 10;
//
//        public RealTimeLocRunnable(int interval) {
//            this.interval = interval;
//        }
//
//
//        @Override
//        public void run() {
//            mTraceClient.queryRealTimeLoc(locRequest, entityListener);
//            realTimeHandler.postDelayed(this, interval * 1000);
//        }
//    }


//    static class RealTimeHandler extends Handler {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//        }
//
//    }
//
//    public void startRealTimeLoc(int interval) {
//        realTimeLocRunnable = new RealTimeLocRunnable(interval);
//        realTimeHandler.post(realTimeLocRunnable);
//    }
//
//    public void stopRealTimeLoc() {
//        if (null != realTimeHandler && null != realTimeLocRunnable) {
//            realTimeHandler.removeCallbacks(realTimeLocRunnable);
//        }
//        mTraceClient.stopRealTimeLoc();
//    }

//    // 初始化监听器
//    OnEntityListener entityListener = new OnEntityListener() {
//
//        @Override
//        public void onReceiveLocation(TraceLocation location) {
//            if (StatusCodes.SUCCESS != location.getStatus() || CommonUtil.isZeroPoint(location.getLatitude(),
//                    location.getLongitude())) {
//                return;
//            }
//
//            Log.e("currentLatLng", location.getLatitude() + "   " + location
//                    .getLongitude());
//            mApp.locationInfo.setmLatitude(String.valueOf(location.getLatitude()));
//            mApp.locationInfo.setmLongitude(String.valueOf(location.getLongitude()));
//
//        }
//    };


    // 初始化轨迹服务监听器
    OnTraceListener mTraceListener = new OnTraceListener() {
        @Override
        public void onBindServiceCallback(int i, String s) {

        }

        // 开启服务回调
        @Override
        public void onStartTraceCallback(int status, String message) {
            LogUtils.e("onStartTraceCallback: " + status + "||||" + message);
        }

        // 停止服务回调
        @Override
        public void onStopTraceCallback(int status, String message) {
            LogUtils.e("onStopTraceCallback: " + status + "||||" + message);
        }

        // 开启采集回调
        @Override
        public void onStartGatherCallback(int status, String message) {
            LogUtils.e("onStartGatherCallback: " + status + "||||" + message);
        }

        // 停止采集回调
        @Override
        public void onStopGatherCallback(int status, String message) {
            LogUtils.e("onStopGatherCallback: " + status + "||||" + message);
        }

        // 推送回调
        @Override
        public void onPushCallback(byte messageNo, PushMessage message) {
        }

        @Override
        public void onInitBOSCallback(int i, String s) {

        }
    };
}
