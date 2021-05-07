package com.pbph.pcc.rong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.pbph.pcc.activity.LoginActivity;
import com.pbph.pcc.application.PccApplication;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;

/**
 * 融云相关监听 事件集合类
 * Created by AMing on 16/1/7.
 * Company RongCloud
 */
public class SealAppContext implements RongIM.ConversationListBehaviorListener,
        RongIMClient.OnReceiveMessageListener,
        RongIM.UserInfoProvider,
        RongIM.LocationProvider,
        RongIMClient.ConnectionStatusListener,
        RongIM.ConversationBehaviorListener {

    private static final int CLICK_CONVERSATION_USER_PORTRAIT = 1;


    private final static String TAG = "SealAppContext";
    public static final String UPDATE_RED_DOT = "update_red_dot";

    private Context mContext;

    private static SealAppContext mRongCloudInstance;

    private LocationCallback mLastLocationCallback;

    private static ArrayList<Activity> mActivities;

    public SealAppContext(Context mContext) {
        this.mContext = mContext;
        initListener();
        mActivities = new ArrayList<>();
    }

    /**
     * 初始化 RongCloud.
     *
     * @param context 上下文。
     */
    public static void init(Context context) {
        if (mRongCloudInstance == null) {
            synchronized (SealAppContext.class) {
                if (mRongCloudInstance == null) {
                    mRongCloudInstance = new SealAppContext(context);
                }
            }
        }

    }

    /**
     * 获取RongCloud 实例。
     *
     * @return RongCloud。
     */
    public static SealAppContext getInstance() {
        return mRongCloudInstance;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * init 后就能设置的监听
     */
    private void initListener() {
        RongIM.setConversationBehaviorListener(this);//设置会话界面操作的监听器。
        RongIM.setConversationListBehaviorListener(this);
        RongIM.setConnectionStatusListener(this);
        RongIM.setUserInfoProvider(this, true);
//        RongIM.setLocationProvider(this);//设置地理位置提供者,不用位置的同学可以注掉此行代码
        setInputProvider();
        //setUserInfoEngineListener();//移到SealUserInfoManager
        setReadReceiptConversationType();
        RongIM.getInstance().enableNewComingMessageIcon(true);
        RongIM.getInstance().enableUnreadMessageIcon(true);
        RongIM.getInstance().setMessageAttachedUserInfo(true);
//        BroadcastManager.getInstance(mContext).addAction(ConstantData.EXIT, new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                quit(false);
//            }
//        });
    }

    private void setReadReceiptConversationType() {
        Conversation.ConversationType[] types = new Conversation.ConversationType[]{
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP,
                Conversation.ConversationType.DISCUSSION
        };
        RongIM.getInstance().setReadReceiptConversationTypeList(types);
    }

    private void setInputProvider() {
        RongIM.setOnReceiveMessageListener(this);
        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
        IExtensionModule defaultModule = null;
        if (moduleList != null) {
            for (IExtensionModule module : moduleList) {
                if (module instanceof DefaultExtensionModule) {
                    defaultModule = module;
                    break;
                }
            }
            if (defaultModule != null) {
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
                RongExtensionManager.getInstance().registerExtensionModule(new SealExtensionModule());

            }
        }
    }

    @Override
    public boolean onConversationPortraitClick(Context context, Conversation.ConversationType conversationType, String s) {
        return false;
    }

    @Override
    public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String s) {
        return false;
    }

    @Override
    public boolean onConversationLongClick(Context context, View view, UIConversation uiConversation) {
        return false;
    }

    @Override
    public boolean onConversationClick(Context context, View view, UIConversation uiConversation) {
        MessageContent messageContent = uiConversation.getMessageContent();
        return false;
    }

    @Override
    public boolean onReceived(Message message, int i) {
        MessageContent messageContent = message.getContent();
        if (Conversation.ConversationType.PRIVATE == message.getConversationType()) {
//            Log.e("onReceived", "   " + message.getConversationType());
//            Intent redIntent = new Intent(ConstantData.SHOW_RED);
//            redIntent.putExtra("isred", true);
//            mContext.sendBroadcast(redIntent);
        }
//        Log.e("onReceived", "   " + messageContent.getJSONUserInfo().toString());
//        Log.e("onReceived", "   " + messageContent.getJsonMentionInfo().toString());
//        Log.e("onReceived", "   " + messageContent.getUserInfo().getName());
//        Log.e("onReceived", "   " + messageContent.getUserInfo().getUserId());
//        Log.e("onReceived", "   " + messageContent.getUserInfo().getPortraitUri().getPath());
        return false;
    }


    /**
     * 用户信息提供者的逻辑移到SealUserInfoManager
     * 先从数据库读,没有数据时从网络获取
     */
    @Override
    public UserInfo getUserInfo(String s) {
        //UserInfoEngine.getInstance(mContext).startEngine(s);
//        SealUserInfoManager.getInstance().getUserInfo(s);
        return null;
    }


    @Override
    public void onStartLocation(Context context, LocationCallback locationCallback) {
    }

    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        if (conversationType == Conversation.ConversationType.CUSTOMER_SERVICE || conversationType == Conversation.ConversationType.PUBLIC_SERVICE || conversationType == Conversation.ConversationType.APP_PUBLIC_SERVICE) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    @Override
    public boolean onMessageClick(final Context context, final View view, final Message message) {
        if (message.getContent() instanceof ImageMessage) {
            /*Intent intent = new Intent(context, PhotoActivity.class);
            intent.putExtra("message", message);
            context.startActivity(intent);*/
        }

        return false;
    }


    private void startRealTimeLocation(Context context, Conversation.ConversationType conversationType, String targetId) {

    }

    private void joinRealTimeLocation(Context context, Conversation.ConversationType conversationType, String targetId) {

    }

    @Override
    public boolean onMessageLinkClick(Context context, String s) {
        return false;
    }

    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {
        return false;
    }


    public LocationCallback getLastLocationCallback() {
        return mLastLocationCallback;
    }

    public void setLastLocationCallback(LocationCallback lastLocationCallback) {
        this.mLastLocationCallback = lastLocationCallback;
    }

    @Override
    public void onChanged(ConnectionStatus connectionStatus) {
//        Log.e("DDDDDDDD", "onChanged: "+"||||||||||||||||||||||||" );
        if (connectionStatus.equals(ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT)) {
//            Log.e("DDDDDDDD", "onChanged: "+"||||||||||||||||||||||||" );
//            Toast.makeText(mContext, "?????????", Toast.LENGTH_SHORT).show();
            quit(true);
        }
    }

    public void pushActivity(Activity activity) {
        mActivities.add(activity);
    }

    public void popActivity(Activity activity) {
        if (mActivities.contains(activity)) {
            activity.finish();
            mActivities.remove(activity);
        }
    }

    public void popAllActivity() {
        try {
//            if (MainTabActivity.mViewPager != null) {
//                MainTabActivity.mViewPager.setCurrentItem(0);
//            }
            for (Activity activity : mActivities) {
                if (activity != null) {
                    activity.finish();
                }
            }
            mActivities.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RongIMClient.ConnectCallback getConnectCallback() {
        RongIMClient.ConnectCallback connectCallback = new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
//                SealUserInfoManager.getInstance().reGetToken();
            }

            @Override
            public void onSuccess(String s) {
                Log.e(TAG, "ConnectCallback connect onSuccess");
//                SharedPreferences sp = mContext.getSharedPreferences("config", Context.MODE_PRIVATE);
//                sp.edit().putString(ConstantData.SEALTALK_LOGIN_ID, s).apply();
            }

            @Override
            public void onError(final RongIMClient.ErrorCode e) {
                Log.e(TAG, "ConnectCallback connect onError-ErrorCode=" + e);
            }
        };
        return connectCallback;
    }


    private void quit(boolean isKicked) {
//        Log.d(TAG, "quit isKicked " + isKicked);
//        SharedPreferences.Editor editor = mContext.getSharedPreferences("config", Context.MODE_PRIVATE).edit();
//        if (!isKicked) {
//            editor.putBoolean("exit", true);
//        }
//        editor.putString("loginToken", "");
////        editor.putString(ConstantData.SEALTALK_LOGIN_ID, "");
//        editor.putInt("getAllUserInfoState", 0);
//        editor.apply();
        /*//这些数据清除操作之前一直是在login界面,因为app的数据库改为按照userID存储,退出登录时先直接删除
        //这种方式是很不友好的方式,未来需要修改同app server的数据同步方式
        //SealUserInfoManager.getInstance().deleteAllUserInfo();*/
//        SealUserInfoManager.getInstance().closeDB();

        PccApplication.setUserID("");
        RongIM.getInstance().logout();
        Intent loginActivityIntent = new Intent();
        loginActivityIntent.setClass(mContext, LoginActivity.class);
        loginActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        loginActivityIntent.putExtra("kickedByOtherClient", true);

        mContext.startActivity(loginActivityIntent);
    }
}
