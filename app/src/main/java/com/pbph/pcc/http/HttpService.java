package com.pbph.pcc.http;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.android.utils.LogUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.pbph.pcc.tools.ConstantData;
import com.pbph.pcc.tools.ThreadUtil;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class HttpService {
    private final static int CONNECTION_ERROR = 0x110;
    private HttpServiceData data = null;
    private HashMap<String, HttpURLConnection> hashMap = new HashMap<String, HttpURLConnection>();

    //    public HttpService(OnHttpCompleteListener completeListener) {
//        this.completeListener = completeListener;
//    }
    public HttpService() {
    }

    public interface OnHttpCompleteListener {
        public void onComplete(HttpServiceData resData);
    }

    public interface OnDownLoadListener {

        public void onComplete(HttpServiceData resData);

        public void onStart();

        public void onLoading(int length);

    }

    public interface OnDownLoadStateListener {

        public void onDownLoadComplete(HttpServiceData resData);

        public void onDownLoadStart();

    }

    private String sendData(String url, String requestMethod,
                            byte[] requestData, OnHttpCompleteListener completeListener) {
        String uuid = UUID.randomUUID().toString();
        data = new HttpServiceData();
        data.serviceURL = url;
        data.requestUUID = uuid;
        data.requestMethodName = "";
        data.requestMethod = requestMethod;
        data.requestBody = requestData;
        data.completeListener = completeListener;
        LogUtils.e("serviceURL  =" + data.serviceURL);
        executeRequestData(data);
        return uuid;
    }

    private String sendDataOkHttp(final String url, final String cookie,
                                  final OnHttpCompleteListener completeListener) {
        String uuid = UUID.randomUUID().toString();
        data = new HttpServiceData();
        data.serviceURL = url;
        data.requestUUID = uuid;
        data.requestMethodName = "";
        data.completeListener = completeListener;
        LogUtils.e("serviceURL=" + data.serviceURL);
        ThreadUtil.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    GetRequest request = OkGo.get(url);
                    if (!TextUtils.isEmpty(cookie))
                        request.headers("Cookie", cookie);
                    okhttp3.Response respons = request.execute();
                    LogUtils.e("code=" + respons.code());
                    data.responseCode = respons.code();
                    data.responseBody = respons.body().bytes();
                } catch (IOException e) {
                    e.printStackTrace();
                    data.responseCode = CONNECTION_ERROR;
                    data.errorMsg = "与服务器链接失败,请稍后尝试";
                }
                Message message = Message.obtain();
                message.obj = data;
                handler.sendMessage(message);
//                completeListener.onComplete(data);
            }
        });
        return uuid;
    }

    private String sendData(String url, String requestMethod,
                            byte[] requestData, Map<String, String> requestHeaders, OnHttpCompleteListener completeListener) {
        String uuid = UUID.randomUUID().toString();
        data = new HttpServiceData();
        data.serviceURL = url;
        data.requestUUID = uuid;
        data.requestMethodName = "";
        data.requestHeaders = requestHeaders;
        data.requestMethod = requestMethod;
        data.requestBody = requestData;
        data.completeListener = completeListener;
        executeRequestData(data);
        return uuid;
    }

    public String downLoadSoundFile(String url, String file,
                                    OnHttpCompleteListener onHttpCompleteListener) {
        String uuid = UUID.randomUUID().toString();
        data = new HttpServiceData();
        data.serviceURL = url;
        data.requestUUID = uuid;
        data.requestMethodName = "";
        data.requestMethod = "GET";
        data.completeListener = onHttpCompleteListener;
        executeDownLoadSound(file, data);
        return uuid;
    }

    public String downLoadImageFile(String url, String file,
                                    OnHttpCompleteListener onHttpCompleteListener) {
        String uuid = UUID.randomUUID().toString();
        data = new HttpServiceData();
        data.serviceURL = url;
        data.requestUUID = uuid;
        data.requestMethodName = "";
        data.requestMethod = "GET";
        data.completeListener = onHttpCompleteListener;
        executeDownLoadImage(file, data);
        return uuid;
    }

    public String downLoadFile(String url,
                               OnDownLoadListener onDownLoadListener) {
        String uuid = UUID.randomUUID().toString();
        data = new HttpServiceData();
        data.serviceURL = url;
        data.requestUUID = uuid;
        data.requestMethodName = "";
        data.requestMethod = "GET";
        data.downLoadListener = onDownLoadListener;
        executeDownLoad(data);
        return uuid;
    }

    public String Post(String url, byte[] requestData, OnHttpCompleteListener completeListener) {
        return sendData(url, ConstantData.POST, requestData, completeListener);
    }

    public String Post(String url, byte[] requestData, Map<String, String> requestHeaders, OnHttpCompleteListener completeListener) {
        return sendData(url, ConstantData.POST, requestData, requestHeaders, completeListener);
    }

    public String Get(String url, OnHttpCompleteListener completeListener) {
        String referer = UUID.randomUUID().toString().replaceAll("-", "");
        return sendData(url, ConstantData.GET, "".getBytes(), getHeaderMap(referer), completeListener);
    }

    public String OKhttpGet(String url, String cookie, OnHttpCompleteListener completeListen) {
        return sendDataOkHttp(url, cookie, completeListen);
    }

    public String Get(String url, Map<String, String> requestHeaders, OnHttpCompleteListener completeListener) {
        return sendData(url, ConstantData.GET, "".getBytes(), requestHeaders, completeListener);
    }

    private HashMap<String, String> getHeaderMap(String referer) {
        HashMap<String, String> mapHeader = new HashMap<String, String>();
        mapHeader.put("Referer", referer);
        return mapHeader;
    }


    public String uploadImage(String url, String id, byte[] imageBytes, OnHttpCompleteListener completeListener) {
        String uuid = UUID.randomUUID().toString();
        data = new HttpServiceData();
        data.requestUUID = uuid;
        data.requestMethod = "POST";
        // data.requestBody = requestData;
        data.requestBody = imageBytes;
        // data.requestECodeBody = requestData;
        LogUtils.e("serviceURL=" + data.serviceURL);
        // Log.e(methodName,
        // new String(Base64.encode(data.requestECodeBody, Base64.DEFAULT)));
        executeRequestData(data);
        return uuid;
    }

    private String getErrorMessage(int responseCode) {
        String errorMsg = "";
        switch (responseCode) {
            case 200:
                errorMsg = "";
                break;
            case 403:
                errorMsg = "请求被服务器拒绝！";
                break;
            case 404:
                errorMsg = "请求的服务不存在！";
                break;
            case 500:
                errorMsg = "服务器处理异常！";
                break;
        }
        return errorMsg;
    }

    private void executeRequestData(final HttpServiceData data) {
        ThreadUtil.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                sendDataByHttpPost(data);
            }
        });
    }

    private void executeDownLoadSound(final String file, final HttpServiceData data) {
        ThreadUtil.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                downSoundFile(data, file);
            }
        });
    }

    private void executeDownLoadImage(final String file, final HttpServiceData data) {
        ThreadUtil.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                downImageFile(data, file);
            }
        });
    }

    private void executeDownLoad(final HttpServiceData data) {
        ThreadUtil.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                downLoadFile(data);
            }
        });
    }

    public void cancelCallService(String callId) {
        synchronized (this) {
            HttpURLConnection connection = hashMap.get(callId);
            if (null != connection) {
                connection.disconnect();
            }
            hashMap.remove(callId);
        }
    }

    private void sendDataByHttpPost(HttpServiceData data) {
        try {
            String address_url = data.serviceURL;
            URL url = new URL(address_url);
            LogUtils.e("url " + address_url);
            HttpURLConnection httpConn = (HttpURLConnection) url
                    .openConnection();
            synchronized (this) {
                hashMap.put(data.requestUUID, httpConn);
            }
            httpConn.setConnectTimeout(1000 * 30);
            httpConn.setReadTimeout(1000 * 30);
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setUseCaches(false);
            httpConn.setRequestMethod(data.requestMethod);
            httpConn.addRequestProperty("Content-length", ""
                    + data.requestBody.length);
            httpConn.addRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            httpConn.addRequestProperty("Accept-Encoding", "utf-8");
            httpConn.addRequestProperty("Connection", "keep-alive");

            if (null != data.requestHeaders && !data.requestHeaders.isEmpty()) {
                Map<String, String> head = data.requestHeaders;
                Set<String> headSet = head.keySet();
                for (String key : headSet) {
                    httpConn.setRequestProperty(key, head.get(key));
                }
            }
            if (ConstantData.POST.equals(data.requestMethod)) {
                OutputStream outputStream = httpConn.getOutputStream();
                outputStream.write(data.requestBody);
                outputStream.close();
            }
            data.responseCode = httpConn.getResponseCode();
            data.errorMsg = getErrorMessage(data.responseCode);
            if (HttpURLConnection.HTTP_OK == data.responseCode) {
                data.responseHeaders = httpConn.getHeaderFields();
                InputStream inputStream = httpConn.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }

                data.responseBody = baos.toByteArray();
                LogUtils.e("responseBody=" + new String(
                        data.responseBody));
                baos.close();
                inputStream.close();

            }
        } catch (Exception e) {
            data.responseCode = CONNECTION_ERROR;
            data.errorMsg = "与服务器链接失败,请稍后尝试";
            e.printStackTrace();
        } finally {
            synchronized (this) {
                hashMap.get(data.requestUUID).disconnect();
                hashMap.remove(data.requestUUID);
            }
            Message message = Message.obtain();
            message.obj = data;
            handler.sendMessage(message);
        }
    }

    public void cancelRequest(String uuid) {
        synchronized (this) {
            try {
                if (!hashMap.containsKey(uuid)) {
                    return;
                }
                HttpURLConnection connection = hashMap.get(uuid);
                if (null != connection) {
                    connection.disconnect();
                }
                hashMap.remove(uuid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void downSoundFile(HttpServiceData data, String filepath) {
        try {
            URL url = new URL(data.serviceURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(1000 * 30);
            conn.setReadTimeout(1000 * 30);
            conn.setUseCaches(false);
            synchronized (this) {
                hashMap.put(data.requestUUID, conn);
            }

            data.responseCode = conn.getResponseCode();
            data.errorMsg = getErrorMessage(data.responseCode);
            LogUtils.e("Load_File " + data.responseCode + "   " + data.errorMsg);

//			if (HttpURLConnection.HTTP_OK == error.responseCode) {
            InputStream inputstream = conn.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(
                    inputstream);
            long length = conn.getContentLength();
            LogUtils.e("Load_File  " + length);
            if (length == 0) {
                inputstream.close();
                dataInputStream.close();
                data.responseCode = ConstantData.DOWN_LOAD_NO_FILE;
                data.errorMsg = "操作失败，请重新尝试";
                return;
            }
            File myFile = new File(filepath);
            LogUtils.e("myFilefilepath " + filepath);
            LogUtils.e("filepath length=" + myFile.length());
            if (!myFile.exists()) {
                myFile.createNewFile();
            } else if (myFile.exists() && myFile.length() != length) {
                myFile.delete();
                myFile.createNewFile();
            } else {
                inputstream.close();
                dataInputStream.close();
                return;
            }
            FileOutputStream outputStream = new FileOutputStream(myFile);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = dataInputStream.read(buffer)) != -1) {
                LogUtils.e("Load_File len=" + len);
                outputStream.write(buffer, 0, len);
            }
            outputStream.close();
            inputstream.close();
            dataInputStream.close();
//			}
        } catch (SocketException e) {
            data.responseCode = CONNECTION_ERROR;
            data.errorMsg = "已取消";
            e.printStackTrace();
        } catch (Exception e) {
            data.responseCode = CONNECTION_ERROR;
            data.errorMsg = "与服务器链接失败,请稍后尝试";
            e.printStackTrace();
        } finally {
            synchronized (this) {
                if (hashMap.containsKey(data.requestUUID)) {
                    hashMap.get(data.requestUUID).disconnect();
                    hashMap.remove(data.requestUUID);
                }
            }
            LogUtils.e("Load_File " + data.responseCode + "   " + data.errorMsg);
            data.responseBody = filepath.getBytes();
            data.completeListener.onComplete(data);
        }

    }

    private void downImageFile(HttpServiceData data, String filepath) {
        try {
            URL url = new URL(data.serviceURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(1000 * 30);
            conn.setReadTimeout(1000 * 30);
            conn.setUseCaches(false);
            synchronized (this) {
                hashMap.put(data.requestUUID, conn);
            }
            data.responseCode = conn.getResponseCode();
            data.errorMsg = getErrorMessage(data.responseCode);
            LogUtils.e("Load_File " + data.responseCode + "   " + data.errorMsg);
            InputStream inputstream = conn.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(
                    inputstream);
            long length = conn.getContentLength();
            LogUtils.e("Load_File " + length);
            if (length == 0) {
                inputstream.close();
                dataInputStream.close();
                data.responseCode = ConstantData.DOWN_LOAD_NO_FILE;
                data.errorMsg = "操作失败，请重新尝试";
                return;
            }
            File myFile = new File(filepath);
            LogUtils.e("myFilefilepath " + filepath);
            LogUtils.e("filepath length=" + myFile.length());
            if (myFile.exists()) {
                myFile.delete();
            }
            myFile.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(myFile);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = dataInputStream.read(buffer)) != -1) {
//                Log.e("Load_File", "len=" + len);
                outputStream.write(buffer, 0, len);
            }
            outputStream.close();
            inputstream.close();
            dataInputStream.close();
//			}
        } catch (SocketException e) {
            data.responseCode = CONNECTION_ERROR;
            data.errorMsg = "已取消";
            e.printStackTrace();
        } catch (Exception e) {
            data.responseCode = CONNECTION_ERROR;
            data.errorMsg = "与服务器链接失败,请稍后尝试";
            e.printStackTrace();
        } finally {
            synchronized (this) {
                if (hashMap.containsKey(data.requestUUID)) {
                    hashMap.get(data.requestUUID).disconnect();
                    hashMap.remove(data.requestUUID);
                }
            }
            LogUtils.e("Load_File " + data.responseCode + "   " + data.errorMsg);
            data.responseBody = filepath.getBytes();
            data.completeListener.onComplete(data);
        }

    }

    private void downLoadFile(HttpServiceData data) {
        HttpServiceData error = new HttpServiceData();
        String filepath = ConstantData.DOWN_LOAD_PATH + "update.apk";
        try {
            URL url = new URL(data.serviceURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(1000 * 30);
            conn.setReadTimeout(1000 * 30);
            synchronized (this) {
                hashMap.put(data.requestUUID, conn);
            }
            error.responseCode = conn.getResponseCode();
            error.errorMsg = getErrorMessage(error.responseCode);
            LogUtils.e("Load_File " + error.responseCode + "   " + error.errorMsg);
//			if (HttpURLConnection.HTTP_OK == error.responseCode) {
            InputStream inputstream = conn.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(
                    inputstream);
            long length = conn.getContentLength();
            LogUtils.e("Load_File " + length);
            if (length == 0) {
                error.responseCode = ConstantData.DOWN_LOAD_NO_FILE;
                error.errorMsg = "操作失败，请重新尝试";
                return;
            }
            LogUtils.e("filepath " + filepath);
            File myFile = new File(filepath);
            if (!myFile.exists()) {
                myFile.createNewFile();
            } else {
                myFile.delete();
                myFile.createNewFile();
            }
            data.downLoadListener.onStart();
            FileOutputStream outputStream = new FileOutputStream(myFile);

            byte[] buffer = new byte[1024];
            int len;
            long count = 0;
            while ((len = dataInputStream.read(buffer)) != -1) {
                count += len;
                outputStream.write(buffer, 0, len);
                data.downLoadListener.onLoading((int) (count * 100 / length));
                // count * 100 / length
            }
            outputStream.close();
            inputstream.close();
            dataInputStream.close();
//			}
        } catch (SocketException e) {
            error.responseCode = CONNECTION_ERROR;
            error.errorMsg = "已取消";
            e.printStackTrace();
        } catch (Exception e) {
            error.responseCode = CONNECTION_ERROR;
            error.errorMsg = "与服务器链接失败,请稍后尝试";
            e.printStackTrace();
        } finally {
            synchronized (this) {
                if (hashMap.containsKey(data.requestUUID)) {
                    hashMap.get(data.requestUUID).disconnect();
                    hashMap.remove(data.requestUUID);
                }
            }
            LogUtils.e("Load_File " + error.responseCode + "   " + error.errorMsg);
            error.responseBody = filepath.getBytes();
            data.downLoadListener.onComplete(error);
        }

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            HttpServiceData data = (HttpServiceData) msg.obj;
//            try {
//                if (data.responseCode == 200
//                        ) {
//                    Log.e(data.requestMethodName, new String(
//                            data.responseBody));
//                    data.data = new String(
//                            data.responseBody);
//                    JSONObject object = new JSONObject(new String(
//                            data.responseBody));
//                    if (ConstantData.LOGIN.equals(data.requestMethodName) && object.has("code")) {
//                        data.errorCode = new JSONObject(object.getString("result")).getInt("code");
//                        data.errorMsg = object.getString("status");
//                    } else {
//                        data.errorCode = object.getInt("code");
//                        data.errorMsg = object.getString("message");
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                data.responseCode = CONNECTION_ERROR;
//                data.errorMsg = "与服务器链接失败,请稍后尝试";
//            }
            data.completeListener.onComplete(data);
        }
    };


    // private String readInPutStream(InputStream inputStream) throws Exception
    // {
    // StringBuffer stringBuffer = new StringBuffer();
    // String readLine = "";
    // BufferedReader responseReader = new BufferedReader(
    // new InputStreamReader(inputStream, "utf-8"));
    // while ((readLine = responseReader.readLine()) != null) {
    // stringBuffer.append(readLine).append("");
    // }
    // responseReader.close();
    //
    // return stringBuffer.toString();
    // }
    public void close() {
        if (null != hashMap && hashMap.isEmpty()) {
            Set<String> keySet = hashMap.keySet();
            for (String key : keySet) {
                HttpURLConnection connection = hashMap.get(key);
                if (null != connection) {
                    connection.disconnect();
                }
            }
        }
    }

    public static class HttpServiceData {

        public int id = 0;
        public OnHttpCompleteListener completeListener;
        public OnDownLoadListener downLoadListener;
        public String serviceURL = null;
        public String requestMethod = "";
        public Map<String, String> requestHeaders = null;
        public byte[] requestBody = null;
        public byte[] requestECodeBody = null;
        public String requestMethodName = "";
        public int responseCode = -1;
        public Map<String, List<String>> responseHeaders = null;
        public byte[] responseBody = null;
        public byte[] responseDcodeBody = null;
        public String requestUUID = "";
        public String errorMsg = "";
        public int errorCode = 0;
        public String data = "";

        public void finalize() {
            LogUtils.d("callData " + this.toString());
        }
    }
}
