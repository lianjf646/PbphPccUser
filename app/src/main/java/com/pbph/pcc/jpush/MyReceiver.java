package com.pbph.pcc.jpush;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.utils.LogUtils;
import com.pbph.pcc.activity.LoginActivity;
import com.pbph.pcc.activity.MainTabActivity;
import com.pbph.pcc.activity.MessageListActivity;
import com.pbph.pcc.activity.RecieveOrderActivity;
import com.pbph.pcc.activity.WelcomeActivity;
import com.pbph.pcc.tools.ConstantData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;


public class MyReceiver extends BroadcastReceiver {

    private static String openPage = "";
    private String messageTitle = "";
    private static String type = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            LogUtils.e("[MyReceiver] " +
                    "接收Registration Id : " + regId);
            //send the Registration Id to your server...
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            LogUtils.e("[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface
                    .EXTRA_MESSAGE));
//            {"recordId":"77","messageType":"4","msg_content":"请到一食堂接取订单，赚取更多的收益吧！",
// "messageId":"463","registration_id":"160a3797c8006b3d61f","title":"推荐派单"}
            String meg = bundle.toString();
            Log.e("FFFFF", "onReceive: " + meg);
            messageTitle = bundle.getString(JPushInterface.EXTRA_TITLE);
            type = bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE);
            LogUtils.e("[MyReceiver] 接收到推送下来title" + messageTitle);
            LogUtils.e("[MyReceiver] 接收到推送下来EXTRA_CONTENT_TYPE" + bundle.getString(JPushInterface
                    .EXTRA_CONTENT_TYPE));
            LogUtils.e("[MyReceiver] 接收到推送下来bb:" + printBundle(bundle));
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            LogUtils.e("[MyReceiver] 接收到推送下来分类消息:" + type);
            LogUtils.e("[MyReceiver] 接收到推送下来的自定义消息: " + message);
            LogUtils.e("[MyReceiver] 接收到推送下来的Tilte" + bundle.getString(JPushInterface.EXTRA_TITLE));
            String currentActivity = getCurrentActivityName(context);

            if (MainTabActivity.class.getName().equals(currentActivity)) {
                context.sendBroadcast(new Intent(ConstantData.REFRESH_UNREAD_MESSAGE_ACITON));
            } else if (MessageListActivity.class.getName().equals(currentActivity)) {
                context.sendBroadcast(new Intent(ConstantData.REFRESH_MESSAGE_LIST_ACITON));
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            LogUtils.e("[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            LogUtils.e("[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            LogUtils.e("[MyReceiver] 用户点击打开了通知" + openPage);
            String currentActivity = getCurrentActivityName(context);

            String contentType = bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE);

            Log.e("FFFFFFFFFFF", "onReceive: " + messageTitle + "___" + bundle.getString
                    (JPushInterface.EXTRA_TITLE));
            Log.e("packname", "packname   =  " + currentActivity);
            if (!currentActivity.contains("com.pbph.pcc")) {
                LogUtils.e("不在本应用中: ");
                return;
            }

            if (!currentActivity.contains(LoginActivity.class.getName()) &&
                    !currentActivity.contains(WelcomeActivity.class.getName()) &&
                    !currentActivity.contains(MessageListActivity.class.getName())) {
                if (type.equals("12")) {
                    type ="";
                    Intent i = new Intent(context,RecieveOrderActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                    return;
                } else {
                    Intent i = new Intent(context, MessageListActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                }
            }
//            //打开自定义的Activity
//            Intent i = new Intent(context, Main.class);
//            Bundle bundleaaa = new Bundle();
//            bundleaaa.putString("openPage", openPage);
//            i.putExtras(bundleaaa);
//
//            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//            context.startActivity(i);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            LogUtils.e("[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface
                    .EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE,
                    false);
            LogUtils.w("[MyReceiver]" + intent.getAction() + " connected state change to " +
                    connected);
        } else {
            LogUtils.e("[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    private String getCurrentActivityName(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context
                .ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = manager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo cinfo = runningTasks.get(0);
        ComponentName component = cinfo.topActivity;
        return component.getClassName();
    }

    //     打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey1:" + key + ", value:" + bundle.getInt(key));

            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey2:" + key + ", value:" + bundle.getBoolean(key));

            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i("DDDD", "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface
                            .EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey3:" + key + ", value: [" + myKey + " - " + json
                                .optString(myKey) + "]");

                        if (myKey.equals("openPage")) {
                            openPage = json.optString(myKey);
                        }

                    }
                } catch (JSONException e) {
                    Log.e("DDDDD", "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey4:" + key + ", value:" + bundle.getString(key));

            }
        }
        return sb.toString();
    }

}
