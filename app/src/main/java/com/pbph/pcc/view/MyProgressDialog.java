package com.pbph.pcc.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pbph.pcc.R;
import com.pbph.pcc.http.HttpAction;
import com.pbph.pcc.http.HttpService;


public class MyProgressDialog implements HttpService.OnDownLoadListener {
    private final int progress_dismiss = 0x11;
    private final int progress_show = 0x12;
    private final int progress_update = 0x13;
    private Dialog dialog = null;
    private HttpService.OnDownLoadStateListener stateListener = null;
    private Context context = null;
    private ProgressBar progressBar = null;
    private int width, height = 0;
    private String uuid = "";

    public MyProgressDialog(Context context,
                            HttpService.OnDownLoadStateListener stateListener) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        this.context = context;
        this.stateListener = stateListener;
        initDialog(context);
    }

    public void requestDownFile(String downURL) {
        uuid = HttpAction.getInstance().downLoadFile(downURL, this);
    }

    public void showDialog() {
        if (null == dialog) {
            initDialog(context);
        }
        handler.sendEmptyMessage(progress_show);
    }

    public void cancel() {
        if (null != dialog) {
            HttpAction.getInstance().cancelCallService(uuid);
            dialog.dismiss();

        }
    }

    private void initDialog(Context context) {

        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_progress_layout, null);
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        TextView title = view
                .findViewById(R.id.tv_dialog_progress_title);
        progressBar = view.findViewById(R.id.pb_dialog_progress);
        Button cancel = view
                .findViewById(R.id.btn_dialog_progresst_cancel);
        cancel.setVisibility(View.GONE);
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // ��ȡ�Ի���ǰ�Ĳ���ֵ
        p.width = width / 10 * 9; // �߶�����Ϊ��Ļ��0.6
        // p.width = (int) (d.getWidth() * 0.65); // �������Ϊ��Ļ��0.65
        dialogWindow.setAttributes(p);
//		view.getLayoutParams().width = width / 4 * 3;

    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case progress_dismiss:
                    dialog.dismiss();
                    stateListener.onDownLoadComplete((HttpService.HttpServiceData) msg.obj);
                    break;
                case progress_show:
                    stateListener.onDownLoadStart();
                    dialog.show();
                    break;
                case progress_update:
                    progressBar.setProgress(msg.arg1);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onComplete(HttpService.HttpServiceData resData) {
        Message message = Message.obtain();
        message.what = progress_dismiss;
        message.obj = resData;
        handler.sendMessage(message);
    }

    @Override
    public void onStart() {
        handler.sendEmptyMessage(progress_show);
    }

    @Override
    public void onLoading(int length) {
        Message message = Message.obtain();
        message.arg1 = length;
        message.what = progress_update;
        handler.sendMessage(message);
    }
}
