package com.pbph.pcc.wxapi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.pbph.pcc.tools.ConstantData;
import com.pbph.pcc.wxutil.JsonUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

@SuppressWarnings("deprecation")
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    protected static final int RETURN_OPENID_ACCESSTOKEN = 0;//
    protected static final int RETURN_NICKNAME_UID = 1;
    protected static final int RETURN_Error = 2;
    IWXAPI api = null;
    private Context context = WXEntryActivity.this;
    SharedPreferences preference;
    Editor edit;

    private void handleIntent(Intent paramIntent) {
        api.handleIntent(paramIntent, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, ConstantData.WX_APP_ID, false);
//        api.registerApp(ConstantData.WX_APP_ID);
        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

//    @Override
//    public void onReq(BaseReq arg0) {
//        finish();
//    }
//
//    @Override
//    public void onResp(BaseResp baseResp) {
//
//    }

    @Override
    public void onResp(BaseResp resp) {

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:

                if (ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX == resp.getType()) {
//                    sendBroadcast(new Intent(ConstantData.DISMISS_DIALOG));
                    Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
                } else if (ConstantsAPI.COMMAND_SENDAUTH == resp.getType() && ((SendAuth.Resp) resp).state.equals("toutoule")) {
                    String errCode = ((SendAuth.Resp) resp).code;
                    // Intent intent = new Intent(WechatUtil.WECHAT_GETCODE_ACTION);
                    // intent.putExtra("code", code);
                    // sendBroadcast(intent);
                    getResult(errCode); // ��ȡ accessToken
                }
//
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
//                System.out.println("΢�ŷ��� ERR_USER_CANCEL  ȡ����¼ �� ����");
                Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                System.out.println("΢�ŷ��� ERR_AUTH_DENIED");
                break;
            default:
//                System.out.println("΢�ŷ��� default");
                break;

        }

        finish();

    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case RETURN_OPENID_ACCESSTOKEN:
                    Bundle bundle1 = (Bundle) msg.obj;
                    String access_token = bundle1.getString("access_token");
                    String openid = bundle1.getString("openid");

                    preference = getSharedPreferences("weiXin", Context.MODE_PRIVATE);
                    edit = preference.edit();
                    edit.putString("access_token", access_token);
                    edit.putString("openid", openid);
                    edit.commit();

                    getUID(openid, access_token);
                    break;

                case RETURN_NICKNAME_UID:

//                    String result = (String) msg.obj;
//                    result = result.replaceAll("'", "��");
//                    sendBroadcast(new Intent(ConstantData.WX_LOGIN_ACTION).putExtra("wxUserInfo", result));

                    break;
//                {"openid":"oiV_Gwul8iDoPEdD2XqYTsFAizWM","nickname":"?","sex":1,"language":"zh_CN","city":"Harbin","province":"Heilongjiang","country":"CN","headimgurl":"http:\/\/wx.qlogo.cn\/mmopen\/Q3auHgzwzM7M4iaebNe6P2u1wI0iasIwMy6ic5XehIZH6JHtYicicqotdDWM1SaF0WfcnoEa5ibhiaSibQSUiaAjYia21tSWLn3JenicjlMq5csFdD4BQM\/0","privilege":[],"unionid":"o2TzpvzF4I9N9JXCp9zWaRnT02XM"}

                case RETURN_Error:
                    // ���ش���

                    Bundle bundle3 = (Bundle) msg.obj;
                    String error = bundle3.getString("error");
                    String errorId = bundle3.getString("errorId");
                    String errorText = bundle3.getString("errorText");

                    if (errorText.length() > 1) {
                        copy(errorText, context);
                    }

                    if (errorId.equals("getResult")) {
//                        Toast.makeText(context, "��ȡ�û���Ϣ����1:" + error, Toast.LENGTH_LONG).show();
                    } else {
//                        Toast.makeText(context, "��ȡ�û���Ϣ����2:" + error, Toast.LENGTH_LONG).show();
                    }

                    break;
                default:
                    break;
            }
        }

        ;
    };

    /**
     * ��ȡopenid accessTokenֵ���ں��ڲ���
     */
    private void getResult(final String code) {
        new Thread() {// ���������߳̽�����������
            public void run() {
                String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + ConstantData.WX_APP_ID + "&secret="
                        + ConstantData.APP_SECRET + "&code=" + code + "&grant_type=authorization_code";
                try {
                    JSONObject jsonObject = JsonUtils.initSSLWithHttpClinet(path);// ����https���Ӳ��õ�json���
                    if (null != jsonObject) {
                        String openid = jsonObject.getString("openid").trim();
                        String access_token = jsonObject.getString("access_token").trim();
//                        Log.e(TAG, "openid = " + openid);
//                        Log.e(TAG, "access_token = " + access_token);
                        if (jsonObject.has("unionid")) {
                            String unionid = jsonObject.getString("unionid").trim();
                            sendBroadcast(new Intent(ConstantData.GET_UNIONID_ACTION).putExtra("unionid", unionid));
                        }
//                        Message msg = handler.obtainMessage();
//                        msg.what = RETURN_OPENID_ACCESSTOKEN;
//                        Bundle bundle = new Bundle();
//                        bundle.putString("openid", openid);
//                        bundle.putString("access_token", access_token);
//                        msg.obj = bundle;
//                        handler.sendMessage(msg);
                    }
                } catch (ClientProtocolException e) {
                    e.printStackTrace();

                    Message msg = handler.obtainMessage();
                    msg.what = RETURN_Error;
                    Bundle bundle = new Bundle();
                    bundle.putString("error", e.getMessage());
                    bundle.putString("errorText", abcgetStackTrace(e));
                    bundle.putString("errorId", "getResult");
                    msg.obj = bundle;
                    handler.sendMessage(msg);

                } catch (IOException e) {
                    e.printStackTrace();

                    Message msg = handler.obtainMessage();
                    msg.what = RETURN_Error;
                    Bundle bundle = new Bundle();
                    bundle.putString("error", e.getMessage());
                    bundle.putString("errorText", abcgetStackTrace(e));
                    bundle.putString("errorId", "getResult");
                    msg.obj = bundle;
                    handler.sendMessage(msg);

                } catch (JSONException e) {
                    e.printStackTrace();

                    Message msg = handler.obtainMessage();
                    msg.what = RETURN_Error;
                    Bundle bundle = new Bundle();
                    bundle.putString("error", e.getMessage());
                    bundle.putString("errorText", abcgetStackTrace(e));
                    bundle.putString("errorId", "getResult");
                    msg.obj = bundle;
                    handler.sendMessage(msg);
                }
                return;
            }

            ;
        }.start();
    }

    /**
     * ��ȡ�쳣�Ķ�ջ��Ϣ
     *
     * @param t
     * @return
     */
    private String abcgetStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        try {
            t.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }

    /**
     * ʵ���ı����ƹ���
     */
    private static void copy(String text, Context context) {
        // �õ������������
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(text.trim());
    }

    /**
     * ��ȡ�û�Ψһ��ʶ
     *
     * @param openId
     * @param accessToken
     */
    private void getUID(final String openId, final String accessToken) {
        new Thread() {
            @Override
            public void run() {
                String path = "https://api.weixin.qq.com/sns/userinfo?lang=zh_CN&access_token=" + accessToken + "&openid="
                        + openId;
                JSONObject jsonObject;
                try {
                    jsonObject = JsonUtils.initSSLWithHttpClinet(path);
                    String nickname = jsonObject.getString("nickname");
                    String unionid = jsonObject.getString("unionid");
                    String headimgurl = jsonObject.getString("headimgurl");
                    String sex = jsonObject.getString("sex");
                    String country = jsonObject.getString("country");
                    String city = jsonObject.getString("city");

                    Message msg = handler.obtainMessage();
                    msg.what = RETURN_NICKNAME_UID;
                    Bundle bundle = new Bundle();
                    bundle.putString("nickname", nickname);
                    bundle.putString("unionid", unionid);
                    bundle.putString("headimgurl", headimgurl);
                    bundle.putString("sex", sex);
                    bundle.putString("country", country);
                    bundle.putString("city", city);
                    msg.obj = jsonObject.toString();
                    handler.sendMessage(msg);

                } catch (ClientProtocolException e) {
                    e.printStackTrace();

                    Message msg = handler.obtainMessage();
                    msg.what = RETURN_Error;
                    Bundle bundle = new Bundle();
                    bundle.putString("error", e.getMessage());
                    bundle.putString("errorText", abcgetStackTrace(e));
                    bundle.putString("errorId", "getUID");
                    msg.obj = bundle;
                    handler.sendMessage(msg);

                } catch (IOException e) {
                    e.printStackTrace();

                    Message msg = handler.obtainMessage();
                    msg.what = RETURN_Error;
                    Bundle bundle = new Bundle();
                    bundle.putString("error", e.getMessage());
                    bundle.putString("errorText", abcgetStackTrace(e));
                    bundle.putString("errorId", "getUID");
                    msg.obj = bundle;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();

                    Message msg = handler.obtainMessage();
                    msg.what = RETURN_Error;
                    Bundle bundle = new Bundle();
                    bundle.putString("error", e.getMessage());
                    bundle.putString("errorText", abcgetStackTrace(e));
                    bundle.putString("errorId", "getUID");
                    msg.obj = bundle;
                    handler.sendMessage(msg);
                }
            }

            ;
        }.start();
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }
}
