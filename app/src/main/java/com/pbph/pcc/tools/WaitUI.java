package com.pbph.pcc.tools;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.Timer;
import java.util.TimerTask;

public class WaitUI {
    public static ProgressDialog progressDialog = null;// 声明进度条对话框
    private static int CHECK_TIME = 1000 * 30;// 超时
    private static Timer[] timers = new Timer[2];
    private static Context con;

    public WaitUI() {
    }

    public static void setTimer(Timer timer) {
        timers[0] = timers[1];
        timers[1] = timer;
    }

    public static void showByTimer(final Context context) {
        try {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (progressDialog != null) {
                        checkThread();
                    }
                }
            }, CHECK_TIME);
            setTimer(timer);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void Show(Context context) {
        Show(context, "请稍候.....");
    }

    @SuppressWarnings("deprecation")
    public static void Show(Context context, String caption, boolean is_button) {

        con = context;
        showByTimer(context);
        if (timers[0] != null) {
            timers[0].cancel();
        }
        if (progressDialog == null) {
            // 创建ProgressDialog对象
            progressDialog = new ProgressDialog(context,
                    ProgressDialog.THEME_HOLO_LIGHT);
            progressDialog.setMessage(caption);
            progressDialog.setCancelable(false);
            if (is_button) {
                progressDialog.setButton("取消", diogbtnL);
            }
            // 设置取消按钮
            progressDialog.show();
        }

    }

    public static void Show(Context context, String caption) {
        Show(context, caption, false);
    }

    private static DialogInterface.OnClickListener diogbtnL = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Cancel();

        }
    };
    private static OnClickListener btnL = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Cancel();
        }
    };

    protected static void checkThread() {

        Message msg = new Message();

        mHandler.sendMessage(msg);

    }

    private static Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            showMsg("请求超时！", con);
            Cancel();
        }
    };

    public static void Cancel() {
        if (progressDialog != null) {
            progressDialog.cancel();
            progressDialog = null;
        }
    }

    private static void showMsg(final String caption, final Context context) {
        Dialog dialog = new AlertDialog.Builder(context).setTitle("提示")
                // 设置标题
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(caption)// 设置内容
                .setPositiveButton("确定",// 设置确定按钮
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                            }
                        }).create();// 创建按钮
        // 显示对话框
        dialog.show();
    }

    void MyDialog(String caption, final Context context) {
        Dialog dialog = new AlertDialog.Builder(context).setTitle("请求超时！")
                // 设置标题
                // .setMessage(upmessage)
                .setMessage(caption)
                // 设置内容
                .setPositiveButton("确定",// 设置确定按钮
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                            }
                        }).create();// 创建按钮
        // 显示对话框
        dialog.show();
        dialog.setCancelable(false);
    }

}
