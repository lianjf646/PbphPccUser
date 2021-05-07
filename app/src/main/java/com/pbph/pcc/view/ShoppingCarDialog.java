package com.pbph.pcc.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/9/13 0013.
 */

public class ShoppingCarDialog {
    public static RxDialogSureCancel getDialog(final Context context,String content){
        final SharedPreferences mySharedPreferences= context.getSharedPreferences("taadeArea", Activity.MODE_PRIVATE);
        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(context);//提示弹窗
        rxDialogSureCancel.getTvSure().setText("取消");
        rxDialogSureCancel.getTvCancel().setText("确定");
        rxDialogSureCancel.getTvContent().setText(content);
        rxDialogSureCancel.hideTitle();
        rxDialogSureCancel.setTitle("提示");
        /*rxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxDialogSureCancel.cancel();
            }
        });
        rxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxDialogSureCancel.cancel();
            }
        });
        rxDialogSureCancel.show();*/
        return rxDialogSureCancel;
    }
}
