package com.pbph.pcc.tools;


import android.content.Context;
import android.support.annotation.NonNull;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.android.utils.LogUtils;
import com.pbph.pcc.R;

public class AliOss {


    private String ENDPOINT = "oss-cn-beijing.aliyuncs.com";
    // 阿里云API的密钥Access Key ID
//    private String ACCESS_KEY_ID = "STS.CoV4ipy54112E4FFNmr6rTqGe";
    // 阿里云API的密钥Access Key Secret
//    private String ACCESS_KEY_SECRET = "EUoaL9ZF5BZP4RPqTnE6M13Y7st71DuxDbC1MPiFgirQ";
    private String SECURITY_TOKEN = "CAIS8QF1q6Ft5B2yfSjIoqzjf9PElOoVhvPZRxL3ok44frleu7Tshzz2IH9IenhhAO0Wv/ozmWBQ6P4blqB6T55OSAmcNZIoVnDgWYDiMeT7oMWQweEuqv/MQBq+aXPS2MvVfJ+KLrf0ceusbFbpjzJ6xaCAGxypQ12iN+/i6/clFKN1ODO1dj1bHtxbCxJ/ocsBTxvrOO2qLwThjxi7biMqmHIl1Tsks/Xin5zEtkCF1AOg8IJP+dSteKrDRtJ3IZJyX+2y2OFLbafb2EZSkUMVqPkn3fUZqWaZ4ozNWwcJug/nNPHP+9luPRJ/dlbmyX+PTS+WGoABDO6PYhYXE5Qo/ZWyCLgM/hNaa6kJv7RHoZDAdABs1QvrTleZp93IWzJvI9YrIbUyRQZkiiu+lZ5eqVnYKkDbO8bQKtCFqtEJj2v27cfCU1haxmjg7+9+Zezew7ndhpzpkzoMKBSsMC+oINq8NwLjhNAkdGp5O31h9EaauGcLe6w=";
    // 測試阿里云API的bucket名称
//    private String BACKET_NAME = "pccbucket";
    private Context context = null;
    // 阿里云API的文件夹名称
//    private String FOLDER = "pcctest/";
    private OSSClient oss;

    private String ACCESS_KEY_ID = "LTAI8rgwWlWV952D";
    // 阿里云API的密钥Access Key Secret
    private String ACCESS_KEY_SECRET = "SVK3ldzNfus2wkVHSyPv7buKEomTuX";
    // 測試阿里云API的bucket名称
    private String BACKET_NAME = "java-xag";

    // 阿里云API的文件夹名称
    private String FOLDER = "xag/";


    //    http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/test
    private AliOss() {

    }

    public void oosConfig(@NonNull Context context, String access_key_id, String access_key_secret, String security_token) {
        this.context = context;
        this.ACCESS_KEY_ID = access_key_id;
        this.ACCESS_KEY_SECRET = access_key_secret;
        this.SECURITY_TOKEN = security_token;
        initOOS();
    }

    public static AliOss getInstance() {
        return Inner.aliOss;
    }

    private static class Inner {
        private static final AliOss aliOss = new AliOss();
    }


//    public static AliOss getInstance() {
//        if (instance == null) {
//            synchronized (AliOss.class) {
//                if (instance == null) {
//                    instance = new AliOss();
//                }
//            }
//        }
//        return instance;
//    }

    private void initOOS() {
//        if (null != oss) {
//            return;
//        }
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(ACCESS_KEY_ID, ACCESS_KEY_SECRET, SECURITY_TOKEN);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
//        OSSLog.enableLog(); //这个开启会支持写入手机sd卡中的一份日志文件位置在SD_path\OSSLog\logs.csv
        OSSLog.disableLog();
        oss = new OSSClient(context, ENDPOINT, credentialProvider, conf);
    }

    public void uplodImage(String key, String path) {
        if (oss == null) {
            initOOS();
        }
        PutObjectRequest put = new PutObjectRequest(BACKET_NAME, FOLDER + key, path);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                LogUtils.e("PutObject currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                LogUtils.e("PutObject UploadSuccess");
                LogUtils.e("ETag" + result.getETag());
                LogUtils.e("RequestId" + result.getRequestId());
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    LogUtils.e("ErrorCode" + serviceException.getErrorCode());
                    LogUtils.e("RequestId" + serviceException.getRequestId());
                    LogUtils.e("HostId" + serviceException.getHostId());
                    LogUtils.e("RawMessage" + serviceException.getRawMessage());
                }
            }
        });
    }

    //    http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/test
    private String getUploadImagePath(String key) {
        return "http://" + BACKET_NAME + "." + ENDPOINT + "/" + FOLDER + key;
    }

    public void uploadImage(String key, byte[] imageBytes, OnOosUploadListener onListener) {
        PutObjectRequest put = new PutObjectRequest(BACKET_NAME, FOLDER + key, imageBytes);
        try {
            PutObjectResult putResult = oss.putObject(put);
            LogUtils.e("getStatusCode  putResult.getStatusCode()=" + putResult.getStatusCode());
            LogUtils.e("toString  putResult.toString()=" + putResult.toString());
            LogUtils.e("Callback  putResult.getServerCallbackReturnBody()=" + putResult.getServerCallbackReturnBody());
            LogUtils.e("ETag" + putResult.getETag());
            LogUtils.e("RequestId" + putResult.getRequestId());
            onListener.onSuccess(getUploadImagePath(key));
        } catch (ClientException e) {
            e.printStackTrace();
            onListener.onFailure(context.getResources().getString(R.string.connect_error));
        } catch (ServiceException e) {
            // 服务异常
            LogUtils.e("RequestId" + e.getRequestId());
            LogUtils.e("ErrorCode" + e.getErrorCode());
            LogUtils.e("HostId" + e.getHostId());
            LogUtils.e("RawMessage" + e.getRawMessage());
            onListener.onFailure(context.getResources().getString(R.string.connect_error));
        }
    }

    public interface OnOosUploadListener {
        public void onSuccess(String path);

        public void onFailure(String msg);
    }

}
