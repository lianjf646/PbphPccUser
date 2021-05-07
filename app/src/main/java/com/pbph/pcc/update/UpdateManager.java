package com.pbph.pcc.update;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.view.KeyEvent;
import android.widget.Toast;

import com.android.utils.LogUtils;
import com.pbph.pcc.BuildConfig;
import com.pbph.pcc.http.HttpService;
import com.pbph.pcc.tools.ConstantData;
import com.pbph.pcc.tools.WaitUI;
import com.pbph.pcc.view.MyProgressDialog;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


public class UpdateManager implements HttpService.OnDownLoadStateListener {

    private static final int DOWNLOAD_FINISH = 2;
    private static final int GetXml_FINISH = 3;
    private static final int GetXml_ERROR = 4;
    HashMap<String, String> mHashMap;
    private static UpdateManager updateManager = null;
    private Context mContext;
    private MyProgressDialog mDownloadDialog;
    private ParseXmlService service = new ParseXmlService();
    private InputStream inputStream;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOAD_FINISH:
                    installApk((String) msg.obj);
                    break;
                case GetXml_ERROR:
                    break;
                case GetXml_FINISH:
                    if (isUpdate()) {
                        showNoticeDialog();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public static UpdateManager getInstance(Context context) {
        if (null == updateManager) {
            updateManager = new UpdateManager(context);
        }
        return updateManager;
    }

    private UpdateManager(Context context) {
        this.mContext = context;
        mDownloadDialog = new MyProgressDialog(context, this);
    }

    public void checkVersion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    HttpURLConnection conn = (HttpURLConnection) new URL(ConstantData.gengXinUrl)
                            .openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    if (conn.getResponseCode() == 200) {
                        inputStream = conn.getInputStream();
                        try {
                            mHashMap = service.parseXml(inputStream);
                            mHandler.sendEmptyMessage(GetXml_FINISH);
                            // inputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        inputStream.close();

//                        mHandler.sendEmptyMessage(GetXml_FINISH);
//                        if (null != inputStream) {
//                            inputStream.close();
//                        }

                    }
                } catch (Exception ignored) {

                }// �ͷ�������������
            }
        }).start();


//        HttpAction.getInstance().checkVersion(new HttpService.OnHttpCompleteListener() {
//            @Override
//            public void onComplete(HttpService.HttpServiceData resData) {
//                try {
//                    String result = new String(resData.responseBody);
//                    if (!TextUtils.isEmpty(result)) {
//                        ByteArrayInputStream inputStream = new ByteArrayInputStream(result.getBytes());
//                        mHashMap = service.parseXml(inputStream);
//                        inputStream.close();
//                        onCheckVersion.isUpdate(isUpdate());
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    private int getVersionCode() {
        try {
            PackageInfo pi = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * �������Ƿ��и��°汾
     *
     * @return
     */
    public boolean isUpdate() {
        try {
            int versionCode = getVersionCode();
            if (null != mHashMap) {
                int serviceCode = Integer.valueOf(mHashMap.get("version"));
                LogUtils.e("isUpdate versionCode=" + versionCode + "     serviceCode=" + serviceCode);
                return serviceCode > versionCode;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void showNoticeDialog() {
        Builder builder = new Builder(mContext);
        builder.setTitle("软件更新");

        String message = "";
        if (mHashMap.get("kb") != null) {
            message = "文件大小：" + mHashMap.get("kb") + "\n";
        }

        if (mHashMap.get("note") != null) {
            message = message + mHashMap.get("note").replaceAll("br", "\n");
        }
        if (message.equals("")) {
            message = "发现新版本,更新？";
        }
        builder.setMessage(message);
        // 更新
        builder.setPositiveButton("更新", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WaitUI.Show(mContext);
                mDownloadDialog.requestDownFile(mHashMap.get("url"));
            }
        });

        // 如果不是必须更新 显示稍后更新
        if (!mHashMap.get("must").equals("true")) {
            // 稍后更新
            builder.setNegativeButton("稍后更新", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }


        builder.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                return true;
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void installApk(String filePath) {
        File apkfile = new File(filePath);
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileProvider", apkfile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
//		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),"application/vnd.android.package-archive");
        mContext.startActivity(intent);
        System.exit(0);
    }

    /**
     * ��װAPK�ļ�
     */
//	private void installApk(String filePath) {
//		Intent i = new Intent(Intent.ACTION_VIEW);
//		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		// i.setAction(android.content.Intent.ACTION_VIEW);
//		// i.addCategory(Intent.CATEGORY_DEFAULT);
//		i.setDataAndType(Uri.parse("file://" + filePath),
//				"application/vnd.android.package-archive");
//		mContext.startActivity(i);
//	}
    @Override
    public void onDownLoadComplete(HttpService.HttpServiceData resData) {
        mDownloadDialog.cancel();
        if (resData.responseCode != HttpURLConnection.HTTP_OK) {
            Toast.makeText(mContext, resData.errorMsg, Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (resData.errorCode != 0) {
            Toast.makeText(mContext, resData.errorMsg, Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        Message message = Message.obtain();
        message.obj = new String(resData.responseBody);
        message.what = DOWNLOAD_FINISH;
        mHandler.sendMessage(message);
    }

    @Override
    public void onDownLoadStart() {
        WaitUI.Cancel();
    }

}
