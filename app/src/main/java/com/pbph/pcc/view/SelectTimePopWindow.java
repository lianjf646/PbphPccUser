package com.pbph.pcc.view;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.pbph.pcc.R;
import com.pbph.pcc.adapter.TimeListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SelectTimePopWindow extends PopupWindow implements View.OnClickListener{
    private RecyclerView timeList;
    @SuppressLint("InflateParams")
    public SelectTimePopWindow(Activity context, TimeListAdapter adapter) {
        LayoutInflater inflater = (LayoutInflater) context
                                  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.popupwindow_sel_time, null);
        View bg = content.findViewById(R.id.bg);
        TextView today = content.findViewById(R.id.today);
        today.setText("今天("+getDay()+")");
        timeList = content.findViewById(R.id.time_list);
        timeList.setLayoutManager(new LinearLayoutManager(context));
        timeList.setAdapter(adapter);
        bg.setOnClickListener(this);
        // 设置SelectPicPopupWindow的View
        this.setContentView(content);
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(dm.widthPixels);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(dm.heightPixels+getStatusBarHeight(context));
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.AnimationPreview);
        this.setClippingEnabled(false);
    }
    public String getDay(){
        String day = "";
        switch (new Date().getDay()){
            case 0:
                day = "周日";
                break;
            case 1:
                day = "周一";
                break;
            case 2:
                day = "周二";
                break;
            case 3:
                day = "周三";
                break;
            case 4:
                day = "周四";
                break;
            case 5:
                day = "周五";
                break;
            case 6:
                day = "周六";
                break;
        }
        return day;
    }
    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent,Activity activity) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            //this.showAsDropDown(parent, 0, 0);
            this.showAtLocation(parent,Gravity.TOP|Gravity.START, 0, -getStatusBarHeight(activity));
        } else {
            this.dismiss();
        }
    }
    /**
     * 获取状态通知栏高度
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bg:
                dismiss();
                break;
        }
    }
}
